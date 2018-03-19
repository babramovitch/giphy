package com.example.babramovitch.giphy

import android.app.Application
import io.realm.Realm

/**
 * Created by babramovitch on 3/18/2018.
 *
 */
class GiphyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Realm.init(this)
    }
}