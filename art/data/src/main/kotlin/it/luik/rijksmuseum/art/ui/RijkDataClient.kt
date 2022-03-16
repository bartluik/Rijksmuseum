package it.luik.rijksmuseum.art.ui

import it.luik.rijksmuseum.network.mapResult
import kotlinx.coroutines.delay
import javax.inject.Inject

internal class RijkDataClient @Inject constructor(
    private val service: RijksDataService,
) {

    suspend fun getCollection(page: Int, resultSize: Int): Result<ArtCollectionResponse> =
        mapResult {
            service.getCollection(page, resultSize)
        }

    suspend fun getArtDetails(artId: String): Result<ArtDetailsObjectResponse> = mapResult {
        delay(3000L)
        service.getArtDetails(artId)
    }.map {
        it.artObject
    }
}
