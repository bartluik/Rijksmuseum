package it.luik.rijksmuseum.art.collection

import app.cash.turbine.test
import io.github.glytching.junit.extension.random.Random
import io.github.glytching.junit.extension.random.RandomBeansExtension
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import it.luik.rijksmuseum.art.ArtRepository
import it.luik.rijksmuseum.art.collection.CollectionItem.ArtCollectionItem
import it.luik.rijksmuseum.art.collection.CollectionItem.HeaderCollectionItem
import it.luik.rijksmuseum.common.error.toErrorMessage
import it.luik.rijksmuseum.common.loading.LoadingDelegate
import it.luik.rijksmuseum.network.NetworkException.AuthException
import it.luik.rijksmuseum.test.CoroutinesTestExtension
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(
    RandomBeansExtension::class,
    CoroutinesTestExtension::class
)
internal class ArtCollectionViewModelTest {

    private val repo = mockk<ArtRepository>(relaxed = true)
    private val delegate = mockk<LoadingDelegate>(relaxed = true)

    private fun initViewModel() = ArtCollectionViewModel(
        repo = repo,
        loadingDelegate = delegate
    )

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
        fun `when initialized and art collection is loaded then loading is started and stopped`() {
            coVerify { delegate.startLoading(1) }
            coVerify { delegate.stopLoading() }
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
            @Random pageTwo: ArtCollectionPage
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
        fun `when load more is called then load more indicator is shown`() {
            viewModel.onLoadMore()

            coVerify { delegate.startLoading(2) }
        }

        @Test
        @Disabled("This should actually work")
        fun `when loading is in progress and load more is called then nothing is done`() {
            every { delegate.isLoading() } returns true

            viewModel.onLoadMore()

            coVerify(exactly = 0) { repo.getCollection(any()) }
        }
    }

    @Test
    fun `when loading collection fails then error is mapped to message`() = runBlocking {
        val exception = AuthException()
        coEvery { repo.getCollection(any()) } returns Result.failure(exception)
        val viewModel = initViewModel()

        viewModel.onErrorMessage.test {
            viewModel.onLoadMore()

            assertEquals(exception.toErrorMessage(), awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }
}
