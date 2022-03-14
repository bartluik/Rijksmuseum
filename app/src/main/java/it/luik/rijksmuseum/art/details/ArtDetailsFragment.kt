package it.luik.rijksmuseum.art.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import coil.load
import coil.size.Scale
import dagger.hilt.android.AndroidEntryPoint
import it.luik.rijksmuseum.R
import it.luik.rijksmuseum.common.StringResource
import it.luik.rijksmuseum.common.view.inStartedLifecycle
import it.luik.rijksmuseum.common.view.shimmerHighlightDrawable
import it.luik.rijksmuseum.common.view.viewBindingLifecycle
import it.luik.rijksmuseum.databinding.FragmentArtDetailsBinding
import it.luik.rijksmuseum.art.details.ArtDetailsViewModel.LoadingState

@AndroidEntryPoint
class ArtDetailsFragment : Fragment() {

    private val viewModel: ArtDetailsViewModel by viewModels()

    private var binding: FragmentArtDetailsBinding by viewBindingLifecycle()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentArtDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeViewModel()
    }

    private fun observeViewModel() {
        with(viewModel) {
            inStartedLifecycle(
                { loadingState.collect(::onShowLoading) },
                { artDetails.collect(::onDetails) },
                { onErrorMessage.collect(::onErrorMessage) },
            )
        }
    }

    private fun onErrorMessage(errorMessage: StringResource) {
        val context = requireContext()
        Toast.makeText(context, errorMessage.get(context), Toast.LENGTH_LONG)
            .show()
    }

    private fun onShowLoading(loadingState: LoadingState) {
        binding.artLoadingBar.isVisible = loadingState == LoadingState.LOADING
    }

    private fun onDetails(details: ArtDetails?) {
        details ?: return
        with(binding) {
            artTitle.text = details.title
            artDescription.text = details.description

            artIllustration.load(details.imageUrl) {
                placeholder(shimmerHighlightDrawable(root.context))
                scale(Scale.FIT)
                error(R.drawable.ic_image_not_supported_large)
            }
        }
    }
}
