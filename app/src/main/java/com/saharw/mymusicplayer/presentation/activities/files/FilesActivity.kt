package com.saharw.mymusicplayer.presentation.activities.files

import android.Manifest
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Build
import android.os.IBinder
import android.util.Log
import android.widget.MediaController
import com.saharw.mymusicplayer.R
import com.saharw.mymusicplayer.entities.data.base.MediaItem
import com.saharw.mymusicplayer.entities.media.MyMediaController
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
class FilesActivity : BaseActivity(), MediaController.MediaPlayerControl{

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

    // music controller
    private lateinit var mMediaController : MyMediaController
    private var mPlaybackPaused: Boolean = false

    private var mIsPaused = false

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

            if(!::mMediaController.isInitialized){
                mMediaController = MyMediaController(this@FilesActivity)
            }

            // inject after music service was instantiated
            DaggerFilesActivityComponent.builder().filesActivityModule(FilesActivityModule(
                    WeakReference(this@FilesActivity),
                    R.layout.layout_files,
                    R.layout.file_item,
                    mMediaItems,
                    WeakReference(mMusicSrvc),
                    WeakReference(mMediaController))).build().inject(this@FilesActivity)
            mPresenter.onPresenterCreate()

            // setup media player controller
            setupMediaController()
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
        if(mIsPaused){
            mIsPaused = false
            setupMediaController()
        }
    }

    override fun onPause() {
        Log.d(TAG, "onPause")
        super.onPause()
        if(::mPresenter.isInitialized) {
            mPresenter.onPresenterPause()
        }
        mIsPaused = true
    }

    override fun onStop() {
        Log.d(TAG, "onStop")
        mMediaController.hide()
        mMusicSrvc.pausePlayer()
        super.onStop()
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

    // ================================= Media Controller ==========================================

    private fun setupMediaController() {
        Log.d(TAG, "setupMediaController")
        if(!::mMediaController.isInitialized) {
            Log.d(TAG, "setupMediaController: new controller instance")
            mMediaController = MyMediaController(this@FilesActivity)
        }
        mMediaController.setPrevNextListeners(

                //next
                {
                    Log.d(TAG, "onNext Clicked!")
                    mMusicSrvc.playNext()
                    if(mPlaybackPaused){
                        setupMediaController()
                        mPlaybackPaused = false
                    }
                    mMediaController.show(0)
                },

                // prev
                {
                    Log.d(TAG, "onPrev clicked!")
                    mMusicSrvc.playPrevious()
                    if(mPlaybackPaused){
                        setupMediaController()
                        mPlaybackPaused = false
                    }
                    mMediaController.show(0)
                }
        )
        mMediaController.setMediaPlayer(this@FilesActivity)
        mMediaController.setAnchorView(findViewById(R.id.main_view))
        mMediaController.isEnabled = true
    }

    override fun isPlaying(): Boolean {
        Log.d(TAG, "isPlaying")
        return if(mMusicServiceBounded){
            mMusicSrvc.isPlaying()
        }else {
            false
        }
    }

    override fun canSeekForward(): Boolean {
        Log.d(TAG, "canSeekForward")
        return true
    }

    override fun getDuration(): Int {
        Log.d(TAG, "getDuration")
        return if(mMusicServiceBounded && mMusicSrvc.isPlaying()){
            mMusicSrvc.getSongDuration()
        }else {
            if(mMediaItems.isNotEmpty()){
                (mMediaItems as List<MediaItem>)[0].duration.toInt()
            }else {
                Log.e(TAG, "getDuration: either service is null or not playing or activity is not bounded to service - returning 0")
                0
            }
        }
    }

    override fun pause() {
        Log.d(TAG, "pause")
        mPlaybackPaused = true
        if(mMusicServiceBounded){
            mMusicSrvc.pausePlayer()
        }
    }

    override fun getBufferPercentage(): Int {
        Log.d(TAG, "getBufferPercentage")
        return 0
    }

    override fun seekTo(pos: Int) {
        Log.d(TAG, "seekTo: pos = $pos")
        if(mMusicServiceBounded){
            mMusicSrvc.seek(pos)
        }
    }

    override fun getCurrentPosition(): Int {
        Log.d(TAG, "getCurrentPosition")
        return if(mMusicServiceBounded && mMusicSrvc.isPlaying()){
            mMusicSrvc.getSongPosition()
        }else {
            0
        }
    }

    override fun canSeekBackward(): Boolean {
        Log.d(TAG, "canSeekBackward")
        return true
    }

    override fun start() {
        Log.d(TAG, "start")
        if(mMusicServiceBounded){
            mMusicSrvc.setSongsList(mMediaItems as List<MediaItem>)
            mMusicSrvc.start()
        }
    }

    override fun getAudioSessionId(): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun canPause(): Boolean {
        Log.d(TAG, "canPause")
        return true
    }
    // ================================= /Media Controller ==========================================
}