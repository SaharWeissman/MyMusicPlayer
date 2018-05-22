package com.saharw.mymusicplayer.dal.runnables

import android.content.ContentResolver
import android.database.Cursor
import android.provider.MediaStore
import android.util.Log
import com.saharw.mymusicplayer.dal.MusicDataProvider
import com.saharw.mymusicplayer.dal.runnables.base.GetDataRunnableBase
import com.saharw.mymusicplayer.entities.data.ComplexMediaItem
import com.saharw.mymusicplayer.entities.data.base.*

/**
 * Created by saharw on 09/05/2018.
 */

class GetAlbumsDataRunnable(contentResolver: ContentResolver) :
        GetDataRunnableBase(contentResolver, MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                DataQuery(
                        arrayOf(COLUMN_ID, COLUMN_DATA, COLUMN_TITLE, COLUMN_DISPLAY_NAME, COLUMN_DURATION, COLUMN_ALBUM, COLUMN_ALBUM_ID, COLUMN_ARTIST, COLUMN_ARTIST_ID, COLUMN_IS_MUSIC, COLUMN_SIZE),
                        "$COLUMN_IS_MUSIC = ?",
                        arrayOf(VALUE_IS_MUSIC.toString()), // only query for items that are considered music items
                        null
                        )) {

    private val TAG = "GetAlbumsDataRunnable"

    override fun extractFromCursor(cursor: Cursor) {
        Log.d(TAG, "extractFromCursor: cursor.count = ${cursor.count}")
        var albumsDataMap = mutableMapOf<Pair<Long, String>, MutableList<MediaItem>>()
        if(cursor.moveToFirst()){
            while(!cursor.isAfterLast){

                /*
                iterate through cursor records, create a media item (POJO/POKO) from each record
                & maintain map for created items, key in map is artist_id
                 */
                var mediaItem = MediaItem.createFromCursor(cursor)
                if(mediaItem != null) {
                    var albumItems = albumsDataMap[Pair(mediaItem.albumId, mediaItem.album)]
                    if (albumItems == null){
                        albumItems = mutableListOf()
                    }
                    albumItems!!.add(mediaItem)
                    albumsDataMap[Pair(mediaItem.albumId,mediaItem.album)] = albumItems
                }else {
                    Log.e(TAG, "extractFromCursor: media item created from cursor is null!")
                }
                cursor.moveToNext()
            }

            // finished populating map - convert to ComplexMediaItem (for convenience)
            var entryIterator = albumsDataMap.entries.iterator()
            var albumsCollection = mutableListOf<ComplexMediaItem>()
            while(entryIterator.hasNext()){
                var entry = entryIterator.next()
                albumsCollection.add(ComplexMediaItem(entry.key.first, entry.key.second, entry.value))
            }
            MusicDataProvider.albumsDataSubject.onNext(albumsCollection)
        }else {
            Log.i(TAG, "extractFromCursor: cursor.moveToFirst() returned false")
        }
    }
}