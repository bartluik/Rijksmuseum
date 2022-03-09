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
import it.luik.rijksmuseum.databinding.ItemArtOverviewBinding.inflate

internal class ArtOverviewAdapter(
    val onItemClick: (ArtOverviewItem) -> Unit
) : ListAdapter<ArtOverviewItem, ArtOverviewAdapter.ArtOverviewViewHolder>(ArtOverviewItemDiff) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ArtOverviewViewHolder(
            inflate(from(parent.context), parent, false)
        )

    override fun onBindViewHolder(holder: ArtOverviewViewHolder, position: Int) =
        holder.bind(getItem(position))

    override fun onViewRecycled(holder: ArtOverviewViewHolder) {
        super.onViewRecycled(holder)
        holder.unbind()
    }

    inner class ArtOverviewViewHolder(
        private val binding: ItemArtOverviewBinding,
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
}
