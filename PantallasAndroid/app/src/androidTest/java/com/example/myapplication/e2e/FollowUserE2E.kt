package com.example.myapplication.e2e

import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.semantics.getOrNull
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.ComposeContentTestRule
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.espresso.Espresso.pressBack
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.myapplication.MainActivity
import com.example.myapplication.data.datasource.AuthRemoteDataSource
import com.example.myapplication.data.datasource.impl.firestore.UserFirestoreDataSourceImpl
import com.example.myapplication.data.repository.AuthRepository
import com.example.myapplication.data.repository.UserRepository
import com.example.myapplication.navigation.Screen
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import org.junit.*
import org.junit.runner.RunWith

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class FollowUserE2E {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeRule = createAndroidComposeRule<MainActivity>()

    private lateinit var authRepository: AuthRepository
    private lateinit var userRepository: UserRepository

    private var user1Uid: String = ""
    private var user2Uid: String = ""
    private var seededReviewId: String = "seed_follow_r1"

    @Before
    fun setup() {
        hiltRule.inject()
        try {
            Firebase.auth.useEmulator("10.0.2.2", 9099)
            Firebase.firestore.useEmulator("10.0.2.2", 8080)
        } catch (_: Exception) { }

        val authRemoteDataSource = AuthRemoteDataSource(Firebase.auth)
        val userDatasource = UserFirestoreDataSourceImpl(Firebase.firestore)
        userRepository = UserRepository(userDatasource, authRemoteDataSource)
        authRepository = AuthRepository(authRemoteDataSource)

        runBlocking {
            // user1
            runCatching { authRepository.register("tester1@example.com", "123456") }
            runCatching { authRepository.login("tester1@example.com", "123456") }
            user1Uid = authRepository.currentUser?.uid ?: error("No se pudo obtener uid de tester1")

            userRepository.registerUser(
                name = "Tester Uno",
                username = "tester1",
                birthdate = "1990/01/01",
                userId = user1Uid,
                FCMToken = ""
            )

            // user2 + review sembrada
            runCatching { authRepository.register("tester2@example.com", "123456") }
            runCatching { authRepository.login("tester2@example.com", "123456") }
            user2Uid = authRepository.currentUser?.uid ?: error("No se pudo obtener uid de tester2")

            userRepository.registerUser(
                name = "Tester Dos",
                username = "tester2",
                birthdate = "1992/02/02",
                userId = user2Uid,
                FCMToken = ""
            )

            val db = Firebase.firestore
            val nowIso = java.time.Instant.ofEpochMilli(System.currentTimeMillis()).toString()
            val reviewDoc = mapOf(
                "id" to seededReviewId,
                "userId" to user2Uid,
                "placeName" to "Nocta Demo Bar",
                "imagePlace" to "https://picsum.photos/seed/follow1/600/400",
                "reviewText" to "Post del usuario seguido ✨",
                "likes" to 0,
                "comments" to 0,
                "parentReviewId" to null,
                "createdAt" to nowIso,
                "updatedAt" to nowIso,
                "user" to mapOf(
                    "id" to user2Uid,
                    "username" to "tester2",
                    "name" to "Tester Dos",
                    "profileImage" to "https://picsum.photos/seed/u2/200/200"
                ),
                "gastroBarId" to "gb_demo_1",
                "gastroBar" to mapOf(
                    "id" to "gb_demo_1",
                    "imagePlace" to "https://picsum.photos/600/400",
                    "name" to "Nocta Demo Bar",
                    "rating" to 4.5f,
                    "reviewCount" to 1,
                    "address" to "Cra 7 # 123-45, Bogotá",
                    "hours" to "L-D 12:00–02:00",
                    "cuisine" to "Fusión / Coctelería",
                    "description" to "Demo lugar para pruebas",
                    "reviewId" to null
                ),
                "liked" to false
            )
            db.collection("reviews").document(seededReviewId).set(reviewDoc).await()

            // volver a dejar logueado a user1
            authRepository.login("tester1@example.com", "123456")
            Assert.assertEquals("Debe quedar logueado tester1", user1Uid, authRepository.currentUser?.uid)
        }
    }

    @After
    fun tearDown() {
        runBlocking {
            val db = Firebase.firestore

            // Borra la review sembrada
            runCatching {
                db.collection("reviews").document(seededReviewId).delete().await()
            }

            // Borra user1
            runCatching {
                if (Firebase.auth.currentUser?.uid != user1Uid) {
                    authRepository.login("tester1@example.com", "123456")
                }
                Firebase.auth.currentUser?.let { u ->
                    u.reload().await()
                    u.delete().await()
                }
            }

            // Borra user2 (cambia de sesión)
            runCatching {
                authRepository.login("tester2@example.com", "123456")
                Firebase.auth.currentUser?.let { u ->
                    u.reload().await()
                    u.delete().await()
                }
            }

            // Sign out final
            runCatching { Firebase.auth.signOut() }
        }
    }

    @Test
    fun followUser_and_seeFollowedPostsInFeed() {
        // HOME
        composeRule.waitUntil(20_000) {
            composeRule.onAllNodesWithTag("home_screen", useUnmergedTree = true)
                .fetchSemanticsNodes().isNotEmpty()
        }
        composeRule.onNodeWithTag("home_screen", useUnmergedTree = true).assertIsDisplayed()

        // Reviews visibles
        composeRule.waitUntil(20_000) {
            composeRule.onAllNodesWithTag("review_item", useUnmergedTree = true)
                .fetchSemanticsNodes().isNotEmpty()
        }

        // Ir al perfil del user2 desde su header
        composeRule.onNodeWithTag("review_user_$user2Uid", useUnmergedTree = true)
            .performScrollTo()
            .performClick()

        // === ASSERT: estamos en UserScreen ===
        composeRule.waitUntil(10_000) {
            composeRule.onAllNodesWithTag("profile_screen", useUnmergedTree = true)
                .fetchSemanticsNodes().isNotEmpty()
        }
        composeRule.onNodeWithTag("profile_screen", useUnmergedTree = true).assertIsDisplayed()

        // (Opcional) Asegura que el nombre renderizó
        composeRule.onNodeWithText("Tester Dos", substring = true, ignoreCase = true).assertIsDisplayed()

        // Follow (usar tag literal, los tags se definen en otro lado)
        composeRule.onNodeWithTag("btn_follow", useUnmergedTree = true)
            .performScrollTo()
            .performClick()

        // Espera 2 segundos para dar tiempo a actualización visual / backend
        Thread.sleep(2000)

        // Volver a HOME
        pressBack()
        composeRule.waitForIdle()
        composeRule.onNodeWithTag("home_screen", useUnmergedTree = true).assertIsDisplayed()

        Thread.sleep(2000)

        composeRule.onNodeWithContentDescription(Screen.MainUser.route, useUnmergedTree = true)
            .performClick()


        composeRule.waitUntil(10_000) {
            composeRule.onAllNodesWithTag("main_user_screen", useUnmergedTree = true)
                .fetchSemanticsNodes().isNotEmpty()
        }
        composeRule.onNodeWithTag("main_user_screen", useUnmergedTree = true).assertIsDisplayed()

// === Click en "Siguiendo: N" (btn_following) ===
        composeRule.onNodeWithTag("btn_following", useUnmergedTree = true)
            .performScrollTo()
            .performClick()

// === ASSERT: se abre FollowingUsers y aparece el usuario seguido (tester2) ===
        composeRule.waitUntil(10_000) {
            composeRule.onAllNodesWithText("tester2", substring = true, ignoreCase = true)
                .fetchSemanticsNodes().isNotEmpty()
        }
        composeRule.onNodeWithText("tester2", substring = true, ignoreCase = true)
            .assertIsDisplayed()

        Thread.sleep(2000)

        composeRule.onNodeWithTag("following_user_$user2Uid", useUnmergedTree = true)
            .performScrollTo()
            .performClick()

        composeRule.waitUntil(10_000) {
            composeRule.onAllNodesWithTag("profile_screen", useUnmergedTree = true)
                .fetchSemanticsNodes().isNotEmpty()
        }
        composeRule.onNodeWithTag("profile_screen", useUnmergedTree = true).assertIsDisplayed()

        composeRule.onNodeWithText("tester2", substring = true, ignoreCase = true).assertIsDisplayed()

    }

    // ==== HELPERS ====
    private fun ComposeContentTestRule.existsTag(tag: String, useUnmergedTree: Boolean = true): Boolean =
        try {
            this.onAllNodesWithTag(tag, useUnmergedTree).fetchSemanticsNodes().isNotEmpty()
        } catch (_: Throwable) { false }

    private fun SemanticsNodeInteraction.readTextOrEmpty(): String {
        val node = this.fetchSemanticsNode()
        val text = node.config.getOrNull(SemanticsProperties.Text)
            ?.joinToString("") { it.text }
        if (!text.isNullOrEmpty()) return text
        val contentDesc = node.config.getOrNull(SemanticsProperties.ContentDescription)
            ?.joinToString("") { it }
        return contentDesc ?: ""
    }

    private fun ComposeContentTestRule.getTextByTag(tag: String, useUnmergedTree: Boolean = true): String {
        return onNodeWithTag(tag, useUnmergedTree).readTextOrEmpty()
    }

    private fun ComposeContentTestRule.getIntByTag(tag: String, useUnmergedTree: Boolean = true): Int {
        val raw = getTextByTag(tag, useUnmergedTree)
        val onlyDigits = raw.filter { it.isDigit() || it == '-' }
        return onlyDigits.toIntOrNull() ?: 0
    }
}
