package com.example.babramovitch.giphy.Giphy.Favourites

import com.example.babramovitch.giphy.Giphy.RecyclerPresenter
import com.example.babramovitch.giphy.Giphy.RecyclerRowContract

/**
 * Created by babramovitch on 10/23/2017.
 *
 */

class FavouritePresenter(private var view: FavouritesContract.View,
                         private var favouritesRepository: FavouritesRepositoryContract) :
        RecyclerPresenter {

    override fun getRecordCount(): Int = favouritesRepository.getCount()

    override fun onBindItem(position: Int, view: RecyclerRowContract.ViewRow) {

        view.updateItemVisibility(false)

        val file = favouritesRepository.getFile(position)
        if (file != null) {
            view.setImage(file)
            view.setFavourite(true)
        }
    }

    override fun toggleFavourite(position: Int, viewRow: RecyclerRowContract.ViewRow) {
        if (position != -1) {
            favouritesRepository.removeFavourite(position)
            view.removeImage(position)
        }
    }

    override fun removeBadRecord(position: Int) {}

}