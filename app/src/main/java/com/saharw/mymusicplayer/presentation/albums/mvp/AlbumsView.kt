package com.saharw.mymusicplayer.presentation.albums.mvp

import android.content.Context
import android.support.v4.app.Fragment
import android.util.Log
import android.view.View
import com.saharw.mymusicplayer.presentation.base.IView

/**
 * Created by saharw on 09/05/2018.
 */
class AlbumsView(private val fragmentAlbums: Fragment) : IView {

    private val TAG = "AlbumsView"

    override fun onViewCreate() {
        Log.d(TAG, "onViewCreate")
    }

    override fun onViewResume() {
        Log.d(TAG, "onViewResume")
        if(fragmentAlbums.view != null) {
            initUIComponents(fragmentAlbums.view!!)
        }
    }

    override fun onViewPause() {
        Log.d(TAG, "onViewPause")
    }

    override fun onViewDestroy() {
        Log.d(TAG, "onViewDestroy")
    }

    private fun initUIComponents(view: View) {
        Log.d(TAG, "initUIComponents")
    }
    override fun getContext(): Context {
        return fragmentAlbums.context
    }

}