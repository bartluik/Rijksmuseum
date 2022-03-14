package it.luik.rijksmuseum.common.error

import it.luik.rijksmuseum.R
import it.luik.rijksmuseum.common.StringResource
import it.luik.rijksmuseum.network.NetworkException

internal fun Throwable.toErrorMessage() = StringResource.Id(
    when (this) {
        is NetworkException.AuthException -> R.string.error_auth
        is NetworkException.ClientException -> R.string.error_client
        is NetworkException.ServerException -> R.string.error_server
        else -> R.string.error_generic
    }
)
