package it.luik.rijksmuseum.art

data class ArtSummary(
    val id: String,
    val title: String,
    val longTitle: String,
    val imageUrl: String?,
    val author: String,
)
