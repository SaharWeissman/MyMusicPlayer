package com.saharw.mymusicplayer.presentation.fragments.albums.mvp

import android.content.ContentResolver
import android.util.Log
import com.saharw.mymusicplayer.dal.MusicDataProvider
import com.saharw.mymusicplayer.entities.data.ComplexMediaItem
import com.saharw.mymusicplayer.presentation.base.IModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.subjects.PublishSubject

/**
 * Created by saharw on 09/05/2018.
 */
class AlbumsModel : IModel {
    private val TAG = "AlbumsModel"

    var albumsData = PublishSubject.create<Collection<ComplexMediaItem>>()

    override fun onModelCreate() {
        Log.d(TAG, "onModelCreate")
    }

    override fun onModelDestroy() {
        Log.d(TAG, "onModelDestroy")
    }

    fun getAlbumsData(contentResolver: ContentResolver){
        Log.d(TAG, "getAlbumsData")

        // register for albums data ready
        MusicDataProvider.albumsDataSubject.observeOn(AndroidSchedulers.mainThread()).subscribe { albumsData.onNext(it) }
        MusicDataProvider.getAlbumsData(contentResolver)
    }
}