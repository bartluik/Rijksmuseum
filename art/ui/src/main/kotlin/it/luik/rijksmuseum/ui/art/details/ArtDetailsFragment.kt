package it.luik.rijksmuseum.ui.art.details

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
import it.luik.rijksmuseum.art.domain.details.ArtDetails
import it.luik.rijksmuseum.ui.R
import it.luik.rijksmuseum.ui.art.text.StringResource
import it.luik.rijksmuseum.ui.art.view.inStartedLifecycle
import it.luik.rijksmuseum.ui.art.view.shimmerHighlightDrawable
import it.luik.rijksmuseum.ui.art.view.viewBindingLifecycle
import it.luik.rijksmuseum.ui.databinding.FragmentArtDetailsBinding

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

    private fun onShowLoading(loadingState: it.luik.rijksmuseum.loading.LoadingState) {
        binding.root.post {
            binding.detailsLoadingBar.isVisible =
                loadingState != it.luik.rijksmuseum.loading.LoadingState.NONE
        }
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
