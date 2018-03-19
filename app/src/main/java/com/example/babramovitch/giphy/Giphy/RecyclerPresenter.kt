package com.example.babramovitch.giphy.Giphy

/**
 * Created by babramovitch on 3/15/2018.
 *
 */
interface RecyclerPresenter {
    fun getRecordCount(): Int
    fun onBindItem(position: Int, view: RecyclerRowContract.ViewRow)
    fun toggleFavourite(position: Int, viewRow: RecyclerRowContract.ViewRow)
    fun removeBadRecord(position: Int)
}