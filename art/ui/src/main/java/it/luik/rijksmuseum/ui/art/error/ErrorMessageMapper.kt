package it.luik.rijksmuseum.ui.art.error

import it.luik.rijksmuseum.ui.art.text.StringResource
import it.luik.rijksmuseum.network.NetworkException
import it.luik.rijksmuseum.ui.R

fun Throwable.toErrorMessage() = StringResource.Id(
    when (this) {
        is NetworkException.AuthException -> R.string.error_auth
        is NetworkException.ClientException -> R.string.error_client
        is NetworkException.ServerException -> R.string.error_server
        else -> R.string.error_generic
    }
)
