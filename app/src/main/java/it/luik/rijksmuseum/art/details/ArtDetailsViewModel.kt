package it.luik.rijksmuseum.art.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import it.luik.rijksmuseum.R
import it.luik.rijksmuseum.art.ArtRepository
import it.luik.rijksmuseum.common.StringResource
import it.luik.rijksmuseum.network.NetworkException
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
internal class ArtDetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val repo: ArtRepository
) : ViewModel() {

    private val artId: String = requireNotNull(savedStateHandle.get<String>("artId"))

    val loadingState: MutableStateFlow<LoadingState> = MutableStateFlow(LoadingState.NONE)
    val onErrorMessage: MutableSharedFlow<StringResource> = MutableSharedFlow()
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
        artDetails.value = details
        stopLoading()
    }

    //TODO: Delegation maybe or interactors
    private fun onDetailsFailed(throwable: Throwable) {
        viewModelScope.launch { stopLoading() }
        Timber.e(throwable, "Failed to load art details")

        val errorMessageRes = when (throwable) {
            is NetworkException.AuthException -> R.string.error_auth
            is NetworkException.ClientException -> R.string.error_client
            is NetworkException.ServerException -> R.string.error_server
            else -> R.string.error_generic
        }

        viewModelScope.launch {
            onErrorMessage.emit(StringResource.Id(errorMessageRes))
        }
    }

    private fun startLoading() = viewModelScope.launch {
        loadingState.emit(LoadingState.LOADING)
    }

    private fun stopLoading() = viewModelScope.launch {
        loadingState.emit(LoadingState.NONE)
    }

    enum class LoadingState {
        LOADING, NONE
    }
}
