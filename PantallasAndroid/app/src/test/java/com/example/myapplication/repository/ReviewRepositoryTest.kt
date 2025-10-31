package com.example.myapplication.repository

import android.util.Log
import com.example.myapplication.data.ReviewInfo
import com.example.myapplication.data.datasource.AuthRemoteDataSource
import com.example.myapplication.data.datasource.impl.firestore.ReviewFireStoreDataSourceImpl
import com.example.myapplication.data.datasource.impl.firestore.UserFirestoreDataSourceImpl
import com.example.myapplication.data.dtos.CreateReviewDto
import com.example.myapplication.data.dtos.ReviewDto
import com.example.myapplication.data.dtos.UserProfileDto
import com.example.myapplication.data.dtos.UserFirestoreDto
import com.example.myapplication.data.dtos.toReviewInfo
import com.example.myapplication.data.repository.ReviewRepository
import com.google.common.truth.Truth.assertThat
import io.mockk.CapturingSlot
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.slot
import kotlinx.coroutines.test.runTest
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Before
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Response

class ReviewRepositoryTest {

    private val reviewDs = mockk<ReviewFireStoreDataSourceImpl>()
    private val userDs = mockk<UserFirestoreDataSourceImpl>()
    private val authDs = mockk<AuthRemoteDataSource>(relaxed = true)

    private val repository = ReviewRepository(reviewDs, userDs, authDs)

    @Before
    fun setup() {
        mockkStatic(Log::class)
        every { Log.d(any(), any()) } returns 0
        every { Log.e(any(), any(), any()) } returns 0
        every { Log.w(any(), any(), any()) } returns 0
        //every { Log.w(any(), any()) } returns 0

        coEvery { authDs.currentUser?.uid } returns "uid-actual"
        mockkStatic("com.example.myapplication.data.dtos.ReviewDtoKt")
    }

    // -----------------------
    // createReview
    // -----------------------

    @Test
    fun `createReview - si el DTO ya trae user, NO consulta Firestore y usa ese user`() = runTest {
        val dtoConUser = CreateReviewDto(
            userId = "u1",
            placeName = "Santorini",
            reviewText = "Excelente sitio",
            parentReviewId = null,
            placeImage = "https://img/place.png",
            user = UserProfileDto(
                id = "u1",
                username = "juan",
                name = "Juan Pérez",
                profileImage = "https://img/jp.png"
            ),
            gastroBarId = "g1"
        )

        // Si alguien intenta consultar Firestore para el user, fallamos el test
        coEvery { userDs.getUserById(any(), any()) } coAnswers {
            throw AssertionError("getUserById NO debe ser llamado cuando el DTO ya trae user")
        }

        // Capturamos el DTO que realmente se envía a createReview
        val capturado: CapturingSlot<CreateReviewDto> = slot()
        coEvery { reviewDs.createReview(capture(capturado)) } returns Unit

        val result = repository.createReview(dtoConUser)

        assertThat(result.isSuccess).isTrue()
        // Afirmamos que lo enviado es exactamente lo esperado
        val enviado = capturado.captured
        assertThat(enviado).isEqualTo(dtoConUser)
    }


    @Test
    fun `createReview - usuario no existe retorna failure(User not found) y NO llama createReview`() = runTest {
        val dtoSinUser = CreateReviewDto(
            userId = "no-existe",
            placeName = "X",
            reviewText = "meh",
            parentReviewId = null,
            placeImage = null,
            user = null,
            gastroBarId = "g1"
        )

        coEvery { userDs.getUserById("no-existe", "uid-actual") } returns null

        // Si se llega a llamar createReview, fallamos el test
        coEvery { reviewDs.createReview(any()) } coAnswers {
            throw AssertionError("createReview NO debe ser llamado cuando el usuario no existe")
        }

        val result = repository.createReview(dtoSinUser)

        assertThat(result.isFailure).isTrue()
        assertThat(result.exceptionOrNull()?.message).isEqualTo("User not found")
    }

    @Test
    fun `createReview - HttpException retorna failure`() = runTest {
        val dtoConUser = CreateReviewDto(
            userId = "u1",
            placeName = "G",
            reviewText = "x",
            parentReviewId = null,
            placeImage = null,
            user = UserProfileDto("u1","u","U", null),
            gastroBarId = "g1"
        )

        val httpEx = mockk<HttpException>()
        coEvery { reviewDs.createReview(any()) } throws httpEx

        val result = repository.createReview(dtoConUser)

        assertThat(result.isFailure).isTrue()
        assertThat(result.exceptionOrNull()).isSameInstanceAs(httpEx)
    }

    // -----------------------
    // updateReview
    // -----------------------

    @Test
    fun `updateReview - success`() = runTest {
        // Capturamos para asegurar que se envía lo esperado (sin verify)
        val capturado: CapturingSlot<CreateReviewDto> = slot()
        coEvery { reviewDs.updateReview("r1", capture(capturado)) } returns Unit

        val dto = CreateReviewDto(
            userId = "u1",
            placeName = "Santorini",
            reviewText = "Editado",
            parentReviewId = null,
            placeImage = null,
            user = null,
            gastroBarId = "g1"
        )

        val result = repository.updateReview("r1", dto)

        assertThat(result.isSuccess).isTrue()
        assertThat(capturado.captured).isEqualTo(dto)
    }

    @Test
    fun `updateReview - HttpException retorna failure`() = runTest {
        val httpEx = mockk<HttpException>()
        coEvery { reviewDs.updateReview(any(), any()) } throws httpEx

        val dto = CreateReviewDto(
            userId = "uX",
            placeName = "P",
            reviewText = "T",
            parentReviewId = null,
            placeImage = null,
            user = null,
            gastroBarId = null
        )

        val result = repository.updateReview("rX", dto)

        assertThat(result.isFailure).isTrue()
        assertThat(result.exceptionOrNull()).isSameInstanceAs(httpEx)
    }

    // -----------------------
    // deleteReview
    // -----------------------

    @Test
    fun `deleteReview - success`() = runTest {
        coEvery { reviewDs.deleteReview("r1") } returns Unit

        val result = repository.deleteReview("r1")

        assertThat(result.isSuccess).isTrue()
    }

    @Test
    fun `deleteReview - Exception retorna failure`() = runTest {
        val ex = RuntimeException("boom")
        coEvery { reviewDs.deleteReview(any()) } throws ex

        val result = repository.deleteReview("r1")

        assertThat(result.isFailure).isTrue()
        assertThat(result.exceptionOrNull()).isSameInstanceAs(ex)
    }

    // -----------------------
    // sendOrDeleteReviewLike
    // -----------------------

    @Test
    fun `sendOrDeleteReviewLike - success`() = runTest {
        // Capturamos argumentos para asegurar valores (sin verify)
        var capturedReviewId: String? = null
        var capturedUserId: String? = null
        coEvery { reviewDs.sendOrDeleteReviewLike(any(), any()) } coAnswers {
            capturedReviewId = firstArg()
            capturedUserId = secondArg()
            Unit
        }

        val result = repository.sendOrDeleteReviewLike("r1", "u1")

        assertThat(result.isSuccess).isTrue()
        assertThat(capturedReviewId).isEqualTo("r1")
        assertThat(capturedUserId).isEqualTo("u1")
    }

    @Test
    fun `sendOrDeleteReviewLike - HttpException retorna failure`() = runTest {
        val httpEx = mockk<HttpException>()
        coEvery { reviewDs.sendOrDeleteReviewLike(any(), any()) } throws httpEx

        val result = repository.sendOrDeleteReviewLike("r1", "u1")

        assertThat(result.isFailure).isTrue()
        assertThat(result.exceptionOrNull()).isSameInstanceAs(httpEx)
    }
    @Test
    fun `getReviewsByUser - success mapea 1 item y retorna Result_success`() = runTest {
        val dto = ReviewDto(
            id = "r1",
            userId = "u1",
            placeName = "Santorini",
            imagePlace = "https://img/1.png",
            reviewText = "Buen sitio",
            likes = 10,
            comments = 2,
            parentReviewId = null,
            createdAt = "2025-10-30T18:00:00Z",
            updatedAt = "2025-10-30T18:00:00Z",
            user = UserProfileDto("u1","juan","Juan Pérez","https://img/jp.png"),
            gastroBarId = "g1",
            gastroBar = null,
            liked = true
        )

        coEvery { reviewDs.getReviewsByUser("u1") } returns listOf(dto)

        val mapped = mockk<ReviewInfo>()
        every { dto.toReviewInfo() } returns mapped

        //Act
        val result = repository.getReviewsByUser("u1")

        //Assert
        assertThat(result.isSuccess).isTrue()
        val list = result.getOrNull()
        assertThat(list).isNotNull()
        assertThat(list!!.size).isEqualTo(1)
        assertThat(list.single()).isSameInstanceAs(mapped)
    }

    @Test
    fun `getReviewsByUser - Exception general retorna Result_failure`() = runTest {
        val ex = RuntimeException("boom")
        coEvery { reviewDs.getReviewsByUser(any()) } throws ex

        val result = repository.getReviewsByUser("u1")

        assertThat(result.isFailure).isTrue()
        assertThat(result.exceptionOrNull()).isSameInstanceAs(ex)
    }

    @Test
    fun `getReviewsByGastroBar - success mapea 1 item y retorna Result_success`() = runTest {
        // DTO real
        val dto = ReviewDto(
            id = "r1",
            userId = "u1",
            placeName = "Santorini",
            imagePlace = "https://img/1.png",
            reviewText = "Buen sitio",
            likes = 7,
            comments = 1,
            parentReviewId = null,
            createdAt = "2025-10-30T18:00:00Z",
            updatedAt = "2025-10-30T18:00:00Z",
            user = UserProfileDto("u1","juan","Juan Pérez","https://img/jp.png"),
            gastroBarId = "g1",
            gastroBar = null,
            liked = true
        )

        // El datasource devuelve 1 review del gastrobar
        coEvery { reviewDs.getReviewsByGastroBar("g1") } returns listOf(dto)

        // Mock del mapper → ReviewInfo “esperado”
        val mapped = mockk<ReviewInfo>()
        every { dto.toReviewInfo() } returns mapped

        // Act
        val result = repository.getReviewsByGastroBar("g1")

        // Assert
        assertThat(result.isSuccess).isTrue()
        val list = result.getOrNull()
        assertThat(list).isNotNull()
        assertThat(list!!.size).isEqualTo(1)
        assertThat(list.single()).isSameInstanceAs(mapped)
    }

    @Test
    fun `getReviewsByGastroBar - HttpException retorna Result_failure`() = runTest {
        val errorBody = """{"error":"boom"}""".toResponseBody("application/json".toMediaTypeOrNull())
        val errorResponse = Response.error<Any>(502, errorBody) // 502 para el ejemplo
        val httpEx = HttpException(errorResponse)

        coEvery { reviewDs.getReviewsByGastroBar(any()) } throws httpEx

        val result = repository.getReviewsByGastroBar("gX")

        assertThat(result.isFailure).isTrue()
        assertThat(result.exceptionOrNull()).isInstanceOf(HttpException::class.java)
        assertThat((result.exceptionOrNull() as HttpException).code()).isEqualTo(502)
    }


}
