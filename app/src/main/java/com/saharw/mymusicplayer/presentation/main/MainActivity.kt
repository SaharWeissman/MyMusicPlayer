package com.saharw.mymusicplayer.presentation.main

import android.util.Log
import com.saharw.mymusicplayer.R
import com.saharw.mymusicplayer.presentation.base.BaseActivity
import com.saharw.mymusicplayer.presentation.base.IPresenter
import com.saharw.mymusicplayer.presentation.main.dagger.DaggerMainActivityComponent
import com.saharw.mymusicplayer.presentation.main.dagger.MainActivityComponent
import javax.inject.Inject

class MainActivity : BaseActivity() {

    private val TAG = "MainActivity"

    @Inject
    lateinit var mPresenter : IPresenter

    override fun initActivity() {
        Log.d(TAG, "initActivity")
        DaggerMainActivityComponent.builder().mainActivityModule(MainActivityComponent.MainActivityModule(this, R.id.main_fragment)).build().inject(this)
        mPresenter.onPresenterCreate()
    }

    override fun onResume() {
        Log.d(TAG, "onResume")
        super.onResume()
        mPresenter.onPresenterResume()
    }

    override fun onPause() {
        Log.d(TAG, "onPause")
        super.onPause()
        mPresenter.onPresenterPause()
    }

    override fun onDestroy() {
        Log.d(TAG, "onDestroy")
        super.onDestroy()
        mPresenter.onPresenterDestroy()
    }
}
