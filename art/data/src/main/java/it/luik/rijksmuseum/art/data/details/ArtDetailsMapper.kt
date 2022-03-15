package it.luik.rijksmuseum.art.data.details

import it.luik.rijksmuseum.art.data.ArtDetailsObjectResponse
import it.luik.rijksmuseum.art.domain.details.ArtDetails

internal fun Result<ArtDetailsObjectResponse>.toArtDetails() = map {
    ArtDetails(
        id = it.objectNumber,
        title = it.title,
        description = it.description,
        imageUrl = it.webImage?.url,
        author = it.principalOrFirstMaker
    )
}
