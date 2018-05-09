package com.saharw.mymusicplayer.presentation.base

import android.content.Context

/**
 * Created by saharw on 06/05/2018.
 */
interface IView {
    fun onViewCreate()
    fun onViewResume()
    fun onViewPause()
    fun onViewDestroy()
    fun getContext() : Context
}