package com.saharw.mymusicplayer.entities.data.base

/**
 * Created by saharw on 09/05/2018.
 */
data class DataQuery(
        val projection: Array<String>?,
        val selection : String?,
        val selectionArgs: Array<String>?,
        val sortOrder : String?)