package com.saharw.mymusicplayer.presentation.artists.mvp

import android.util.Log
import android.view.View
import com.saharw.mymusicplayer.R
import com.saharw.mymusicplayer.dal.MusicDataProvider
import com.saharw.mymusicplayer.entities.data.ArtistsItem
import com.saharw.mymusicplayer.presentation.base.IModel
import com.saharw.mymusicplayer.presentation.base.IPresenter
import com.saharw.mymusicplayer.presentation.base.IView
import io.reactivex.android.schedulers.AndroidSchedulers

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

        // register for artists data ready
        MusicDataProvider.artistsDataSubject.observeOn(AndroidSchedulers.mainThread()).subscribe { onArtistsDataReady(it) }

        // fetch data
        MusicDataProvider.getAlbumsData(view.getContext().contentResolver)
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
    }

    private fun onArtistsDataReady(data: Collection<ArtistsItem>?) {
        Log.d(TAG, "onArtistsDataReady: data = $data")
        if(data != null){
            (view as ArtistsView).displayArtistsData(data as List<ArtistsItem>, R.layout.artist_item, this)
        }else {
            Log.e(TAG, "onArtistsDataReady: no data exists!")
        }
    }

    override fun onClick(v: View?) {
        Log.d(TAG, "onClick: v = $v")
    }
}