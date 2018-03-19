package com.example.babramovitch.giphy.Giphy.Trending

import com.example.babramovitch.giphy.Giphy.RecyclerPresenter
import com.example.babramovitch.giphy.Giphy.Favourites.FavouritesRepositoryContract
import com.example.babramovitch.giphy.Giphy.RecyclerRowContract
import com.example.babramovitch.giphy.Network.ConnectionCheckerContract
import com.example.babramovitch.giphy.Network.GiphyNetworkCallback
import com.example.babramovitch.giphy.Network.GiphyRepositoryContract
import com.example.babramovitch.giphy.Network.Models.Data
import com.example.babramovitch.giphy.Network.Models.GiphyTrending

/**
 * Created by babramovitch on 10/23/2017.
 *
 */

class TrendingPresenter(private var view: TrendingContract.View,
                        private var connectionChecker: ConnectionCheckerContract,
                        private var favouritesRepository: FavouritesRepositoryContract,
                        private var giphyRepository: GiphyRepositoryContract) :
        GiphyNetworkCallback, RecyclerPresenter {

    companion object {
        private var page = 0
        private var trending: MutableList<Data> = mutableListOf()
    }

    private var pagingLoading = false
    private val pagingOffset = 10
    private val maxItemsPerPage = 25


    override fun updateUi(results: GiphyTrending) {
        page += 1
        pagingLoading = false
        view.setLoading(false)
        trending.addAll(results.data)

        if (trending.isEmpty()) {
            view.noResultsFound()
        } else {
            view.updateData()
        }
    }

    fun loadGiphyData(query: String) {
        if (connectionChecker.isInternetAvailable()) {
            page = 0
            view.setLoading(true)
            view.updateData()

            trending.clear()

            if (query.isNotEmpty()) {
                giphyRepository.getSearch(query, this)
            } else {
                giphyRepository.getTrending(this)
            }
        } else {
            view.showError(connectionChecker.noInternetMessage)
        }
    }

    fun resetAllData() {
        trending.clear()
        page = 0
        view.updateData()
    }

    fun pageIfEndOfList(visibleItemCount: Int, firstVisibleItemPosition: Int, query: String) {
        if (!pagingLoading && (visibleItemCount + firstVisibleItemPosition) >= (trending.size - pagingOffset)
                && firstVisibleItemPosition >= 0) {

            pagingLoading = true
            loadNextPage(query)
        }
    }

    private fun loadNextPage(query: String) {
        if (connectionChecker.isInternetAvailable()) {
            if (query.isEmpty()) {
                giphyRepository.getNextTrending((page * maxItemsPerPage + 1), this)
            } else {
                giphyRepository.getNextSearch(query, (page * maxItemsPerPage + 1), this)
            }
        } else {
            pagingLoading = false
        }
    }

    override fun getRecordCount(): Int {
        return trending.count()
    }

    private fun getImage(position: Int): String {
        return trending[position].images.fixed_height.url
    }

    private fun getId(position: Int): String {
        return trending[position].id
    }

    override fun onBindItem(position: Int, view: RecyclerRowContract.ViewRow) {
        view.updateItemVisibility(false)
        view.setImage(getImage(position))
        view.setFavourite(favouritesRepository.isFavourite(getId(position)))
    }

    override fun toggleFavourite(position: Int, viewRow: RecyclerRowContract.ViewRow) {
        val url = getImage(position)
        val id = getId(position)

        val favourite: Boolean

        if (favouritesRepository.isFavourite(id)) {
            favourite = false
            favouritesRepository.removeFavourite(id)
        } else {
            favourite = true
            favouritesRepository.addFavourite(id, url)
        }

        viewRow.updateFavouriteStatus(favourite)
    }

    override fun removeBadRecord(position: Int) {
        try {
            trending.removeAt(position)
            view.removeItem(position)
        } catch (exception: Exception) {
            exception.printStackTrace()
        }
    }

    override fun giphyErrorResponse(message: String?) {
        view.showError(message)
    }

    fun showHideFabOnScroll(dy: Int, visibility: Int, visible: Int) {
        if (dy > 0 && visibility == visible) {
            view.hideFab()
        } else if (dy < 0 && visibility != visible) {
            view.showFab()
        }
    }
}
