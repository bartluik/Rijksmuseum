package it.luik.rijksmuseum

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import it.luik.rijksmuseum.databinding.ActivityArtBinding

@AndroidEntryPoint
internal class ArtActivity : AppCompatActivity() {

    private lateinit var binding: ActivityArtBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityArtBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}
