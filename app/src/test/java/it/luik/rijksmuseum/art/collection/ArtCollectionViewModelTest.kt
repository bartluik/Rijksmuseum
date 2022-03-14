package it.luik.rijksmuseum.art.collection

import androidx.annotation.StringRes
import app.cash.turbine.test
import io.github.glytching.junit.extension.random.Random
import io.github.glytching.junit.extension.random.RandomBeansExtension
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import it.luik.rijksmuseum.R
import it.luik.rijksmuseum.art.ArtRepository
import it.luik.rijksmuseum.common.StringResource
import it.luik.rijksmuseum.network.NetworkException.*
import it.luik.rijksmuseum.art.collection.ArtCollectionViewModel.LoadingState.*
import it.luik.rijksmuseum.art.collection.CollectionItem.ArtCollectionItem
import it.luik.rijksmuseum.art.collection.CollectionItem.HeaderCollectionItem
import it.luik.rijksmuseum.test.CoroutinesTestExtension
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.io.IOException
import java.util.stream.Stream

@ExtendWith(
    RandomBeansExtension::class,
    CoroutinesTestExtension::class
)
internal class ArtCollectionViewModelTest {

    private val repo = mockk<ArtRepository>(relaxed = true)

    private fun initViewModel() = ArtCollectionViewModel(repo = repo)

    @Test
    fun `when art item is clicked then navigate to details`(
        @Random artItem: ArtCollectionItem
    ) = runBlocking {
        val vm = initViewModel()

        vm.onNavigateToItem.test {
            vm.onCollectionItemClick(artItem)
            assertEquals(artItem.id, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `when collection fetched then the total items count is updated`(
        @Random page: ArtCollectionPage
    ) = runBlocking {
        coEvery { repo.getCollection(any()) } returns Result.success(page)

        val vm = initViewModel()

        vm.totalItemsCount.test {
            assertEquals(page.totalCount, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Nested
    inner class InitTest {

        @Random
        private lateinit var defaultPage: ArtCollectionPage

        private lateinit var viewModel: ArtCollectionViewModel

        @BeforeEach
        fun setup() {
            coEvery { repo.getCollection(any()) } returns Result.success(defaultPage)
            viewModel = initViewModel()
        }

        @Test
        fun `when initialized then the first page of art collection is fetched`() {
            coVerify { repo.getCollection(1) }
        }

        @Test
        fun `when initialized then is fetched then pagination is updated`(
        ) {
            assertEquals(defaultPage.totalCount, viewModel.totalItemsCount.value)
        }

        @Test
        fun `when initialized and art collection is loaded then loading indicator is shown`() =
            runBlocking {
                viewModel.loadingState.test {
                    viewModel.loadArtCollection()

                    assertEquals(NONE, awaitItem())
                    assertEquals(LOADING, awaitItem())
                    assertEquals(NONE, awaitItem())
                    cancelAndIgnoreRemainingEvents()
                }
            }
    }

    @Nested
    inner class HeadersTest {

        @Test
        fun `when art collection is fetched then author header is added to view items`(
            @Random artSummary: ArtSummary,
            @Random page: ArtCollectionPage
        ) {
            coEvery { repo.getCollection(any()) } returns Result.success(
                page.copy(summaries = listOf(artSummary))
            )

            val viewModel = initViewModel()

            val headers = viewModel.collectionItems.value.filterIsInstance<HeaderCollectionItem>()
            assertEquals(1, headers.size)
            assertEquals(HeaderCollectionItem(artSummary.author), headers.first())
        }

        @Test
        fun `when art collection is fetched and multiple items from same author then author header is added once`(
            @Random artSummary: ArtSummary,
            @Random secondArtSummary: ArtSummary,
            @Random page: ArtCollectionPage
        ) {
            coEvery { repo.getCollection(any()) } returns Result.success(
                page.copy(
                    summaries = listOf(
                        artSummary,
                        secondArtSummary.copy(author = artSummary.author)
                    )
                )
            )

            val viewModel = initViewModel()

            val headers = viewModel.collectionItems.value.filterIsInstance<HeaderCollectionItem>()
            assertEquals(1, headers.size)
            assertEquals(HeaderCollectionItem(artSummary.author), headers.first())
        }

        @Test
        fun `when art collection is fetched and multiple items from different authors then multiple author headers are added`(
            @Random artSummary: ArtSummary,
            @Random secondArtSummary: ArtSummary,
            @Random page: ArtCollectionPage
        ) {
            coEvery { repo.getCollection(any()) } returns Result.success(
                page.copy(
                    summaries = listOf(
                        artSummary.copy(author = "jaap"),
                        secondArtSummary.copy(author = "klaas")
                    )
                )
            )

            val viewModel = initViewModel()

            val headers = viewModel.collectionItems.value.filterIsInstance<HeaderCollectionItem>()
            assertEquals(2, headers.size)
            assertEquals(HeaderCollectionItem("jaap"), headers.first())
            assertEquals(HeaderCollectionItem("klaas"), headers.last())
        }
    }

    @Nested
    inner class LoadMoreTest {

        private lateinit var viewModel: ArtCollectionViewModel

        @BeforeEach
        fun setup(@Random defaultPage: ArtCollectionPage) {
            coEvery { repo.getCollection(any()) } returns Result.success(defaultPage)
            viewModel = initViewModel()
        }

        @Test
        fun `when load more is called then second page is fetched`() {
            viewModel.onLoadMore()

            coVerify { repo.getCollection(2) }
        }

        @Test
        fun `when second page is fetched then the result is added to the view items`(
            @Random pageOne: ArtCollectionPage,
            @Random pageTwo: ArtCollectionPage,
            @Random artSummary: ArtSummary
        ) = runBlocking {
            coEvery { repo.getCollection(1) } returns Result.success(pageOne)
            coEvery { repo.getCollection(2) } returns Result.success(pageTwo)
            viewModel = initViewModel()

            viewModel.collectionItems.test {
                viewModel.onLoadMore()

                awaitItem() // ignore initial item
                assertEquals(
                    pageOne.summaries.size + pageTwo.summaries.size,
                    awaitItem()
                        .filterIsInstance<ArtCollectionItem>()
                        .count()
                )
                cancelAndIgnoreRemainingEvents()
            }
        }

        @Test
        fun `when load more is called then load more indicator is shown`() = runBlocking {
            viewModel.loadingState.test {
                viewModel.onLoadMore()

                assertEquals(NONE, awaitItem())
                assertEquals(LOADING_MORE, awaitItem())
                assertEquals(NONE, awaitItem())
                cancelAndIgnoreRemainingEvents()
            }
        }
    }

    @ParameterizedTest
    @MethodSource("failureProvider")
    fun `when loading collection fails then exception is shown`(
        failure: Throwable, @StringRes errorMessage: Int
    ) = runBlocking {
        coEvery { repo.getCollection(any()) } returns Result.failure(AuthException())
        val viewModel = initViewModel()

        viewModel.onErrorMessage.test {
            viewModel.onLoadMore()

            assertEquals(StringResource.Id(R.string.error_auth), awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    companion object {
        @JvmStatic
        fun failureProvider(): Stream<Arguments> {
            return Stream.of(
                Arguments.of(AuthException(), R.string.error_auth),
                Arguments.of(ClientException(), R.string.error_client),
                Arguments.of(ServerException(), R.string.error_server),
                Arguments.of(IllegalStateException(), R.string.error_generic),
                Arguments.of(IOException(), R.string.error_generic),
                Arguments.of(Throwable(), R.string.error_generic),
            )
        }
    }
}
