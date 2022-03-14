package it.luik.rijksmuseum.art

import it.luik.rijksmuseum.art.collection.ArtCollectionPage
import it.luik.rijksmuseum.art.details.ArtDetails

interface ArtRepository {

    suspend fun getCollection(page: Int): Result<ArtCollectionPage>

    suspend fun getDetails(artId: String): Result<ArtDetails>
}
