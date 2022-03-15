package it.luik.rijksmuseum.art.collection

import it.luik.rijksmuseum.art.remote.ArtCollectionResponse

internal fun Result<ArtCollectionResponse>.toArtCollectionPage(resultSize: Int) = map {
    ArtCollectionPage(
        summaries = it.artObjects.map { artObject ->
            ArtSummary(
                id = artObject.objectNumber,
                title = artObject.title,
                longTitle = artObject.longTitle,
                imageUrl = artObject.webImage?.url,
                author = artObject.principalOrFirstMaker
            )
        },
        resultSize = resultSize,
        totalCount = it.count
    )
}
