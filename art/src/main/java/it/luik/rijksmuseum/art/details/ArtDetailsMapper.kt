package it.luik.rijksmuseum.art.details

import it.luik.rijksmuseum.art.remote.ArtDetailsObjectResponse

internal fun Result<ArtDetailsObjectResponse>.toArtDetails() = map {
    ArtDetails(
        id = it.objectNumber,
        title = it.title,
        description = it.description,
        imageUrl = it.webImage?.url,
        author = it.principalOrFirstMaker
    )
}
