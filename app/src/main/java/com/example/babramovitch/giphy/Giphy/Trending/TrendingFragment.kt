package com.example.babramovitch.giphy.Giphy.Trending

import android.app.Activity
import android.content.res.Configuration
import android.os.Bundle
import android.support.design.widget.AppBarLayout
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView

import butterknife.BindView
import butterknife.ButterKnife
import com.airbnb.lottie.LottieAnimationView
import com.example.babramovitch.giphy.Giphy.Favourites.FavouriteChanges
import com.example.babramovitch.giphy.Giphy.Favourites.FavouritesRepository
import com.example.babramovitch.giphy.Giphy.Adapters.GiphyRecyclerAdapter
import com.example.babramovitch.giphy.Network.ConnectionChecker
import com.example.babramovitch.giphy.Network.GiphyProvider
import com.example.babramovitch.giphy.R
import com.jakewharton.rxbinding2.widget.RxTextView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.realm.Realm
import java.util.concurrent.TimeUnit

class TrendingFragment : Fragment(), TrendingContract.View, FavouriteChanges {

    @BindView(R.id.recycler_view)
    lateinit var recyclerView: RecyclerView

    @BindView(R.id.search_layout)
    lateinit var searchLayout: AppBarLayout

    @BindView(R.id.search_text)
    lateinit var searchText: EditText

    @BindView(R.id.fab)
    lateinit var fab: FloatingActionButton

    @BindView(R.id.lottie_animation)
    lateinit var loadingAnimation: LottieAnimationView

    @BindView(R.id.swipe_refresh)
    lateinit var swipeRefreshLayout: SwipeRefreshLayout

    @BindView(R.id.no_results)
    lateinit var noResults: TextView

    private lateinit var presenter: TrendingPresenter
    private var alertDialog: AlertDialog? = null
    private val savedStateSearch = "searchText"
    private val savedStateFirstPositionVisible = "firstVisible"

    companion object {
        fun newInstance(): TrendingFragment {
            return TrendingFragment()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        presenter = TrendingPresenter(this, ConnectionChecker(context!!), FavouritesRepository(Realm.getDefaultInstance(), context!!), GiphyProvider.provideGiphyRepository())
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.giphy_trending_fragment, container, false)
        ButterKnife.bind(this, rootView)

        var firstPositionVisible = 0

        if (savedInstanceState != null) {
            //Specifically setting saved text before RxTextView observer is configured.
            searchText.setText(savedInstanceState.getString(savedStateSearch, ""))
            firstPositionVisible = savedInstanceState.getInt(savedStateFirstPositionVisible, 0)
            setLoading(false)
        }

        setupRecyclerView(firstPositionVisible)
        setupSearch()

        if (savedInstanceState == null || presenter.getRecordCount() == 0) {
            presenter.loadGiphyData(searchText.text.toString())
        }

        return rootView
    }

    private fun setupRecyclerView(firstPositionVisible: Int) {
        if (isPortrait()) {
            recyclerView.layoutManager = LinearLayoutManager(activity)
        } else {
            recyclerView.layoutManager = StaggeredGridLayoutManager(2, 1)
        }

        recyclerView.adapter = GiphyRecyclerAdapter(presenter, R.layout.giphy_item)

        swipeRefreshLayout.setOnRefreshListener {
            presenter.loadGiphyData(searchText.text.toString())
        }

        recyclerView.scrollToPosition(firstPositionVisible)

        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                presenter.showHideFabOnScroll(dy, fab.visibility, View.VISIBLE)

                if (recyclerView != null && dy > 0) {
                    val row = getFirstVisibleItem()
                    presenter.pageIfEndOfList(recyclerView.layoutManager.childCount, row, searchText.text.toString())
                }
            }
        })
    }

    private fun setupSearch() {
        fab.setOnClickListener {
            searchButtonPressed()
        }

        RxTextView.textChanges(searchText)
                .skip(2)
                .debounce(1000, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    presenter.loadGiphyData(it.toString())
                }

        searchText.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                hideSoftKeyboard(searchText)
                true
            } else {
                false
            }
        }
    }

    private fun getFirstVisibleItem(): Int {
        var row = 0

        if (recyclerView.layoutManager is LinearLayoutManager) {
            val llm = recyclerView.layoutManager as LinearLayoutManager
            row = llm.findFirstVisibleItemPosition()
        } else {
            val llm = recyclerView.layoutManager as StaggeredGridLayoutManager
            val firstVisibleItemPosition = llm.findFirstVisibleItemPositions(null)
            if (firstVisibleItemPosition != null && firstVisibleItemPosition.isNotEmpty()) {
                row = firstVisibleItemPosition[0]
            }
        }

        return row
    }

    private fun searchButtonPressed() {
        recyclerView.stopScroll()
        searchLayout.setExpanded(true)
        showSoftKeyboard(searchText)
    }

    override fun setLoading(refreshing: Boolean) {
        if (!refreshing) {
            swipeRefreshLayout.isRefreshing = false
        } else {
            noResults.visibility = View.GONE
        }
        loadingAnimation.visibility = if (refreshing) View.VISIBLE else View.GONE
    }

    override fun noResultsFound() {
        noResults.visibility = View.VISIBLE
    }

    override fun updateData() {
        recyclerView.adapter.notifyDataSetChanged()
    }

    override fun updateFavourites() {
        recyclerView.adapter.notifyDataSetChanged()
    }

    override fun removeItem(position: Int) {
        recyclerView.adapter.notifyItemRemoved(position)
    }

    private fun isPortrait(): Boolean {
        return resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT
    }

    override fun showFab() {
        fab.show()
    }

    override fun hideFab() {
        fab.hide()
    }

    private fun showSoftKeyboard(view: View) {
        val inputMethodManager = activity!!.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        view.requestFocus()
        inputMethodManager.showSoftInput(view, 0)
    }

    private fun hideSoftKeyboard(view: View) {
        val inputMethodManager = activity?.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        view.clearFocus()
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

    override fun showError(message: String?) {
        setLoading(false)

        if (activity != null) {
            alertDialog = AlertDialog.Builder(activity!!).create()
            alertDialog?.setTitle(getString(R.string.error_title))

            val displayMessage = message ?: getString(R.string.unknown_error)

            alertDialog?.setMessage(displayMessage)
            alertDialog?.setButton(AlertDialog.BUTTON_POSITIVE, getString(R.string.ok), { dialogInterface, i ->
            })

            alertDialog?.setButton(AlertDialog.BUTTON_NEGATIVE, getString(R.string.try_again), { dialogInterface, i ->
                presenter.loadGiphyData(searchText.text.toString())
            })

            alertDialog?.show()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString(savedStateSearch, searchText.text.toString())
        outState.putInt(savedStateFirstPositionVisible, getFirstVisibleItem())
        super.onSaveInstanceState(outState)
    }

    override fun onDestroy() {
        alertDialog?.dismiss()
        super.onDestroy()
    }
}