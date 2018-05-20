package com.saharw.mymusicplayer.entities.adapters

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.saharw.mymusicplayer.R
import com.saharw.mymusicplayer.entities.data.base.MediaItem

/**
 * Created by saharw on 10/05/2018.
 */
class MediaItemsAdapter(private val ctxt : Context, private val data: List<MediaItem>, private val layoutResId : Int) : BaseAdapter() {

    private val TAG = "MediaItemsAdapter"

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var viewHolder : ViewHolder
        var convertView = convertView
        if(convertView == null){
            convertView = (ctxt as Activity).layoutInflater.inflate(layoutResId, parent, false)
            viewHolder = ViewHolder()
            viewHolder.mIdx = convertView!!.findViewById(R.id.txtV_file_index)
            viewHolder.mDisplayName = convertView!!.findViewById(R.id.txtV_file_display_name)
            viewHolder.mIcon = convertView!!.findViewById(R.id.imgV_item_icon)
            convertView.tag = viewHolder
        }else {
            viewHolder = convertView.tag as ViewHolder
        }

        viewHolder.mIdx.text = position.toString()
        viewHolder.mDisplayName.text = data[position].name
        viewHolder.mIcon.setImageResource(R.drawable.icons_music_file)
        viewHolder.mMediaItem = data[position]
        return convertView
    }

    override fun getItem(position: Int): MediaItem {
        return data[position]
    }

    override fun getItemId(position: Int): Long {
        return data[position]._id
    }

    override fun getCount(): Int {
        return data.size
    }

    class ViewHolder {
        lateinit var mIdx : TextView
        lateinit var mIcon : ImageView
        lateinit var mDisplayName : TextView
        lateinit var mMediaItem: MediaItem
    }
}