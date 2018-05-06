package com.saharw.mymusicplayer.presentation.main.mvp

import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.saharw.mymusicplayer.presentation.base.IModel
import com.saharw.mymusicplayer.presentation.base.IPresenter
import com.saharw.mymusicplayer.presentation.base.IView

/**
 * Created by saharw on 06/05/2018.
 */
class MainPresenter(private val activity: AppCompatActivity, private val layoutId: Int, private val view: IView, private val model: IModel) : IPresenter {

    private val TAG = "MainPresenter"

    override fun onPresenterCreate() {
        Log.d(TAG, "onPresenterCreate")

        // init view
        view.onViewCreate(activity, layoutId)

        // init model
        model.onModelCreate()
    }

    override fun onPresenterResume() {
        Log.d(TAG, "onPresenterResume")

        // propagate to view
        view.onViewResume()
    }

    override fun onPresenterPause() {
        Log.d(TAG, "onPresenterPause")

        // propagate to view
        view.onViewPause()
    }

    override fun onPresenterDestroy() {
        Log.d(TAG, "onPresenterDestroy")

        // destroy view
        view.onViewDestroy()

        // destroy model
        model.onModelDestroy()
    }
}