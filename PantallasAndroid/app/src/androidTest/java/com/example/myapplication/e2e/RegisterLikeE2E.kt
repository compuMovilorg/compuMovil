package com.example.myapplication.e2e

import androidx.compose.ui.semantics.getOrNull
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.ComposeContentTestRule
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.espresso.Espresso.pressBack
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.myapplication.MainActivity
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
class RegisterLikeE2E {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeRule = createAndroidComposeRule<MainActivity>()

    // Bandera para saber si en este test se creó usuario y así borrarlo en @After
    private var createdUser = false

    @Before
    fun setup() {
        hiltRule.inject()
        try {
            Firebase.auth.useEmulator("10.0.2.2", 9099)
            Firebase.firestore.useEmulator("10.0.2.2", 8080)
        } catch (_: Exception) { /* no-op */ }

        // Asegura sesión cerrada al iniciar el test
        runBlocking {
            try { Firebase.auth.signOut() } catch (_: Exception) {}
        }
    }

    companion object {
        private const val BAR_ID = "gb_demo_1"
        private val ROOT_REVIEW_IDS = listOf("root_r1")
        private val SUB_REVIEW_IDS = listOf("r1", "r2")
        @Volatile private var didSeed = false

        @JvmStatic
        @BeforeClass
        fun seedEmulator() = runBlocking {
            // Conecta a emuladores (ignora si ya se configuró)
            try {
                Firebase.auth.useEmulator("10.0.2.2", 9099)
                Firebase.firestore.useEmulator("10.0.2.2", 8080)
            } catch (_: Exception) {}

            val db = Firebase.firestore

            // Si ya hay al menos 1 gastrobar, no sembramos de nuevo
            val hasBars = try {
                db.collection("gastroBars").limit(1).get().await().isEmpty.not()
            } catch (_: Exception) { false }
            if (hasBars) {
                didSeed = false
                return@runBlocking
            }

            val nowMs = System.currentTimeMillis()
            val nowIso = java.time.Instant.ofEpochMilli(nowMs).toString()
            val oneHourAgoIso = java.time.Instant.ofEpochMilli(nowMs - 3_600_000L).toString()
            val yesterdayIso = java.time.Instant.ofEpochMilli(nowMs - 86_400_000L).toString()

            val batch = db.batch()

            // -------------------------------
            // GastroBarDto (colección: gastroBars)
            // -------------------------------
            val barDoc = mapOf(
                "id" to BAR_ID,
                "imagePlace" to "https://picsum.photos/600/400",
                "name" to "Nocta Demo Bar",
                "rating" to 4.5f,                 // Float
                "reviewCount" to 3,               // Int
                "address" to "Cra 7 # 123-45, Bogotá",
                "hours" to "L-D 12:00–02:00",
                "cuisine" to "Fusión / Coctelería",
                "description" to "Ambiente nocturno con música en vivo y cocteles de autor.",
                "reviewId" to null                // FK opcional
            )
            val barRef = db.collection("gastroBars").document(BAR_ID)
            batch.set(barRef, barDoc)

            // -------------------------------
            // UserProfileDto (embebido dentro de ReviewDto)
            // -------------------------------
            val user1 = mapOf(
                "id" to "u1",
                "username" to "tester",
                "name" to "Tester Uno",
                "profileImage" to "https://picsum.photos/seed/u1/200/200"
            )
            val user2 = mapOf(
                "id" to "u2",
                "username" to "tester2",
                "name" to "Tester Dos",
                "profileImage" to "https://picsum.photos/seed/u2/200/200"
            )
            val user3 = mapOf(
                "id" to "u3",
                "username" to "tester3",
                "name" to "Tester Tres",
                "profileImage" to "https://picsum.photos/seed/u3/200/200"
            )

            // -------------------------------
            // GastroBarDto embebido dentro de ReviewDto
            // -------------------------------
            val barEmbedded = barDoc

            // -------------------------------
            // ReviewDto - raíz (/reviews)
            // -------------------------------
            val rootR1 = mapOf(
                "id" to "root_r1",
                "userId" to "u3",
                "placeName" to "Nocta Demo Bar",
                "imagePlace" to "https://picsum.photos/seed/revRoot1/600/400",
                "reviewText" to "La música un 10/10",
                "likes" to 4,
                "comments" to 0,
                "parentReviewId" to null,
                "createdAt" to oneHourAgoIso,     // String ISO
                "updatedAt" to oneHourAgoIso,
                "user" to user3,                  // UserProfileDto embebido
                "gastroBarId" to BAR_ID,
                "gastroBar" to barEmbedded,       // GastroBarDto embebido
                "liked" to false
            )
            batch.set(db.collection("reviews").document("root_r1"), rootR1)

            // -------------------------------
            // Reviews como subcolección del bar (/gastroBars/{id}/reviews)
            // -------------------------------
            val r1 = mapOf(
                "id" to "r1",
                "userId" to "u1",
                "placeName" to "Nocta Demo Bar",
                "imagePlace" to "https://picsum.photos/seed/rev1/600/400",
                "reviewText" to "Excelente ambiente 🎵",
                "likes" to 3,
                "comments" to 1,
                "parentReviewId" to null,
                "createdAt" to nowIso,
                "updatedAt" to nowIso,
                "user" to user1,
                "gastroBarId" to BAR_ID,
                "gastroBar" to barEmbedded,
                "liked" to false
            )

            val r2 = mapOf(
                "id" to "r2",
                "userId" to "u2",
                "placeName" to "Nocta Demo Bar",
                "imagePlace" to "https://picsum.photos/seed/rev2/600/400",
                "reviewText" to "Buenos cócteles 🍸",
                "likes" to 1,
                "comments" to 0,
                "parentReviewId" to null,
                "createdAt" to yesterdayIso,
                "updatedAt" to yesterdayIso,
                "user" to user2,
                "gastroBarId" to BAR_ID,
                "gastroBar" to barEmbedded,
                "liked" to false
            )

            batch.set(barRef.collection("reviews").document("r1"), r1)
            batch.set(barRef.collection("reviews").document("r2"), r2)

            // Commit
            batch.commit().await()
            didSeed = true
        }

        @JvmStatic
        @AfterClass
        fun clearSeed() = runBlocking {
            if (!didSeed) return@runBlocking

            try {
                Firebase.auth.useEmulator("10.0.2.2", 9099)
                Firebase.firestore.useEmulator("10.0.2.2", 8080)
            } catch (_: Exception) {}

            val db = Firebase.firestore

            // 1) Borra reviews en raíz
            try {
                val batchRoot = db.batch()
                ROOT_REVIEW_IDS.forEach { id ->
                    batchRoot.delete(db.collection("reviews").document(id))
                }
                batchRoot.commit().await()
            } catch (_: Exception) { /* no-op */ }

            // 2) Borra reviews de la subcolección del bar
            try {
                val subReviewsSnap = db.collection("gastroBars")
                    .document(BAR_ID)
                    .collection("reviews")
                    .get()
                    .await()
                if (subReviewsSnap.documents.isNotEmpty()) {
                    val batchSub = db.batch()
                    subReviewsSnap.documents.forEach { doc ->
                        // solo borra los que sembramos (opcional)
                        if (doc.id in SUB_REVIEW_IDS) batchSub.delete(doc.reference)
                    }
                    batchSub.commit().await()
                }
            } catch (_: Exception) { /* no-op */ }

            // 3) Borra el gastrobar
            try {
                db.collection("gastroBars").document(BAR_ID).delete().await()
            } catch (_: Exception) { /* no-op */ }
        }
    }

    @After
    fun tearDown() {
        // Estabiliza compose
        composeRule.waitForIdle()
        composeRule.runOnIdle { /* no-op */ }

        // Limpia el usuario creado en este test (borrar ANTES de signOut)
        runBlocking {
            try {
                if (createdUser) {
                    Firebase.auth.currentUser?.let { user ->
                        try {
                            user.reload().await() // asegura credenciales frescas en emulador
                        } catch (_: Exception) {}
                        try {
                            user.delete().await()
                        } catch (_: Exception) {
                            // Si falla (p.ej. perdió estado), intenta reloguear en emulador si fuera necesario
                        }
                    }
                }
            } finally {
                try { Firebase.auth.signOut() } catch (_: Exception) {}
            }
        }
    }

    @Test
    fun newUser_registerFixPassword_navigateFeed_likeAndUnlikeReview() {
        composeRule.onNodeWithTag("btn_register").performClick()

        composeRule.onNodeWithTag("name_field").performTextInput("Admin")
        composeRule.onNodeWithTag("username_field").performTextInput("adminUser")
        composeRule.onNodeWithTag("birthdate_field", useUnmergedTree = true)
            .performTextInput("1990/01/01")

        composeRule.onNodeWithTag("email_field").performTextInput("admin@example.com")

        composeRule.onNodeWithTag("password_field")
            .performScrollTo()
            .performTextInput("1234") // inválida

        composeRule.onNodeWithTag("btn_create_account").performClick()

        // 3) Verificar mensaje de error por contraseña corta
        composeRule.waitUntil(timeoutMillis = 10_000) {
            composeRule.onAllNodesWithTag("register_error_text")
                .fetchSemanticsNodes().isNotEmpty()
        }
        composeRule.onNodeWithTag("register_error_text").assertIsDisplayed()

        // 4) Corregir password y registrar
        composeRule.onNodeWithTag("password_field")
            .performScrollTo()
            .performTextClearance()

        composeRule.onNodeWithTag("password_field")
            .performTextInput("123456")

        composeRule.onNodeWithTag("btn_create_account")
            .performScrollTo()
            .performClick()

        composeRule.waitForIdle()

        // Marca que se creó usuario (si llega a Home se asume registro OK)
        createdUser = true

        composeRule.waitUntil(timeoutMillis = 20_000) {
            composeRule.onAllNodesWithTag("home_screen")
                .fetchSemanticsNodes().isNotEmpty()
        }
        composeRule.onNodeWithTag("home_screen").assertIsDisplayed()
        composeRule.waitForIdle()
        composeRule.waitUntil(timeoutMillis = 20_000) {
            composeRule.onAllNodesWithTag("review_item", useUnmergedTree = true)
                .fetchSemanticsNodes().isNotEmpty()
        }
        Thread.sleep(1000)
        composeRule.onAllNodesWithTag("review_item", useUnmergedTree = true)
            .onFirst()
            .performClick()
        composeRule.waitForIdle()

        composeRule.onNodeWithTag("detail_screen", useUnmergedTree = true).assertIsDisplayed()
        Thread.sleep(1000)
        composeRule.onNodeWithTag("btn_view_reviews_gb_demo_1", useUnmergedTree = true)
            .performScrollTo()
            .performClick()
        Thread.sleep(1000)

        composeRule.onNodeWithTag("bar_reviews_screen_gb_demo_1", useUnmergedTree = true).assertIsDisplayed()

// Espera a que haya ítems
        composeRule.waitUntil(20_000) {
            composeRule.onAllNodesWithTag("review_item", useUnmergedTree = true)
                .fetchSemanticsNodes().isNotEmpty()
        }

        val id = "root_r1"
        val before = composeRule.getIntByTag("review_like_count_$id", useUnmergedTree = true)

        composeRule.onNodeWithTag("review_like_btn_$id", useUnmergedTree = true)
            .performScrollTo()
            .performClick()

        composeRule.waitUntil(5_000) {
            composeRule.getIntByTag("review_like_count_$id", useUnmergedTree = true) == before + 1

        }
        Thread.sleep(2000)

        pressBack() // vuelve desde BarReviews -> Detail
        composeRule.waitForIdle()
        composeRule.onNodeWithTag("detail_screen", useUnmergedTree = true).assertIsDisplayed()

        composeRule.onNodeWithTag("btn_view_reviews_gb_demo_1", useUnmergedTree = true)
            .performScrollTo()
            .performClick()
        composeRule.waitForIdle()
        composeRule.onNodeWithTag("bar_reviews_screen_gb_demo_1", useUnmergedTree = true).assertIsDisplayed()

        composeRule.waitUntil(20_000) {
            composeRule.onAllNodesWithTag("review_item", useUnmergedTree = true)
                .fetchSemanticsNodes().isNotEmpty()
        }

        composeRule.getIntByTag("review_like_count_$id", useUnmergedTree = true)
        composeRule.onNodeWithTag("review_like_btn_$id", useUnmergedTree = true)
            .performScrollTo()
            .performClick()
        composeRule.waitForIdle()
    }

    // --- Helpers para leer texto/enteros desde un testTag ---
    private fun SemanticsNodeInteraction.readTextOrEmpty(): String {
        val node = this.fetchSemanticsNode()
        val text = node.config.getOrNull(androidx.compose.ui.semantics.SemanticsProperties.Text)
            ?.joinToString(separator = "") { it.text }
        if (!text.isNullOrEmpty()) return text
        val contentDesc = node.config.getOrNull(androidx.compose.ui.semantics.SemanticsProperties.ContentDescription)
            ?.joinToString(separator = "") { it }
        return contentDesc ?: ""
    }

    private fun ComposeContentTestRule.getTextByTag(tag: String, useUnmergedTree: Boolean = true): String {
        return onNodeWithTag(tag, useUnmergedTree).readTextOrEmpty()
    }

    private fun ComposeContentTestRule.getIntByTag(tag: String, useUnmergedTree: Boolean = true): Int {
        val raw = getTextByTag(tag, useUnmergedTree)
        // Extrae dígitos por si el texto incluye algo más
        val onlyDigits = raw.filter { it.isDigit() || it == '-' }
        return onlyDigits.toIntOrNull() ?: 0
    }

}
