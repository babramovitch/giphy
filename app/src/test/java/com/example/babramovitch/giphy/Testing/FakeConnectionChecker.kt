package com.example.babramovitch.giphy.Testing

import com.example.babramovitch.giphy.Network.ConnectionCheckerContract

/**
 * Created by babramovitch on 3/18/2018.
 *
 */

class FakeConnectionChecker : ConnectionCheckerContract() {

    override var noInternetMessage = "No Internet"

    var internetStatus = true

    override fun isInternetAvailable(): Boolean {
        return internetStatus
    }
}