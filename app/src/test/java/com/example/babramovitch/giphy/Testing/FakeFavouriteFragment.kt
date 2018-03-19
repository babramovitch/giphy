package com.example.babramovitch.giphy.Testing

import android.support.v4.app.Fragment
import com.example.babramovitch.giphy.Giphy.Favourites.FavouriteChanges
import com.example.babramovitch.giphy.Giphy.Favourites.FavouritesContract

class FakeFavouriteFragment : Fragment(), FavouritesContract.View, FavouriteChanges {
    override fun updateFavourites() {}
    override fun removeImage(position: Int) {}
}