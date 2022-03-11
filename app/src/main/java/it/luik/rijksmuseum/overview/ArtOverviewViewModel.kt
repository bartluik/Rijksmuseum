package it.luik.rijksmuseum.overview

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import it.luik.rijksmuseum.art.ArtCollectionRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
internal class ArtOverviewViewModel @Inject constructor(
    private val repo: ArtCollectionRepository
) : ViewModel() {

    val overviewItems = MutableStateFlow<List<ArtOverviewItem>>(emptyList())
    val onNavigateToItem: MutableSharedFlow<String> = MutableSharedFlow()

    init {
        viewModelScope.launch {
            repo.getCollection()
                .map { collection ->
                    collection.map {
                        ArtOverviewItem(
                            id = it.id,
                            title = it.title,
                            imageUrl = it.imageUrl
                        )
                    }
                }.fold(
                    onSuccess = ::onCollection,
                    onFailure = ::onCollectionFailed
                )
        }
    }

    private fun onCollection(collection: List<ArtOverviewItem>) {
        overviewItems.value = collection
    }

    private fun onCollectionFailed(throwable: Throwable) {
        Timber.e(throwable, "Failed to load art collection")
    }

    fun onOverviewItemClick(artOverviewItem: ArtOverviewItem) {
        Timber.i("On overview item click $artOverviewItem")
        viewModelScope.launch {
            onNavigateToItem.emit(artOverviewItem.id)
        }
    }
}
