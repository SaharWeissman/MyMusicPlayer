package com.saharw.mymusicplayer.presentation.fragments.artists.mvp

import android.content.Intent
import android.util.Log
import android.view.View
import com.saharw.mymusicplayer.R
import com.saharw.mymusicplayer.entities.adapters.ArtistsAdapter
import com.saharw.mymusicplayer.entities.data.ArtistsItem
import com.saharw.mymusicplayer.entities.data.base.MediaItem
import com.saharw.mymusicplayer.presentation.activities.files.FilesActivity
import com.saharw.mymusicplayer.presentation.base.IModel
import com.saharw.mymusicplayer.presentation.base.IPresenter
import com.saharw.mymusicplayer.presentation.base.IView
import io.reactivex.android.schedulers.AndroidSchedulers
import java.io.Serializable

/**
 * Created by saharw on 08/05/2018.
 */
class ArtistsPresenter(private val view: IView,
                       private val model: IModel) : IPresenter, View.OnClickListener {

    private val TAG = "ArtistsPresenter"

    override fun onPresenterCreate() {
        Log.d(TAG, "onPresenterCreate")
        view.onViewCreate()
        model.onModelCreate()
    }

    override fun onPresenterResume() {
        Log.d(TAG, "onPresenterResume")
        view.onViewResume()

        // register for artists dataPath
        (model as ArtistsModel).artistsData.observeOn(AndroidSchedulers.mainThread()).subscribe { onArtistsDataReady(it) }
        model.getArtistsData(view.getContext().contentResolver)
    }

    override fun onPresenterPause() {
        Log.d(TAG, "onPresenterPause")
        view.onViewPause()
    }

    override fun onPresenterDestroy() {
        Log.d(TAG, "onPresenterDestroy")
        view.onViewDestroy()
    }

    private fun onArtistsDataReady(data: Collection<ArtistsItem>?) {
        Log.d(TAG, "onArtistsDataReady: dataPath = $data")
        if(data != null){
            (view as ArtistsView).displayArtistsData(data as List<ArtistsItem>, R.layout.artist_item, this)
        }else {
            Log.e(TAG, "onArtistsDataReady: no dataPath exists!")
        }
    }

    override fun onClick(v: View?) {
        Log.d(TAG, "onClick: v = $v")

        // extract items from view's tag
        var viewHolder = v?.tag as ArtistsAdapter.ViewHolder
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
        var nextActivityIntent = Intent(view.getContext(),FilesActivity::class.java)
        nextActivityIntent.putExtra(FilesActivity.BUNDLE_KEY_MEDIA_ITEMS, mediaItems as Serializable)
        view.getContext().startActivity(nextActivityIntent)
    }
}