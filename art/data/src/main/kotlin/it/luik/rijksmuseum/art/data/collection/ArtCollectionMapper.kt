package it.luik.rijksmuseum.art.data.collection

import it.luik.rijksmuseum.art.data.ArtCollectionResponse
import it.luik.rijksmuseum.art.domain.collection.ArtCollectionPage
import it.luik.rijksmuseum.art.domain.collection.ArtSummary

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
