package com.example.myapplication.repository

import android.util.Log
import com.example.myapplication.data.datasource.AuthRemoteDataSource
import com.example.myapplication.data.datasource.impl.firestore.UserFirestoreDataSourceImpl
import com.example.myapplication.data.dtos.UserFirestoreDto
import com.example.myapplication.data.repository.UserRepository
import com.google.common.truth.Truth
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class UserRepositoryTest {

    private val mockDataSource = mockk<UserFirestoreDataSourceImpl>()
    private val authDataSource = mockk<AuthRemoteDataSource>()

    private val repository = UserRepository(mockDataSource, authDataSource)

    @Before
    fun setup() {
        // Mockea todos los métodos usados de android.util.Log
        mockkStatic(Log::class)
        every { Log.d(any(), any()) } returns 0
        every { Log.e(any(), any(), any()) } returns 0
        every { Log.w(any(), any(), any()) } returns 0
        //every { Log.w(any(), any()) } returns 0
    }

    @Test
    fun `al llamar getUserById, si el id es valido retorna result con el userInfo`() = runTest{
        //Arrange
        val dto = UserFirestoreDto(
            id = "123",
            username = "Juan",
            email = "juan@example.com",
            password = "password123",
            name = "Juan",
            birthdate = "1990-05-15",
            followersCount = 120,
            followingCount = 80,
            profileImage = "https://example.com/profile_juan.jpg",
            followed = false
        )
        coEvery { authDataSource.currentUser?.uid } returns "1"
        coEvery { mockDataSource.getUserById("123", "1") } returns dto
        //Act
        val result = repository.getUserById("123")

        //Assert
        Truth.assertThat(result.isSuccess).isTrue()
        Truth.assertThat(result.getOrNull()?.id).isEqualTo("123")
        Truth.assertThat(result.getOrNull()?.name).isEqualTo("Juan")
    }
    @Test
    fun `al llamar getUserById, si el id es invalido retorna result con failure`() = runTest {
        //Arrange
        coEvery { authDataSource.currentUser?.uid } returns "1"
        coEvery { mockDataSource.getUserById("123", "1") } returns null

        //Act
        val result = repository.getUserById("123")

        //Assert
        Truth.assertThat(result.isFailure).isTrue()
        Truth.assertThat(result.exceptionOrNull()?.message).isEqualTo("User not found")

    }
    @Test
    fun `al llamar getUserById, si el data source  devuelve campos vacios`() = runTest {
        val dto = UserFirestoreDto(
            id = "123",
            username = "",
            email = "",
            password = "",
            name = "",
            birthdate = "",
            followersCount = 0,
            followingCount = 0,
            profileImage = null,
            followed = false
        )
        //Arrange
        coEvery { authDataSource.currentUser?.uid } returns "1"
        coEvery { mockDataSource.getUserById("123", "1") } returns dto

        //Act

        val result = repository.getUserById("123")

        //Assert
        Truth.assertThat(result.isSuccess).isTrue()
        Truth.assertThat(result.getOrNull()?.id).isEqualTo("123")
        Truth.assertThat(result.getOrNull()?.profileImage).isEqualTo("No hay foto de perfil")

    }
}