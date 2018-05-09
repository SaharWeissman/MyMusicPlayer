package com.saharw.mymusicplayer.presentation.artists.mvp

import android.content.ContentResolver
import android.util.Log
import com.saharw.mymusicplayer.dal.MusicDataProvider
import com.saharw.mymusicplayer.presentation.base.IModel

/**
 * Created by saharw on 08/05/2018.
 */
class ArtistsModel : IModel {

    private val TAG = "ArtistsModel"

    override fun onModelCreate() {
        Log.d(TAG, "onModelCreate")
    }

    override fun onModelDestroy() {
        Log.d(TAG, "onModelDestroy")
    }

    fun getArtistsData(contentResolver: ContentResolver) {
        MusicDataProvider.getAlbumsData(contentResolver)
    }
}