package it.luik.rijksmuseum.ui

import io.github.glytching.junit.extension.random.Random
import io.github.glytching.junit.extension.random.RandomBeansExtension
import it.luik.rijksmuseum.art.domain.collection.ArtSummary
import it.luik.rijksmuseum.ui.art.collection.CollectionItem.HeaderCollectionItem
import it.luik.rijksmuseum.ui.art.collection.toOverviewItems
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(RandomBeansExtension::class)
internal class CollectionMapperKtTest {

    @Test
    fun `when art summary is mapped then author header is added to view items`(
        @Random artSummary: ArtSummary
    ) {
        val mappedItems = listOf(artSummary).toOverviewItems()

        val headers = mappedItems.filterIsInstance<HeaderCollectionItem>()
        Assertions.assertEquals(1, headers.size)
        Assertions.assertEquals(HeaderCollectionItem(artSummary.author), headers.first())
    }

    @Test
    fun `when art summaries are mapped and multiple items have same author then author header is added once`(
        @Random artSummary: ArtSummary,
        @Random secondArtSummary: ArtSummary
    ) {
        val mappedItems = listOf(
            artSummary,
            secondArtSummary.copy(author = artSummary.author)
        ).toOverviewItems()

        val headers = mappedItems.filterIsInstance<HeaderCollectionItem>()
        Assertions.assertEquals(1, headers.size)
        Assertions.assertEquals(HeaderCollectionItem(artSummary.author), headers.first())
    }

    @Test
    fun `when art summaries are mapped and multiple items have different authors then multiple author headers are added`(
        @Random artSummary: ArtSummary,
        @Random secondArtSummary: ArtSummary
    ) {
        val mappedItems = listOf(
            artSummary.copy(author = "jaap"),
            secondArtSummary.copy(author = "klaas")
        ).toOverviewItems()

        val headers = mappedItems.filterIsInstance<HeaderCollectionItem>()
        Assertions.assertEquals(2, headers.size)
        Assertions.assertEquals(HeaderCollectionItem("jaap"), headers.first())
        Assertions.assertEquals(HeaderCollectionItem("klaas"), headers.last())
    }
}
