package it.luik.rijksmuseum.art

data class ArtDetails(
    val id: String,
    val title: String,
    val description: String? = null,
    val imageUrl: String? = null,
    val author: String,
)
