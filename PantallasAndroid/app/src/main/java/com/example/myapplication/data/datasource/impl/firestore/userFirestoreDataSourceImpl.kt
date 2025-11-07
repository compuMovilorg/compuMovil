package com.example.myapplication.data.datasource.impl.firestore

import android.util.Log
import com.example.myapplication.data.datasource.UserRemoteDataSource
import com.example.myapplication.data.dtos.RegisterUserDto
import com.example.myapplication.data.dtos.UserDtoGeneric
import com.example.myapplication.data.dtos.UserFirestoreDto
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.Source
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

private const val TAG = "FIRESTORE_DS"

class UserFirestoreDataSourceImpl @Inject constructor(
    private val db: FirebaseFirestore
) : UserRemoteDataSource {

    private fun users() = db.collection("users")

    // 🔹 Obtener todos los usuarios
    override suspend fun getAllUsers(): List<UserDtoGeneric> {
        Log.d(TAG, "Solicitando todos los usuarios...")

        val snaps = users().get(Source.SERVER).await()
        Log.d(TAG, "Total de documentos (SERVER): ${snaps.size()}")

        snaps.documents.forEach { doc ->
            Log.d(TAG, "User -> id=${doc.id}, data=${doc.data}")
        }

        // Mapear a UserFirestoreDto (concreta) y devolver como lista de UserDtoGeneric
        return snaps.documents.mapNotNull { it.toObject(UserFirestoreDto::class.java) }
    }

    // 🔹 Obtener usuario por ID
//    override suspend fun getUserById(id: String, currentUserId: String): UserFirestoreDto {
//        Log.d(TAG, "Buscando usuario por ID: $id")
//        val docRef = db.collection("users").document(id)
//        val respuesta = docRef.get(Source.SERVER).await()
//        val user = respuesta.toObject(UserFirestoreDto::class.java) ?: throw NoSuchElementException("User not found id=$id")
//
//        val followerDoc = db.collection("users").document(id).collection("followers").document(currentUserId).get().await()
//
//        val exist = followerDoc.exists()
//        user.followed = exist
//
//        return user
//    }
    override suspend fun getUserById(id: String, currentUserId: String): UserFirestoreDto? {
        Log.d(TAG, "Buscando usuario por ID: $id, currentUserId: $currentUserId")

        // Validaciones básicas para evitar pasar rutas con '/'
        require(id.isNotBlank()) { "user id no puede ser vacío" }
        require(!id.contains('/')) { "user id no debe contener '/' (recibido: $id)" }
        if (currentUserId.isBlank()) {
            Log.w(TAG, "currentUserId vacío; se asumirá que no sigue al usuario $id")
        }
        if (currentUserId.contains('/')) {
            Log.w(TAG, "currentUserId contiene '/' (recibido: $currentUserId). Se limpiará antes de usar.")
        }

        try {
            val docRef = db.collection("users").document(id)
            val respuesta = docRef.get(Source.SERVER).await()
            var user = respuesta.toObject(UserFirestoreDto::class.java) ?: return null

            user = user.copy(id = id)

            // Si currentUserId está vacío o inválido, no preguntamos por follower (asumimos false)
            val safeCurrentUserId = currentUserId.trim().takeIf { it.isNotEmpty() && !it.contains('/') }
            if (safeCurrentUserId == null) {
                user.followed = false
                return user
            }

            // Construcción segura de la referencia a subcolección
            val followerRef = db.collection("users")
                .document(id)
                .collection("followers")
                .document(safeCurrentUserId)

            Log.d(TAG, "Checking follower path: ${followerRef.path}") // -> debe ser users/{id}/followers/{currentUserId}

            val followerSnap = followerRef.get().await()
            user.followed = followerSnap.exists()

            return user
        } catch (iae: IllegalArgumentException) {
            // Capturamos el IllegalArgumentException que Firestore lanza al recibir rutas inválidas
            Log.e(TAG, "Ruta inválida al construir referencia: ${iae.message}")
            throw iae // opcional: puedes mapearlo a tu excepción de dominio
        } catch (e: Exception) {
            Log.e(TAG, "Error al obtener usuario $id: ${e.message}", e)
            throw e
        }
    }


    // 🔹 Obtener usuario por correo
    override suspend fun getUserByEmail(email: String): UserFirestoreDto {
        Log.d(TAG, "Buscando usuario por email: $email")

        // Nota: asegúrate de que guardas 'email' en el documento users/{uid}
        val q = users().whereEqualTo("email", email).limit(1).get(Source.SERVER).await()
        val doc = q.documents.firstOrNull()

        if (doc != null) {
            Log.d(TAG, "Usuario encontrado: id=${doc.id}, data=${doc.data}")
        } else {
            Log.w(TAG, "No existe usuario con email=$email (SERVER)")
        }

        return doc?.toObject(UserFirestoreDto::class.java)
            ?: throw NoSuchElementException("User not found email=$email")
    }

    // 🔹 Obtener usuario por UID de Firebase (asume doc id == firebaseUid)
//    override suspend fun getUserByFirebaseUid(firebaseUid: String): UserDtoGeneric {
//        Log.d(TAG, "Buscando usuario por UID de Firebase: $firebaseUid")
//        // Si tu colección usa uid como id, devolvemos getUserById
//        return getUserById(firebaseUid, currentUserId)
//    }

    // 🔹 Crear usuario
    override suspend fun createUser(user: UserDtoGeneric) {
        val uid = extractUid(user)
        Log.d(TAG, "CREATE -> users/${uid ?: "(auto)"} user=$user")

        if (uid != null && uid.isNotBlank()) {
            users().document(uid).set(user, SetOptions.merge()).await()

            // read-back SERVER
            val snap = users().document(uid).get(Source.SERVER).await()
            if (!snap.exists()) error("Post-write readback FAILED (createUser) for users/$uid")
            Log.d(TAG, "READBACK OK (createUser) <- ${snap.id} data=${snap.data}")
        } else {
            val ref = users().add(user).await()

            // read-back SERVER
            val snap = users().document(ref.id).get(Source.SERVER).await()
            if (!snap.exists()) error("Post-write readback FAILED (createUser autoID) for users/${ref.id}")
            Log.d(TAG, "READBACK OK (createUser autoID) <- ${snap.id} data=${snap.data}")
        }
    }

    // 🔹 Actualizar usuario
    override suspend fun updateUser(id: String, user: UserDtoGeneric) {
        Log.d(TAG, "UPDATE -> users/$id user=$user")
        val ref = users().document(id)

        // 1) Verificar existencia en SERVER
        val exists = ref.get(Source.SERVER).await().exists()
        if (!exists) throw Exception("User not found")

        // 2) Actualizar (merge)
        ref.set(user, SetOptions.merge()).await()

        // 3) Read-back SERVER
        val snap = ref.get(Source.SERVER).await()
        if (!snap.exists()) error("Post-write readback FAILED (updateUser) for users/$id")
        Log.d(TAG, "READBACK OK (updateUser) <- ${snap.id} data=${snap.data}")
    }


    // 🔹 Eliminar usuario
    override suspend fun deleteUser(id: String) {
        Log.d(TAG, "DELETE -> users/$id")
        val ref = users().document(id)

        // 1) Verificar existencia EN SERVIDOR antes de borrar
        val exists = ref.get(Source.SERVER).await().exists()
        if (!exists) {
            // Mantén el mismo mensaje que usas en otros métodos/tests
            throw Exception("User not found")
        }

        // 2) Borrar
        ref.delete().await()

        // 3) Read-back para asegurar borrado
        val snap = ref.get(Source.SERVER).await()
        check(!snap.exists()) { "Post-delete readback FAILED (deleteUser) for users/$id" }

        Log.d(TAG, "DELETE OK (no existe en SERVER) users/$id")
    }


    // 🔹 Registrar usuario (post login/registro Auth)
    override suspend fun registerUser(registerUserDto: RegisterUserDto, userId: String) {
        try {
            Log.d(TAG, "WRITE(registerUser) -> users/$userId dto=$registerUserDto")
            val docRef = db.collection("users").document(userId)
            docRef.set(registerUserDto).await()

            // Leer desde servidor para verificar
            val snap = users().document(userId).get(Source.SERVER).await()
            if (!snap.exists()) error("Post-write readback FAILED (registerUser) for users/$userId")
            Log.d(TAG, "READBACK OK (registerUser) <- ${snap.id} data=${snap.data}")
        } catch (e: Exception) {
            Log.e(TAG, "FIRESTORE WRITE ERROR: ${e.javaClass.simpleName}: ${e.message}", e)
            throw e
        }
    }

    // 🔹 Actualizar solo la URL de la imagen de perfil
    override suspend fun updateProfileImage(id: String, profileImageUrl: String) {
        try {
            Log.d(TAG, "UPDATE PROFILE IMAGE -> users/$id url=$profileImageUrl")
            // Usamos update() para cambiar solo el campo profileImage
            users().document(id).update("profileImage", profileImageUrl).await()

            // Verificación read-back desde SERVER
            val snap = users().document(id).get(Source.SERVER).await()
            if (!snap.exists()) error("Post-write readback FAILED (updateProfileImage) for users/$id")
            val stored = snap.getString("profileImage")
            if (stored != profileImageUrl) {
                error("Profile image mismatch after update (expected='$profileImageUrl' got='$stored')")
            }
            Log.d(TAG, "READBACK OK (updateProfileImage) <- ${snap.id} profileImage=$stored")
        } catch (e: Exception) {
            Log.e(TAG, "Error updating profile image for users/$id: ${e.message}", e)
            throw e
        }
    }

    override suspend fun followOrUnfollowUser(
        currentUserId: String,
        targetUserId: String
    ) = try {
        db.runTransaction { transaction ->
            val currentUserRef = db.collection("users").document(currentUserId)
            val targetUserRef = db.collection("users").document(targetUserId)

            val followingsRef = currentUserRef.collection("followings").document(targetUserId)
            val followersRef = targetUserRef.collection("followers").document(currentUserId)

            val followingDoc = transaction.get(followingsRef)
            if (followingDoc.exists()) {
                transaction.delete(followingsRef)
                transaction.delete(followersRef)
                transaction.update(currentUserRef, "followingCount", FieldValue.increment(-1))
                transaction.update(targetUserRef, "followersCount", FieldValue.increment(-1))
            } else {
                transaction.set(followingsRef, mapOf("timestamp" to FieldValue.serverTimestamp()))
                transaction.set(followersRef, mapOf("timestamp" to FieldValue.serverTimestamp()))
                transaction.update(currentUserRef, "followingCount", FieldValue.increment(1))
                transaction.update(targetUserRef, "followersCount", FieldValue.increment(1))
            }
        }.await()
        Result.success(Unit)
    } catch (e: Exception) {
        Log.e("UserRepo", "Error follow/unfollow", e)
        Result.failure(e)
    }


    // Intenta extraer 'uid' por reflexión si tu DTO lo tiene.
    private fun extractUid(user: UserDtoGeneric): String? {
        return try {
            val kClass = user::class
            val uidProp = kClass.members.firstOrNull { it.name == "uid" }
            uidProp?.call(user) as? String
        } catch (_: Exception) {
            null
        }
    }

    override suspend fun getFollowingIds(
        userId: String,
        fromServer: Boolean
    ): List<String> {
        require(userId.isNotBlank()) { "userId vacío" }
        val source = if (fromServer) Source.SERVER else Source.DEFAULT

        val snap = users()
            .document(userId)
            .collection("followings")
            .get(source)
            .await()

        return snap.documents.map { it.id }
    }

}
