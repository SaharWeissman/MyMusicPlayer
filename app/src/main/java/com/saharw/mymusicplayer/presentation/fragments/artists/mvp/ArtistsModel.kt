package com.saharw.mymusicplayer.presentation.fragments.artists.mvp

import android.content.ContentResolver
import android.util.Log
import com.saharw.mymusicplayer.dal.MusicDataProvider
import com.saharw.mymusicplayer.entities.data.ArtistsItem
import com.saharw.mymusicplayer.presentation.base.IModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.subjects.PublishSubject

/**
 * Created by saharw on 08/05/2018.
 */
class ArtistsModel : IModel {

    private val TAG = "ArtistsModel"

    var artistsData = PublishSubject.create<Collection<ArtistsItem>>()

    override fun onModelCreate() {
        Log.d(TAG, "onModelCreate")
    }

    override fun onModelDestroy() {
        Log.d(TAG, "onModelDestroy")
    }

    fun getArtistsData(contentResolver: ContentResolver) {

        // register for artists dataPath ready
        MusicDataProvider.artistsDataSubject.observeOn(AndroidSchedulers.mainThread()).subscribe { artistsData.onNext(it) }
        MusicDataProvider.getAlbumsData(contentResolver)
    }
}