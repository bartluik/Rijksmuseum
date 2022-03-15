package it.luik.rijksmuseum.loading

import kotlinx.coroutines.flow.StateFlow

interface LoadingDelegate {

    val loadingState: StateFlow<LoadingState>

    fun isLoading(): Boolean

    suspend fun stopLoading()
    suspend fun startLoading(pageNumber: Int = 1)
}
