package com.example.babramovitch.giphy.Giphy.Favourites

import com.example.babramovitch.giphy.Giphy.RecyclerRowContract

/**
 * Created by babramovitch on 10/23/2017.
 *
 */

class FavouritesContract : RecyclerRowContract() {
    interface View {
        fun removeImage(position: Int)
    }
}

