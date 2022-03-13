package it.luik.rijksmuseum.art

interface ArtCollectionRepository {

    suspend fun getCollection(page: Int): Result<List<Art>>
}
