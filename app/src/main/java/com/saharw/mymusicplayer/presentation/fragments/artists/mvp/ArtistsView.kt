package com.saharw.mymusicplayer.presentation.fragments.artists.mvp

import android.content.Context
import android.util.Log
import android.view.View
import android.widget.GridView
import com.saharw.mymusicplayer.R
import com.saharw.mymusicplayer.entities.adapters.MyGridViewAdapter
import com.saharw.mymusicplayer.entities.data.ComplexMediaItem
import com.saharw.mymusicplayer.presentation.base.IView
import com.saharw.mymusicplayer.presentation.fragments.artists.FragmentArtists

/**
 * Created by saharw on 08/05/2018.
 */
class ArtistsView(private val fragmentArtists: FragmentArtists) : IView{

    private val TAG = "ArtistsView"

    private lateinit var mArtistsView: GridView
    private lateinit var mArtistsAdapter : MyGridViewAdapter

    override fun onViewCreate() {
        Log.d(TAG, "onViewCreate")
    }

    override fun onViewResume() {
        Log.d(TAG, "onViewResume")
        if(fragmentArtists.view != null) {
            initUIComponents(fragmentArtists.view!!)
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
        mArtistsView = view.findViewById(R.id.gridView)
    }

    fun displayArtistsData(artistsItems: List<ComplexMediaItem>, itemLayoutId : Int, itemOnClickListener: View.OnClickListener) {
        mArtistsAdapter = MyGridViewAdapter(fragmentArtists.activity, artistsItems, itemLayoutId, itemOnClickListener)
        mArtistsView.adapter = mArtistsAdapter
        mArtistsAdapter.notifyDataSetChanged() // redundant?
    }

    override fun getContext(): Context {
        return fragmentArtists.context
    }
}