package it.luik.rijksmuseum.loading

import app.cash.turbine.test
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

internal class DefaultLoadingDelegateTest {

    private val loadingDelegate = DefaultLoadingDelegate()

    @Test
    fun `when loading state is collected then default state is none`() = runBlocking {
        loadingDelegate.loadingState.test {

            loadingDelegate.stopLoading()
            assertEquals(LoadingState.NONE, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `when loading starts with page one then state is loading`() = runBlocking {
        loadingDelegate.loadingState.test {

            loadingDelegate.startLoading()
            assertEquals(LoadingState.NONE, awaitItem())
            assertEquals(LoadingState.LOADING, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `when loading starts with page two then state is loading`() = runBlocking {
        loadingDelegate.loadingState.test {

            loadingDelegate.startLoading(2)
            assertEquals(LoadingState.NONE, awaitItem())
            assertEquals(LoadingState.LOADING_MORE, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `when loading stops then state is none`() = runBlocking {
        loadingDelegate.loadingState.test {

            loadingDelegate.stopLoading()
            //same as default so no new items will be emitted
            assertEquals(LoadingState.NONE, awaitItem())

            val remaining = cancelAndConsumeRemainingEvents()
            assertTrue { remaining.isEmpty() }
        }
    }
}
