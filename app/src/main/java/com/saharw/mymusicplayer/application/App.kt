package com.saharw.mymusicplayer.application

import android.app.Application
import android.util.Log

/**
 * Created by saharw on 06/05/2018.
 */
class App : Application() {

    private val TAG = "App"

    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "onPresenterCreate")
    }
}