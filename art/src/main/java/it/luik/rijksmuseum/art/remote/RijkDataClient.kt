package it.luik.rijksmuseum.art.remote

import it.luik.rijksmuseum.network.mapResult
import javax.inject.Inject

internal class RijkDataClient @Inject constructor(
    private val service: RijksDataService,
) {

    suspend fun getCollection(page: Int, resultSize: Int): Result<ArtCollectionResponse> =
        mapResult {
            service.getCollection(page, resultSize)
        }

    suspend fun getArtDetails(artId: String): Result<ArtDetailsObjectResponse> = mapResult {
        service.getArtDetails(artId)
    }.map {
        it.artObject
    }
}
