package com.example.babramovitch.giphy.Giphy

import java.io.File

/**
 * Created by babramovitch on 3/16/2018.
 *
 */

open class RecyclerRowContract {
    interface ViewRow {
        fun setImage(url: String)
        fun setImage(file: File)
        fun updateFavouriteStatus(isFavourite: Boolean)
        fun setFavourite(isFavourite: Boolean)
        fun updateItemVisibility(loaded: Boolean)
    }
}