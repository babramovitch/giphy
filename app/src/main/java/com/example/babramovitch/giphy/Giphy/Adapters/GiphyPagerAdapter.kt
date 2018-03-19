package com.example.babramovitch.giphy.Giphy.Adapters

import android.content.Context
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.example.babramovitch.giphy.Giphy.Favourites.FavouriteFragment
import com.example.babramovitch.giphy.Giphy.Trending.TrendingFragment
import com.example.babramovitch.giphy.R
import java.lang.Exception

/**
 * Created by babramovitch on 2018-03-14.
 *
 */

class GiphyPagerAdapter(context: Context, fragmentManager: FragmentManager)
    : FragmentPagerAdapter(fragmentManager) {

    private var titles = context.resources.getStringArray(R.array.tabs_names)

    override fun getCount(): Int = 2

    override fun getItem(position: Int): Fragment = when (position) {
        0 -> TrendingFragment.newInstance()
        1 -> FavouriteFragment.newInstance()
        else -> {
            throw Exception("Invalid Position")
        }
    }

    override fun getPageTitle(position: Int): String = titles[position]
}