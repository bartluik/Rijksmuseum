package it.luik.rijksmuseum.art.collection

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import it.luik.rijksmuseum.art.ArtRepository
import it.luik.rijksmuseum.art.collection.CollectionItem.ArtCollectionItem
import it.luik.rijksmuseum.art.collection.CollectionItem.HeaderCollectionItem
import it.luik.rijksmuseum.common.StringResource
import it.luik.rijksmuseum.common.error.toErrorMessage
import it.luik.rijksmuseum.common.loading.LoadingDelegate
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.getAndUpdate
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class ArtCollectionViewModel @Inject constructor(
    private val repo: ArtRepository,
    private val loadingDelegate: LoadingDelegate
) : ViewModel(), LoadingDelegate by loadingDelegate {

    val onNavigateToItem: MutableSharedFlow<String> = MutableSharedFlow()
    val onErrorMessage: MutableSharedFlow<StringResource> = MutableSharedFlow()

    val collectionItems = MutableStateFlow<List<CollectionItem>>(emptyList())
    val totalItemsCount = MutableStateFlow(0)

    private var page: Int = 1

    init {
        loadArtCollection()
    }

    @VisibleForTesting
    internal fun loadArtCollection() {
        viewModelScope.launch {
            startLoading(pageNumber = page)
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

        viewModelScope.launch { stopLoading() }
    }

    private fun onCollectionFailed(throwable: Throwable) {
        viewModelScope.launch {
            onErrorMessage.emit(throwable.toErrorMessage())
        }

        viewModelScope.launch { stopLoading() }
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
        if (!isLoading()) {
            page++
            loadArtCollection()
        }
    }
}
