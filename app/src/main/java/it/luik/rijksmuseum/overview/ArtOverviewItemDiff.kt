package it.luik.rijksmuseum.overview

import androidx.recyclerview.widget.DiffUtil

internal object ArtOverviewItemDiff : DiffUtil.ItemCallback<OverviewItem>() {

    override fun areItemsTheSame(oldItem: OverviewItem, newItem: OverviewItem): Boolean {
        return if (oldItem is OverviewItem.ArtOverviewItem && newItem is OverviewItem.ArtOverviewItem) {
            oldItem.id == newItem.id
        } else {
            oldItem is OverviewItem.HeaderOverviewItem && newItem is OverviewItem.HeaderOverviewItem
        }
    }

    override fun areContentsTheSame(oldItem: OverviewItem, newItem: OverviewItem): Boolean =
        oldItem == newItem
}
