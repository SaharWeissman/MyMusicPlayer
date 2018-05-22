package com.saharw.mymusicplayer.entities.data

import com.saharw.mymusicplayer.entities.data.base.MediaItem

/**
 * Created by saharw on 09/05/2018.
 */
class ComplexMediaItem(val _id : Long, val name: String, val items: List<MediaItem>)