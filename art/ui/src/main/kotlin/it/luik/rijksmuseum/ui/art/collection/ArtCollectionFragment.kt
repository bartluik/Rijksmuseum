package it.luik.rijksmuseum.ui.art.collection

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import it.luik.rijksmuseum.ui.art.text.StringResource
import it.luik.rijksmuseum.loading.LoadingState
import it.luik.rijksmuseum.ui.art.artOverviewLoadMoreListener
import it.luik.rijksmuseum.ui.art.navigation.navigate
import it.luik.rijksmuseum.ui.art.view.inStartedLifecycle
import it.luik.rijksmuseum.ui.art.view.viewBindingLifecycle
import it.luik.rijksmuseum.ui.databinding.FragmentArtOverviewBinding

@AndroidEntryPoint
internal class ArtCollectionFragment : Fragment() {

    private val viewModel: ArtCollectionViewModel by viewModels()

    private var binding: FragmentArtOverviewBinding by viewBindingLifecycle()

    private lateinit var adapter: ArtCollectionAdapter

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
        adapter = ArtCollectionAdapter {
            viewModel.onCollectionItemClick(it)
        }
        binding.artList.adapter = adapter
        observeViewModel()
    }

    private fun observeViewModel() {
        with(viewModel) {
            inStartedLifecycle(
                { onNavigateToItem.collect(::onNavigateToDetails) },
                { collectionItems.collect(adapter::submitList) },
                { loadingState.collect(::onShowLoading) },
                { totalItemsCount.collect(::onTotalItemsCount) },
                { onErrorMessage.collect(::onErrorMessage) }
            )
        }
    }

    private fun onErrorMessage(errorMessage: StringResource) {
        val context = requireContext()
        Toast.makeText(context, errorMessage.get(context), Toast.LENGTH_LONG)
            .show()
    }

    private fun onTotalItemsCount(totalItemsCount: Int) {
        if (totalItemsCount == 0) binding.artList.clearOnScrollListeners()
        binding.artList.artOverviewLoadMoreListener(totalItemsCount) {
            viewModel.onLoadMore()
        }
    }

    private fun onShowLoading(loadingState: LoadingState) {
        with(binding) {
            collectionLoadingBar.isVisible = loadingState == LoadingState.LOADING
            collectionLoadMoreBar.isVisible = loadingState == LoadingState.LOADING_MORE
        }
    }

    private fun onNavigateToDetails(id: String) {
        navigate(ArtCollectionFragmentDirections.toArtDetails(id))
    }
}
