package it.luik.rijksmuseum.art.data.details

import io.github.glytching.junit.extension.random.Random
import io.github.glytching.junit.extension.random.RandomBeansExtension
import it.luik.rijksmuseum.art.data.ArtDetailsObjectResponse
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(RandomBeansExtension::class)
internal class ArtDetailsMapperKtTest {

    @Test
    fun `when art details response object is mapped then domain object is result`(
        @Random response: ArtDetailsObjectResponse
    ) {
        val res = Result.success(response)

        assertEquals(
            ArtDetails(
                id = response.objectNumber,
                title = response.title,
                description = response.description,
                imageUrl = response.webImage?.url,
                author = response.principalOrFirstMaker
            ),
            res.toArtDetails()
                .getOrNull()
        )
    }
}
