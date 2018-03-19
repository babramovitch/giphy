package com.example.babramovitch.giphy.Network

import com.example.babramovitch.giphy.Network.Models.GiphyTrending
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

/**
 * Created by babramovitch on 10/25/2017.
 *
 */

interface GiphyRepositoryContract  {
    fun getNextTrending(pagination: Int, callback: GiphyNetworkCallback)
    fun getNextSearch(query: String, pagination: Int, callback: GiphyNetworkCallback)
    fun getTrending(callback: GiphyNetworkCallback)
    fun getSearch(query: String, callback: GiphyNetworkCallback)

}
class GiphyRepository(private val service: GiphyService) : GiphyRepositoryContract {

    private val giphyAPiKey = "mzaxdOGBpceeCouCFrU8ksA0sZSTfiTT"
    private var disposables: CompositeDisposable = CompositeDisposable()
    private val maxItemsPerPage = 25

    override fun getNextTrending(pagination: Int, callback: GiphyNetworkCallback) {
        execute(service.getTrending(giphyAPiKey, maxItemsPerPage, pagination), callback)
    }

    override fun getNextSearch(query: String, pagination: Int, callback: GiphyNetworkCallback) {
        execute(service.getSearch(giphyAPiKey, query, maxItemsPerPage, pagination), callback)
    }

    private fun execute(observable: Observable<GiphyTrending>, callback: GiphyNetworkCallback) {
        val disposable = observable.observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({ result ->
                    callback.updateUi(result)
                }, { error ->
                    error.printStackTrace()
                    callback.giphyErrorResponse(error.message)
                })

        disposables.add(disposable)
    }

    override fun getTrending(callback: GiphyNetworkCallback) {
        getNextTrending(0, callback)
    }

    override fun getSearch(query: String, callback: GiphyNetworkCallback) {
        getNextSearch(query, 0, callback)
    }
}
