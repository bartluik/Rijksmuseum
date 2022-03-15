package it.luik.rijksmuseum.art.domain.collection

data class ArtCollectionPage(
    val summaries: List<ArtSummary>,
    val resultSize: Int,
    val totalCount: Int
)
