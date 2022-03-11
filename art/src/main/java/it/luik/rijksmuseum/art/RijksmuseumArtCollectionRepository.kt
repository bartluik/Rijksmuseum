package it.luik.rijksmuseum.art

import it.luik.rijksmuseum.art.remote.RijkDataClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

internal class RijksmuseumArtCollectionRepository @Inject constructor(
    private val client: RijkDataClient
) : ArtCollectionRepository {

    override suspend fun getCollection(): Result<List<Art>> {
        return withContext(Dispatchers.IO) {
            client.getCollection().map {
                it.artObjects.map { artObject ->
                    Art(
                        id = artObject.id,
                        title = artObject.longTitle,
                        imageUrl = artObject.webImage.url
                    )
                }
            }
        }
    }
}
