package com.saharw.mymusicplayer.presentation.activities.files.mvp

import android.util.Log
import com.saharw.mymusicplayer.presentation.base.IModel
import com.saharw.mymusicplayer.presentation.base.IPresenter
import com.saharw.mymusicplayer.presentation.base.IView

/**
 * Created by saharw on 10/05/2018.
 */
class FilesPresenter(private val view: IView, private val model: IModel) : IPresenter {

    private val TAG = "FilesPresenter"

    override fun onPresenterCreate() {
        Log.d(TAG, "onPresenterCreate")

        // init view
        view.onViewCreate()

        // init model
        model.onModelCreate()
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
}