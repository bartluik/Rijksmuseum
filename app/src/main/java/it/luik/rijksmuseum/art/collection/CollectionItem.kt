package it.luik.rijksmuseum.art.collection

internal sealed class CollectionItem {

    data class HeaderCollectionItem(
        val title: String
    ) : CollectionItem()

    data class ArtCollectionItem(
        val id: String,
        val title: String,
        val imageUrl: String?
    ) : CollectionItem()
}
