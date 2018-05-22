package com.saharw.mymusicplayer.dal

import android.content.ContentResolver
import android.util.Log
import com.saharw.mymusicplayer.dal.runnables.GetAlbumsDataRunnable
import com.saharw.mymusicplayer.dal.runnables.GetArtistsDataRunnable
import com.saharw.mymusicplayer.entities.data.ComplexMediaItem
import io.reactivex.subjects.PublishSubject
import java.util.concurrent.Executors

/**
 * Created by saharw on 09/05/2018.
 */
object MusicDataProvider {

    private val TAG = "MusicDataProvider"

    var artistsDataSubject = PublishSubject.create<Collection<ComplexMediaItem>>()
    var albumsDataSubject = PublishSubject.create<Collection<ComplexMediaItem>>()

    private var mediaDataExecutor = Executors.newSingleThreadExecutor() // for running cp access in background

    fun getArtistsData(contentResolver: ContentResolver) {
        Log.d(TAG, "getArtistsData")
        var runnable = GetArtistsDataRunnable(contentResolver)
        mediaDataExecutor.execute(runnable)
    }

    fun getAlbumsData(contentResolver: ContentResolver){
        Log.d(TAG, "getAlbumsData")
        var runnable = GetAlbumsDataRunnable(contentResolver)
        mediaDataExecutor.execute(runnable)
    }
}