package com.saharw.mymusicplayer.presentation.albums.mvp

import android.util.Log
import com.saharw.mymusicplayer.presentation.base.IModel

/**
 * Created by saharw on 09/05/2018.
 */
class AlbumsModel : IModel {
    private val TAG = "AlbumsModel"

    override fun onModelCreate() {
        Log.d(TAG, "onModelCreate")
    }

    override fun onModelDestroy() {
        Log.d(TAG, "onModelDestroy")
    }
}