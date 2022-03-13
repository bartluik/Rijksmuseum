package it.luik.rijksmuseum.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import coil.load
import coil.size.Scale
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import it.luik.rijksmuseum.R
import it.luik.rijksmuseum.common.view.shimmerHighlightDrawable
import it.luik.rijksmuseum.common.view.viewBindingLifecycle
import it.luik.rijksmuseum.databinding.ItemArtOverviewBinding

class ArtDetailFragment : BottomSheetDialogFragment() {

    private var binding: ItemArtOverviewBinding by viewBindingLifecycle()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ItemArtOverviewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.artTitle.text = "Details"
        val imageUrl =
            "https://lh6.ggpht.com/mQ9Y5SUyvn23stCYYeEd5E3zpSZ5M6Nog5kSjUQAV5O3xwaYCtOSyXtd0RH2fQUyirDNCdkse1OS19rweprah8ogEw=s0"
        binding.artIllustration.load(imageUrl) {
            placeholder(shimmerHighlightDrawable(view.context))
            scale(Scale.FIT)
            error(R.drawable.image_art_error)
        }
    }
}
