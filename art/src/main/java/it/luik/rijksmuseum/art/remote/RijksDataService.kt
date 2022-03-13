package it.luik.rijksmuseum.art.remote

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

internal interface RijksDataService {

    @GET("/api/nl/collection")
    suspend fun getCollection(@Query("p") page: Int): Response<ArtCollectionResponse>
}
