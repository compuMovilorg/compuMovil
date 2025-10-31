package com.example.myapplication.datasource

import com.example.myapplication.data.datasource.impl.firestore.ReviewFireStoreDataSourceImpl
import com.example.myapplication.data.datasource.impl.firestore.UserFirestoreDataSourceImpl
import com.example.myapplication.data.dtos.CreateReviewDto
import com.example.myapplication.data.dtos.UserFirestoreDto
import com.example.myapplication.data.dtos.UserProfileDto
import com.google.common.truth.Truth
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test

class FireBaseReviewDataSourceTest {

    private val db = Firebase.firestore

    private lateinit var userDataSource: UserFirestoreDataSourceImpl
    private lateinit var reviewDataSource: ReviewFireStoreDataSourceImpl

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
        } catch (_: Exception) { }

        userDataSource = UserFirestoreDataSourceImpl(db)
        reviewDataSource = ReviewFireStoreDataSourceImpl(db)

        // poblar datos de prueba (usuarios) igual que tu ejemplo
        val batch = db.batch()
        repeat(10) { i ->
            val user = generateUser(i)
            batch.set(db.collection("users").document(user.id), user)
        }
        batch.commit().await()
    }

    @Test
    fun createReview_insertDocument_reviewExists() = runTest {
        // Arrange
        val author = generateUser(1)
        val dto = CreateReviewDto(
            userId = author.id,
            placeName = "Santorini GastroBar",
            reviewText = "Ambiente top y comida excelente",
            parentReviewId = null,
            placeImage = "https://example.com/santorini.jpg",
            user = UserProfileDto(
                id = author.id,
                username = author.username,
                name = author.name,
                profileImage = "https://example.com/profile.jpg"
            ),
            gastroBarId = "gastro123"
        )

        // Act
        reviewDataSource.createReview(dto)

        // Assert SOLO con el data source (sin leer Firestore directo)
        val reviews = reviewDataSource.getReviewsByUser(author.id)
        Truth.assertThat(reviews).isNotEmpty()
        val created = reviews.first { it.reviewText == dto.reviewText }
        Truth.assertThat(created.userId).isEqualTo(author.id)
        Truth.assertThat(created.gastroBarId).isEqualTo("gastro123")
    }

    @Test
    fun getReviewsByUser_validId_returnsUserReviews() = runTest {
        // Arrange
        val author = generateUser(2)
        val dto = CreateReviewDto(
            userId = author.id,
            placeName = "Santorini GastroBar",
            reviewText = "Volvería sin dudarlo",
            parentReviewId = null,
            placeImage = "https://example.com/santorini.jpg",
            user = UserProfileDto(
                id = author.id,
                username = author.username,
                name = author.name,
                profileImage = "https://example.com/profile.jpg"
            ),
            gastroBarId = "gastro123"
        )

        // Act
        reviewDataSource.createReview(dto)
        val reviews = reviewDataSource.getReviewsByUser(author.id)

        // Assert (sin tocar Firestore)
        Truth.assertThat(reviews).isNotEmpty()
        Truth.assertThat(reviews.any { it.reviewText == dto.reviewText }).isTrue()
    }

    @Test
    fun getReviewsByGastroBar_validId_returnsGastroReviews() = runTest {
        // Arrange
        val u1 = generateUser(3)
        val u2 = generateUser(4)

        reviewDataSource.createReview(
            CreateReviewDto(
                userId = u1.id,
                placeName = "Santorini GastroBar",
                reviewText = "R1 para gastro123",
                parentReviewId = null,
                placeImage = "https://example.com/santorini.jpg",
                user = UserProfileDto(
                    id = u1.id,
                    username = u1.username,
                    name = u1.name,
                    profileImage = null
                ),
                gastroBarId = "gastro123"
            )
        )
        reviewDataSource.createReview(
            CreateReviewDto(
                userId = u2.id,
                placeName = "Santorini GastroBar",
                reviewText = "R2 para gastro123",
                parentReviewId = null,
                placeImage = "https://example.com/santorini.jpg",
                user = UserProfileDto(
                    id = u2.id,
                    username = u2.username,
                    name = u2.name,
                    profileImage = null
                ),
                gastroBarId = "gastro123"
            )
        )

        // Act
        // Si tu implementación espera el NOMBRE en vez del ID, cambia por "Santorini GastroBar".
        val list = reviewDataSource.getReviewsByGastroBar("gastro123")

        // Assert (solo usando el data source)
        Truth.assertThat(list.size).isAtLeast(2)
        val texts = list.map { it.reviewText }.toSet()
        Truth.assertThat(texts).containsAtLeast("R1 para gastro123", "R2 para gastro123")
    }

    @Test
    fun createReplyReview_parentId_isPersisted_withoutFirestoreReads() = runTest {
        // Arrange: crear reseña padre
        val author = generateUser(5)
        val parentText = "Reseña padre"

        reviewDataSource.createReview(
            CreateReviewDto(
                userId = author.id,
                placeName = "Santorini GastroBar",
                reviewText = parentText,
                parentReviewId = null,
                placeImage = "https://example.com/santorini.jpg",
                user = UserProfileDto(
                    id = author.id,
                    username = author.username,
                    name = author.name,
                    profileImage = null
                ),
                gastroBarId = "gastro123"
            )
        )

        // Obtener el ID de la reseña padre SOLO vía data source
        val parentList = reviewDataSource.getReviewsByUser(author.id)
        Truth.assertThat(parentList).isNotEmpty()
        val parent = parentList.first { it.reviewText == parentText }
        val parentId = parent.id

        // Act
        val replier = generateUser(6)
        val replyText = "Soy un comentario/respuesta"

        reviewDataSource.createReview(
            CreateReviewDto(
                userId = replier.id,
                placeName = "Santorini GastroBar",
                reviewText = replyText,
                parentReviewId = parentId,
                placeImage = "https://example.com/santorini.jpg",
                user = UserProfileDto(
                    id = replier.id,
                    username = replier.username,
                    name = replier.name,
                    profileImage = null
                ),
                gastroBarId = "gastro123"
            )
        )

        // Assert
        val replies = reviewDataSource.getReviewsByUser(replier.id)
        Truth.assertThat(replies).isNotEmpty()
        val reply = replies.first { it.reviewText == replyText }
        Truth.assertThat(reply.parentReviewId).isEqualTo(parentId)
        Truth.assertThat(reply.userId).isEqualTo(replier.id)
    }

    @Test
    fun getReviewsByUser_invalidId_returnsEmpty() = runTest {
        // Act
        val reviews = reviewDataSource.getReviewsByUser("no_existe")

        // Assert
        Truth.assertThat(reviews).isEmpty()
    }

    @Test
    fun getReviewsByGastroBar_invalid_returnsEmpty() = runTest {
        // Act
        val reviews = reviewDataSource.getReviewsByGastroBar("gastro_inexistente")

        // Assert
        Truth.assertThat(reviews).isEmpty()
    }

    @After
    fun tearDown() = runTest {
        // Mismo estilo de cleanup que tu ejemplo (vía db)
        val users = db.collection("users").get().await()
        for (userDoc in users) {
            val followers = userDoc.reference.collection("followers").get().await()
            for (follower in followers) follower.reference.delete().await()
            val following = userDoc.reference.collection("following").get().await()
            for (f in following) f.reference.delete().await()
        }
        users.documents.forEach { doc ->
            db.collection("users").document(doc.id).delete().await()
        }

        // Limpieza de reviews creadas en los tests
        val reviews = db.collection("reviews").get().await()
        for (r in reviews) {
            // Si tu modelo no usa subcolecciones, esto no afecta
            val likes = r.reference.collection("likes").get().await()
            for (l in likes) l.reference.delete().await()
            val comments = r.reference.collection("comments").get().await()
            for (c in comments) c.reference.delete().await()
            r.reference.delete().await()
        }
    }
}
