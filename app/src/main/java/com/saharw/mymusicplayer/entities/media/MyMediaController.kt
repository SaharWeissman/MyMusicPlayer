package com.saharw.mymusicplayer.entities.media

import android.app.Activity
import android.content.Context
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.widget.MediaController
import android.widget.TextView
import com.saharw.mymusicplayer.R

/**
 * Created by saharw on 21/05/2018.
 */
class MyMediaController(private val ctxt : Context) : MediaController(ctxt){

    private val TAG = "MyMediaController"
    private val DEFAULT_TEXT_SONG_TITLE = "Empty"
    private lateinit var mCustomSongTitleView: TextView

    override fun hide() {
    }

    override fun dispatchKeyEvent(event: KeyEvent?): Boolean {
        Log.d(TAG, "dispatchKeyEvent")
        val keyCode = event?.keyCode
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            hide()
            (ctxt as Activity).finish()
            return true
        }
        return super.dispatchKeyEvent(event)
    }

    override fun setAnchorView(view: View?) {
        Log.d(TAG, "setAnchorView")
        super.setAnchorView(view)
        mCustomSongTitleView = View.inflate(context, R.layout.song_title_view, null) as TextView
        mCustomSongTitleView.text = DEFAULT_TEXT_SONG_TITLE
        addView(mCustomSongTitleView)
        invalidate()
    }

    fun setSongTitle(songTitle: String) {
        Log.d(TAG, "setSongTitle: songTitle = $songTitle")
        mCustomSongTitleView.text = songTitle
    }
}