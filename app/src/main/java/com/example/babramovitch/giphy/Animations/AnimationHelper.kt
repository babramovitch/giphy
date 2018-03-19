package com.example.babramovitch.giphy.Animations

import android.content.Context
import android.graphics.drawable.TransitionDrawable
import com.example.babramovitch.giphy.R

/**
 * Created by babramovitch on 2018-03-15.
 *
 */
object AnimationHelper {
    fun getFavoriteTransitionDrawable(context: Context, initialFavorite: Boolean): TransitionDrawable {

        val transitionDrawable = if (initialFavorite) {
            MyTransitionDrawable(arrayOf(
                    context.getDrawable(R.drawable.ic_favorite_white_24dp),
                    context.getDrawable(R.drawable.ic_favorite_border_white_24dp)),
                    true)

        } else {
            MyTransitionDrawable(arrayOf(
                    context.getDrawable(R.drawable.ic_favorite_border_white_24dp),
                    context.getDrawable(R.drawable.ic_favorite_white_24dp)),
                    false)
        }

        transitionDrawable.isCrossFadeEnabled = true

        return transitionDrawable
    }
}