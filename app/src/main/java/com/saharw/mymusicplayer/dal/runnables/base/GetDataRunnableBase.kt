package com.saharw.mymusicplayer.dal.runnables.base

import android.content.ContentResolver
import android.database.Cursor
import android.net.Uri
import android.util.Log
import com.saharw.mymusicplayer.entities.data.base.DataQuery

/**
 * Created by saharw on 09/05/2018.
 */
abstract class GetDataRunnableBase(private val contentResolver: ContentResolver, private val uri: Uri, private val dataQuery: DataQuery? = DataQuery(null, null, null, null)) : Runnable{

    private val TAG = "GetDataRunnableBase"

    override fun run() {
        Log.d(TAG, "GetDataRunnableBase")
        try{
            var cursor = contentResolver.query(uri, dataQuery?.projection, dataQuery?.selection, dataQuery?.selectionArgs, dataQuery?.sortOrder)
            extractFromCursor(cursor)
        }catch (e: Throwable){
            Log.e(TAG, "GetDataRunnableBase - throwable while trying to get cursor for uri: $uri", e)
        }
    }

    abstract fun extractFromCursor(cursor: Cursor)
}