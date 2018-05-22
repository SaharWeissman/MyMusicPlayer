package com.saharw.mymusicplayer.presentation.activities.files.mvp

import android.util.Log
import com.saharw.mymusicplayer.entities.data.base.MediaItem
import com.saharw.mymusicplayer.presentation.base.IModel
import com.saharw.mymusicplayer.service.MusicService
import java.lang.ref.WeakReference

/**
 * Created by saharw on 10/05/2018.
 */
class FilesModel(private val musicServWeakRef: WeakReference<MusicService>, private val mediaItems: Collection<MediaItem>?) : IModel {

    private val TAG = "FilesModel"

    override fun onModelCreate() {
        Log.d(TAG,  "onModelCreate")
        val serviceInstance = musicServWeakRef.get()
        serviceInstance?.setSongsList(mediaItems as List<MediaItem>)
    }

    override fun onModelDestroy() {
        Log.d(TAG,  "onModelDestroy")
    }

    fun toggleMusicService(mediaItem: MediaItem) {
        Log.d(TAG, "toggleMusicService")
        val serviceInstance = musicServWeakRef.get()
        if(serviceInstance?.isPlaying()!!){
            Log.d(TAG, "toggleMusicService: service is playing - checking if song clicked is the one that's playing")
            if(serviceInstance.getCurrSong() == mediaItem){
                Log.d(TAG, "toggleMusicService: same song - pausing")
                serviceInstance.pausePlayer()
            }else {
                Log.d(TAG, "toggleMusicService: not same song - playing")
                serviceInstance.pausePlayer()
                serviceInstance.setSong(mediaItem)
                serviceInstance.play()
            }
        }else {
            Log.d(TAG, "toggleMusicService: service is not playing, setting song & playing...")
            serviceInstance.setSong(mediaItem)
            serviceInstance.play()
        }
    }
}