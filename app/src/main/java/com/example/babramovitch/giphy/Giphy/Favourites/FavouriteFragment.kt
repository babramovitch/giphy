package com.example.babramovitch.giphy.Giphy.Favourites

import android.content.res.Configuration
import android.os.Bundle
import android.support.v4.app.Fragment
import butterknife.ButterKnife
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.*
import butterknife.BindView
import com.example.babramovitch.giphy.Giphy.Adapters.GiphyRecyclerAdapter
import com.example.babramovitch.giphy.R
import io.realm.Realm

class FavouriteFragment : Fragment(), FavouritesContract.View, FavouriteChanges {

    @BindView(R.id.recycler_view)
    lateinit var recyclerView: RecyclerView

    private lateinit var presenter: FavouritePresenter

    companion object {
        fun newInstance(): FavouriteFragment {
            return FavouriteFragment()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        presenter = FavouritePresenter(this, FavouritesRepository(Realm.getDefaultInstance(), context!!))
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.giphy_favourite_fragment, container, false)
        ButterKnife.bind(this, rootView)

        setupRecyclerView()

        return rootView
    }

    private fun setupRecyclerView() {
        val spans = if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) 2 else 3

        recyclerView.layoutManager = StaggeredGridLayoutManager(spans, 1)
        recyclerView.adapter = GiphyRecyclerAdapter(presenter, R.layout.giphy_item_small)
    }

    override fun updateFavourites() {
        recyclerView.adapter.notifyDataSetChanged()
    }

    override fun removeImage(position: Int) {
        recyclerView.adapter.notifyItemRemoved(position)
    }
}