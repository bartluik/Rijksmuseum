package it.luik.rijksmuseum.art.collection

import it.luik.rijksmuseum.art.RijksmuseumArtRepository
import it.luik.rijksmuseum.art.remote.ArtCollectionResponse

internal fun Result<ArtCollectionResponse>.toArtCollectionPage() = map {
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
        resultSize = RijksmuseumArtRepository.DEFAULT_RESULT_SIZE,
        totalCount = it.count
    )
}
