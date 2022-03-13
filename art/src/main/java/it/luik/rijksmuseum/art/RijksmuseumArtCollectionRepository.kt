package it.luik.rijksmuseum.art

import it.luik.rijksmuseum.art.remote.RijkDataClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

internal class RijksmuseumArtCollectionRepository @Inject constructor(
    private val client: RijkDataClient
) : ArtCollectionRepository {

    override suspend fun getCollection(page: Int): Result<List<ArtSummary>> {
        return withContext(Dispatchers.IO) {
            client.getCollection(page).map {
                it.artObjects.map { artObject ->
                    ArtSummary(
                        id = artObject.objectNumber,
                        title = artObject.title,
                        longTitle = artObject.longTitle,
                        imageUrl = artObject.webImage?.url,
                        author = artObject.principalOrFirstMaker
                    )
                }
            }
        }
    }

    override suspend fun getDetails(artId: String): Result<ArtDetails> {
        return withContext(Dispatchers.IO) {
            client.getArtDetails(artId).map { artObject ->
                ArtDetails(
                    id = artObject.objectNumber,
                    title = artObject.title,
                    description = artObject.description,
                    imageUrl = artObject.webImage?.url,
                    author = artObject.principalOrFirstMaker
                )
            }
        }
    }
}
