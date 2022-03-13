package it.luik.rijksmuseum.overview

internal sealed class OverviewItem {

    data class HeaderOverviewItem(
        val title: String
    ) : OverviewItem()

    data class ArtOverviewItem(
        val id: String,
        val title: String,
        val imageUrl: String?
    ) : OverviewItem()
}
