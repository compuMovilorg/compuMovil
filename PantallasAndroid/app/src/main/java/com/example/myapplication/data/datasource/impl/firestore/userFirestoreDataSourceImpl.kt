package com.example.myapplication.data.datasource.impl.firestore

import android.util.Log
import com.example.myapplication.data.datasource.UserRemoteDataSource
import com.example.myapplication.data.dtos.RegisterUserDto
import com.example.myapplication.data.dtos.UserDtoGeneric
import com.example.myapplication.data.dtos.UserFirestoreDto
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

        // ⚠️ OJO: UserDtoGeneric es abstracta; asegúrate de mapear a una clase concreta.
        // Si tienes un UserFirestoreDto que implementa UserDtoGeneric, usa ese.
        // Aquí dejamos el toObject() como lo tenías, pero considera cambiarlo a tu DTO concreto.
        return snaps.documents.mapNotNull { it.toObject(UserDtoGeneric::class.java) }
    }

    // 🔹 Obtener usuario por ID
    override suspend fun getUserById(id: String): UserFirestoreDto {
        Log.d(TAG, "Buscando usuario por ID: $id")

        val snap = users().document(id).get(Source.SERVER).await()

        if (snap.exists()) {
            Log.d(TAG, "Usuario encontrado: ${snap.data}")
        } else {
            Log.w(TAG, "No se encontró el usuario con ID=$id (SERVER)")
        }

        return snap.toObject(UserFirestoreDto::class.java)
            ?: throw NoSuchElementException("User not found id=$id")
    }

    // 🔹 Obtener usuario por correo
    override suspend fun getUserByEmail(email: String): UserDtoGeneric {
        Log.d(TAG, "Buscando usuario por email: $email")

        // ⚠️ WARNING: RegisterUserDto no guarda 'email';
        // si nunca guardas ese campo en 'users/{uid}', esta query NO encontrará resultados.
        val q = users().whereEqualTo("email", email).limit(1).get(Source.SERVER).await()
        val doc = q.documents.firstOrNull()

        if (doc != null) {
            Log.d(TAG, "Usuario encontrado: id=${doc.id}, data=${doc.data}")
        } else {
            Log.w(TAG, "No existe usuario con email=$email (SERVER)")
        }

        return doc?.toObject(UserDtoGeneric::class.java)
            ?: throw NoSuchElementException("User not found email=$email")
    }

    // 🔹 Obtener usuario por UID de Firebase
    override suspend fun getUserByFirebaseUid(firebaseUid: String): UserDtoGeneric {
        Log.d(TAG, "Buscando usuario por UID de Firebase: $firebaseUid")
        return getUserById(firebaseUid)
    }

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
        users().document(id).set(user, SetOptions.merge()).await()

        // read-back SERVER
        val snap = users().document(id).get(Source.SERVER).await()
        if (!snap.exists()) error("Post-write readback FAILED (updateUser) for users/$id")
        Log.d(TAG, "READBACK OK (updateUser) <- ${snap.id} data=${snap.data}")
    }

    // 🔹 Eliminar usuario
    override suspend fun deleteUser(id: String) {
        Log.d(TAG, "DELETE -> users/$id")
        users().document(id).delete().await()

        // read-back SERVER
        val snap = users().document(id).get(Source.SERVER).await()
        if (snap.exists()) error("Post-delete readback FAILED (deleteUser) for users/$id")
        Log.d(TAG, "DELETE OK (no existe en SERVER) users/$id")
    }

    // 🔹 Registrar usuario (post login/registro Auth)
    override suspend fun registerUser(registerUserDto: RegisterUserDto, userId: String) {
        try {
            Log.d(TAG, "WRITE(registerUser) -> users/$userId dto=$registerUserDto")
           val docRef = db.collection("users").document(userId)
            docRef.set(registerUserDto).await()

            // Leer desde servidor para verificar
            val snap = users().document(userId).get(com.google.firebase.firestore.Source.SERVER).await()
            if (!snap.exists()) error("Post-write readback FAILED (registerUser) for users/$userId")
            Log.d(TAG, "READBACK OK (registerUser) <- ${snap.id} data=${snap.data}")
        } catch (e: Exception) {
            Log.e(TAG, "FIRESTORE WRITE ERROR: ${e.javaClass.simpleName}: ${e.message}", e)
            throw e
        }
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
}
