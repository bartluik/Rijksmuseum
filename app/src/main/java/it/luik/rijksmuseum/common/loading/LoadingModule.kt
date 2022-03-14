package it.luik.rijksmuseum.common.loading

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal abstract class LoadingModule {

    @Binds
    abstract fun bindLoadingDelegate(default: DefaultLoadingDelegate): LoadingDelegate
}
