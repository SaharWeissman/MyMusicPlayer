package com.saharw.mymusicplayer.presentation.fragments.albums.mvp

import android.content.Context
import android.support.v4.app.Fragment
import android.util.Log
import android.view.View
import android.widget.GridView
import com.saharw.mymusicplayer.R
import com.saharw.mymusicplayer.entities.adapters.MyGridViewAdapter
import com.saharw.mymusicplayer.entities.data.ComplexMediaItem
import com.saharw.mymusicplayer.presentation.base.IView

/**
 * Created by saharw on 09/05/2018.
 */
class AlbumsView(private val fragmentAlbums: Fragment) : IView {

    private val TAG = "AlbumsView"
    private lateinit var mAlbumsView : GridView
    private lateinit var mAlbumsAdapter : MyGridViewAdapter

    override fun onViewCreate() {
        Log.d(TAG, "onViewCreate")
    }

    override fun onViewResume() {
        Log.d(TAG, "onViewResume")
        if(fragmentAlbums.view != null) {
            initUIComponents(fragmentAlbums.view!!)
        }
    }

    override fun onViewPause() {
        Log.d(TAG, "onViewPause")
    }

    override fun onViewDestroy() {
        Log.d(TAG, "onViewDestroy")
    }

    private fun initUIComponents(view: View) {
        Log.d(TAG, "initUIComponents")
        mAlbumsView = view.findViewById(R.id.gridView)
    }

    fun displayAlbumsData(albumsData: List<ComplexMediaItem>, itemLayoutId : Int, itemOnClickListener: View.OnClickListener){
        mAlbumsAdapter = MyGridViewAdapter(fragmentAlbums.activity, albumsData, itemLayoutId, itemOnClickListener)
        mAlbumsView.adapter = mAlbumsAdapter
        mAlbumsAdapter.notifyDataSetChanged() // redundant?
    }

    override fun getContext(): Context {
        return fragmentAlbums.context
    }
}