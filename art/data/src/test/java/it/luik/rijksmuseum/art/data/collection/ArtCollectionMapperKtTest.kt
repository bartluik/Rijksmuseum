package it.luik.rijksmuseum.art.data.collection

import io.github.glytching.junit.extension.random.Random
import io.github.glytching.junit.extension.random.RandomBeansExtension
import it.luik.rijksmuseum.art.data.ArtCollectionResponse
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(RandomBeansExtension::class)
internal class ArtCollectionMapperKtTest {

    @Test
    fun `when art collection response is mapped then result size is as given`(
        @Random response: ArtCollectionResponse
    ) {
        val res = Result.success(response)

        assertEquals(
            10,
            res.toArtCollectionPage(10)
                .getOrNull()
                ?.resultSize
        )
    }

    @Test
    fun `when art collection response is mapped then total count is set`(
        @Random response: ArtCollectionResponse
    ) {
        val res = Result.success(response)

        assertEquals(
            response.count,
            res.toArtCollectionPage(10)
                .getOrNull()
                ?.totalCount
        )
    }

    @Test
    fun `when art collection response is mapped then art objects are mapped `(
        @Random response: ArtCollectionResponse
    ) {
        val res = Result.success(response)

        assertEquals(
            response.artObjects.map {
                ArtSummary(
                    id = it.objectNumber,
                    title = it.title,
                    longTitle = it.longTitle,
                    imageUrl = it.webImage?.url,
                    author = it.principalOrFirstMaker
                )
            },
            res.toArtCollectionPage(10)
                .getOrNull()
                ?.summaries
        )
    }
}
