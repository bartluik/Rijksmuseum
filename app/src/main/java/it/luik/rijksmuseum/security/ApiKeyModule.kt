package it.luik.rijksmuseum.security

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import it.luik.rijksmuseum.BuildConfig
import it.luik.rijksmuseum.network.RijksDataApiKey

@Module
@InstallIn(SingletonComponent::class)
internal object ApiKeyModule {

    @Provides
    @RijksDataApiKey
    fun provideApiKey(): String = BuildConfig.RIJKS_DATA_API_KEY
}
