package com.example.myapplication.data.datasource.impl.firestore

import android.util.Log
import com.example.myapplication.data.datasource.ReviewRemoteDataSource
import com.example.myapplication.data.dtos.CreateReviewDto
import com.example.myapplication.data.dtos.GastroBarDto
import com.example.myapplication.data.dtos.ReviewDto
import com.example.myapplication.data.dtos.UserProfileDto
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class ReviewFireStoreDataSourceImpl @Inject constructor(
    private val db: FirebaseFirestore
) : ReviewRemoteDataSource {

    private val collectionName = "reviews"
    private val TAG = "ReviewFireStoreDS"

    // Helper: leer un campo que puede ser String o Number y devolver String?
    private fun Any?.asStringOrNull(): String? {
        return when (this) {
            is String -> this
            is Number -> this.toString()
            else -> this?.toString()
        }
    }

    // Mapear DocumentSnapshot -> ReviewDto de forma segura
    private fun mapDocToReviewDto(docId: String, data: Map<String, Any?>): ReviewDto {
        val userId = data["userId"].asStringOrNull()
            ?: data["uid"].asStringOrNull()

        val placeName = data["placeName"].asStringOrNull()
            ?: data["place_name"].asStringOrNull()
            ?: ""

        val imagePlace = data["imagePlace"].asStringOrNull()
            ?: data["placeImage"].asStringOrNull()

        val reviewText = data["reviewText"].asStringOrNull()
            ?: data["text"].asStringOrNull()
            ?: ""

        val likes = (data["likes"] as? Number)?.toInt() ?: 0
        val comments = (data["comments"] as? Number)?.toInt() ?: 0

        val parentReviewId = data["parentReviewId"].asStringOrNull()
            ?: data["replyToId"].asStringOrNull()
            ?: data["parent_id"].asStringOrNull()

        val createdAt = data["createdAt"].asStringOrNull() ?: data["created_at"].asStringOrNull() ?: ""
        val updatedAt = data["updatedAt"].asStringOrNull() ?: data["updated_at"].asStringOrNull() ?: ""

        // ✅ Leer explícitamente el gastroBarId
        val gastroBarId = data["gastroBarId"].asStringOrNull()
            ?: data["gastro_bar_id"].asStringOrNull()

        // user mapping
        val userMap = data["user"] as? Map<*, *>
        val userProfile = if (userMap != null) {
            val uid = (userMap["id"] ?: userMap["uid"] ?: userMap["userId"]).asStringOrNull()
            val username = (userMap["username"] ?: userMap["userName"]).asStringOrNull() ?: ""
            val name = (userMap["name"] ?: userMap["fullName"]).asStringOrNull() ?: ""
            val profileImage = (userMap["profileImage"] ?: userMap["image"]).asStringOrNull()
            UserProfileDto(
                id = uid ?: "",
                username = username,
                name = name,
                profileImage = profileImage
            )
        } else UserProfileDto()

        val gastroBarMap = data["gastroBar"] as? Map<*, *>
        val gastroBarDto = if (gastroBarMap != null) {
            null // podrías mapearlo si quisieras
        } else null

        return ReviewDto(
            id = docId,
            userId = userId ?: "",
            placeName = placeName,
            imagePlace = imagePlace,
            reviewText = reviewText,
            likes = likes,
            comments = comments,
            parentReviewId = parentReviewId,
            createdAt = createdAt,
            updatedAt = updatedAt,
            user = userProfile,
            gastroBarId = gastroBarId, // ✅ AHORA se incluye
            gastroBar = gastroBarDto
        )
    }


    override suspend fun getAllReviews(): List<ReviewDto> {
        val snapshot = db.collection(collectionName).get().await()
        val list = mutableListOf<ReviewDto>()
        for (doc in snapshot.documents) {
            try {
                val data = doc.data ?: emptyMap()
                @Suppress("UNCHECKED_CAST")
                val map = data as Map<String, Any?>
                val dto = mapDocToReviewDto(doc.id, map)
                list.add(dto)
            } catch (e: Exception) {
                Log.e(TAG, "Error parsing review doc ${doc.id}", e)
                // continue con los demás documentos
            }
        }
        return list
    }

    override suspend fun getReviewById(id: String, currentUserId: String): ReviewDto {

      val reviewRef = db.collection("reviews").document(id)
        val reviewSnapshot = reviewRef.get().await()
        val review = reviewSnapshot.toObject(ReviewDto::class.java) ?: throw NoSuchElementException("Review con id $id no encontrada")

        if(currentUserId.isNotEmpty()){
            val likeSnapshot = reviewRef.collection("likes").document(currentUserId).get().await()
            val hasLiked = likeSnapshot.exists()

            if(hasLiked){
                review.liked = true
            }
            return review
        }else{
            return review

        }

    }

    override suspend fun createReview(review: CreateReviewDto) {
        // asegúrate de guardar userId como String en el DTO/creación
        db.collection(collectionName).add(review).await()
    }

    override suspend fun deleteReview(id: String) {
        val docRef = db.collection(collectionName).document(id)
        val snapshot = docRef.get().await()
        if (!snapshot.exists()) throw NoSuchElementException("Review con id $id no encontrada para eliminar")
        docRef.delete().await()
    }

    override suspend fun updateReview(id: String, review: CreateReviewDto) {
        val docRef = db.collection(collectionName).document(id)
        val snapshot = docRef.get().await()
        if (!snapshot.exists()) throw NoSuchElementException("Review con id $id no encontrada para actualizar")
        docRef.set(review).await()
    }

    override suspend fun getReviewsReplies(id: String): List<ReviewDto> {
        // soporta tanto replyToId como parentReviewId
        val querySnapshot = db.collection(collectionName)
            .whereEqualTo("replyToId", id)
            .get()
            .await()

        val list = mutableListOf<ReviewDto>()
        for (doc in querySnapshot.documents) {
            try {
                val data = doc.data ?: emptyMap<String, Any?>()
                @Suppress("UNCHECKED_CAST")
                list.add(mapDocToReviewDto(doc.id, data as Map<String, Any?>))
            } catch (e: Exception) {
                Log.e(TAG, "Error parsing reply doc ${doc.id}", e)
            }
        }

        // Si no hay resultados por replyToId, intenta buscar por parentReviewId (nombre alternativo)
        if (list.isEmpty()) {
            val altSnapshot = db.collection(collectionName)
                .whereEqualTo("parentReviewId", id)
                .get()
                .await()
            for (doc in altSnapshot.documents) {
                try {
                    val data = doc.data ?: emptyMap<String, Any?>()
                    @Suppress("UNCHECKED_CAST")
                    list.add(mapDocToReviewDto(doc.id, data as Map<String, Any?>))
                } catch (e: Exception) {
                    Log.e(TAG, "Error parsing reply alt doc ${doc.id}", e)
                }
            }
        }

        return list
    }

    override suspend fun getReviewsByUser(userId: String): List<ReviewDto> {
        // userId in firestore could be stored as number or string; attempt 2 queries if necessary
        val list = mutableListOf<ReviewDto>()

        // first try as string
        var querySnapshot = db.collection(collectionName)
            .whereEqualTo("userId", userId)
            .get()
            .await()

        for (doc in querySnapshot.documents) {
            try {
                val data = doc.data ?: emptyMap<String, Any?>()
                @Suppress("UNCHECKED_CAST")
                list.add(mapDocToReviewDto(doc.id, data as Map<String, Any?>))
            } catch (e: Exception) {
                Log.e(TAG, "Error parsing review doc ${doc.id}", e)
            }
        }

        // if empty, try querying numeric form (Long) — convert userId to Long if possible
        if (list.isEmpty()) {
            val possibleLong = userId.toLongOrNull()
            if (possibleLong != null) {
                querySnapshot = db.collection(collectionName)
                    .whereEqualTo("userId", possibleLong)
                    .get()
                    .await()
                for (doc in querySnapshot.documents) {
                    try {
                        val data = doc.data ?: emptyMap<String, Any?>()
                        @Suppress("UNCHECKED_CAST")
                        list.add(mapDocToReviewDto(doc.id, data as Map<String, Any?>))
                    } catch (e: Exception) {
                        Log.e(TAG, "Error parsing review doc ${doc.id}", e)
                    }
                }
            }
        }

        return list
    }

    override suspend fun getReviewsByGastroBar(gastroBarId: String): List<ReviewDto> {
        val list = mutableListOf<ReviewDto>()

        Log.d(TAG, "getReviewsByGastroBar -> inicio con id='$gastroBarId'")

        // Consulta principal filtrando por gastroBarId
        val querySnapshot = db.collection(collectionName)
            .whereEqualTo("gastroBarId", gastroBarId)
            .get()
            .await()

        Log.d(TAG, "getReviewsByGastroBar -> consulta devolvió ${querySnapshot.size()} documentos")

        for (doc in querySnapshot.documents) {
            try {
                val data = doc.data ?: emptyMap<String, Any?>()
                val actualGastroBarId = data["gastroBarId"]
                Log.d(TAG, "getReviewsByGastroBar -> doc '${doc.id}' tiene gastroBarId='$actualGastroBarId'")

                // Solo añade si coincide exactamente
                if (actualGastroBarId == gastroBarId) {
                    @Suppress("UNCHECKED_CAST")
                    val reviewDto = mapDocToReviewDto(doc.id, data as Map<String, Any?>)
                    list.add(reviewDto)
                    Log.d(TAG, "getReviewsByGastroBar -> documento '${doc.id}' añadido correctamente.")
                } else {
                    Log.w(TAG, "getReviewsByGastroBar -> documento '${doc.id}' ignorado: gastroBarId='$actualGastroBarId' no coincide.")
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error al parsear review doc ${doc.id}", e)
            }
        }

        if (list.isEmpty()) {
            Log.w(TAG, "getReviewsByGastroBar -> no se encontraron reseñas para id='$gastroBarId'")
        } else {
            Log.d(TAG, "getReviewsByGastroBar -> total final=${list.size} reseñas para id='$gastroBarId'")
        }

        return list
    }

    override suspend fun sendOrDeleteReviewLike(reviewId: String, userId: String) {
        val reviewRef = db.collection("reviews").document(reviewId)
        val likeRef = reviewRef.collection("likes").document(userId)

        db.runTransaction { transaction ->

            val likeDoc = transaction.get(likeRef)
            if (likeDoc.exists()) {
                transaction.delete(likeRef)
                transaction.update(reviewRef, "likes", FieldValue.increment(-1))
            } else {
                transaction.set(likeRef, mapOf("timestamp" to FieldValue.serverTimestamp()))
                transaction.update(reviewRef, "likes", FieldValue.increment(1))
            }

        }
    }

    override suspend fun listenAllReviews(): Flow<List<ReviewDto>> = callbackFlow {
        val listener = db.collection("reviews").addSnapshotListener { snapshot, error ->
            if (error != null) {
                Log.e(TAG, "Error al escuchar cambios en reviews", error)
                close(error)
                return@addSnapshotListener
            }

            if (snapshot == null) {
                Log.w(TAG, "Snapshot nulo en listenAllReviews()")
                trySend(emptyList())
                return@addSnapshotListener
            }

            try {
                val reviews = snapshot.documents.mapNotNull { doc ->
                    val docId = doc.id
                    val userId = doc.getString("userId") ?: ""
                    val placeName = doc.getString("placeName") ?: ""
                    val reviewText = doc.getString("reviewText") ?: ""

                    if (userId.isBlank() || placeName.isBlank()) {
                        Log.e(TAG, "Documento invalido reviews/$docId: falta userId o placeName. Se descarta.")
                        return@mapNotNull null
                    }

                    val likes = (doc.getLong("likes") ?: 0).toInt()
                    val comments = (doc.getLong("comments") ?: 0).toInt()
                    val parentReviewId = doc.getString("parentReviewId")
                    val createdAt = doc.getString("createdAt") ?: ""
                    val updatedAt = doc.getString("updatedAt") ?: ""
                    val liked = doc.getBoolean("liked") ?: false

                    // Imagen: desde imagePlace o gastroBar
                    val imagePlace = doc.getString("imagePlace") ?: run {
                        val gastroBarMap = doc.get("gastroBar") as? Map<*, *>
                        gastroBarMap?.get("imagePlace") as? String
                    }

                    // Parsear campo user
                    val userDto = try {
                        doc.get("user")?.let {
                            val map = it as? Map<*, *>
                            UserProfileDto(
                                id = map?.get("id") as? String ?: "",
                                username = map?.get("username") as? String ?: "",
                                name = map?.get("name") as? String ?: "",
                                profileImage = map?.get("profileImage") as? String
                            )
                        } ?: UserProfileDto()
                    } catch (e: Exception) {
                        Log.e(TAG, "Error al parsear campo user en review ${doc.id}", e)
                        UserProfileDto()
                    }

                    // Parsear campo gastroBar (si existe)
                    val gastroBarDto = try {
                        doc.get("gastroBar")?.let {
                            val map = it as? Map<*, *>
                            GastroBarDto(
                                id = map?.get("id") as? String ?: "",
                                imagePlace = map?.get("imagePlace") as? String,
                                name = map?.get("name") as? String ?: "",
                                rating = (map?.get("rating") as? Number)?.toFloat() ?: 0f,
                                reviewCount = (map?.get("reviewCount") as? Number)?.toInt() ?: 0,
                                address = map?.get("address") as? String ?: "",
                                hours = map?.get("hours") as? String ?: "",
                                cuisine = map?.get("cuisine") as? String ?: "",
                                description = map?.get("description") as? String ?: "",
                                reviewId = map?.get("reviewId") as? String
                            )
                        } ?: GastroBarDto()
                    } catch (e: Exception) {
                        Log.e(TAG, "Error al parsear campo gastroBar en review ${doc.id}", e)
                        GastroBarDto()
                    }

                    // Construir ReviewDto completo
                    ReviewDto(
                        id = docId,
                        userId = userId,
                        placeName = placeName,
                        imagePlace = imagePlace,
                        reviewText = reviewText,
                        likes = likes,
                        comments = comments,
                        parentReviewId = parentReviewId,
                        createdAt = createdAt,
                        updatedAt = updatedAt,
                        user = userDto,
                        gastroBarId = doc.getString("gastroBarId"),
                        gastroBar = gastroBarDto,
                        liked = liked
                    )
                }

                trySend(reviews).isSuccess
            } catch (e: Exception) {
                Log.e(TAG, "Error procesando snapshot de reviews", e)
                close(e)
            }
        }

        awaitClose { listener.remove() }
    }



}
