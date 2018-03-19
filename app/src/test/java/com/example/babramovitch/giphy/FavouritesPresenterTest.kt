package com.example.babramovitch.giphy

import com.example.babramovitch.giphy.Giphy.Favourites.FavouritePresenter
import com.example.babramovitch.giphy.Testing.*
import org.junit.Test
import org.junit.Assert.*

class FavouritesPresenterTest {

    @Test
    fun EmptyRepository() {
        val fakeFavouritesRepository = FakeFavouritesRepository()
        val presenter = FavouritePresenter(FakeFavouriteFragment(), fakeFavouritesRepository)
        assertEquals(0, presenter.getRecordCount())
    }

    @Test
    fun AddItemThenRemoveIt() {
        val fakeFavouritesRepository = FakeFavouritesRepository()
        val presenter = FavouritePresenter(FakeFavouriteFragment(), fakeFavouritesRepository)

        fakeFavouritesRepository.addFavourite("abc", "test.gif")
        assertEquals(1, presenter.getRecordCount())
        presenter.toggleFavourite(0, FakeViewHolder())
        assertEquals(0, presenter.getRecordCount())
    }

    @Test
    fun RemoveNegativePositionFailRemove() {
        val fakeFavouritesRepository = FakeFavouritesRepository()
        val presenter = FavouritePresenter(FakeFavouriteFragment(), fakeFavouritesRepository)

        fakeFavouritesRepository.addFavourite("abc", "test.gif")
        assertEquals(1, presenter.getRecordCount())
        presenter.toggleFavourite(-1, FakeViewHolder())
        assertEquals(1, presenter.getRecordCount())
    }
}