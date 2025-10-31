package com.example.myapplication.viewModels

import android.text.TextUtils
import android.util.Log
import com.example.myapplication.data.repository.AuthRepository
import com.example.myapplication.data.repository.UserRepository
import com.example.myapplication.ui.register.RegisterViewModel
import com.google.common.base.Verify.verify
import com.google.common.truth.Truth.assertThat
import io.mockk.Called
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkAll
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

class RegisterViewModelUnitTest {

    private lateinit var viewModel: RegisterViewModel
    private lateinit var authRepository: AuthRepository
    private lateinit var userRepository: UserRepository
    private val testDispatcher = StandardTestDispatcher()
@OptIn(ExperimentalCoroutinesApi::class)
@Before
fun setup(){
    Dispatchers.setMain(testDispatcher)
    mockkStatic(Log::class)
    every { Log.d(any(), any()) } returns 0
    every { Log.i(any(), any()) } returns 0
   // every { Log.w(any(), any()) } returns 0
    every { Log.e(any(), any()) } returns 0
    every { Log.e(any(), any(), any<Throwable>()) } returns 0

    mockkStatic(TextUtils::class)
    every { TextUtils.isEmpty(any()) } answers { firstArg<CharSequence?>().isNullOrEmpty() }

    authRepository = mockk()
    userRepository = mockk()
    viewModel = RegisterViewModel(
        authRepository = authRepository,
        userRepository = userRepository,
        ioDispatcher = testDispatcher,
        blockingDispatcher = testDispatcher
    )
    }

    @Test
    fun register_success_createUserAndUpdateUI() = runTest(testDispatcher) {
        viewModel.updatePassword("123456")
        viewModel.updateName("test")
        viewModel.updateUsername("test")
        viewModel.updateBirthdate("1990/01/01")

        coEvery { authRepository.register(any(), any()) } returns Result.success(Unit)
        coEvery { authRepository.currentUser?.uid } returns "1"
        coEvery { userRepository.registerUser(any(), any(), any(), any(), any()) } returns Result.success(Unit)

        viewModel.onRegisterSucces()
        advanceUntilIdle()

        val navigate = viewModel.uiState.map { it.navigate }.first { it }
        assertThat(navigate).isTrue()

        val state = viewModel.uiState.value
        assertThat(state.navigate).isTrue()
        assertThat(state.errorMessage).isEmpty()
    }

    @Test
    @OptIn(ExperimentalCoroutinesApi::class)
    fun register_alreadyUsedEmail_showsErrorMessage() = runTest(testDispatcher) {
        viewModel.updateEmail("test@example.com")
        viewModel.updatePassword("123456")
        viewModel.updateName("Juan")
        viewModel.updateUsername("juanito")
        viewModel.updateBirthdate("1990/01/01")

        coEvery { authRepository.register(any(), any()) } returns
                Result.failure(Exception("Ya existe una cuenta con ese correo."))

        viewModel.register(onSuccess = {}, onError = {})
        advanceUntilIdle()

        val finalState = viewModel.uiState.first { !it.isLoading || it.errorMessage?.isNotEmpty() == true }
        assertThat(finalState.navigate).isFalse()
        assertThat(finalState.errorMessage).isNotEmpty()
        assertThat(finalState.errorMessage).isEqualTo("Ya existe una cuenta con ese correo.")
    }

    @Test
    fun register_nameEmpty_showsError() = runTest(testDispatcher) {
        // Arrange
        viewModel.updateName("")
        viewModel.updateUsername("usuarioOK")
        viewModel.updateBirthdate("1990/01/01")
        viewModel.updateEmail("ok@example.com")
        viewModel.updatePassword("123456")

        // Act
        viewModel.register(onSuccess = {}, onError = {})

        // Assert (validación es inmediata)
        val state = viewModel.uiState.value
        assertThat(state.navigate).isFalse()
        assertThat(state.errorMessage).isEqualTo("El nombre es obligatorio.")
    }


    @After
    fun tearDown(){
        unmockkAll()
        Dispatchers.resetMain()
    }
}