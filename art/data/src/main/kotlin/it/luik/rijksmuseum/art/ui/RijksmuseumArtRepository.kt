package it.luik.rijksmuseum.art.ui

import it.luik.rijksmuseum.art.ui.collection.toArtCollectionPage
import it.luik.rijksmuseum.art.ui.details.toArtDetails
import it.luik.rijksmuseum.art.domain.ArtRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

internal class RijksmuseumArtRepository @Inject constructor(
    private val client: RijkDataClient
) : ArtRepository {

    override suspend fun getCollection(page: Int) = withContext(Dispatchers.IO) {
        client.getCollection(page, DEFAULT_RESULT_SIZE)
            .toArtCollectionPage(DEFAULT_RESULT_SIZE)
    }


    override suspend fun getDetails(artId: String) = withContext(Dispatchers.IO) {
        client.getArtDetails(artId)
            .toArtDetails()
    }

    companion object {
        const val DEFAULT_RESULT_SIZE = 20
    }
}
