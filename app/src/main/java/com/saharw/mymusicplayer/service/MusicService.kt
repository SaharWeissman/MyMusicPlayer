package com.saharw.mymusicplayer.service

import android.app.*
import android.content.ComponentName
import android.content.Intent
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.MediaPlayer
import android.media.session.MediaSession
import android.net.Uri
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.os.PowerManager
import android.util.Log
import com.saharw.mymusicplayer.R
import com.saharw.mymusicplayer.entities.data.base.MediaItem
import com.saharw.mymusicplayer.presentation.activities.files.FilesActivity
import io.reactivex.subjects.PublishSubject
import java.io.File
import java.io.Serializable


class MusicService : Service(),
        MediaPlayer.OnPreparedListener,
        MediaPlayer.OnErrorListener,
        MediaPlayer.OnCompletionListener {

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

    val songNameSubject = PublishSubject.create<String>()

    val onPreparedSubject = PublishSubject.create<Boolean>()

    // media session
    private lateinit var mMediaSession: MediaSession
    private val ACTION_TOGGLE_PLAYBACK = "com.saharw.mymusicplayer.service.TOGGLE_PLAYBACK"
    private val ACTION_TOGGLE_PLAYBACK_VALUE = 1
    private val ACTION_PREV = "com.saharw.mymusicplayer.service.PREV"
    private val ACTION_PREV_VALUE = 3
    private val ACTION_NEXT = "com.saharw.mymusicplayer.service.NEXT"
    private val ACTION_NEXT_VALUE = 2

    // notifications
    private lateinit var mNotificationBuilder: Notification.Builder
    private lateinit var mNotifActionWhenPaused : Array<Notification.Action>

    private lateinit var mNotifActionWhenPlaying : Array<Notification.Action>

    override fun onCreate() {
        Log.d(TAG, "onCreate")
        super.onCreate()
        initMusicPlayer()

        mNotifActionWhenPaused = arrayOf(
                buildAction(R.drawable.ic_prev, getString(R.string.notification_action_prev), retreivePlaybackAction(ACTION_PREV_VALUE)!!),
                buildAction(R.drawable.ic_play, getString(R.string.notification_action_pause), retreivePlaybackAction(ACTION_TOGGLE_PLAYBACK_VALUE)!!),
                buildAction(R.drawable.ic_next, getString(R.string.notification_action_next), retreivePlaybackAction(ACTION_NEXT_VALUE)!!))

        mNotifActionWhenPlaying = arrayOf(
                buildAction(R.drawable.ic_prev, getString(R.string.notification_action_prev), retreivePlaybackAction(ACTION_PREV_VALUE)!!),
                buildAction(R.drawable.ic_pause, getString(R.string.notification_action_pause), retreivePlaybackAction(ACTION_TOGGLE_PLAYBACK_VALUE)!!),
                buildAction(R.drawable.ic_next, getString(R.string.notification_action_next), retreivePlaybackAction(ACTION_NEXT_VALUE)!!))
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

    /**
     * we also wish to allow starting the service with start command - to enable interacting with service
     * from notification
     */
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d(TAG, "onStartCommand: startId = $startId")
        if(intent != null){
            var action = intent.action
            when(action){
                ACTION_NEXT -> {
                    Log.d(TAG, "case \"ACTION_NEXT\"")
                    playNext()
                }
                ACTION_PREV -> {
                    Log.d(TAG, "case \"ACTION_PREV\"")
                    playPrevious()
                }
                ACTION_TOGGLE_PLAYBACK -> {
                    Log.d(TAG, "case \"ACTION_TOGGLE_PLAYBACK\"")
                    if(isPlaying()){
                        pausePlayer()
                    }else {
                        play()
                    }
                }
                else -> {

                }
            }
        }
        return super.onStartCommand(intent, flags, startId)
    }

    // methods for music service
    fun play() {
        Log.d(TAG, "play: mSongIdx = $mSongIdx")
        mPlayer.reset()
        try{
            val mediaItem = mSongsList[mSongIdx]
            songNameSubject.onNext(mediaItem.name)
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
        var notification = buildNotification(mSongsList[mSongIdx])
        startForeground(NOTIFICATION_ID, notification)
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

            // call subject (for listeners to update for ex. the UI of media controller)
            onPreparedSubject.onNext(true)

            // create pending intent (for starting activity from notification) & add notification
            var notification = buildNotification(mSongsList[mSongIdx])
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

    private fun buildNotification(mediaItem: MediaItem): Notification {
        Log.d(TAG, "buildNotification")

        // here we'll build all pending intent we wish to use in our notification
        mActivity.runOnUiThread {
            var intent = Intent(applicationContext, FilesActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
            intent.putExtra(FilesActivity.BUNDLE_KEY_MEDIA_ITEMS, mSongsList as Serializable)

            var filesActivityPendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT)

            // create media session
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                mMediaSession = MediaSession(applicationContext, TAG)

                // set metadata (i.e. album artwork, artist name etc.)

                mMediaSession.isActive = true
                mMediaSession.setCallback(object : MediaSession.Callback() {
                    override fun onPlay() {
                        super.onPlay()
                    }

                    override fun onPause() {
                        super.onPause()
                    }

                    override fun onSkipToNext() {
                        super.onSkipToNext()
                    }

                    override fun onSkipToPrevious() {
                        super.onSkipToPrevious()
                    }

                    override fun onFastForward() {
                        super.onFastForward()
                    }

                    override fun onRewind() {
                        super.onRewind()
                    }

                    override fun onStop() {
                        super.onStop()
                    }

                    override fun onSeekTo(pos: Long) {
                        super.onSeekTo(pos)
                    }
                })
            }

            // for Android O - need to create a "notification channel"
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                Log.d(TAG, "buildNotification: sdk int is Oreo or above - creating notification channel")
                var name = getString(R.string.notification_channel_name)
                var desc = getString(R.string.notification_channel_description)
                var importance = NotificationManager.IMPORTANCE_DEFAULT
                var channel = NotificationChannel(NOTIFICATION_CHANNEL_ID, name, importance)
                channel.description = desc

                // register channel with system
                var notifManager = getSystemService(NotificationManager::class.java)
                notifManager.createNotificationChannel(channel)
            } else {
                Log.d(TAG, "buildNotification: sdk int below Oreo - no need for notification channel")
            }

            mNotificationBuilder = Notification.Builder(this, NOTIFICATION_CHANNEL_ID)
                    .setShowWhen(false) // Hide the timestamp
                    .setStyle(Notification.MediaStyle() // Set the Notification style
                            .setMediaSession(mMediaSession.sessionToken) // Attach our MediaSession token
                            .setShowActionsInCompactView(0, 1, 2))// Show our playback controls in the compat view
                    .setSmallIcon(R.drawable.ic_notification)
                    .setColor(R.color.notification_background_color)
                    .setContentTitle(mediaItem.artist)
                    .setContentText(mediaItem.name)
                    .setPriority(Notification.PRIORITY_DEFAULT)
                    .setContentIntent(filesActivityPendingIntent)
                    .setAutoCancel(true) // automatically remove notification after user taps it

            if (isPlaying()) {
                mNotificationBuilder.setActions(*mNotifActionWhenPlaying)
            } else {
                mNotificationBuilder.setActions(*mNotifActionWhenPaused)
            }
        }
        return mNotificationBuilder.build()
    }

    private fun retreivePlaybackAction(which: Int): PendingIntent? {
        val action: Intent
        val pendingIntent: PendingIntent
        val serviceName = ComponentName(this, MusicService::class.java!!)
        when (which) {
            ACTION_TOGGLE_PLAYBACK_VALUE -> {
                // Play and pause
                action = Intent(ACTION_TOGGLE_PLAYBACK)
                action.component = serviceName
                pendingIntent = PendingIntent.getService(this, ACTION_TOGGLE_PLAYBACK_VALUE, action, 0)
                return pendingIntent
            }
            ACTION_NEXT_VALUE -> {
                // Skip tracks
                action = Intent(ACTION_NEXT)
                action.component = serviceName
                pendingIntent = PendingIntent.getService(this, ACTION_NEXT_VALUE, action, 0)
                return pendingIntent
            }
            ACTION_PREV_VALUE -> {
                // Previous tracks
                action = Intent(ACTION_PREV)
                action.component = serviceName
                pendingIntent = PendingIntent.getService(this, ACTION_PREV_VALUE, action, 0)
                return pendingIntent
            }
            else -> {
            }
        }
        return null
    }

    private fun buildAction(icon: Int, title : CharSequence, intent : PendingIntent) : Notification.Action {
        Log.d(TAG, "buildAction")
        return Notification.Action.Builder(icon, title, intent).build()
    }

    private fun notificationAlreadyExist(): Boolean {
        Log.d(TAG, "notificationAlreadyExist")
        var notificationManager = getSystemService(NotificationManager::class.java)
        notificationManager.activeNotifications.forEach { notification ->
            if(notification.id == NOTIFICATION_ID){
                return true
            }
        }
        return false
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