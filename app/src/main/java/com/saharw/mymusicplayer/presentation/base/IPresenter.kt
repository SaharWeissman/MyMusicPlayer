package com.saharw.mymusicplayer.presentation.base

/**
 * Created by saharw on 06/05/2018.
 */
interface IPresenter {
    fun onPresenterCreate()
    fun onPresenterResume()
    fun onPresenterPause()
    fun onPresenterDestroy()
}