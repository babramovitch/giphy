package com.example.babramovitch.giphy.Giphy.Favourites

import android.content.Context
import android.util.Log
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.example.babramovitch.giphy.Giphy.Favourites.Models.FavouritesRealm
import com.example.babramovitch.giphy.GlideApp
import io.realm.Realm
import java.io.*

/**
 * Created by babramovitch on 2018-03-15.
 *
 */

interface FavouritesRepositoryContract {
    fun addFavourite(id: String, url: String)
    fun removeFavourite(id: String)
    fun removeFavourite(position: Int)
    fun isFavourite(id: String): Boolean
    fun getCount(): Int
    fun getFile(position: Int): File?
}

class FavouritesRepository(val realm: Realm, val context: Context) : FavouritesRepositoryContract {

    private var results = realm.where(FavouritesRealm::class.java).findAllSorted("filename")

    override fun addFavourite(id: String, url: String) {
        GlideApp.with(context)
                .downloadOnly() // notice this is much earlier in the chain
                .load(url)
                .into(object : SimpleTarget<File>() {
                    override fun onResourceReady(resource: File, transition: Transition<in File>?) {
                        val newFile = File(context.filesDir.absolutePath + "/" + id + ".gif")
                        resource.copyTo(newFile, true)

                        realm.executeTransaction {
                            val favourite = realm.createObject(FavouritesRealm::class.java)
                            favourite.filename = id
                        }
                        realm.refresh()
                    }
                })
    }

    override fun removeFavourite(id: String) {
        val newFile = File(context.filesDir.absolutePath + "/" + id + ".gif")
        if (newFile.exists()) {
            newFile.delete()

            realm.executeTransaction {
                realm.where(FavouritesRealm::class.java).equalTo("filename", id).findFirst()?.deleteFromRealm()
            }
            realm.refresh()
        }
    }

    override fun removeFavourite(position: Int) {
        Log.i("TAG", "Removing Position " + position)
        val fileName = results[position]?.filename
        if (fileName != null) {
            removeFavourite(fileName)
        }
    }

    override fun isFavourite(id: String): Boolean {
        return realm.where(FavouritesRealm::class.java).equalTo("filename", id).findFirst() != null
    }

    override fun getCount(): Int {
        return realm.where(FavouritesRealm::class.java).findAll().count()
    }

    override fun getFile(position: Int): File? {
        val filename = results[position]?.filename
        return if (!File(context.filesDir.absolutePath + "/" + filename + ".gif").exists()) {
            null
        } else {
            File(context.filesDir.absolutePath + "/" + filename + ".gif")
        }
    }
}
