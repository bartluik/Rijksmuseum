package it.luik.rijksmuseum.art.ui

import it.luik.rijksmuseum.art.domain.ArtRepository
import it.luik.rijksmuseum.art.domain.collection.ArtCollectionPage
import it.luik.rijksmuseum.art.domain.collection.ArtSummary
import it.luik.rijksmuseum.art.domain.details.ArtDetails

internal class FakeArtRepository : ArtRepository {
    override suspend fun getCollection(page: Int): Result<ArtCollectionPage> {
        return Result.success(ArtCollectionPage(testSummaries, 1, 1))
    }

    override suspend fun getDetails(artId: String): Result<ArtDetails> {
        return Result.failure(IllegalStateException())
    }
}

internal val testSummaries = listOf(
    ArtSummary(
        id = "id",
        title = "title",
        imageUrl = "imageUrl",
        author = "author"
    )
)
