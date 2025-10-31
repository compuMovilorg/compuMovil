package com.example.myapplication.e2e

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.test.performTextClearance
import androidx.compose.ui.test.performTextInput
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
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class ModifyUserE2E {
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
            authRepository.register("prueba@prueba.com", "123456")
            authRepository.login("prueba@prueba.com", "123456")

            val userId = authRepository.currentUser?.uid ?: return@runBlocking

            userRepository.registerUser(
                name = "Nuevo",
                username = "Prueba",
                birthdate = "1990/01/01",
                userId = userId,
                FCMToken = ""
            )
        }
    }
    @Test
    fun updateUserInfo_InfoUpdated(){
        composeRule.onNodeWithContentDescription(Screen.MainUser.route).performClick()
        composeRule.onNodeWithTag("main_user_screen").assertIsDisplayed()

        composeRule.waitUntil(timeoutMillis = 10_000) {
            composeRule.onAllNodesWithTag("btn_profile_settings", useUnmergedTree = true)
                .fetchSemanticsNodes().isNotEmpty()
        }

        composeRule.onNodeWithTag("btn_profile_settings", useUnmergedTree = true)
            .performClick()

        composeRule.onNodeWithTag("profile_screen").assertIsDisplayed()
        composeRule.onNodeWithTag("btn_edit_profile").performClick()

        composeRule.onNodeWithTag("edit_name_field").assertTextContains("Nuevo",substring = true)
        composeRule.onNodeWithTag("edit_name_field").performTextClearance()

        composeRule.onNodeWithTag("edit_name_field").performTextInput("Modificado")
        composeRule.onNodeWithTag("edit_name_field").assertTextContains("Modificado",substring = true)
        composeRule.onNodeWithTag("btn_save").performClick()
        composeRule.onNodeWithContentDescription(Screen.MainUser.route).performClick()
       // composeRule.onNodeWithTag("edit_name_field").assertTextContains("Modificado",substring = true)
    }

    @After
    fun tearDown() = runTest {
        val user = Firebase.auth.currentUser
        if (user != null) {
            Firebase.auth.signOut()
        }
        user?.delete()?.await()
        Firebase.firestore.collection("users").document("prueba@prueba.com").delete().await()
    }
}