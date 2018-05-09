package com.saharw.mymusicplayer.presentation.albums.mvp

import android.support.v4.app.Fragment
import android.util.Log
import android.view.View
import android.widget.TextView
import com.saharw.mymusicplayer.R
import com.saharw.mymusicplayer.presentation.base.IView

/**
 * Created by saharw on 09/05/2018.
 */
class AlbumsView(private val fragmentAlbums: Fragment) : IView {

    private val TAG = "AlbumsView"

    private lateinit var mTxtVHeader : TextView

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
        mTxtVHeader = view.findViewById(R.id.txtV_header)
        mTxtVHeader.text = "Hello from $TAG"
    }
}