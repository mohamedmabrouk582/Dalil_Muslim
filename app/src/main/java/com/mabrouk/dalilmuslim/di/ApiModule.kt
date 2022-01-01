package com.mabrouk.dalilmuslim.di

import android.content.Context
import com.mabrouk.dalilmuslim.BuildConfig
import com.mabrouk.data.api.HadithApi
import com.mabrouk.data.api.QuranApi
import com.mabrouk.data.api.TafseerApi
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.readystatesoftware.chuck.ChuckInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ApiModule {

    @Provides
    @Singleton
    fun getQuranApi(@Quran retrofit: Retrofit): QuranApi =
        retrofit.create(QuranApi::class.java)

    @Provides
    @Singleton
    fun getTafseerApi(@Tafseer retrofit: Retrofit): TafseerApi =
        retrofit.create(TafseerApi::class.java)

    @Provides
    @Singleton
    fun getHadithApi(@Hadith retrofit: Retrofit): HadithApi =
        retrofit.create(HadithApi::class.java)

    @Provides
    @Singleton
    @Quran
    fun getRetrofit(client: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .build()

    @Provides
    @Singleton
    @Hadith
    fun getRetrofitSunnah(@Hadith client: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .baseUrl(BuildConfig.Base_Url_sunnah)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .build()

    @Provides
    @Singleton
    @Tafseer
    fun getRetrofitTafseer(client: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .baseUrl(BuildConfig.Base_Url_tafseer)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .build()


    @Provides
    @Singleton
    @Hadith
    fun getClients(
        interceptor: HttpLoggingInterceptor,
        @ApplicationContext context: Context
    ): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .addInterceptor(ChuckInterceptor(context))
            .addInterceptor { chain ->
                var request = chain.request()
                request = request.newBuilder()
                    .addHeader("X-API-Key", BuildConfig.API_KEY).build()
                return@addInterceptor chain.proceed(request)
            }
            .build()

    @Provides
    @Singleton
    fun getInterceptor(): HttpLoggingInterceptor =
        HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

    @Provides
    @Singleton
    fun getClient(
        interceptor: HttpLoggingInterceptor,
        @ApplicationContext context: Context
    ): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(ChuckInterceptor(context))
            .addInterceptor(interceptor)
            .build()

}