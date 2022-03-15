package it.luik.rijksmuseum.ui.art

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import it.luik.rijksmuseum.ui.databinding.ActivityArtBinding

@AndroidEntryPoint
internal class ArtActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(ActivityArtBinding.inflate(layoutInflater).root)
    }
}
