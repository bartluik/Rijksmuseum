package it.luik.rijksmuseum.art.domain.details

data class ArtDetails(
    val id: String,
    val title: String,
    val description: String? = null,
    val imageUrl: String? = null,
    val author: String,
)
