package com.example.myapplication.data.datasource.impl.firestore

import com.example.myapplication.data.datasource.UserRemoteDataSource
import com.example.myapplication.data.dtos.RegisterUserDto
import com.example.myapplication.data.dtos.UserDtoGeneric
import com.example.myapplication.data.dtos.UserFirestoreDto
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class userFirestoreDataSourceImpl @Inject constructor(
    private val db: FirebaseFirestore
) : UserRemoteDataSource {

    private fun users() = db.collection("users")

    override suspend fun getAllUsers(): List<UserDtoGeneric> {
        val snaps = users().get().await()
        // Mapea directamente a UserDtoGeneric (asegúrate de que sea Firestore-serializable)
        return snaps.documents.mapNotNull { it.toObject(UserDtoGeneric::class.java) }
    }

    override suspend fun getUserById(id: String): UserDtoGeneric {
        val snap = users().document(id).get().await()
        return snap.toObject(UserDtoGeneric::class.java)
            ?: throw NoSuchElementException("User not found id=$id")
    }

    override suspend fun getUserByEmail(email: String): UserDtoGeneric {
        val q = users().whereEqualTo("email", email).limit(1).get().await()
        val doc = q.documents.firstOrNull()
            ?: throw NoSuchElementException("User not found email=$email")
        return doc.toObject(UserDtoGeneric::class.java)
            ?: throw IllegalStateException("Invalid user payload for email=$email")
    }

    override suspend fun getUserByFirebaseUid(firebaseUid: String): UserDtoGeneric {
        // Si guardas el doc con ID = uid, esto es equivalente a getUserById(uid)
        return getUserById(firebaseUid)
    }

    override suspend fun createUser(user: UserDtoGeneric) {
        // Si tu UserDtoGeneric contiene un campo uid (recomendado), usa ese como ID del doc:
        val uid = extractUid(user)
        if (uid != null && uid.isNotBlank()) {
            users().document(uid).set(user, SetOptions.merge()).await()
        } else {
            // Si NO tienes uid en el dto, se creará un ID aleatorio (no recomendado)
            users().add(user).await()
        }
    }

    override suspend fun updateUser(id: String, user: UserDtoGeneric) {
        users().document(id).set(user, SetOptions.merge()).await()
    }

    override suspend fun deleteUser(id: String) {
        users().document(id).delete().await()
    }

    override suspend fun registerUser(
        registerUserDto: RegisterUserDto,
        userId: String
    ) {
        // Upsert de perfil mínimo tras login/registro. Usa merge para no pisar otros campos.
        users().document(userId).set(registerUserDto, SetOptions.merge()).await()
    }

    /**
     * Intenta recuperar un 'uid' del DTO genérico sin atar este data source a una clase específica.
     * Ajusta según tus campos reales en UserDtoGeneric (por ejemplo: user.uid, user.id, etc.).
     */
    private fun extractUid(user: UserDtoGeneric): String? {
        return try {
            val kClass = user::class
            val uidProp = kClass.members.firstOrNull { it.name == "uid" }
            val value = uidProp?.call(user) as? String
            value
        } catch (_: Exception) {
            null
        }
    }
}