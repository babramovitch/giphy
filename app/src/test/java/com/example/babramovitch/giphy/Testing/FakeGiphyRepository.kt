package com.example.babramovitch.giphy.Testing

import com.example.babramovitch.giphy.Network.GiphyNetworkCallback
import com.example.babramovitch.giphy.Network.GiphyRepositoryContract
import com.example.babramovitch.giphy.Network.Models.*

/**
 * Created by babramovitch on 10/25/2017.
 *
 */

class FakeGiphyRepository() : GiphyRepositoryContract {
    override fun getNextTrending(pagination: Int, callback: GiphyNetworkCallback) {
        execute("", callback)
    }

    override fun getNextSearch(query: String, pagination: Int, callback: GiphyNetworkCallback) {
        execute(query, callback)
    }

    private fun execute(query: String, callback: GiphyNetworkCallback) {
        val data: MutableList<Data> = mutableListOf()

        if (query != "no_results") {
            val count = if (query.isEmpty()) 25 else 10

            for (i in 1..count) {
                data.add(Data("", System.currentTimeMillis().toString(), "", "", "", "", "", "", "", "", "", "", "", 0, 0, "", "",
                        Images(FixedHeight("www.something.url", "", "", "", "", "", "", "")), ""))
            }
        }

        callback.updateUi(GiphyTrending(data,
                Pagination(0, 0, 0),
                Meta(0, "", "")))
    }

    override fun getTrending(callback: GiphyNetworkCallback) {
        getNextTrending(0, callback)
    }

    override fun getSearch(query: String, callback: GiphyNetworkCallback) {
        if (query != "error") {
            getNextSearch(query, 0, callback)
        } else {
            callback.giphyErrorResponse("BOOM")
        }
    }
}