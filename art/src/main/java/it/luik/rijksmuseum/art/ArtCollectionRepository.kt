package it.luik.rijksmuseum.art

interface ArtCollectionRepository {

    suspend fun getCollection(): Result<List<Art>>
}
