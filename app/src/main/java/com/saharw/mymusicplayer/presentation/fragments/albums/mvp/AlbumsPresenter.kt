package com.saharw.mymusicplayer.presentation.fragments.albums.mvp

import android.content.Intent
import android.util.Log
import android.view.View
import com.saharw.mymusicplayer.R
import com.saharw.mymusicplayer.entities.adapters.MyGridViewAdapter
import com.saharw.mymusicplayer.entities.data.ComplexMediaItem
import com.saharw.mymusicplayer.entities.data.base.MediaItem
import com.saharw.mymusicplayer.presentation.activities.files.FilesActivity
import com.saharw.mymusicplayer.presentation.base.IModel
import com.saharw.mymusicplayer.presentation.base.IPresenter
import com.saharw.mymusicplayer.presentation.base.IView
import io.reactivex.android.schedulers.AndroidSchedulers
import java.io.Serializable

/**
 * Created by saharw on 09/05/2018.
 */
class AlbumsPresenter(private val view: IView, private val model: IModel) : IPresenter, View.OnClickListener {

    private val TAG = "AlbumsPresenter"

    override fun onPresenterCreate() {
        Log.d(TAG, "onPresenterCreate")
        view.onViewCreate()
        model.onModelCreate()
    }

    override fun onPresenterResume() {
        Log.d(TAG, "onPresenterResume")
        view.onViewResume()

        (model as AlbumsModel).albumsData.observeOn(AndroidSchedulers.mainThread()).subscribe { onAlbumsDataReady(it) }
        model.getAlbumsData(view.getContext().contentResolver)
    }

    override fun onPresenterPause() {
        Log.d(TAG, "onPresenterPause")
        view.onViewPause()
    }

    override fun onPresenterDestroy() {
        Log.d(TAG, "onPresenterDestroy")
        view.onViewDestroy()
    }

    private fun onAlbumsDataReady(data: Collection<ComplexMediaItem>?) {
        Log.d(TAG, "onAlbumsDataReady: data = $data")
        if(data != null){
            (view as AlbumsView).displayAlbumsData(data as List<ComplexMediaItem>, R.layout.complex_media_item, this)
        }else {
            Log.d(TAG, "onAlbumsDataReady: no data exists!")
        }
    }

    override fun onClick(v: View?) {
        Log.d(TAG, "onClick")

        // extract items from view's tag
        var viewHolder = v?.tag as MyGridViewAdapter.ViewHolder
        var mediaItems = viewHolder.mMediaItems
        if(mediaItems != null && mediaItems.isNotEmpty()){
            Log.d(TAG, "onClick: artist \"${viewHolder.mName}\" has ${mediaItems.size} media items, sending to files fragment")
            onMoveToFilesActivity(mediaItems)
        }else {
            Log.e(TAG, "onClick: no media items exist for artist \"${viewHolder.mName}\"")
        }
    }

    private fun onMoveToFilesActivity(mediaItems: Collection<MediaItem>) {
        Log.d(TAG, "onMoveToFilesActivity")
        var nextActivityIntent = Intent(view.getContext(), FilesActivity::class.java)
        nextActivityIntent.putExtra(FilesActivity.BUNDLE_KEY_MEDIA_ITEMS, mediaItems as Serializable)
        view.getContext().startActivity(nextActivityIntent)
    }
}