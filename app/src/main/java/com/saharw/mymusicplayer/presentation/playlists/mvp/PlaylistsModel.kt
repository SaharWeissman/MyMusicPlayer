package com.saharw.mymusicplayer.presentation.artists.mvp

import android.util.Log
import com.saharw.mymusicplayer.presentation.base.IModel

/**
 * Created by saharw on 08/05/2018.
 */
class PlaylistsModel : IModel {

    private val TAG = "PlaylistsModel"

    override fun onModelCreate() {
        Log.d(TAG, "onModelCreate")
    }

    override fun onModelDestroy() {
        Log.d(TAG, "onModelDestroy")
    }
}