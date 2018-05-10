package com.saharw.mymusicplayer.presentation.activities.main.mvp

import android.annotation.SuppressLint
import android.content.Context
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import com.saharw.mymusicplayer.R
import com.saharw.mymusicplayer.entities.adapters.MusicPlayerPagerAdapter
import com.saharw.mymusicplayer.presentation.base.IView
import java.lang.ref.WeakReference

@SuppressLint("ValidFragment")

/**
 * Created by saharw on 06/05/2018.
 */
class MainView(private val activity: AppCompatActivity, private val layoutId: Int, private var tabsFragArray : Array<Fragment>) : IView {

    private lateinit var mActivity : WeakReference<AppCompatActivity>
    private val TAG = "MainView"

    // UI Components (TODO: transpose to DataBinding usage)
    lateinit var mViewPager : ViewPager

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

    private fun initUIComponents(view: View) {
        Log.d(TAG, "initUIComponents")
        mViewPager = view.findViewById(R.id.pager)
        if(mViewPager != null) {
            initAndSetAdapter(mViewPager, tabsFragArray)
        }else {
            Log.e(TAG, "initUIComponents: view pager is null!")
        }
    }

    private fun initAndSetAdapter(viewPager: ViewPager, fragmentsArray : Array<Fragment>) {
        Log.d(TAG, "initAndSetAdapter")
        var adapter = MusicPlayerPagerAdapter(fragmentsArray, activity.supportFragmentManager)
        viewPager.adapter = adapter
    }

    override fun getContext(): Context {
        return activity
    }
}