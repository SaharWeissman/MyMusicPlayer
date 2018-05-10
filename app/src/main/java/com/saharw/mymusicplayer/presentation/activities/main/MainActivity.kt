package com.saharw.mymusicplayer.presentation.activities.main

import android.Manifest
import android.os.Build
import android.util.Log
import com.saharw.mymusicplayer.R
import com.saharw.mymusicplayer.presentation.activities.main.dagger.DaggerMainActivityComponent
import com.saharw.mymusicplayer.presentation.activities.main.dagger.MainActivityComponent
import com.saharw.mymusicplayer.presentation.base.BaseActivity
import com.saharw.mymusicplayer.presentation.base.IPresenter
import com.saharw.mymusicplayer.presentation.fragments.albums.FragmentAlbums
import com.saharw.mymusicplayer.presentation.fragments.artists.FragmentArtists
import com.saharw.mymusicplayer.presentation.fragments.playlists.FragmentPlaylists
import com.tbruyelle.rxpermissions2.RxPermissions
import javax.inject.Inject

class MainActivity : BaseActivity() {

    private val TAG = "MainActivity"

    @Inject
    lateinit var mPresenter : IPresenter

    override fun initActivity() {
        Log.d(TAG, "initActivity")
        DaggerMainActivityComponent.builder().mainActivityModule(
                MainActivityComponent.MainActivityModule(this, R.layout.fragment_main,
                        arrayOf(
                                FragmentArtists.newInstance(),
                                FragmentAlbums.newInstance(),
                                FragmentPlaylists.newInstance()
                        )))
                .build().inject(this)
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            Log.d(TAG, "initActivity: running on device with SDK_INT > ${Build.VERSION_CODES.M} (${Build.VERSION.SDK_INT}), requesting runtime permissions...")
            var rxPermissions = RxPermissions(this)
            rxPermissions
                    .request(Manifest.permission.READ_EXTERNAL_STORAGE)
                    .subscribe { granted ->
                        if (granted) {
                            Log.d(TAG, "permission granted")
                            mPresenter.onPresenterCreate()
                        } else {
                            Log.e(TAG, "permission NOT granted!")
                        }
                    }
        }
    }

    override fun onStart() {
        Log.d(TAG, "onStart")
        super.onStart()
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
