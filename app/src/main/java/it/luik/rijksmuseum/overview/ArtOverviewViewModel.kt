package it.luik.rijksmuseum.overview

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import it.luik.rijksmuseum.art.ArtCollectionRepository
import it.luik.rijksmuseum.art.ArtSummary
import it.luik.rijksmuseum.overview.OverviewItem.ArtOverviewItem
import it.luik.rijksmuseum.overview.OverviewItem.HeaderOverviewItem
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
internal class ArtOverviewViewModel @Inject constructor(
    private val repo: ArtCollectionRepository
) : ViewModel() {

    val overviewItems = MutableStateFlow<List<OverviewItem>>(emptyList())
    val onNavigateToItem: MutableSharedFlow<String> = MutableSharedFlow()
    val totalItemCount = MutableStateFlow(0)

    val showLoading = MutableStateFlow(false)
    val showLoadMore = MutableStateFlow(false)

    private var page: Int = 0
    private var isLoading = false

    init {
        loadArtCollection()
    }

    private fun loadArtCollection() {
        viewModelScope.launch {
            startLoading()
            repo.getCollection(page)
                .map(::toArtItemsByAuthor)
                .fold(
                    onSuccess = ::onCollection,
                    onFailure = ::onCollectionFailed
                )
        }
    }

    private fun startLoading() {
        isLoading = true
        if (page == 0) showLoading.value = isLoading
        else showLoadMore.value = isLoading
    }

    private fun toArtItemsByAuthor(collection: List<ArtSummary>): List<OverviewItem> {
        return collection.groupBy { it.author }
            .flatMap { (author, artItems) ->
                listOf(HeaderOverviewItem(author)) +
                        artItems.map {
                            ArtOverviewItem(
                                id = it.id,
                                title = it.title,
                                imageUrl = it.imageUrl
                            )
                        }
            }
    }

    private fun onCollection(collection: List<OverviewItem>) {
        overviewItems.value = overviewItems.value + collection
        stopLoading()
    }

    private fun onCollectionFailed(throwable: Throwable) {
        stopLoading()
        Timber.e(throwable, "Failed to load art collection")
        // TODO: handle failure
    }

    fun onOverviewItemClick(item: OverviewItem) {
        Timber.i("On overview item click $item")
        when (item) {
            is HeaderOverviewItem -> Unit
            is ArtOverviewItem -> viewModelScope.launch {
                onNavigateToItem.emit(item.id)
            }
        }
    }

    fun onLoadMore() {
        if (!isLoading) {
            page++
            loadArtCollection()
        }
    }

    private fun stopLoading() {
        isLoading = false
        showLoading.value = isLoading
        showLoadMore.value = isLoading
    }
}
