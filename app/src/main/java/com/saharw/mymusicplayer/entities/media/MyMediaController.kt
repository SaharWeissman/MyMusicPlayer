package com.saharw.mymusicplayer.entities.media

import android.app.Activity
import android.content.Context
import android.view.KeyEvent
import android.widget.MediaController

/**
 * Created by saharw on 21/05/2018.
 */
class MyMediaController(private val ctxt : Context) : MediaController(ctxt){

    private val TAG = "MyMediaController"

    override fun hide() {
    }

    override fun dispatchKeyEvent(event: KeyEvent?): Boolean {
        val keyCode = event?.keyCode
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            hide()
            (ctxt as Activity).finish()
            return true
        }
        return super.dispatchKeyEvent(event)
    }
}