package it.luik.rijksmuseum.art.ui

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
abstract class ArtModule {

    @Binds
    @Reusable
    internal abstract fun bindCollectionRepo(repo: RijksmuseumArtRepository): ArtRepository

    companion object {

        @Provides
        @Reusable
        internal fun provideRijksmuseumService(retrofit: Retrofit): RijksDataService =
            retrofit.create()
    }
}
