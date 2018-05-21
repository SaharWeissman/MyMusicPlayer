package com.saharw.mymusicplayer.service

import android.app.*
import android.content.Intent
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.MediaPlayer
import android.net.Uri
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.os.PowerManager
import android.support.v4.app.NotificationCompat
import android.util.Log
import com.saharw.mymusicplayer.R
import com.saharw.mymusicplayer.entities.data.base.MediaItem
import com.saharw.mymusicplayer.presentation.activities.files.FilesActivity
import java.io.File
import java.io.Serializable


class MusicService : Service(),
        MediaPlayer.OnPreparedListener,
        MediaPlayer.OnErrorListener,
        MediaPlayer.OnCompletionListener {

    private val mPlaying: CharSequence = "mPlaying"

    private val TAG = "MusicService"
    lateinit var mPlayer : MediaPlayer
    var mSongOffset : Int = 0
    val mBinder = MusicBinder()
    var mSongIdx = 0
    lateinit var mSongsList: List<MediaItem>
    lateinit var mActivity: Activity
    private var mSongTitle: CharSequence = "Empty"
    private val NOTIFICATION_CHANNEL_ID = "MuMusicPlayer Notification"
    private val NOTIFICATION_ID: Int = 1

    private var mPlayerReleased: Boolean = false

    override fun onCreate() {
        Log.d(TAG, "onCreate")
        super.onCreate()
        initMusicPlayer()
    }

    override fun onBind(intent: Intent?): IBinder {
        Log.d(TAG, "onBind")
        return mBinder
    }

    override fun onUnbind(intent: Intent?): Boolean {
        Log.d(TAG, "onUnbind")
        mPlayer.stop()
        mPlayer.release()
        mPlayerReleased = true
        return false
    }

    // methods for music service
    fun play() {
        Log.d(TAG, "play: mSongIdx = $mSongIdx")
        mPlayer.reset()
        try{
            val mediaItem = mSongsList[mSongIdx]
            mSongTitle = mediaItem.name
            mPlayer.setDataSource(applicationContext, Uri.fromFile(File(mediaItem.dataPath)))
            mPlayer.prepareAsync()
        }catch (t: Throwable){
            Log.e(TAG, "play: error setting data source for song pos: $mSongIdx", t)
        }
    }

    fun setSongsList(songsList: List<MediaItem>) {
        Log.d(TAG, "setSongsList")
        this.mSongsList = songsList
    }

    fun setSong(mediaItem: MediaItem){
        Log.d(TAG, "setSong: item = $mediaItem")
        mSongIdx = mSongsList.indexOf(mediaItem)
    }

    fun playPrevious() {
        Log.d(TAG, "playPrevious")
        mSongIdx--

        // cyclic behavior
        if(mSongIdx < 0) mSongIdx = mSongsList.size-1
        play()
    }

    fun playNext() {
        Log.d(TAG, "playNext")
        mSongIdx++

        // cyclic behavior
        if(mSongIdx >= mSongsList.size) mSongIdx = 0
        play()
    }

    // methods for music controller
    fun getSongPosition(): Int {
        Log.d(TAG, "getSongPosition")
        return mPlayer.currentPosition
    }

    fun getSongDuration(): Int {
        Log.d(TAG, "getSongDuration")
        return mPlayer.duration
    }

    fun isPlaying(): Boolean {
        Log.d(TAG, "isPlaying")
        return if(!mPlayerReleased) {
            mPlayer.isPlaying
        }else {
            false
        }
    }

    fun pausePlayer() {
        Log.d(TAG, "pausePlayer")
        mPlayer.pause()
    }


    fun seek(position: Int) {
        Log.d(TAG, "seek: position = $position")
        mPlayer.seekTo(position)
    }

    fun start() {
        Log.d(TAG, "start")
        mPlayer.start()
    }

    // Media player callbacks
    override fun onPrepared(mp: MediaPlayer?) {
        Log.d(TAG, "onPrepared: start playing song : ${mSongsList[mSongIdx]}")
        if(mp != null) {
            mp.start()

            // create pending intent (for starting activity from notification) & add notification
            var notification = buildNotification()
            startForeground(NOTIFICATION_ID, notification)

        }else {
            Log.e(TAG, "onPrepared: player is null!")
        }
    }

    override fun onError(mp: MediaPlayer?, what: Int, extra: Int): Boolean {
        Log.e(TAG, "onError: mp = $mp, what = $what, extra = $extra, currSongIdx = ${mSongIdx}")
        mPlayer.reset()
        return false
    }

    override fun onCompletion(mp: MediaPlayer?) {
        Log.d(TAG, "onCompletion: curSongIdx = $mSongIdx")
        if(mPlayer.currentPosition > 0){
            mPlayer.reset()
            playNext()
        }
    }

    override fun onDestroy() {
        Log.d(TAG, "onDestroy")
        super.onDestroy()
        stopForeground(true)
    }

    private fun initMusicPlayer() {
        Log.d(TAG, "initMusicPlayer")
        mSongOffset = 0
        mPlayer = MediaPlayer()

        mPlayer.setWakeMode(applicationContext, PowerManager.PARTIAL_WAKE_LOCK)

        // distinguish between API < Lollipop (before deprecation) and after
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            Log.d(TAG, "initMusicPlayer: api < Lollipop, using 'setAudioAttributes' method")
            mPlayer.setAudioAttributes(AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .build())

        }else {
            Log.d(TAG, "initMusicPlayer: api < Lollipop, using 'setAudioStreamType' method")
            mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC)
        }

        // set listeners
        mPlayer.setOnPreparedListener(this)
        mPlayer.setOnCompletionListener (this)
        mPlayer.setOnErrorListener(this)
    }

    private fun buildNotification(): Notification {
        Log.d(TAG, "buildNotification")

        // here we'll build all pending intent we wish to use in our notification
        var intent = Intent(applicationContext, FilesActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK and Intent.FLAG_ACTIVITY_CLEAR_TASK
        intent.putExtra(FilesActivity.BUNDLE_KEY_MEDIA_ITEMS, mSongsList as Serializable)

        var filesActivityPendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)

        // for Android O - need to create a "notification channel"
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            Log.d(TAG, "buildNotification: sdk int is Oreo or above - creating notification channel")
            var name = getString(R.string.notification_channel_name)
            var desc = getString(R.string.notification_channel_description)
            var importance = NotificationManager.IMPORTANCE_DEFAULT
            var channel = NotificationChannel(NOTIFICATION_CHANNEL_ID, name, importance)
            channel.description = desc

            // register channel with system
            var notifManager = getSystemService(NotificationManager::class.java)
            notifManager.createNotificationChannel(channel)
        }else {
            Log.d(TAG, "buildNotification: sdk int below Oreo - no need for notification channel")
        }

        var notification = NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle(getString(R.string.notification_title))
                .setContentText(getString(R.string.notification_content) + ": ${isPlaying()}")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(filesActivityPendingIntent)
                .setAutoCancel(true) // automatically remove notification after user taps it

        return notification.build()

    }

    // for binding to service
    inner class MusicBinder : Binder() {
        fun getService(activity: Activity) : MusicService {
            Log.d(TAG, "getService")
            this@MusicService.mActivity = activity
            return this@MusicService
        }
    }
}