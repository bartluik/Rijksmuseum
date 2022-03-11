package it.luik.rijksmuseum.art

import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.Reusable
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import it.luik.rijksmuseum.art.remote.RijksDataService
import retrofit2.Retrofit
import retrofit2.create

@Module
@InstallIn(SingletonComponent::class)
internal abstract class ArtModule {

    @Binds
    abstract fun bindCollectionRepo(repo: RijksmuseumArtCollectionRepository): ArtCollectionRepository

    companion object {

        @Provides
        @Reusable
        fun provideRijksmuseumService(retrofit: Retrofit): RijksDataService =
            retrofit.create()
    }
}
