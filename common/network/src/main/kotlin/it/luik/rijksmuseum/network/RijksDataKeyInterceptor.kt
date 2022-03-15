package it.luik.rijksmuseum.network

import dagger.Reusable
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

@Reusable
internal class RijksDataKeyInterceptor @Inject constructor(
    @RijksDataApiKey private val apiKey: String
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val authenticatedUrl = request.url.newBuilder()
            .addQueryParameter(API_KEY_QUERY_PARAM, apiKey)
            .build()
        val authenticatedRequest = request.newBuilder()
            .url(authenticatedUrl)
            .build()
        return chain.proceed(authenticatedRequest)
    }

    companion object {
        private const val API_KEY_QUERY_PARAM = "key"
    }
}
