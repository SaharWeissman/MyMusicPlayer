package com.saharw.mymusicplayer.presentation.activities.files.mvp

import android.util.Log
import com.saharw.mymusicplayer.presentation.base.IModel

/**
 * Created by saharw on 10/05/2018.
 */
class FilesModel : IModel {

    private val TAG = "FilesModel"

    override fun onModelCreate() {
        Log.d(TAG,  "onModelCreate")
    }

    override fun onModelDestroy() {
        Log.d(TAG,  "onModelDestroy")
    }
}