package com.saharw.mymusicplayer.presentation.fragments.artists.mvp

import android.content.Context
import android.util.Log
import android.view.View
import com.saharw.mymusicplayer.presentation.base.IView
import com.saharw.mymusicplayer.presentation.fragments.playlists.FragmentPlaylists

/**
 * Created by saharw on 08/05/2018.
 */
class PlaylistsView(private val fragmentPlaylists: FragmentPlaylists) : IView{

    private val TAG = "PlaylistsView"

    override fun onViewCreate() {
        Log.d(TAG, "onViewCreate")
    }

    override fun onViewResume() {
        Log.d(TAG, "onViewResume")
        if(fragmentPlaylists.view != null) {
            initUIComponents(fragmentPlaylists.view!!)
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
        return fragmentPlaylists.context
    }
}