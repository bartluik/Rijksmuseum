package it.luik.rijksmuseum.art.domain

import it.luik.rijksmuseum.art.domain.collection.ArtCollectionPage
import it.luik.rijksmuseum.art.domain.details.ArtDetails

interface ArtRepository {

    suspend fun getCollection(page: Int): Result<ArtCollectionPage>

    suspend fun getDetails(artId: String): Result<ArtDetails>
}
