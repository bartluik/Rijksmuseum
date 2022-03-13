package it.luik.rijksmuseum.overview

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import it.luik.rijksmuseum.common.navigate
import it.luik.rijksmuseum.common.view.addLoadMoreListener
import it.luik.rijksmuseum.common.view.inStartedLifecycle
import it.luik.rijksmuseum.common.view.viewBindingLifecycle
import it.luik.rijksmuseum.databinding.FragmentArtOverviewBinding

@AndroidEntryPoint
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
            { viewModel.overviewItems.collect(adapter::submitList) },
            { viewModel.showLoadMore.collect(::onShowLoadMore) },
            { viewModel.showLoading.collect(::onShowLoading) },
            { viewModel.totalItemCount.collect(::onTotalItemCount) }
        )
    }

    private fun onTotalItemCount(totalItems: Int) {
        //paging update
        binding.artList.addLoadMoreListener(10) {
            viewModel.onLoadMore()
        }
    }

    private fun onShowLoading(loading: Boolean) {
        binding.artLoadingBar.isVisible = loading
    }

    private fun onShowLoadMore(loading: Boolean) {
        binding.artLoadMoreBar.isVisible = loading

    }

    private fun onNavigateToDetails(id: String) {
        navigate(ArtOverviewFragmentDirections.toArtDetail())
    }
}
