package com.example.babramovitch.giphy.Testing

import android.support.v4.app.Fragment
import com.example.babramovitch.giphy.Giphy.Favourites.FavouriteChanges
import com.example.babramovitch.giphy.Giphy.Trending.TrendingContract

class FakeTrendingFragment : Fragment(), TrendingContract.View, FavouriteChanges {

    var fabVisible: Boolean = true

    companion object {
        fun newInstance(): FakeTrendingFragment {
            return FakeTrendingFragment()
        }
    }

    override fun showFab() {
        fabVisible = true
    }

    override fun hideFab() {
        fabVisible = false
    }

    override fun setLoading(refreshing: Boolean) {
    }

    override fun noResultsFound() {
    }

    override fun updateData() {
    }

    override fun updateFavourites() {
    }

    override fun removeItem(position: Int) {
    }

    override fun showError(message: String?) {
    }
}