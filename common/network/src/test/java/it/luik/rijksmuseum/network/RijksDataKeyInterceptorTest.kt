package it.luik.rijksmuseum.network

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import okhttp3.Interceptor
import okhttp3.Request
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.net.URL

internal class RijksDataKeyInterceptorTest {

    private val chain = mockk<Interceptor.Chain>(relaxed = true)

    private val interceptor = RijksDataKeyInterceptor(
        apiKey = TEST_KEY
    )

    @Test
    fun `when request is intercepted then key param is added`() {
        every { chain.request() } returns Request.Builder().url(URL("https://some.thing")).build()
        interceptor.intercept(chain)

        verify {
            chain.proceed(withArg {
                assertEquals(TEST_KEY, it.url.queryParameter("key"))
            })
        }
    }

    companion object {
        private const val TEST_KEY = "test_api_key"
    }
}
