package it.luik.rijksmuseum.ui.art.collection

import android.view.LayoutInflater.from
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.dispose
import coil.load
import coil.size.Scale
import it.luik.rijksmuseum.ui.R
import it.luik.rijksmuseum.ui.art.collection.ArtCollectionAdapter.ViewType.ART
import it.luik.rijksmuseum.ui.art.collection.ArtCollectionAdapter.ViewType.HEADER
import it.luik.rijksmuseum.ui.art.collection.CollectionItem.ArtCollectionItem
import it.luik.rijksmuseum.ui.art.collection.CollectionItem.HeaderCollectionItem
import it.luik.rijksmuseum.ui.art.view.shimmerHighlightDrawable
import it.luik.rijksmuseum.ui.databinding.ItemArtOverviewBinding
import it.luik.rijksmuseum.ui.databinding.ItemHeaderOverviewBinding

internal class ArtCollectionAdapter(
    val onItemClick: (CollectionItem) -> Unit
) : ListAdapter<CollectionItem, RecyclerView.ViewHolder>(CollectionItemDiff) {

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is ArtCollectionItem -> ART
            is HeaderCollectionItem -> HEADER
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
            is ArtCollectionItem -> (holder as ArtOverviewViewHolder).bind(item)
            is HeaderCollectionItem -> (holder as HeaderOverviewViewHolder).bind(item)
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

        fun bind(item: HeaderCollectionItem) {
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

        fun bind(item: ArtCollectionItem) {
            with(binding) {
                artTitle.text = item.title
                artIllustration.load(item.imageUrl) {
                    placeholder(shimmerHighlightDrawable(root.context))
                    scale(Scale.FIT)
                    error(R.drawable.ic_image_not_supported)
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
