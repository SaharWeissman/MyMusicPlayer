package com.saharw.mymusicplayer.presentation.artists.mvp

import android.util.Log
import android.view.View
import com.saharw.mymusicplayer.presentation.artists.ArtistsFragment
import com.saharw.mymusicplayer.presentation.base.IView

/**
 * Created by saharw on 08/05/2018.
 */
class ArtistsView(private val fragment: ArtistsFragment) : IView {

    private val TAG = "ArtistsView"

    override fun onViewCreate() {
        Log.d(TAG, "onViewCreate")
    }

    override fun onViewResume() {
        Log.d(TAG, "onViewResume")
        if(fragment.view != null) {
            initUIComponents(fragment.view!!)
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
}