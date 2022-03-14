package it.luik.rijksmuseum.art.collection

import androidx.recyclerview.widget.DiffUtil

internal object CollectionItemDiff : DiffUtil.ItemCallback<CollectionItem>() {

    override fun areItemsTheSame(oldItem: CollectionItem, newItem: CollectionItem): Boolean {
        return if (oldItem is CollectionItem.ArtCollectionItem && newItem is CollectionItem.ArtCollectionItem) {
            oldItem.id == newItem.id
        } else {
            oldItem is CollectionItem.HeaderCollectionItem && newItem is CollectionItem.HeaderCollectionItem
        }
    }

    override fun areContentsTheSame(oldItem: CollectionItem, newItem: CollectionItem): Boolean =
        oldItem == newItem
}
