package com.example.myapplication.e2e

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.test.performTextInput
import com.example.myapplication.MainActivity
import com.example.myapplication.data.datasource.AuthRemoteDataSource
import com.example.myapplication.data.datasource.impl.firestore.UserFirestoreDataSourceImpl
import com.example.myapplication.data.repository.AuthRepository
import com.example.myapplication.data.repository.UserRepository
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class RegisterNewUserE2E {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeRule = createAndroidComposeRule<MainActivity>()

    private lateinit var authRepository: AuthRepository
    private lateinit var userRepository: UserRepository

    @Before
    fun setup() {
        hiltRule.inject()
        try {
            Firebase.auth.useEmulator("10.0.2.2", 9099)
            Firebase.firestore.useEmulator("10.0.2.2", 8080)
        } catch (e: Exception) {

        }

        val authRemoteDataSource = AuthRemoteDataSource(Firebase.auth)
        val userDatasource = UserFirestoreDataSourceImpl(Firebase.firestore)
        userRepository = UserRepository(userDatasource, authRemoteDataSource)
        authRepository = AuthRepository(authRemoteDataSource)
        runBlocking {
            authRepository.register("admin@admin.com", "123456")
            authRepository.signOut()
        }
    }

    @Test
    fun navigate_fromStart_toLogin() {
        composeRule.onNodeWithTag("btn_login").performClick()

        composeRule.waitUntil(timeoutMillis = 5_000) {
            try {
                composeRule.onNodeWithTag("login_screen").assertIsDisplayed()
                true
            } catch (_: AssertionError) {
                false
            }
        }
    }


    @Test
    fun registerUser_shortPassword_showMessage() {
        composeRule.onNodeWithTag("btn_register").performClick()

        composeRule.onNodeWithTag("name_field").performTextInput("Admin")
        composeRule.onNodeWithTag("username_field").performTextInput("adminUser")
        composeRule.onNodeWithTag("birthdate_field").performTextInput("1990/01/01")
        composeRule.onNodeWithTag("email_field").performTextInput("admin@example.com")
        composeRule.onNodeWithTag("password_field").performTextInput("123") // corta

        composeRule.onNodeWithTag("btn_create_account").performClick()

        composeRule.onNodeWithTag("register_error_text").assertIsDisplayed()
    }

    @Test
    fun registerUser_usedEmail_showMessage() {
        composeRule.onNodeWithTag("btn_register").performClick()

        composeRule.onNodeWithTag("name_field", useUnmergedTree = true).performTextInput("Admin")
        composeRule.onNodeWithTag("username_field", useUnmergedTree = true).performTextInput("adminUser")
        composeRule.onNodeWithTag("birthdate_field", useUnmergedTree = true).performTextInput("1990/01/01")
        composeRule.onNodeWithTag("email_field", useUnmergedTree = true).performTextInput("admin@admin.com")

        composeRule.onNodeWithTag("password_field", useUnmergedTree = true)
            .performScrollTo()
            .performTextInput("123456")

        composeRule.onNodeWithTag("btn_create_account").performClick()
        composeRule.onNodeWithTag("register_error_text").assertIsDisplayed()
    }

    @Test
    fun registerUser_allValidInputs_navigateHome() {
        composeRule.onNodeWithTag("btn_register").performClick()

        composeRule.onNodeWithTag("name_field").performTextInput("Admin")
        composeRule.onNodeWithTag("username_field").performTextInput("adminUser")
        composeRule.onNodeWithTag("birthdate_field", useUnmergedTree = true)
            .performTextInput("1990/01/01")

        composeRule.onNodeWithTag("email_field").performTextInput("admin@example.com")

        composeRule.onNodeWithTag("password_field")
            .performScrollTo()
            .performTextInput("123456")

        composeRule.onNodeWithTag("btn_create_account").performClick()
        composeRule.waitForIdle()

        composeRule.waitUntil(timeoutMillis = 10_000) {
            composeRule.onAllNodesWithTag("home_screen").fetchSemanticsNodes().isNotEmpty()
        }
        composeRule.onNodeWithTag("home_screen").assertIsDisplayed()
    }
    @After
    fun tearDown() {
        // Asegura que no haya recomposiciones pendientes
        composeRule.waitForIdle()
        composeRule.runOnIdle { /* no-op: garantiza que el owner esté estable */ }

        // Operaciones de Firebase en un blocking seguro
        runBlocking {
            Firebase.auth.currentUser?.let { user ->
                try {
                    Firebase.auth.signOut()
                } catch (_: Exception) {}
                try {
                    user.delete().await()
                } catch (_: Exception) {}
            }
        }
    }

}