package it.luik.rijksmuseum.overview

import androidx.recyclerview.widget.DiffUtil

internal object ArtOverviewItemDiff : DiffUtil.ItemCallback<ArtOverviewItem>() {

    override fun areItemsTheSame(oldItem: ArtOverviewItem, newItem: ArtOverviewItem) =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: ArtOverviewItem, newItem: ArtOverviewItem) =
        oldItem == newItem
}
