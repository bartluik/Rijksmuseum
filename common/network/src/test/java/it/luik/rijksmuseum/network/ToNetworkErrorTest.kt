package it.luik.rijksmuseum.network

import kotlinx.coroutines.runBlocking
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import org.junitpioneer.jupiter.params.IntRangeSource
import retrofit2.Response

internal class ToNetworkErrorTest {

    @Test
    fun `when response is ok given body is not null then result is success`() = runBlocking {
        val result = mapResult {
            Response.success("{\"some\": \"data\" }")
        }
        assertTrue { result.isSuccess }
    }

    @Test
    fun `when response is ok given body is null then result is empty`() = runBlocking {
        val result = mapResult<String?> {
            Response.success(null)
        }
        assertTrue { result.isFailure }
        assertTrue { result.exceptionOrNull() is NetworkException.EmptyException }
    }

    @Test
    fun `when response code is bad request then result is client exception`() = runBlocking {
        val result = mapResult<String?> {
            Response.error(400, "something".toResponseBody())
        }
        assertTrue { result.isFailure }
        assertTrue { result.exceptionOrNull() is NetworkException.ClientException }
    }

    @Test
    fun `when response code is very odd then result is unknown exception`() = runBlocking {
        val result = mapResult<String?> {
            Response.error(1337, "something".toResponseBody())
        }
        assertTrue { result.isFailure }
        assertTrue { result.exceptionOrNull() is NetworkException.UnknownException }
    }

    @ParameterizedTest
    @ValueSource(ints = [401, 402, 403])
    fun `when response is auth error then result is failure with auth exception`(code: Int) =
        runBlocking {
            val result = mapResult<String?> {
                Response.error(code, "something".toResponseBody())
            }
            assertTrue { result.isFailure }
            assertTrue { result.exceptionOrNull() is NetworkException.AuthException }
        }

    @ParameterizedTest
    @IntRangeSource(from = 404, to = 451)
    fun `when response is misc client error then result is failure with client exception`(code: Int) =
        runBlocking {
            val result = mapResult<String?> {
                Response.error(code, "something".toResponseBody())
            }
            assertTrue { result.isFailure }
            assertTrue { result.exceptionOrNull() is NetworkException.ClientException }
        }


    @ParameterizedTest
    @IntRangeSource(from = 500, to = 511)
    fun `when response is server error then result is failure with server exception`(code: Int) =
        runBlocking {
            val result = mapResult<String?> {
                Response.error(code, "something".toResponseBody())
            }
            assertTrue { result.isFailure }
            assertTrue { result.exceptionOrNull() is NetworkException.ServerException }
        }
}
