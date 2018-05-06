package com.saharw.mymusicplayer.presentation.base

import android.support.v7.app.AppCompatActivity

/**
 * Created by saharw on 06/05/2018.
 */
interface IView {
    fun onViewCreate(activity: AppCompatActivity, layoutId: Int)
    fun onViewResume()
    fun onViewPause()
    fun onViewDestroy()
}