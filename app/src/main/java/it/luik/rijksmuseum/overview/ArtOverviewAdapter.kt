package it.luik.rijksmuseum.overview

import android.view.LayoutInflater.from
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.dispose
import coil.load
import it.luik.rijksmuseum.R
import it.luik.rijksmuseum.common.view.shimmerHighlightDrawable
import it.luik.rijksmuseum.databinding.ItemArtOverviewBinding
import it.luik.rijksmuseum.databinding.ItemHeaderOverviewBinding
import it.luik.rijksmuseum.overview.ArtOverviewAdapter.ViewType.ART
import it.luik.rijksmuseum.overview.ArtOverviewAdapter.ViewType.HEADER
import it.luik.rijksmuseum.overview.OverviewItem.ArtOverviewItem
import it.luik.rijksmuseum.overview.OverviewItem.HeaderOverviewItem

internal class ArtOverviewAdapter(
    val onItemClick: (OverviewItem) -> Unit
) : ListAdapter<OverviewItem, RecyclerView.ViewHolder>(ArtOverviewItemDiff) {

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is ArtOverviewItem -> ART
            is HeaderOverviewItem -> HEADER
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = from(parent.context)
        return when (viewType) {
            ART -> ArtOverviewViewHolder(
                ItemArtOverviewBinding.inflate(inflater, parent, false)
            )
            HEADER -> HeaderOverviewViewHolder(
                ItemHeaderOverviewBinding.inflate(inflater, parent, false)
            )
            else -> throw IllegalStateException("Unknown viewType encountered: $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = getItem(position)) {
            is ArtOverviewItem -> (holder as ArtOverviewViewHolder).bind(item)
            is HeaderOverviewItem -> (holder as HeaderOverviewViewHolder).bind(item)
        }
    }

    override fun onViewRecycled(holder: RecyclerView.ViewHolder) {
        super.onViewRecycled(holder)
        when (holder) {
            is ArtOverviewViewHolder -> holder.unbind()
        }
    }

    inner class HeaderOverviewViewHolder(
        private val binding: ItemHeaderOverviewBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: HeaderOverviewItem) {
            with(binding) {
                overviewHeaderTitle.text = item.title
                root.setOnClickListener {
                    onItemClick(item)
                }
            }
        }
    }

    inner class ArtOverviewViewHolder(
        private val binding: ItemArtOverviewBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: ArtOverviewItem) {
            with(binding) {
                artTitle.text = item.title
                artIllustration.load(item.imageUrl) {
                    placeholder(shimmerHighlightDrawable(root.context))
                    error(R.drawable.image_art_error)
                }
                root.setOnClickListener {
                    onItemClick(item)
                }
            }
        }

        fun unbind() {
            binding.artIllustration.dispose()
        }
    }

    private object ViewType {
        const val ART = 1
        const val HEADER = 2
    }
}
