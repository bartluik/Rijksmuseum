package it.luik.rijksmuseum.overview

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import it.luik.rijksmuseum.common.view.inStartedLifecycle
import it.luik.rijksmuseum.common.view.viewBindingLifecycle
import it.luik.rijksmuseum.databinding.FragmentArtOverviewBinding

internal class ArtOverviewFragment : Fragment() {

    private val viewModel: ArtOverviewViewModel by viewModels()

    private var binding: FragmentArtOverviewBinding by viewBindingLifecycle()

    private lateinit var adapter: ArtOverviewAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentArtOverviewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = ArtOverviewAdapter {
            viewModel.onOverviewItemClick(it)
        }
        binding.artList.adapter = adapter
        observeViewModel()
    }

    private fun observeViewModel() {
        inStartedLifecycle(
            { viewModel.onNavigateToItem.collect(::onNavigateToDetails) },
            { viewModel.overviewItems.collect(adapter::submitList) }
        )
    }

    private fun onNavigateToDetails(id: String) {
        //TODO: Navigation
    }
}
