package com.saharw.mymusicplayer.presentation.artists.mvp

import android.util.Log
import android.view.View
import android.widget.TextView
import com.saharw.mymusicplayer.R
import com.saharw.mymusicplayer.presentation.base.IView
import com.saharw.mymusicplayer.presentation.playlists.FragmentPlaylists

/**
 * Created by saharw on 08/05/2018.
 */
class PlaylistsView(private val fragmentPlaylists: FragmentPlaylists) : IView {

    private val TAG = "PlaylistsView"

    private lateinit var mTxtVHeader : TextView

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
        mTxtVHeader = view.findViewById(R.id.txtV_header)
        mTxtVHeader.text = "Hello from $TAG"
    }
}