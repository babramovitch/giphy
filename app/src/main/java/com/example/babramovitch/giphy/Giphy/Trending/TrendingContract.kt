package com.example.babramovitch.giphy.Giphy.Trending

import com.example.babramovitch.giphy.Giphy.RecyclerRowContract

/**
 * Created by babramovitch on 10/23/2017.
 *
 */

class TrendingContract : RecyclerRowContract() {
    interface View {
        fun updateData()
        fun removeItem(position: Int)
        fun setLoading(refreshing: Boolean)
        fun showError(message: String?)
        fun showFab()
        fun hideFab()
        fun noResultsFound()
    }
}

