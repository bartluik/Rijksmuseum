package it.luik.rijksmuseum.common.loading

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class DefaultLoadingDelegate @Inject constructor() : LoadingDelegate {

    private val _loadingState: MutableStateFlow<LoadingState> = MutableStateFlow(LoadingState.NONE)
    override val loadingState: StateFlow<LoadingState> = _loadingState

    override fun isLoading(): Boolean = _loadingState.value != LoadingState.NONE

    override suspend fun startLoading(pageNumber: Int) =
        if (pageNumber > 1) _loadingState.emit(LoadingState.LOADING_MORE)
        else _loadingState.emit(LoadingState.LOADING)

    override suspend fun stopLoading() = _loadingState.emit(LoadingState.NONE)
}
