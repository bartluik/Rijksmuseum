package it.luik.rijksmuseum.common.view

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

fun RecyclerView.addLoadMoreListener(itemOffset: Int = 0, onLoadMore: () -> Unit) {
    clearOnScrollListeners()
    addOnScrollListener(object : RecyclerView.OnScrollListener() {

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            // TODO: add max item stuff
            if (dy < 0) return
            val layoutManager = recyclerView.layoutManager as? LinearLayoutManager ?: return
            val visibleItemCount: Int = layoutManager.childCount
            val totalItemCount: Int = layoutManager.itemCount
            val pastVisibleItems: Int = layoutManager.findFirstVisibleItemPosition()
            if (pastVisibleItems + visibleItemCount >= (totalItemCount - itemOffset)) {
                onLoadMore()
            }
        }
    })
}
