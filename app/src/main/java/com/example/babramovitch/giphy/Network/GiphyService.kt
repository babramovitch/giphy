package com.example.babramovitch.giphy.Network

import com.example.babramovitch.giphy.Network.Models.GiphyTrending
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by babramovitch on 10/23/2017.
 *
 */

interface GiphyService {
    @GET("/v1/gifs/trending")
    fun getTrending(@Query("api_key") apiKey: String,
                    @Query("limit") limit: Int,
                    @Query("offset") offset: Int): Observable<GiphyTrending>

    @GET("/v1/gifs/search")
    fun getSearch(@Query("api_key") apiKey: String,
                  @Query("q") searchQuery: String,
                  @Query("limit") limit: Int,
                  @Query("offset") offset: Int): Observable<GiphyTrending>
}


