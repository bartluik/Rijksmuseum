package it.luik.rijksmuseum.network

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import timber.log.Timber
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object NetworkModule {

    @Provides
    fun provideMoshi(): Moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    @Provides
    fun provideLoggingInterceptor(): HttpLoggingInterceptor =
        HttpLoggingInterceptor { message ->
            Timber.tag("Rijksmuseum-HTTP").i(message)
        }.setLevel(HttpLoggingInterceptor.Level.BODY)

    @Provides
    internal fun provideOkHttpClientBuilder(
        loggingInterceptor: HttpLoggingInterceptor,
        @ApplicationContext context: Context,
    ): OkHttpClient.Builder =
        OkHttpClient.Builder()
            .addNetworkInterceptor(loggingInterceptor)
            .addInterceptor(ChuckerInterceptor.Builder(context).build())

    @Provides
    @Singleton
    fun provideOkHttpClient(
        builder: OkHttpClient.Builder,
        keyInterceptor: RijksDataKeyInterceptor,
    ): OkHttpClient = builder
        .addInterceptor(keyInterceptor)
        .build()

    @Provides
    @Singleton
    fun provideRetrofitClient(
        httpClient: OkHttpClient,
        moshi: Moshi
    ): Retrofit = Retrofit.Builder()
        .baseUrl(BuildConfig.RIJKS_DATA_BASE_URL)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .client(httpClient)
        .build()
}
