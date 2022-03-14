package it.luik.rijksmuseum.art

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import it.luik.rijksmuseum.art.collection.ArtCollectionAdapter
import it.luik.rijksmuseum.art.collection.CollectionItem

fun RecyclerView.artOverviewLoadMoreListener(
    totalItems: Int = 0,
    onLoadMore: () -> Unit
) {
    clearOnScrollListeners()
    addOnScrollListener(object : RecyclerView.OnScrollListener() {

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

            if (dy < 0) return // Scrolled up no need to load more
            val layoutManager = recyclerView.layoutManager as? LinearLayoutManager ?: return

            val totalArtItemsCount = (recyclerView.adapter as ArtCollectionAdapter).currentList
                .count { it is CollectionItem.ArtCollectionItem }
            if (totalArtItemsCount == totalItems) return // No more items to load

            val lastVisibleItemPos = layoutManager.findLastVisibleItemPosition()
            if (lastVisibleItemPos == layoutManager.itemCount - 1) {
                onLoadMore()
            }
        }
    })
}
