package it.luik.rijksmuseum.art.data

import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.Reusable
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import it.luik.rijksmuseum.art.domain.ArtRepository
import retrofit2.Retrofit
import retrofit2.create

@Module
@InstallIn(SingletonComponent::class)
internal abstract class ArtModule {

    @Binds
    abstract fun bindCollectionRepo(repo: RijksmuseumArtRepository): ArtRepository

    companion object {

        @Provides
        @Reusable
        fun provideRijksmuseumService(retrofit: Retrofit): RijksDataService =
            retrofit.create()
    }
}
