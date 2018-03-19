package com.example.babramovitch.giphy

import com.example.babramovitch.giphy.Giphy.Trending.TrendingPresenter
import com.example.babramovitch.giphy.Testing.*
import org.junit.Test
import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class TrendingPresenterTest {
    @Test
    fun fabHiddenAtStart() {
        val fakeTrendingFragment = FakeTrendingFragment.newInstance()
        val presenter = createPresenter(fakeTrendingFragment)

        presenter.showHideFabOnScroll(0, 1, 1)
        assertEquals(true, fakeTrendingFragment.fabVisible)
    }

    @Test
    fun fabReShownFromHiddenOnScrollUp() {
        val fakeTrendingFragment = FakeTrendingFragment.newInstance()
        val presenter = createPresenter(fakeTrendingFragment)

        presenter.showHideFabOnScroll(-1, 0, 1)
        assertEquals(true, fakeTrendingFragment.fabVisible)
    }

    @Test
    fun fabHiddenOnScrollDown() {
        val fakeTrendingFragment = FakeTrendingFragment.newInstance()
        val presenter = createPresenter(fakeTrendingFragment)

        presenter.showHideFabOnScroll(1, 1, 1)
        assertEquals(false, fakeTrendingFragment.fabVisible)
    }

    @Test
    fun fabScrollDownThenUp() {
        val fakeTrendingFragment = FakeTrendingFragment.newInstance()
        val presenter = createPresenter(fakeTrendingFragment)

        presenter.showHideFabOnScroll(1, 1, 1)
        presenter.showHideFabOnScroll(-1, 0, 1)

        assertEquals(true, fakeTrendingFragment.fabVisible)
    }

    @Test
    fun loadGiphyData() {
        val presenter = createPresenter()
        presenter.loadGiphyData("")
        assertEquals(25, presenter.getRecordCount())
    }

    @Test
    fun QuerySearchGivesDifferntResults() {
        val presenter = createPresenter()
        presenter.loadGiphyData("hello world")
        assertEquals(10, presenter.getRecordCount())
    }

    @Test
    fun RequestDataFailsNoInternet() {
        val fakeConnectionChecker = FakeConnectionChecker()
        fakeConnectionChecker.internetStatus = false

        val presenter = createPresenter(fakeConnectionChecker)
        presenter.loadGiphyData("")
        assertEquals(0, presenter.getRecordCount())
    }

    @Test
    fun RequestDataNoResults() {
        val presenter = createPresenter()
        presenter.loadGiphyData("no_results")
        assertEquals(0, presenter.getRecordCount())
    }

    @Test
    fun RequestDataThrowsError() {
        val presenter = createPresenter()
        presenter.loadGiphyData("error")
        assertEquals(0, presenter.getRecordCount())
    }

    @Test
    fun RequestDataFailsNoInternetAfterFirstRequestDataIsRetained() {
        val fakeConnectionChecker = FakeConnectionChecker()
        fakeConnectionChecker.internetStatus = true

        val presenter = createPresenter(fakeConnectionChecker)

        presenter.loadGiphyData("")
        assertEquals(25, presenter.getRecordCount())

        fakeConnectionChecker.internetStatus = false

        presenter.loadGiphyData("")
        assertEquals(25, presenter.getRecordCount())
    }

    @Test
    fun RequestNewPageDataSkip() {
        val presenter = createPresenter()
        presenter.loadGiphyData("")
        presenter.pageIfEndOfList(0, 0, "")
        assertEquals(25, presenter.getRecordCount())
    }

    @Test
    fun RequestNewPageDataSkipOneShortRequirement() {
        val presenter = createPresenter()
        presenter.loadGiphyData("")

        //25 total, so page happens at 25-10 = 15th item visible
        //If 4 are visible, then paging happens at position 11
        presenter.pageIfEndOfList(4, 10, "")

        assertEquals(25, presenter.getRecordCount())
    }

    @Test
    fun RequestNewPageDataSkipNegativeVisiblePosition() {
        val presenter = createPresenter()
        presenter.loadGiphyData("")
        presenter.pageIfEndOfList(4, -1, "")
        assertEquals(25, presenter.getRecordCount())
    }

    @Test
    fun RequestNewPageDataSuccess() {
        val presenter = createPresenter()
        presenter.loadGiphyData("")

        //25 total, so page happens at 25-10 = 15th item visible
        //If 4 are visible, then paging happens at position 11
        presenter.pageIfEndOfList(4, 11, "")

        assertEquals(50, presenter.getRecordCount())
    }

    @Test
    fun RequestNewPageDataSuccessPagedTwice() {
        val fakeConnectionChecker = FakeConnectionChecker()
        fakeConnectionChecker.internetStatus = true

        val presenter = createPresenter(fakeConnectionChecker)

        presenter.loadGiphyData("")
        presenter.pageIfEndOfList(4, 11, "")
        assertEquals(50, presenter.getRecordCount())

        fakeConnectionChecker.internetStatus = true

        presenter.pageIfEndOfList(4, 40, "")
        assertEquals(75, presenter.getRecordCount())
    }

    @Test
    fun RequestNewPageDataSuccessButNoInternet() {
        val fakeConnectionChecker = FakeConnectionChecker()
        fakeConnectionChecker.internetStatus = true

        val presenter = createPresenter(fakeConnectionChecker)

        presenter.loadGiphyData("")
        presenter.pageIfEndOfList(4, 11, "")
        assertEquals(50, presenter.getRecordCount())

        fakeConnectionChecker.internetStatus = false

        presenter.pageIfEndOfList(4, 40, "")
        assertEquals(50, presenter.getRecordCount())
    }

    @Test
    fun RemoveBadItem() {
        val presenter = createPresenter()
        presenter.loadGiphyData("")
        presenter.removeBadRecord(4)

        assertEquals(24, presenter.getRecordCount())
    }

    @Test
    fun RemoveBadItemInvalidIndex() {
        val presenter = createPresenter()
        presenter.loadGiphyData("")
        presenter.removeBadRecord(-1)

        assertEquals(25, presenter.getRecordCount())

        presenter.removeBadRecord(26)

        assertEquals(25, presenter.getRecordCount())
    }

    @Test
    fun AddItemToFavouritesFirstTimeAndStatusRestoredNextOnBind() {

        val fakeFavouritesRepository = FakeFavouritesRepository()
        val presenter = createPresenter(fakeFavouritesRepository)

        presenter.loadGiphyData("")

        val viewHolder = FakeViewHolder()

        presenter.onBindItem(0, viewHolder)
        assertEquals(false, viewHolder.faved)

        presenter.toggleFavourite(0, viewHolder)
        assertEquals(true, viewHolder.faved)

        presenter.onBindItem(0, viewHolder)
        assertEquals(true, viewHolder.faved)

    }

    @Test
    fun AddItemToFavouritesThenRemove() {

        val fakeFavouritesRepository = FakeFavouritesRepository()
        val presenter = createPresenter(fakeFavouritesRepository)

        presenter.loadGiphyData("")

        val viewHolder = FakeViewHolder()

        presenter.onBindItem(0, viewHolder)
        assertEquals(false, viewHolder.faved)

        presenter.toggleFavourite(0, viewHolder)
        assertEquals(true, viewHolder.faved)

        presenter.toggleFavourite(0, viewHolder)
        assertEquals(false, viewHolder.faved)

    }

    fun createPresenter(fakeTrendingFragment: FakeTrendingFragment,
                        fakeConnectionChecker: FakeConnectionChecker,
                        fakeFavouritesRepository: FakeFavouritesRepository,
                        fakeGiphyRepository: FakeGiphyRepository): TrendingPresenter {


        val presenter = TrendingPresenter(
                fakeTrendingFragment,
                fakeConnectionChecker,
                fakeFavouritesRepository,
                fakeGiphyRepository)

        presenter.resetAllData()
        return presenter

    }

    fun createPresenter(): TrendingPresenter {
        val fakeTrendingFragment = FakeTrendingFragment.newInstance()
        val fakeConnectionChecker = FakeConnectionChecker()
        val fakeFavouritesRepository = FakeFavouritesRepository()
        val fakeGiphyRepository = FakeGiphyRepository()
        return createPresenter(fakeTrendingFragment, fakeConnectionChecker, fakeFavouritesRepository, fakeGiphyRepository)
    }

    fun createPresenter(fakeConnectionChecker: FakeConnectionChecker): TrendingPresenter {
        val fakeTrendingFragment = FakeTrendingFragment.newInstance()
        val fakeFavouritesRepository = FakeFavouritesRepository()
        val fakeGiphyRepository = FakeGiphyRepository()
        return createPresenter(fakeTrendingFragment, fakeConnectionChecker, fakeFavouritesRepository, fakeGiphyRepository)
    }

    fun createPresenter(fakeTrendingFragment: FakeTrendingFragment): TrendingPresenter {
        val fakeConnectionChecker = FakeConnectionChecker()
        val fakeFavouritesRepository = FakeFavouritesRepository()
        val fakeGiphyRepository = FakeGiphyRepository()
        return createPresenter(fakeTrendingFragment, fakeConnectionChecker, fakeFavouritesRepository, fakeGiphyRepository)
    }

    fun createPresenter(fakeFavouritesRepository: FakeFavouritesRepository): TrendingPresenter {
        val fakeTrendingFragment = FakeTrendingFragment.newInstance()
        val fakeConnectionChecker = FakeConnectionChecker()
        val fakeGiphyRepository = FakeGiphyRepository()
        return createPresenter(fakeTrendingFragment, fakeConnectionChecker, fakeFavouritesRepository, fakeGiphyRepository)
    }
}