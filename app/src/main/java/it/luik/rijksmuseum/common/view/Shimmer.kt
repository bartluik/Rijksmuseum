package it.luik.rijksmuseum.common.view

import android.content.Context
import androidx.core.content.ContextCompat
import com.facebook.shimmer.Shimmer
import com.facebook.shimmer.ShimmerDrawable
import it.luik.rijksmuseum.R


fun shimmerHighlightDrawable(context: Context) = ShimmerDrawable().apply {
    setShimmer(
        Shimmer.ColorHighlightBuilder()
            .setBaseColor(ContextCompat.getColor(context, R.color.dim_dark_grey))
            .setHighlightColor(ContextCompat.getColor(context, R.color.white))
            .setBaseAlpha(1f)
            .setHighlightAlpha(1f)
            .setDuration(SHIMMER_DURATION)
            .setDirection(Shimmer.Direction.LEFT_TO_RIGHT)
            .setAutoStart(true)
            .build()
    )
}

private const val SHIMMER_DURATION = 500L
