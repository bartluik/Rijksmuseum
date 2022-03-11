package it.luik.rijksmuseum.overview

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import it.luik.rijksmuseum.art.Art
import it.luik.rijksmuseum.art.ArtCollectionRepository
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

    init {
        viewModelScope.launch {
            repo.getCollection()
                .map(::toArtItemsByAuthor)
                .fold(
                    onSuccess = ::onCollection,
                    onFailure = ::onCollectionFailed
                )
        }
    }

    private fun toArtItemsByAuthor(collection: List<Art>): List<OverviewItem> {
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
        overviewItems.value = collection
    }

    private fun onCollectionFailed(throwable: Throwable) {
        Timber.e(throwable, "Failed to load art collection")
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
}
