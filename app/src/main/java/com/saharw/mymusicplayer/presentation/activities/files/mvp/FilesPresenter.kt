package com.saharw.mymusicplayer.presentation.activities.files.mvp

import android.util.Log
import com.saharw.mymusicplayer.entities.data.base.MediaItem
import com.saharw.mymusicplayer.presentation.base.IModel
import com.saharw.mymusicplayer.presentation.base.IPresenter
import com.saharw.mymusicplayer.presentation.base.IView
import com.saharw.mymusicplayer.service.MusicService
import io.reactivex.schedulers.Schedulers
import java.lang.ref.WeakReference

/**
 * Created by saharw on 10/05/2018.
 */
class FilesPresenter(
        private val view: IView,
        private val model: IModel,
        private val mediaItems : Collection<MediaItem>?,
        private val serviceWeakRef : WeakReference<MusicService>) : IPresenter {

    private val TAG = "FilesPresenter"

    override fun onPresenterCreate() {
        Log.d(TAG, "onPresenterCreate")

        // init view
        view.onViewCreate()
        (view as FilesView).mOnItemClickSubject.observeOn(Schedulers.io()).subscribe { onMediaItemClicked(it) }

        // init model
        model.onModelCreate()

        // init music service
        initMusicService()
    }

    override fun onPresenterResume() {
        Log.d(TAG, "onPresenterResume")
        view.onViewResume()
    }

    override fun onPresenterPause() {
        Log.d(TAG, "onPresenterPause")
        view.onViewPause()
    }

    override fun onPresenterDestroy() {
        Log.d(TAG, "onPresenterDestroy")
        view.onViewDestroy()
        model.onModelDestroy()
    }

    private fun initMusicService() {
        Log.d(TAG, "initMusicService")
        val serviceInstance = serviceWeakRef.get()
        serviceInstance?.setSongsList(mediaItems as List<MediaItem>)
        serviceInstance?.songNameSubject!!.subscribeOn(Schedulers.io()).subscribe {(view as FilesView).setSongTitle(it)}
    }

    private fun onMediaItemClicked(mediaItem: MediaItem?) {
        Log.d(TAG, "onMediaItemClicked: mediaItem: $mediaItem")
        (view as FilesView).showMediaController()
        if(mediaItem != null) {
            var service = serviceWeakRef.get()
            if(service != null){
                if(service.isPlaying()){
                    Log.d(TAG, "onMediaItemClicked: service is playing, pausing")
                    service.pausePlayer()
                }else {
                    service.setSong(mediaItem)
                    service.play()
                }
            }else {
                Log.e(TAG, "onMediaItemClicked: service is null!")
            }
        }else {
            Log.e(TAG, "onMediaItemClicked: item is null!")
        }
    }
}