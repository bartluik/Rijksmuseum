package it.luik.rijksmuseum.art.remote

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

internal interface RijksDataService {

    @GET("/api/nl/collection?s=artist")
    suspend fun getCollection(@Query("p") page: Int): Response<ArtCollectionResponse>

    @GET("/api/nl/collection/{id}")
    suspend fun getArtDetails(@Path("id") artId: String): Response<ArtDetailsResponse>
}
