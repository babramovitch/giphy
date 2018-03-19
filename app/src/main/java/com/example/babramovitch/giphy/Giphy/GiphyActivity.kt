package com.example.babramovitch.giphy.Giphy

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager

import butterknife.BindView
import butterknife.ButterKnife
import com.astuetz.PagerSlidingTabStrip
import com.example.babramovitch.giphy.Giphy.Adapters.GiphyPagerAdapter
import com.example.babramovitch.giphy.Giphy.Favourites.FavouriteChanges
import com.example.babramovitch.giphy.Giphy.Trending.TrendingFragment
import com.example.babramovitch.giphy.R

class GiphyActivity : AppCompatActivity() {

    @BindView(R.id.tabbed_pager)
    lateinit var viewPager: ViewPager
    @BindView(R.id.tabs)
    lateinit var slidingTabs: PagerSlidingTabStrip

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.giphy_activity)
        ButterKnife.bind(this)

        supportActionBar?.elevation = 0.0f

        setupViewPager()
        setupTabs()
    }

    private fun setupViewPager() {
        viewPager.adapter = buildAdapter()
    }

    private fun setupTabs() {
        slidingTabs.shouldExpand = true

        slidingTabs.setOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            var currentPage = 0

            override fun onPageScrolled(i: Int, v: Float, i2: Int) {
            }

            override fun onPageSelected(i: Int) {
                currentPage = i

                val fragment = viewPager.adapter?.instantiateItem(viewPager, i) as FavouriteChanges
                fragment.updateFavourites()

                val trendingFragment = viewPager.adapter?.instantiateItem(viewPager, 0) as TrendingFragment
                if (i == 1) {
                    trendingFragment.hideFab()
                } else {
                    trendingFragment.showFab()
                }
            }

            override fun onPageScrollStateChanged(i: Int) {
                if (currentPage == 0) {
                    val trendingFragment = viewPager.adapter?.instantiateItem(viewPager, 0) as TrendingFragment
                    if (i != ViewPager.SCROLL_STATE_IDLE) {
                        trendingFragment.hideFab()
                    } else if (i == ViewPager.SCROLL_STATE_IDLE) {
                        trendingFragment.showFab()
                    }
                }
            }
        })

        slidingTabs.setViewPager(viewPager)
    }

    private fun buildAdapter(): PagerAdapter {
        return GiphyPagerAdapter(this, supportFragmentManager)
    }
}
