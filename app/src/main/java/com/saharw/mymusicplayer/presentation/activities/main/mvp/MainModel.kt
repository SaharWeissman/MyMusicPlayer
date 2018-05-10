package com.saharw.mymusicplayer.presentation.activities.main.mvp

import android.util.Log
import com.saharw.mymusicplayer.presentation.base.IModel

/**
 * Created by saharw on 06/05/2018.
 */
class MainModel : IModel {

    private val TAG = "MainModel"

    override fun onModelCreate() {
        Log.d(TAG, "onModelCreate")
    }

    override fun onModelDestroy() {
        Log.d(TAG, "onModelDestroy")
    }
}