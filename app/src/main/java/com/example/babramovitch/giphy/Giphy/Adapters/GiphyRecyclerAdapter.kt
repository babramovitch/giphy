package com.example.babramovitch.giphy.Giphy.Adapters

import android.graphics.Bitmap
import android.graphics.Color
import android.support.v7.graphics.Palette
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import butterknife.BindView
import butterknife.ButterKnife
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.babramovitch.giphy.R
import com.nebulights.coinstacks.Extensions.inflate
import java.io.File
import android.util.Log
import com.airbnb.lottie.LottieAnimationView
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.babramovitch.giphy.Animations.AnimationHelper
import com.example.babramovitch.giphy.Giphy.RecyclerPresenter
import com.example.babramovitch.giphy.GlideApp
import com.example.babramovitch.giphy.Animations.MyTransitionDrawable
import com.example.babramovitch.giphy.Giphy.RecyclerRowContract

/**
 * Created by babramovitch on 10/26/2017.
 *
 */

class GiphyRecyclerAdapter(private var presenter: RecyclerPresenter, private var layout: Int) : RecyclerView.Adapter<GiphyRecyclerAdapter.ViewHolder>() {

    override fun onBindViewHolder(holder: GiphyRecyclerAdapter.ViewHolder, position: Int) {
        presenter.onBindItem(holder.adapterPosition, holder)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GiphyRecyclerAdapter.ViewHolder {
        val inflatedView = parent.inflate(layout, false)
        return ViewHolder(presenter, inflatedView)
    }

    override fun getItemCount(): Int {
        return presenter.getRecordCount()
    }

    class ViewHolder(val presenter: RecyclerPresenter, view: View) : RecyclerView.ViewHolder(view), RecyclerRowContract.ViewRow {

        @BindView(R.id.giphy_image)
        lateinit var image: ImageView

        @BindView(R.id.giphy_image_static)
        lateinit var imageStatic: ImageView

        @BindView(R.id.giphy_favourite)
        lateinit var favouriteImage: ImageView

        @BindView(R.id.lottie_animation)
        lateinit var loadingAnimation: LottieAnimationView

        private val imageDimension = 250
        private val favouriteAnimationDurationInMillis = 300

        init {
            ButterKnife.bind(this, view)

            favouriteImage.setOnClickListener {
                presenter.toggleFavourite(adapterPosition, this)
            }
        }

        override fun updateItemVisibility(loaded: Boolean) {
            favouriteImage.visibility = if (loaded) View.VISIBLE else View.GONE
            loadingAnimation.visibility = if (loaded) View.GONE else View.VISIBLE
        }

        override fun setFavourite(isFavourite: Boolean) {
            val transitionDrawable = AnimationHelper.getFavoriteTransitionDrawable(favouriteImage.context, isFavourite)
            favouriteImage.setImageDrawable(transitionDrawable)
        }

        override fun updateFavouriteStatus(isFavourite: Boolean) {
            if (isFavourite) {
                val transitionDrawable = favouriteImage.drawable as MyTransitionDrawable
                transitionDrawable.favoriteStart(favouriteAnimationDurationInMillis)
            } else {
                val transitionDrawable = favouriteImage.drawable as MyTransitionDrawable
                transitionDrawable.favoriteReverse(favouriteAnimationDurationInMillis)
            }
        }

        private fun getColorForFavourite(bitmap: Bitmap): Int {
            val optimalSwatchPosition = 4

            val palette = createPaletteSync(bitmap)
            val list = palette.swatches.toMutableList()

            list.sortByDescending { it.population }

            return if (list.size > optimalSwatchPosition) {
                list[optimalSwatchPosition].rgb
            } else {
                Color.BLACK
            }
        }

        private fun setFavouriteColor(color: Int) {
            favouriteImage.setColorFilter(color)
        }

        private fun createPaletteSync(bitmap: Bitmap): Palette = Palette.from(bitmap).generate()

        override fun setImage(url: String) {
            loadGifImage(url)
            loadStaticImage(url)
        }

        override fun setImage(file: File) {
            loadGifImage(file)
            loadStaticImage(file)
        }

        private fun loadGifImage(source: Any) {
            GlideApp.with(image.context)
                    .asGif()
                    .load(source)
                    .override(imageDimension, imageDimension)
                    .listener(object : RequestListener<com.bumptech.glide.load.resource.gif.GifDrawable> {
                        override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<com.bumptech.glide.load.resource.gif.GifDrawable>?, isFirstResource: Boolean): Boolean {
                            Log.e("ERROR", "Load failed", e)
                            presenter.removeBadRecord(adapterPosition)
                            return false
                        }

                        override fun onResourceReady(resource: com.bumptech.glide.load.resource.gif.GifDrawable?, model: Any?, target: Target<com.bumptech.glide.load.resource.gif.GifDrawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                            updateItemVisibility(true)
                            return false
                        }
                    })
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(image)
        }

        private fun loadStaticImage(source: Any) {
            GlideApp.with(image.context)
                    .asBitmap()
                    .load(source)
                    .override(imageDimension, imageDimension)
                    .listener(object : RequestListener<Bitmap> {
                        override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Bitmap>?, isFirstResource: Boolean): Boolean {
                            return false
                        }

                        override fun onResourceReady(resource: Bitmap?, model: Any?, target: Target<Bitmap>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                            if (resource != null) {
                                setFavouriteColor(getColorForFavourite(resource))
                            }
                            return false
                        }
                    })
                    .into(imageStatic)
        }
    }
}

