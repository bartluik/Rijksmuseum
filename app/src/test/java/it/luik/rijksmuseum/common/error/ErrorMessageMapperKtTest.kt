package it.luik.rijksmuseum.common.error

import androidx.annotation.StringRes
import it.luik.rijksmuseum.R
import it.luik.rijksmuseum.common.StringResource
import it.luik.rijksmuseum.network.NetworkException
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.io.IOException
import java.util.stream.Stream

internal class ErrorMessageMapperKtTest {

    @ParameterizedTest
    @MethodSource("failureProvider")
    fun `when exception is mapped then error message is returned`(
        failure: Throwable, @StringRes errorMessage: Int
    ) {
        assertEquals(StringResource.Id(errorMessage), failure.toErrorMessage())
    }

    companion object {
        @JvmStatic
        fun failureProvider(): Stream<Arguments> {
            return Stream.of(
                Arguments.of(NetworkException.AuthException(), R.string.error_auth),
                Arguments.of(NetworkException.ClientException(), R.string.error_client),
                Arguments.of(NetworkException.ServerException(), R.string.error_server),
                Arguments.of(IllegalStateException(), R.string.error_generic),
                Arguments.of(IOException(), R.string.error_generic),
                Arguments.of(Throwable(), R.string.error_generic),
            )
        }
    }
}
