package com.example.myapplication

import com.example.myapplication.data.datasource.impl.firestore.ReviewFireStoreDataSourceImpl
import com.google.firebase.Firebase
import com.example.myapplication.data.datasource.impl.firestore.UserFirestoreDataSourceImpl
import com.example.myapplication.data.dtos.CreateReviewDto
import com.example.myapplication.data.dtos.RegisterUserDto
import com.example.myapplication.data.dtos.UserFirestoreDto
import com.example.myapplication.data.dtos.UserProfileDto
import com.google.common.truth.Truth
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test

class FirebaseUserDataSourceTest {
    private val db = Firebase.firestore

    private lateinit var dataSource: UserFirestoreDataSourceImpl

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
        }catch (e: Exception){

        }
        dataSource = UserFirestoreDataSourceImpl(db)
        reviewDataSource = ReviewFireStoreDataSourceImpl(db)


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
        // Arrange
        val id = "user_9"
        val expectedName = "Name 9"

        // Act
        val result = dataSource.getUserById(id, "")

        // Assert
        Truth.assertThat(result).isNotNull()
        Truth.assertThat(result?.id).isEqualTo(id)
        Truth.assertThat(result?.name).isEqualTo(expectedName)
    }

    @Test
    fun getUserById_invalidId_null() = runTest {
        // Arrange
        val invalidId = "no_existe"

        // Act
        val result = dataSource.getUserById(invalidId, "")

        // Assert
        Truth.assertThat(result).isNull()
    }

    @Test
    fun registerUser_insertDocument_DocumentExists() = runTest {
        // Arrange
        val user = RegisterUserDto(
            name = "name",
            username = "username",
            birthdate = "1990/01/01",
            FCMToken = "token"
        )

        // Act
        dataSource.registerUser(user, "999")

        // Assert
        val result = dataSource.getUserById("999", "")
        Truth.assertThat(result).isNotNull()
        Truth.assertThat(result?.name).isEqualTo("name")
        Truth.assertThat(result?.id).isEqualTo("999")



    }

    @Test
    fun followOrUnfollowUser_followUser_UsersFollowed() = runTest {
        // Arrange
        val currentUser = generateUser(1)
        val targetUser = generateUser(2)

        val oldData = dataSource.getUserById(targetUser.id, currentUser.id)


        // Act
        dataSource.followOrUnfollowUser(currentUser.id, targetUser.id)

        // Assert
        val targetUserResult = dataSource.getUserById(targetUser.id, currentUser.id)
        Truth.assertThat(targetUserResult?.followersCount).isGreaterThan(oldData?.followersCount)
        Truth.assertThat(targetUserResult?.followed).isTrue()
    }

    @Test
    fun followOrUnfollowUser_followUser_followersCountIncrement() = runTest {
        // Arrange: usuarios ya creados en setUp()
        val currentUser = generateUser(1)
        val targetUser = generateUser(2)

        // Act
        val result = dataSource.followOrUnfollowUser(currentUser.id, targetUser.id)

        // Assert
        Truth.assertThat(result.isSuccess).isTrue()

        val targetUserResult = dataSource.getUserById(targetUser.id, currentUser.id)
        Truth.assertThat(targetUserResult).isNotNull()
        Truth.assertThat(targetUserResult?.followed).isTrue()

        // verificar que existen los docs en subcolecciones
        val followerDoc = db.collection("users").document(targetUser.id)
            .collection("followers").document(currentUser.id).get().await()
        val followingDoc = db.collection("users").document(currentUser.id)
            .collection("followings").document(targetUser.id).get().await()

        Truth.assertThat(followerDoc.exists()).isTrue()
        Truth.assertThat(followingDoc.exists()).isTrue()

        // verificar que counts fueron incrementados (si antes eran 0 -> ahora >=1)
        val freshTarget = db.collection("users").document(targetUser.id).get().await()
        val followersCount = freshTarget.getLong("followersCount") ?: 0L
        Truth.assertThat(followersCount).isAtLeast(1L)
    }


    @Test
    fun followOrUnfollowUser_unfollow_followedFalse() = runTest {
        // Arrange
        val currentUser = generateUser(1)
        val targetUser = generateUser(2)

        // Act: follow
        dataSource.followOrUnfollowUser(currentUser.id, targetUser.id)
        dataSource.followOrUnfollowUser(currentUser.id, targetUser.id)

        // Assert
        val targetUserResult = dataSource.getUserById(targetUser.id, currentUser.id)
        Truth.assertThat(targetUserResult?.followed).isFalse()

    }

    @Test
    fun getUserReviews_validId_correctReviews() = runTest {
        // Arrange
        val user = generateUser(1)

        val createReviewDto = CreateReviewDto(
            userId = user.id,
            placeName = "Santorini GastroBar",
            reviewText = "Excelente comida y ambiente.",
            parentReviewId = null,
            placeImage = "https://example.com/santorini.jpg",
            user = UserProfileDto(
                id = user.id,
                username = user.username,
                name = user.name,
                profileImage = "https://example.com/profile.jpg"
            ),
            gastroBarId = "gastro123"
        )
        reviewDataSource.createReview(createReviewDto)
        
        val reviews = reviewDataSource.getReviewsByUser(user.id)
        Truth.assertThat(reviews).isNotEmpty()
        Truth.assertThat(reviews.first().reviewText).isEqualTo(createReviewDto.reviewText)



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

