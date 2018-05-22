package com.saharw.mymusicplayer.presentation.activities.files.mvp

import android.util.Log
import com.saharw.mymusicplayer.entities.data.base.MediaItem
import com.saharw.mymusicplayer.presentation.base.IModel
import com.saharw.mymusicplayer.presentation.base.IPresenter
import com.saharw.mymusicplayer.presentation.base.IView
import com.saharw.mymusicplayer.service.MusicService
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import java.lang.ref.WeakReference

/**
 * Created by saharw on 10/05/2018.
 */
class FilesPresenter(
        private val view: IView,
        private val model: IModel,
        private val onMediaPlayerPreparedSubject: PublishSubject<Boolean>,
        private val musicServWeakRef: WeakReference<MusicService>) : IPresenter {

    private val TAG = "FilesPresenter"

    override fun onPresenterCreate() {
        Log.d(TAG, "onPresenterCreate")

        // init view
        view.onViewCreate()
        (view as FilesView).mOnItemClickSubject.observeOn(Schedulers.single()).subscribe { onMediaItemClicked(it) }
        onMediaPlayerPreparedSubject.observeOn(Schedulers.single()).subscribe { view.showMediaController() }

        // init model
        model.onModelCreate()

        // register for updating song title
        musicServWeakRef.get()?.songNameSubject!!.subscribeOn(Schedulers.single()).subscribe {view.setSongTitle(it)}
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

    private fun onMediaItemClicked(mediaItem: MediaItem?) {
        Log.d(TAG, "onMediaItemClicked: mediaItem: $mediaItem")
        if(mediaItem != null) {
            (view as FilesView).showMediaController()
            (model as FilesModel).toggleMusicService(mediaItem)
        }else {
            Log.e(TAG, "onMediaItemClicked: mediaItem is null!")
        }
    }
}