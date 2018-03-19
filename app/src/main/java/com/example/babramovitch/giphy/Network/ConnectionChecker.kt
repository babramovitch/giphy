package com.example.babramovitch.giphy.Network

import android.content.Context
import android.net.ConnectivityManager
import com.example.babramovitch.giphy.R

/**
 * Created by babramovitch on 3/18/2018.
 *
 */

abstract class ConnectionCheckerContract {
    abstract var noInternetMessage: String
    abstract fun isInternetAvailable(): Boolean
}

class ConnectionChecker(val context: Context) : ConnectionCheckerContract() {

    override var noInternetMessage: String = context.getString(R.string.no_internet_connection)

    override fun isInternetAvailable(): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo?.isConnected ?: false
    }
}