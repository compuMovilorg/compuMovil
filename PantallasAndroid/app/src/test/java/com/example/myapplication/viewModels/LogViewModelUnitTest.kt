package com.example.myapplication.viewModels

import com.example.myapplication.data.repository.AuthRepository
import com.example.myapplication.data.repository.UserRepository
import com.example.myapplication.ui.log.LoginViewModel
import com.google.common.truth.Truth.assertThat
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthRecentLoginRequiredException
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import kotlin.test.Test

class LogViewModelUnitTest {

    @OptIn(ExperimentalCoroutinesApi::class)
    private val testDispatcher = StandardTestDispatcher()

    private lateinit var authRepo: AuthRepository
    private lateinit var userRepo: UserRepository
    private lateinit var viewModel: LoginViewModel

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        authRepo = mockk()
        userRepo = mockk() // no se usa en los tests actuales, pero el VM lo requiere
        viewModel = LoginViewModel(authRepo, userRepo)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @After
    fun tearDown() {
        Dispatchers.resetMain()
        unmockkAll()
    }

    // ---------------------------
    // Actualizaciones de estado
    // ---------------------------

    @Test
    fun `updateEmail actualiza el estado con el nuevo email`() {
        viewModel.updateEmail("juan@email.com")
        assertThat(viewModel.uiState.value.email).isEqualTo("juan@email.com")
    }

    @Test
    fun `updatePassword actualiza el estado con el nuevo password`() {
        viewModel.updatePassword("123456")
        assertThat(viewModel.uiState.value.password).isEqualTo("123456")
    }

    // ---------------------------
    // Validación de formulario
    // ---------------------------

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `login con formulario invalido ejecuta onError y NO llama repo`() = runTest {
        // email sin @ y password menor a 6
        viewModel.updateEmail("juanemail.com")
        viewModel.updatePassword("123")

        var errorMsg: String? = null
        var successCalled = false

        viewModel.login(
            onSuccess = { successCalled = true },
            onError = { msg -> errorMsg = msg }
        )

        // Como la validación corta el flujo, no hace falta advanceUntilIdle
        coVerify(exactly = 0) { authRepo.login(any(), any()) }
        assertThat(successCalled).isFalse()
        assertThat(errorMsg).isEqualTo("Formulario inválido")
    }

    // ---------------------------
    // Éxito
    // ---------------------------

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `login exitoso llama repo, limpia formulario y ejecuta onSuccess`() = runTest {
        // Arrange
        coEvery { authRepo.login("juan@email.com", "123456") } returns Result.success(Unit)

        viewModel.updateEmail("juan@email.com")
        viewModel.updatePassword("123456")

        var successCalled = false
        var errorMsg: String? = null

        // Act
        viewModel.login(
            onSuccess = { successCalled = true },
            onError = { msg -> errorMsg = msg }
        )
        advanceUntilIdle()

        // Assert
        coVerify(exactly = 1) { authRepo.login("juan@email.com", "123456") }
        assertThat(successCalled).isTrue()
        assertThat(errorMsg).isNull()

        // El ViewModel hace clearForm() tras éxito
        val cleared = viewModel.uiState.value
        assertThat(cleared.email).isEqualTo("")
        assertThat(cleared.password).isEqualTo("")
    }

    // ---------------------------
    // Errores mapeados
    // ---------------------------

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `login falla con FirebaseAuthInvalidUserException y mapea mensaje correcto`() = runTest {
        val ex = mockk<FirebaseAuthInvalidUserException>()
        every { ex.message } returns "X" // no se usa, el VM sobreescribe el mensaje

        coEvery { authRepo.login(any(), any()) } throws ex

        viewModel.updateEmail("a@a.com")
        viewModel.updatePassword("123456")

        var errorMsg: String? = null
        viewModel.login(
            onSuccess = { },
            onError = { msg -> errorMsg = msg }
        )
        advanceUntilIdle()

        assertThat(errorMsg).isEqualTo("El usuario no existe o fue eliminado.")
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `login falla con FirebaseAuthInvalidCredentialsException y mapea mensaje correcto`() = runTest {
        val ex = mockk<FirebaseAuthInvalidCredentialsException>()
        every { ex.message } returns "X"

        coEvery { authRepo.login(any(), any()) } throws ex

        viewModel.updateEmail("a@a.com")
        viewModel.updatePassword("123456")

        var errorMsg: String? = null
        viewModel.login(
            onSuccess = { },
            onError = { msg -> errorMsg = msg }
        )
        advanceUntilIdle()

        assertThat(errorMsg).isEqualTo("Correo o contraseña incorrectos.")
    }


    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `login falla con Exception generica y retorna el message`() = runTest {
        coEvery { authRepo.login(any(), any()) } throws Exception("Fallo genérico")

        viewModel.updateEmail("a@a.com")
        viewModel.updatePassword("123456")

        var errorMsg: String? = null
        viewModel.login(
            onSuccess = { },
            onError = { msg -> errorMsg = msg }
        )
        advanceUntilIdle()

        assertThat(errorMsg).isEqualTo("Fallo genérico")
    }
}
