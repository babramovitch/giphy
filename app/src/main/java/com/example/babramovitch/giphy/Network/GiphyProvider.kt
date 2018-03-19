package com.example.babramovitch.giphy.Network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory

/**
 * Created by babramovitch on 10/25/2017.
 *
 */

object GiphyProvider {

    private val client: OkHttpClient = setupOkHttpClient()

    private val GIPHY_URL = "https://api.giphy.com"

    private fun setupOkHttpClient(): OkHttpClient {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BASIC // .BODY for full log output
        return OkHttpClient.Builder().addInterceptor(interceptor).build()
    }

    private fun <T> create(service: Class<T>, baseUrl: String): T {
        val retrofit = Retrofit.Builder()
                .client(client)
                .addConverterFactory(MoshiConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(baseUrl)
                .build()

        return retrofit.create(service)
    }

    fun provideGiphyRepository(): GiphyRepository {
        return GiphyRepository(create(GiphyService::class.java, GIPHY_URL))
    }
}
