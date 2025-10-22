package com.example.myapplication.data.datasource.impl.firestore

import android.util.Log
import com.example.myapplication.data.datasource.ReviewRemoteDataSource
import com.example.myapplication.data.dtos.CreateReviewDto
import com.example.myapplication.data.dtos.ReviewDto
import com.example.myapplication.data.dtos.UserProfileDto
import com.google.firebase.firestore.FirebaseFirestore
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
            ?: data["uid"].asStringOrNull() // alternativa si lo guardaron con otro nombre

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

        // parent id might be stored under different names (parentReviewId, replyToId, parent_id...)
        val parentReviewId = data["parentReviewId"].asStringOrNull()
            ?: data["replyToId"].asStringOrNull()
            ?: data["parent_id"].asStringOrNull()

        val createdAt = data["createdAt"].asStringOrNull() ?: data["created_at"].asStringOrNull() ?: ""
        val updatedAt = data["updatedAt"].asStringOrNull() ?: data["updated_at"].asStringOrNull() ?: ""

        // user profile may be embedded or absent — try to map minimal user profile
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
        } else {
            // empty profile so mapper doesn't crash; your toReviewInfo uses user?.profileImage etc.
            UserProfileDto()
        }

        // gastroBar embedded map (optional)
        val gastroBarMap = data["gastroBar"] as? Map<*, *>
        val gastroBarDto = if (gastroBarMap != null) {
            // you can map a minimal GastroBarDto if needed; returning null here to keep it simple
            null
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

    override suspend fun getReviewById(id: String): ReviewDto {
        val docSnapshot = db.collection(collectionName).document(id).get().await()
        if (!docSnapshot.exists()) throw NoSuchElementException("Review con id $id no encontrada")
        val data = docSnapshot.data ?: emptyMap<String, Any?>()
        @Suppress("UNCHECKED_CAST")
        return mapDocToReviewDto(docSnapshot.id, data as Map<String, Any?>)
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
        // try common field names
        val fieldCandidates = listOf("gastroBarId", "gastrobarId", "gastro_bar_id")
        for (field in fieldCandidates) {
            val snapshot = db.collection(collectionName)
                .whereEqualTo(field, gastroBarId)
                .get()
                .await()
            for (doc in snapshot.documents) {
                try {
                    val data = doc.data ?: emptyMap<String, Any?>()
                    @Suppress("UNCHECKED_CAST")
                    list.add(mapDocToReviewDto(doc.id, data as Map<String, Any?>))
                } catch (e: Exception) {
                    Log.e(TAG, "Error parsing review doc ${doc.id}", e)
                }
            }
            if (list.isNotEmpty()) break
        }
        return list
    }
}
