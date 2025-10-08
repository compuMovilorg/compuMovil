package com.example.myapplication.data.datasource.impl.firestore

import com.example.myapplication.data.datasource.UserRemoteDataSource
import com.example.myapplication.data.dtos.RegisterUserDto
import com.example.myapplication.data.dtos.UserDtoGeneric
import com.example.myapplication.data.dtos.UserFirestoreDto
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class userFirestoreDataSourceImpl @Inject constructor(
    private val db: FirebaseFirestore
): UserRemoteDataSource {
    override suspend fun getAllUsers(): List<UserDtoGeneric> {
        TODO("Not yet implemented")
    }

    override suspend fun getUserById(id: Int): UserDtoGeneric {
        val docRef = db.collection("users").document(id.toString())
        val respuesta = docRef.get().await()
        return respuesta.toObject(UserFirestoreDto::class.java) ?: throw Exception("User not found")
    }

    override suspend fun getUserByEmail(email: String): UserDtoGeneric {
        TODO("Not yet implemented")
    }

    override suspend fun getUserByFirebaseUid(firebaseUid: String): UserDtoGeneric {
        TODO("Not yet implemented")
    }

    override suspend fun createUser(user: UserDtoGeneric) {
        TODO("Not yet implemented")
    }

    override suspend fun updateUser(
        id: Int,
        user: UserDtoGeneric
    ) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteUser(id: Int) {
        TODO("Not yet implemented")
    }

    override suspend fun registerUser(
        registerUserDto: RegisterUserDto,
        userId: String
    ) {
       val docRef = db.collection("users").document(userId)
        docRef.set(registerUserDto).await()
    }
}