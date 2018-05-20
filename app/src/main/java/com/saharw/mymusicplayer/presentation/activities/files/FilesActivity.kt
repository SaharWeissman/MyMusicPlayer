package com.saharw.mymusicplayer.presentation.activities.files

import android.Manifest
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Build
import android.os.IBinder
import android.util.Log
import com.saharw.mymusicplayer.R
import com.saharw.mymusicplayer.entities.data.base.MediaItem
import com.saharw.mymusicplayer.presentation.activities.files.dagger.DaggerFilesActivityComponent
import com.saharw.mymusicplayer.presentation.base.BaseActivity
import com.saharw.mymusicplayer.presentation.base.IPresenter
import com.saharw.mymusicplayer.presentation.fragments.base.dagger.FilesActivityModule
import com.saharw.mymusicplayer.service.MusicService
import com.tbruyelle.rxpermissions2.RxPermissions
import java.lang.ref.WeakReference
import javax.inject.Inject

/**
 * Created by saharw on 10/05/2018.
 */
class FilesActivity : BaseActivity() {

    private val TAG = "FilesActivity"

    companion object {
        const val BUNDLE_KEY_MEDIA_ITEMS = "mediaItems"
    }

    @Inject
    lateinit var mPresenter : IPresenter

    // music service
    var mMediaItems : Collection<MediaItem> = emptyList()
    private var mMusicServiceBounded: Boolean = false
    private lateinit var mMusicSrvc: MusicService

    private var mPlayIntent: Intent? = null

    // required runtime permissions
    private val mRuntimePermissions = listOf(Manifest.permission.READ_EXTERNAL_STORAGE)

    // service connection
    private var mMusicServiceConnection = object : ServiceConnection {
        override fun onServiceDisconnected(p0: ComponentName?) {
            Log.d(TAG, "onServiceDisconnected")
            mMusicServiceBounded = false
        }

        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            Log.d(TAG, "onServiceConnected")
            var binder = service as MusicService.MusicBinder
            mMusicSrvc = binder.getService(this@FilesActivity)
            mMusicServiceBounded = true

            // inject after music service was instantiated
            DaggerFilesActivityComponent.builder().filesActivityModule(FilesActivityModule(this@FilesActivity, R.layout.fragment_files, R.layout.file_item, mMediaItems, WeakReference(mMusicSrvc))).build().inject(this@FilesActivity)
            mPresenter.onPresenterCreate()
        }
    }

    override fun onStart() {
        Log.d(TAG, "onStart")
        super.onStart()
    }

    override fun initActivity() {
        Log.d(TAG, "initActivity")
        if(intent.hasExtra(BUNDLE_KEY_MEDIA_ITEMS)){
            mMediaItems = intent.getSerializableExtra(BUNDLE_KEY_MEDIA_ITEMS) as List<MediaItem>
        }else {
            Log.e(TAG, "initActivity: no items exist!")
        }

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            Log.d(TAG, "onStart: device is with SDK_INT >= ${Build.VERSION_CODES.M}, requesting runtime permissions")
            requestRuntimePermissions()
        }else {
            bindAndStartMusicService()
        }
    }

    override fun onResume() {
        Log.d(TAG, "onResume")
        super.onResume()
        if(::mPresenter.isInitialized) {
            mPresenter.onPresenterResume()
        }
    }

    override fun onPause() {
        Log.d(TAG, "onPause")
        super.onPause()
        if(::mPresenter.isInitialized) {
            mPresenter.onPresenterPause()
        }
    }

    override fun onDestroy() {
        Log.d(TAG, "onDestroy")
        super.onDestroy()
        if(::mPresenter.isInitialized) {
            mPresenter.onPresenterDestroy()
        }
        stopService(mPlayIntent)
        unbindService(mMusicServiceConnection)
    }

    private fun requestRuntimePermissions() {
        Log.d(TAG, "requestRuntimePermissions")
        val rxPermissions = RxPermissions(this@FilesActivity)
        mRuntimePermissions.forEach { perm ->
            rxPermissions.request(perm)
                .subscribe {granted ->
                    if(granted){
                        Log.d(TAG, "permissions granted!")
                        bindAndStartMusicService()
                    }else {
                        Log.e(TAG, "permissions NOT granted!")
                    }
                }
        }
    }

    private fun bindAndStartMusicService() {
        Log.d(TAG, "bindAndStartMusicService")
        if(mPlayIntent == null){
            Log.d(TAG, "bindAndStartMusicService: intent is null, initializing")
            mPlayIntent = Intent(this@FilesActivity, MusicService::class.java)
            bindService(mPlayIntent, mMusicServiceConnection, Context.BIND_AUTO_CREATE)
            startService(mPlayIntent)
        }else {
            Log.d(TAG, "bindAndStartMusicService: intent is already initialized (and also service...)")
        }
    }
}