package com.example.myapplication.repository

import com.example.myapplication.data.datasource.AuthRemoteDataSource
import com.example.myapplication.data.datasource.impl.firestore.ReviewFireStoreDataSourceImpl
import com.example.myapplication.data.datasource.impl.firestore.UserFirestoreDataSourceImpl
import com.example.myapplication.data.dtos.CreateReviewDto
import com.example.myapplication.data.dtos.ReviewDto
import com.example.myapplication.data.dtos.UserProfileDto
import com.example.myapplication.data.repository.ReviewRepository
import com.google.common.truth.Truth.assertThat
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import org.junit.After
import org.junit.Before
import org.junit.Test

class ReviewRepositoryIntegrationTest {

    // Se inicializan después de useEmulator en setUp
    private lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth

    private lateinit var repository: ReviewRepository

    // -------- helpers --------
    private fun userDoc(
        id: String,
        username: String = "username_$id",
        email: String = "$id@example.com",
        name: String = "Name $id",
        profileImage: String? = null
    ) = mapOf(
        "id" to id,
        "username" to username,
        "email" to email,
        "password" to "secret",
        "name" to name,
        "birthdate" to "1990/01/01",
        "followersCount" to 0,
        "followingCount" to 0,
        "profileImage" to profileImage,
        "followed" to false
    )

    private suspend fun seedUser(id: String) {
        db.collection("users").document(id).set(userDoc(id)).await()
    }

    private suspend fun seedReviewFixedId(dto: ReviewDto) {
        // Sin mapeo: Firestore serializa el DTO directamente
        db.collection("reviews").document(dto.id).set(dto).await()
    }

    // -------- setup/teardown --------
    @Before
    fun setUp() = runBlocking {
        // Configurar emuladores ANTES de obtener instancias
        try { Firebase.firestore.useEmulator("10.0.2.2", 8080) } catch (_: Exception) {}
        try { Firebase.auth.useEmulator("10.0.2.2", 9099) } catch (_: Exception) {}

        db = Firebase.firestore
        auth = Firebase.auth
        if (auth.currentUser == null) auth.signInAnonymously().await()

        repository = ReviewRepository(
            ReviewFireStoreDataSourceImpl(db),
            UserFirestoreDataSourceImpl(db),
            AuthRemoteDataSource(auth)
        )

        // Seed determinístico
        seedUser("u1")

        seedReviewFixedId(
            ReviewDto(
                id = "r1",
                userId = "u1",
                placeName = "Santorini",
                imagePlace = "https://img/santo.png",
                reviewText = "Muy buen sitio",
                likes = 5,
                comments = 1,
                parentReviewId = null,
                createdAt = "2025-10-30T18:00:00Z",
                updatedAt = "2025-10-30T18:00:00Z",
                user = UserProfileDto("u1", "username_u1", "Name u1", null),
                gastroBarId = "g1",
                gastroBar = null,
                liked = true
            )
        )
        seedReviewFixedId(
            ReviewDto(
                id = "r2",
                userId = "u1",
                placeName = "Bacanal",
                imagePlace = null,
                reviewText = "Excelente",
                likes = 2,
                comments = 0,
                parentReviewId = null,
                createdAt = "2025-10-30T18:10:00Z",
                updatedAt = "2025-10-30T18:10:00Z",
                user = UserProfileDto("u1", "username_u1", "Name u1", null),
                gastroBarId = "g2",
                gastroBar = null,
                liked = false
            )
        )
    }

    @After
    fun tearDown() = runBlocking {
        // Limpiar reviews
        val reviews = db.collection("reviews").get().await()
        for (doc in reviews) doc.reference.delete().await()

        // Limpiar users + subcolecciones
        val users = db.collection("users").get().await()
        for (u in users) {
            val followers = u.reference.collection("followers").get().await()
            for (f in followers) f.reference.delete().await()
            val following = u.reference.collection("following").get().await()
            for (f in following) f.reference.delete().await()
            u.reference.delete().await()
        }
    }

    // -------- tests --------
    @Test
    fun getReviewsByUser_returnsMappedList() = runBlocking {
        val result = repository.getReviewsByUser("u1")
        if (result.isFailure) {
            println("getReviewsByUser error: ${result.exceptionOrNull()?.javaClass?.name} - ${result.exceptionOrNull()?.message}")
        }
        assertThat(result.isSuccess).isTrue()

        val list = result.getOrNull()
        assertThat(list).isNotNull()
        assertThat(list!!.size).isEqualTo(2)

        val ids = list.map { it.id }
        assertThat(ids).containsAtLeast("r1", "r2")

        val r1 = list.first { it.id == "r1" }
        assertThat(r1.placeName).isEqualTo("Santorini")
        assertThat(r1.gastroBarId).isEqualTo("g1")
    }

    @Test
    fun getReviewsByGastroBar_returnsOnlyMatchingBar() = runBlocking {
        val result = repository.getReviewsByGastroBar("g1")
        assertThat(result.isSuccess).isTrue()

        val list = result.getOrNull()
        assertThat(list).isNotNull()
        assertThat(list!!.size).isEqualTo(1)

        val item = list.single()
        assertThat(item.id).isEqualTo("r1")
        assertThat(item.placeName).isEqualTo("Santorini")
        assertThat(item.gastroBarId).isEqualTo("g1")
    }

    @Test
    fun getReviewsByGastroBar_emptyWhenNoMatches() = runBlocking {
        val result = repository.getReviewsByGastroBar("g999")
        assertThat(result.isSuccess).isTrue()
        assertThat(result.getOrNull()).isEmpty()
    }

    @Test
    fun updateReview_overwritesText() = runBlocking {
        val dto = CreateReviewDto(
            userId = "u1",
            placeName = "Bacanal",
            reviewText = "Texto editado",
            parentReviewId = null,
            placeImage = null,
            user = UserProfileDto("u1", "username_u1", "Name u1", null),
            gastroBarId = "g2"
        )

        val result = repository.updateReview("r2", dto)
        assertThat(result.isSuccess).isTrue()

        val snap = db.collection("reviews").document("r2").get().await()
        assertThat(snap.exists()).isTrue()
        assertThat(snap.getString("reviewText")).isEqualTo("Texto editado")
    }

    @Test
    fun deleteReview_removesDocument() = runBlocking {
        val result = repository.deleteReview("r1")
        assertThat(result.isSuccess).isTrue()

        val snap = db.collection("reviews").document("r1").get().await()
        assertThat(snap.exists()).isFalse()
    }
}
