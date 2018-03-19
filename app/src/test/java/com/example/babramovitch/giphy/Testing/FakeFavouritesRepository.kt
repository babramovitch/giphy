package com.example.babramovitch.giphy.Testing

import com.example.babramovitch.giphy.Giphy.Favourites.FavouritesRepositoryContract
import java.io.File

/**
 * Created by babramovitch on 2018-03-15.
 *
 */
class FakeFavouritesRepository : FavouritesRepositoryContract {

    private var results: MutableList<String> = mutableListOf()

    override fun addFavourite(id: String, url: String) {
        results.add(id)
    }

    override fun removeFavourite(id: String) {
        results.remove(id)
    }

    override fun removeFavourite(position: Int) {
        results.removeAt(position)
    }

    override fun isFavourite(id: String): Boolean {
        return results.indexOf(id) != -1
    }

    override fun getCount(): Int {
        return results.count()
    }

    override fun getFile(position: Int): File? {
        return File.createTempFile("test", "gif")
    }
}