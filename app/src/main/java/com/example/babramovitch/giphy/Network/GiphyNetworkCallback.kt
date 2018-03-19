package com.example.babramovitch.giphy.Network

import com.example.babramovitch.giphy.Network.Models.GiphyTrending

/**
 * Created by babramovitch on 3/16/2018.
 *
 */
interface GiphyNetworkCallback {
    fun updateUi(results: GiphyTrending)
    fun giphyErrorResponse(message: String?)
}
