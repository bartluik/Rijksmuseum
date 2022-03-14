package it.luik.rijksmuseum.art.collection

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import it.luik.rijksmuseum.R
import it.luik.rijksmuseum.art.ArtRepository
import it.luik.rijksmuseum.art.collection.ArtCollectionViewModel.LoadingState.*
import it.luik.rijksmuseum.art.collection.CollectionItem.ArtCollectionItem
import it.luik.rijksmuseum.art.collection.CollectionItem.HeaderCollectionItem
import it.luik.rijksmuseum.common.StringResource
import it.luik.rijksmuseum.network.NetworkException.*
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.getAndUpdate
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
internal class ArtCollectionViewModel @Inject constructor(
    private val repo: ArtRepository
) : ViewModel() {

    val onNavigateToItem: MutableSharedFlow<String> = MutableSharedFlow()
    val onErrorMessage: MutableSharedFlow<StringResource> = MutableSharedFlow()

    val collectionItems = MutableStateFlow<List<CollectionItem>>(emptyList())
    val totalItemsCount = MutableStateFlow(0)

    val loadingState: MutableStateFlow<LoadingState> = MutableStateFlow(NONE)

    private var page: Int = 1

    init {
        loadArtCollection()
    }

    @VisibleForTesting
    internal fun loadArtCollection() {
        startLoading()
        viewModelScope.launch {
            repo.getCollection(page)
                .fold(
                    onSuccess = ::onCollection,
                    onFailure = ::onCollectionFailed
                )
        }
    }


    private fun onCollection(collectionPage: ArtCollectionPage) {
        totalItemsCount.value = collectionPage.totalCount
        val newPageItems = collectionPage.summaries.toOverviewItems()
        collectionItems.getAndUpdate { it + newPageItems }
        stopLoading()
    }

    private fun onCollectionFailed(throwable: Throwable) {
        viewModelScope.launch { stopLoading() }
        Timber.e(throwable, "Failed to load art collection")

        val errorMessageRes = when (throwable) {
            is AuthException -> R.string.error_auth
            is ClientException -> R.string.error_client
            is ServerException -> R.string.error_server
            else -> R.string.error_generic
        }

        viewModelScope.launch {
            onErrorMessage.emit(StringResource.Id(errorMessageRes))
        }
    }

    fun onCollectionItemClick(item: CollectionItem) {
        when (item) {
            is HeaderCollectionItem -> Unit
            is ArtCollectionItem -> viewModelScope.launch {
                onNavigateToItem.emit(item.id)
            }
        }
    }

    fun onLoadMore() {
        if (loadingState.value == NONE) {
            page++
            loadArtCollection()
        }
    }

    private fun startLoading() = viewModelScope.launch {
        if (page > 1) loadingState.emit(LOADING_MORE)
        else loadingState.emit(LOADING)
    }

    private fun stopLoading() = viewModelScope.launch {
        loadingState.emit(NONE)
    }

    enum class LoadingState {
        LOADING, LOADING_MORE, NONE
    }
}
