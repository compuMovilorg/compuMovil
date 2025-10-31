package com.example.myapplication.repository

import com.example.myapplication.data.UserInfo
import com.example.myapplication.data.datasource.AuthRemoteDataSource
import com.example.myapplication.data.datasource.impl.firestore.UserFirestoreDataSourceImpl
import com.example.myapplication.data.dtos.RegisterUserDto
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

    private fun registerDto(
        username: String,
        name: String,
        birthdate: String = "1990/01/01",
        FCMToken: String = "token-123",
        email: String
    ) = RegisterUserDto(
        username = username,
        name = name,
        birthdate = birthdate,
        FCMToken = FCMToken,
        email = email
    )

    @Before
    fun setUp() = runTest {
        try {
            db.useEmulator("10.0.2.2", 8080)
            auth.useEmulator("10.0.2.2", 9099)
        } catch (_: Exception) {}

        if (auth.currentUser == null) {
            auth.signInAnonymously().await()
        }

        userRepository = UserRepository(
            UserFirestoreDataSourceImpl(db),
            AuthRemoteDataSource(auth)
        )

        // Semilla base: 10 usuarios
        val batch = db.batch()
        repeat(10) { i ->
            val u = generateUser(i)
            batch.set(db.collection("users").document(u.id), u)
        }
        batch.commit().await()
    }

    // ---------------- getUserById ----------------

    @Test
    fun getUserById_validId_correctUser() = runTest {
        val id = "user_9"
        val expectedName = "Name 9"

        val result = userRepository.getUserById(id)

        Truth.assertThat(result.isSuccess).isTrue()
        Truth.assertThat(result.getOrNull()?.name).isEqualTo(expectedName)
    }

    @Test
    fun getUserById_invalidId_returnFailure() = runTest {
        val id = "user_999"

        val result = userRepository.getUserById(id)

        Truth.assertThat(result.isFailure).isTrue()
        Truth.assertThat(result.exceptionOrNull()?.message).isEqualTo("User not found")
        Truth.assertThat(result.getOrNull()).isNull()
    }

    // ---------------- createUser (user: RegisterUserDto, userId: String) ----------------

    @Test
    fun createUser_success_persistsDocument() = runTest {
        val userId = "user_100"
        val expectedEmail = "email100@example.com"
        val dto = registerDto(
            username = "username100",
            birthdate = "1990/01/01",
            name = "Name 100",
            email = expectedEmail
        )

        val createResult = userRepository.createUser(dto, userId)
        Truth.assertThat(createResult.isSuccess).isTrue()

        val snap = db.collection("users").document(userId).get().await()
        Truth.assertThat(snap.exists()).isTrue()
        Truth.assertThat(snap.getString("name")).isEqualTo("Name 100")
        Truth.assertThat(snap.getString("email")).isEqualTo(expectedEmail)

        val read = userRepository.getUserById(userId)
        Truth.assertThat(read.isSuccess).isTrue()
        Truth.assertThat(read.getOrNull()?.email).isEqualTo(expectedEmail)
    }


    @Test
    fun createUser_invalidId_returnsFailure() = runTest {
        val invalidUserId = ""  // id vacío debe fallar en el repo/datasource
        val dto = registerDto(
            username = "nouser",
            birthdate = "1990/01/01",
            name = "No Name",
            email = "nouser@example.com"
        )

        val result = userRepository.createUser(dto, invalidUserId)
        Truth.assertThat(result.isFailure).isTrue()

        val snaps = db.collection("users").get().await()
        Truth.assertThat(snaps.documents.any { it.id.isBlank() }).isFalse()
    }

    // ---------------- updateUser (user: UserInfo) ----------------

    @Test
    fun updateUser_success_overwritesFields() = runTest {
        // user_3 existe por la semilla
        val userInfo = UserInfo(
            id = "user_3",
            username = "username3_updated",
            email = "email3_updated@example.com",
            name = "Name 3 Updated",
            birthdate = "1990/01/01",
            followersCount = 33,
            followingCount = 44,
            profileImage = "https://img/p3.png"
        )

        val result = userRepository.updateUser(userInfo)
        Truth.assertThat(result.isSuccess).isTrue()

        val snap = db.collection("users").document("user_3").get().await()
        Truth.assertThat(snap.exists()).isTrue()
        Truth.assertThat(snap.getString("name")).isEqualTo("Name 3 Updated")
        Truth.assertThat(snap.getString("email")).isEqualTo("email3_updated@example.com")
        Truth.assertThat(snap.getString("username")).isEqualTo("username3_updated")
        Truth.assertThat(snap.getLong("followersCount")?.toInt()).isEqualTo(33)
        Truth.assertThat(snap.getLong("followingCount")?.toInt()).isEqualTo(44)
    }

    @Test
    fun updateUser_nonExistingId_returnsFailure() = runTest {
        // id no existente
        val userInfo = UserInfo(
            id = "user_404",
            username = "ghost",
            email = "ghost@example.com",
            name = "Ghost",
            birthdate = "1900/01/01",
            followersCount = 0,
            followingCount = 0,
            profileImage = null
        )

        val result = userRepository.updateUser(userInfo)
        Truth.assertThat(result.isFailure).isTrue()

        val snap = db.collection("users").document("user_404").get().await()
        Truth.assertThat(snap.exists()).isFalse()
    }

    // ---------------- deleteUser ----------------

    @Test
    fun deleteUser_success_removesDocument() = runTest {
        val toDeleteId = "user_5" // existe por semilla
        val before = db.collection("users").document(toDeleteId).get().await()
        Truth.assertThat(before.exists()).isTrue()

        val result = userRepository.deleteUser(toDeleteId)
        Truth.assertThat(result.isSuccess).isTrue()

        val after = db.collection("users").document(toDeleteId).get().await()
        Truth.assertThat(after.exists()).isFalse()
    }

    @Test
    fun deleteUser_nonExistingId_returnsFailure() = runTest {
        val result = userRepository.deleteUser("user_9999")
        Truth.assertThat(result.isFailure).isTrue()
    }

    @After
    fun tearDown() = runTest {
        val users = db.collection("users").get().await()
        for (userDoc in users) {
            val followers = userDoc.reference.collection("followers").get().await()
            for (follower in followers) {
                follower.reference.delete().await()
            }
            val following = userDoc.reference.collection("following").get().await()
            for (following in following) {
                following.reference.delete().await()
            }
        }
        users.documents.forEach { doc ->
            db.collection("users").document(doc.id).delete().await()
        }
    }
}
