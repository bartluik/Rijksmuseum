package it.luik.rijksmuseum.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import it.luik.rijksmuseum.art.ArtCollectionRepository
import it.luik.rijksmuseum.art.ArtDetails
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
internal class ArtDetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val repo: ArtCollectionRepository
) : ViewModel() {

    private val artId: String = requireNotNull(savedStateHandle.get<String>("artId"))

    val showLoading = MutableStateFlow(false)
    val artDetails = MutableStateFlow<ArtDetails?>(null)

    init {
        loadArtDetails(artId)
    }

    private fun loadArtDetails(artId: String) {
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
        stopLoading()
        artDetails.value = details
    }

    private fun onDetailsFailed(throwable: Throwable) {
        stopLoading()
        Timber.e(throwable, "Failed to loaidd art collection")
        // TODO: handle failure
    }

    private fun startLoading() {
        showLoading.value = true
    }

    private fun stopLoading() {
        showLoading.value = false
    }
}
