package it.luik.rijksmuseum.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import coil.load
import coil.size.Scale
import dagger.hilt.android.AndroidEntryPoint
import it.luik.rijksmuseum.R
import it.luik.rijksmuseum.art.ArtDetails
import it.luik.rijksmuseum.common.view.inStartedLifecycle
import it.luik.rijksmuseum.common.view.shimmerHighlightDrawable
import it.luik.rijksmuseum.common.view.viewBindingLifecycle
import it.luik.rijksmuseum.databinding.FragmentArtDetailsBinding

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
        inStartedLifecycle(
            { viewModel.showLoading.collect(::onShowLoading) },
            { viewModel.artDetails.collect(::onDetails) }

        )
    }

    private fun onShowLoading(loading: Boolean) {
        binding.artLoadingBar.isVisible = loading
    }

    private fun onDetails(details: ArtDetails?) {
        details ?: return
        with(binding) {
            artTitle.text = details.title
            artDescription.text = details.description

            artIllustration.load(details.imageUrl) {
                placeholder(shimmerHighlightDrawable(root.context))
                scale(Scale.FIT)
                error(R.drawable.image_art_error)
            }
        }
    }
}
