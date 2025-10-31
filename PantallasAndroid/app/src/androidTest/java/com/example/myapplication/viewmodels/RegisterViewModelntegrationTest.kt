package com.example.myapplication.viewmodels

import android.util.Log
import com.example.myapplication.data.datasource.AuthRemoteDataSource
import com.example.myapplication.data.datasource.impl.firestore.UserFirestoreDataSourceImpl
import com.example.myapplication.data.repository.AuthRepository
import com.example.myapplication.data.repository.UserRepository
import com.example.myapplication.ui.register.RegisterViewModel
import com.google.common.truth.Truth.assertThat
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class RegisterViewModelntegrationTest {

    private lateinit var viewModel: RegisterViewModel
    private lateinit var authRepository: AuthRepository
    private lateinit var userRepository: UserRepository

    @Before
    fun setUp() = runTest {
        try {
            Firebase.auth.useEmulator("10.0.2.2", 9099)
            Firebase.firestore.useEmulator("10.0.2.2", 8080)
        } catch (_: Exception) { /* no-op */ }

        authRepository = AuthRepository(AuthRemoteDataSource(Firebase.auth))
        userRepository = UserRepository(
            UserFirestoreDataSourceImpl(Firebase.firestore),
            AuthRemoteDataSource(Firebase.auth)
        )

        // 💡 Evita tiempo virtual: usa Default.limitedParallelism(1) como Main
        val realDispatcher = Dispatchers.Default.limitedParallelism(1)
        Dispatchers.setMain(realDispatcher)

        viewModel = RegisterViewModel(authRepository, userRepository, realDispatcher)
    }

    @Test
    @OptIn(ExperimentalCoroutinesApi::class)
    fun register_success_createUserAndUpdateUI() = runTest {
        viewModel.updateEmail("test@example.com")
        viewModel.updatePassword("123456")
        viewModel.updateName("test")
        viewModel.updateUsername("test")
        viewModel.updateBirthdate("1990/01/01")

        // Act
        viewModel.onRegisterSucces()

        val navigate = viewModel.uiState.map { it.navigate }.first { it }
        assertThat(navigate).isTrue()

        // Assert
        val state = viewModel.uiState.value
        assertThat(state.navigate).isTrue()
        assertThat(state.errorMessage).isEmpty()
    }

    @Test
    fun register_alreadyUsedEmail_showsErrorMessage() = runTest {
        // Arrange: crea la cuenta existente en el emulador
        authRepository.register("test@test.com", "123456")

        Firebase.auth.signOut()

        viewModel.updateEmail("test@test.com")
        viewModel.updatePassword("123456")
        viewModel.updateName("Juan")
        viewModel.updateUsername("juanito")
        viewModel.updateBirthdate("1990/01/01")

        // Act: dispara el flujo real de registro
        viewModel.register(onSuccess = {}, onError = { msg ->
            Log.d("RegisterTest", "onError callback -> $msg")
        })

        // Espera a que termine (isLoading -> false)
        val finalState = viewModel.uiState.first { !it.isLoading }

        // Assert
        assertThat(finalState.navigate).isFalse()
        assertThat(finalState.errorMessage).isNotEmpty()
        assertThat(finalState.errorMessage).isEqualTo("Ya existe una cuenta con ese correo.")
    }

    @Test
    fun register_emailInvalid_showsErrorMessage() = runTest {
        // Arrange
        viewModel.updateEmail("testtest.com") // <— inválido
        viewModel.updatePassword("123456")
        viewModel.updateName("Juan")
        viewModel.updateUsername("juanito")
        viewModel.updateBirthdate("1990/01/01")

        // Act
        viewModel.register(onSuccess = {}, onError = { msg ->
            Log.d("RegisterTest", "onError callback -> $msg")
        })

        // Puedes leer el estado inmediatamente:
        val finalState = viewModel.uiState.value

        // Assert
        assertThat(finalState.navigate).isFalse()
        assertThat(finalState.errorMessage).isNotEmpty()
        assertThat(finalState.errorMessage).isEqualTo("Correo inválido.")
    }


    @After
    fun tearDown() = runTest {
        val user = Firebase.auth.currentUser
        if (user != null) {
            Firebase.auth.signOut()
            user.delete().await()
        }
    }
}
