package it.luik.rijksmuseum.art.ui

import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.adevinta.android.barista.assertion.BaristaListAssertions.assertDisplayedAtPosition
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import it.luik.rijksmuseum.art.domain.ArtRepository
import it.luik.rijksmuseum.ui.R
import it.luik.rijksmuseum.ui.art.ArtActivity
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
@UninstallModules(ArtModule::class)
class OverviewListTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    val activityRule = ActivityScenarioRule(ArtActivity::class.java)

    @BindValue
    @JvmField
    internal val artRepository: ArtRepository = FakeArtRepository()

    @Test
    fun checkAuthorHeaderIsDisplayed() {
        assertDisplayedAtPosition(
            R.id.art_list,
            0,
            R.id.overview_header_title,
            testSummaries.first().author
        )
    }

    @Test
    fun checkTitleIsDisplayed() {
        assertDisplayedAtPosition(
            R.id.art_list,
            1,
            R.id.art_title,
            testSummaries.first().title
        )
    }
}
