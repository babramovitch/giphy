package com.example.babramovitch.giphy.Animations

import android.graphics.drawable.Drawable
import android.graphics.drawable.TransitionDrawable

/**
 * Created by babramovitch on 2018-03-15.
 *
 */
class MyTransitionDrawable(layers: Array<Drawable>, private var initialFavorite: Boolean) : TransitionDrawable(layers) {

    fun favoriteStart(durationMillis: Int) {
        if (!initialFavorite) {
            super.startTransition(durationMillis)
        } else {
            super.reverseTransition(durationMillis)
        }
    }

    fun favoriteReverse(durationMillis: Int) {
        if (!initialFavorite) {
            super.reverseTransition(durationMillis)
        } else {
            super.startTransition(durationMillis)
        }
    }
}