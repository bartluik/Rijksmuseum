package it.luik.rijksmuseum.ui.details

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import io.github.glytching.junit.extension.random.Random
import io.github.glytching.junit.extension.random.RandomBeansExtension
import io.mockk.*
import it.luik.rijksmuseum.art.data.ArtRepository
import it.luik.rijksmuseum.art.data.details.ArtDetails
import it.luik.rijksmuseum.ui.art.error.toErrorMessage
import it.luik.rijksmuseum.test.CoroutinesTestExtension
import it.luik.rijksmuseum.ui.art.details.ArtDetailsViewModel
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(RandomBeansExtension::class, CoroutinesTestExtension::class)
internal class ArtDetailsViewModelTest {

    private val repo = mockk<ArtRepository>(relaxed = true)
    private val delegate = mockk<it.luik.rijksmuseum.loading.LoadingDelegate>(relaxed = true)
    private val savedStateHandle = mockk<SavedStateHandle>(relaxed = true)

    private val defaultArg = "id1"

    private fun initViewModel() = ArtDetailsViewModel(
        savedStateHandle = savedStateHandle,
        repo = repo,
        loadingDelegate = delegate
    )

    @Random
    private lateinit var defaultDetails: ArtDetails

    private lateinit var viewModel: ArtDetailsViewModel

    @BeforeEach
    fun setup() {
        every { savedStateHandle.get<String>("artId") } returns defaultArg
        coEvery { repo.getDetails(any()) } returns Result.success(defaultDetails)
        viewModel = initViewModel()
    }

    @Test
    fun `when initialized then art details are fetched for default arg id`() {
        // Given @BeforeEach

        coVerify { repo.getDetails(defaultArg) }
    }

    @Test
    fun `when details are collected then loading is handled`() {
        // Given @BeforeEach

        coVerifySequence {
            delegate.startLoading()
            delegate.stopLoading()
        }
    }

    @Test
    fun `when details fail then loading is handled`() {
        clearAllMocks() //clear init invocations
        coEvery { repo.getDetails(any()) } returns Result.failure(Exception())

        viewModel.loadArtDetails(defaultArg)

        coVerifySequence {
            delegate.startLoading()
            delegate.stopLoading()
        }
    }

    @Test
    fun `when details fail then message is triggered`() = runBlocking {
        val failure = Exception()
        coEvery { repo.getDetails(any()) } returns Result.failure(failure)
        viewModel.onErrorMessage.test {

            viewModel.loadArtDetails(defaultArg)
            assertEquals(failure.toErrorMessage(), awaitItem())
        }
    }
}
