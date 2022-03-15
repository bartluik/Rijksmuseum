package it.luik.rijksmuseum.ui.art.details

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import it.luik.rijksmuseum.art.domain.ArtRepository
import it.luik.rijksmuseum.art.domain.details.ArtDetails
import it.luik.rijksmuseum.loading.LoadingDelegate
import it.luik.rijksmuseum.ui.art.error.toErrorMessage
import it.luik.rijksmuseum.ui.art.text.StringResource
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class ArtDetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val repo: ArtRepository,
    private val loadingDelegate: LoadingDelegate
) : ViewModel(), LoadingDelegate by loadingDelegate {

    private val artId: String = requireNotNull(savedStateHandle.get<String>("artId"))

    val onErrorMessage: MutableSharedFlow<StringResource> = MutableSharedFlow()
    val artDetails = MutableStateFlow<ArtDetails?>(null)

    init {
        loadArtDetails(artId)
    }

    @VisibleForTesting
    internal fun loadArtDetails(artId: String) {
        viewModelScope.launch {
            startLoading()
            repo.getDetails(artId)
                .fold(
                    onSuccess = ::onDetails,
                    onFailure = ::onDetailsFailed
                )
        }
    }

    private fun onDetails(details: ArtDetails) {
        artDetails.value = details
        viewModelScope.launch { stopLoading() }
    }

    private fun onDetailsFailed(throwable: Throwable) {
        viewModelScope.launch {
            onErrorMessage.emit(throwable.toErrorMessage())
            stopLoading()
        }
    }
}
