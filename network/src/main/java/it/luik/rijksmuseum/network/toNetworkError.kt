package it.luik.rijksmuseum.network

import it.luik.rijksmuseum.network.StatusCodes.AUTH_ERRORS
import it.luik.rijksmuseum.network.StatusCodes.BAD_REQUEST
import it.luik.rijksmuseum.network.StatusCodes.CLIENT_ERRORS
import it.luik.rijksmuseum.network.StatusCodes.SERVER_ERRORS
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response
import timber.log.Timber

suspend inline fun <reified T> mapResult(
    crossinline networkCall: suspend () -> Response<T>,
): Result<T> = withContext(Dispatchers.IO) {
    try {
        networkCall().mapResult()
    } catch (e: Exception) {
        Timber.w(e)
        Result.failure(e)
    }
}

fun <T> Response<T>.mapResult(): Result<T> {
    return when {
        isSuccessful -> {
            val body = body()
            if (body == null) Result.failure(NetworkException.EmptyException())
            else Result.success(body)
        }
        else -> Result.failure(code().toNetworkException())
    }
}

private fun Int.toNetworkException() = when (this) {
    in AUTH_ERRORS -> NetworkException.AuthException()
    in CLIENT_ERRORS, BAD_REQUEST -> NetworkException.ClientException()
    in SERVER_ERRORS -> NetworkException.ServerException()
    else -> NetworkException.UnknownException()
}

@Suppress("MagicNumber")
private object StatusCodes {
    val AUTH_ERRORS = 401..403
    val CLIENT_ERRORS = 404..451
    val SERVER_ERRORS = 500..511
    const val BAD_REQUEST = 400
}

sealed class NetworkException : RuntimeException() {
    class AuthException : NetworkException()
    class ClientException : NetworkException()
    class ServerException : NetworkException()
    class UnknownException : NetworkException()
    class EmptyException : NetworkException()
}
