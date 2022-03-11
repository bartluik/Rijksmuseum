package it.luik.rijksmuseum.art.remote

import retrofit2.Response
import retrofit2.http.GET

internal interface RijksDataService {

    @GET("/api/nl/collection")
    suspend fun getCollection(): Response<ArtCollectionResponse>
}
