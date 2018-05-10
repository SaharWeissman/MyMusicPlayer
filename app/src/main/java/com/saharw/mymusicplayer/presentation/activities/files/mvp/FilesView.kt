package com.saharw.mymusicplayer.presentation.activities.files.mvp

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import com.saharw.mymusicplayer.entities.data.base.MediaItem
import com.saharw.mymusicplayer.presentation.base.IView
import java.lang.ref.WeakReference

/**
 * Created by saharw on 10/05/2018.
 */
class FilesView(private val activity: AppCompatActivity, private val layoutId: Int, private val mediaItems: Collection<MediaItem>?) : IView {

    private val TAG = "FilesView"
    private lateinit var mActivity : WeakReference<AppCompatActivity>

    override fun onViewCreate() {
        Log.d(TAG, "onViewCreate")
        mActivity = WeakReference(activity)
        var view = mActivity.get()?.layoutInflater?.inflate(layoutId, null,false)
        if(view != null){
            initUIComponents(view)
        }
        mActivity.get()?.setContentView(view)
    }

    override fun onViewResume() {
        Log.d(TAG, "onViewResume")
    }

    override fun onViewPause() {
        Log.d(TAG, "onViewPause")
    }

    override fun onViewDestroy() {
        Log.d(TAG, "onViewDestroy")
    }

    override fun getContext(): Context {
        return activity
    }

    private fun initUIComponents(view: View) {
        Log.d(TAG, "initUIComponents")
    }
}