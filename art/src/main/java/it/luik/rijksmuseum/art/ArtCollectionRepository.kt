package it.luik.rijksmuseum.art

interface ArtCollectionRepository {

    suspend fun getCollection(page: Int): Result<List<ArtSummary>>

    suspend fun getDetails(artId: String): Result<ArtDetails>
}
