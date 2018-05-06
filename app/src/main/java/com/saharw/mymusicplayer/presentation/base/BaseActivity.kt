package com.saharw.mymusicplayer.presentation.base

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.WindowManager
import com.saharw.mymusicplayer.R

/**
 * Created by saharw on 06/05/2018.
 */
abstract class BaseActivity : AppCompatActivity()  {

    private val TAG = "BaseActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(TAG, "onCreate")
        super.onCreate(savedInstanceState)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        setContentView(R.layout.activity_base)
        initActivity()
    }

    abstract fun initActivity()
}