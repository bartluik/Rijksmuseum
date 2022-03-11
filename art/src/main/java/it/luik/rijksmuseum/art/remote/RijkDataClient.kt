package it.luik.rijksmuseum.art.remote

import it.luik.rijksmuseum.network.mapResult
import javax.inject.Inject

internal class RijkDataClient @Inject constructor(
    private val service: RijksDataService,
) {

    suspend fun getCollection(): Result<ArtCollectionResponse> = mapResult {
        service.getCollection()
    }
}
