package com.saharw.mymusicplayer.presentation.main.mvp

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.saharw.mymusicplayer.R
import com.saharw.mymusicplayer.presentation.base.IView
import java.lang.ref.WeakReference

/**
 * Created by saharw on 06/05/2018.
 */
class MainView : IView, Fragment() {

    private lateinit var mActivity : WeakReference<AppCompatActivity>
    private val TAG = "MainView"

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view = inflater?.inflate(R.layout.fragment_main, container,false)
        if(view != null){
            initUIComponents(view)
        }
        return view
    }

    override fun onViewCreate(activity: AppCompatActivity, layoutId: Int) {
        Log.d(TAG, "onViewCreate")
        mActivity = WeakReference(activity)
        mActivity.get()!!.supportFragmentManager.beginTransaction().add(layoutId, this).addToBackStack(TAG).commit()
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
    }
}