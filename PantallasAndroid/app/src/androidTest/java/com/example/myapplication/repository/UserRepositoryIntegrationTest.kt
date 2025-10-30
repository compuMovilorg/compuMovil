package com.example.myapplication.repository

import com.example.myapplication.data.datasource.AuthRemoteDataSource
import com.example.myapplication.data.datasource.impl.firestore.ReviewFireStoreDataSourceImpl
import com.example.myapplication.data.datasource.impl.firestore.UserFirestoreDataSourceImpl
import com.example.myapplication.data.dtos.UserFirestoreDto
import com.example.myapplication.data.repository.UserRepository
import com.google.common.truth.Truth
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test

class UserRepositoryIntegrationTest {

    private val db = Firebase.firestore
    private lateinit var userRepository: UserRepository
    private val auth = Firebase.auth

    private fun generateUser(i: Int): UserFirestoreDto = UserFirestoreDto(
        id = "user_$i",
        username = "username$i",
        email = "email$i@example.com",
        password = "password$i",
        name = "Name $i",
        birthdate = "1990/01/01",
        profileImage = null,
        followed = false
    )

    @Before
    fun setUp() = runTest {
        try {
            db.useEmulator("10.0.2.2", 8080)
        }catch (e: Exception){

        }

        userRepository = UserRepository(UserFirestoreDataSourceImpl(db), AuthRemoteDataSource(auth))


        // poblar datos de prueba
        val batch = db.batch()
        repeat(10) { i ->
            val user = generateUser(i)
            batch.set(db.collection("users").document(user.id), user)
        }
        batch.commit().await()
    }

    @Test
    fun getUserById_validId_correctUser() = runTest {
        //arrange
        val id = "user_9"
        val expectedName = "Name 9"
        //act
        val result = userRepository.getUserById(id,"")
        //assert
        Truth.assertThat(result.isSuccess).isTrue()
        //Truth.assertThat(result.getOrNull()?.id).isEqualTo(id)
        Truth.assertThat(result.getOrNull()?.name).isEqualTo(expectedName)

    }

    @Test
    fun getUserById_invalidId_returnFailure() = runTest {
        //arrange
        val id = "user_999"
        //act
        val result = userRepository.getUserById(id,"")
        //assert
        Truth.assertThat(result.isFailure).isTrue()
        Truth.assertThat(result.exceptionOrNull()?.message).isEqualTo("User not found")
        Truth.assertThat(result.getOrNull()).isNull()

    }



    @After
    fun tearDown() = runTest {
        val users = db.collection("users").get().await()

        for(userDoc in users){
            val followers = userDoc.reference.collection("followers").get().await()
            for(follower in followers){
                follower.reference.delete().await()
            }
            val following = userDoc.reference.collection("following").get().await()
            for(following in following){
                following.reference.delete().await()
            }
        }
        users.documents.forEach { doc ->
            db.collection("users").document(doc.id).delete().await()
        }
    }
}