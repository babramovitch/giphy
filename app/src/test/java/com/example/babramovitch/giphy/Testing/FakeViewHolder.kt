package com.example.babramovitch.giphy.Testing

import com.example.babramovitch.giphy.Giphy.RecyclerRowContract
import java.io.File

/**
 * Created by babramovitch on 3/18/2018.
 *
 */
class FakeViewHolder : RecyclerRowContract.ViewRow {

    var faved = false

    override fun setImage(url: String) {}

    override fun setImage(file: File) {}

    override fun updateFavouriteStatus(isFavourite: Boolean) {
        faved = isFavourite
    }

    override fun setFavourite(isFavourite: Boolean) {
        faved = isFavourite
    }

    override fun updateItemVisibility(loaded: Boolean) {}
}