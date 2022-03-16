package it.luik.rijksmuseum.art.ui.collection

import it.luik.rijksmuseum.art.ui.ArtCollectionResponse
import it.luik.rijksmuseum.art.domain.collection.ArtCollectionPage
import it.luik.rijksmuseum.art.domain.collection.ArtSummary

internal fun Result<ArtCollectionResponse>.toArtCollectionPage(resultSize: Int) = map {
    ArtCollectionPage(
        summaries = it.artObjects.map { artObject ->
            ArtSummary(
                id = artObject.objectNumber,
                title = artObject.title,
                imageUrl = artObject.webImage?.url,
                author = artObject.principalOrFirstMaker
            )
        },
        resultSize = resultSize,
        totalCount = it.count
    )
}
