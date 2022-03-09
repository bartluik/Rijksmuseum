package it.luik.rijksmuseum.overview

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class ArtOverviewViewModel @Inject constructor(

) : ViewModel() {

    val overviewItems = MutableStateFlow<List<ArtOverviewItem>>(emptyList())
    val onNavigateToItem: MutableSharedFlow<String> = MutableSharedFlow()

    init {
        overviewItems.value = listOf(
            ArtOverviewItem(
                id = "1",
                title = "De kunstgalerij van Jan Gildemeester Jansz, Adriaan de Lelie, 1794 - 1795",
                imageUrl = "https://lh3.googleusercontent.com/vkoS9jmZLZWuWH1LNIG3eJUVI6W7XqOUKmFf_lcuB4m1nJydWPXZGggi3XGwmirNj1wLdiO7sH6x5fJ60XJnH2expg=s0"
            ),
            ArtOverviewItem(
                id = "2",
                title = "Stilleven met vergulde bokaal, Willem Claesz. Heda, 1635",
                imageUrl = "https://lh3.googleusercontent.com/mZj-trnVh6jeUDsl1o0a3xNXPat_UOZtKecS4LaZdTLcNoIqtd_yf6beJKCUVzk3NT5SSFeQ-hOzJEOOSV9sg8dHE6VjFjUrGfxwe5Sg=s0"
            ),
            ArtOverviewItem(
                id = "4",
                title = "De kunstgalerij van Jan Gildemeester Jansz",
                imageUrl = "https://lh3.googleusercontent.com/YjKvOkkf3epcceNunYHLeCrDFYNfADyeWnx_TkKyF1tzWPhotNzQaLztgeyfujmhgLG1LSBUv_oOtGL0bTWmgYxBEw=s0"
            ),
            ArtOverviewItem(
                id = "3",
                title = "De Nachtwacht, Rembrandt van Rijn, 1642",
                imageUrl = "https://lh3.googleusercontent.com/J-mxAE7CPu-DXIOx4QKBtb0GC4ud37da1QK7CzbTIDswmvZHXhLm4Tv2-1H3iBXJWAW_bHm7dMl3j5wv_XiWAg55VOM=s0"
            )
        )
    }

    fun onOverviewItemClick(artOverviewItem: ArtOverviewItem) {
        // tracker.log("art_overview_item_click") Some analytics probably
        viewModelScope.launch {
            onNavigateToItem.emit(artOverviewItem.id)
        }
    }
}
