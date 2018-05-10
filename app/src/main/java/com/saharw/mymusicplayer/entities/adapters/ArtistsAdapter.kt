package com.saharw.mymusicplayer.entities.adapters

import android.app.Activity
import android.content.Context
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.saharw.mymusicplayer.R
import com.saharw.mymusicplayer.entities.data.ArtistsItem
import com.saharw.mymusicplayer.entities.data.base.MediaItem

/**
 * Created by saharw on 09/05/2018.
 */
class ArtistsAdapter(private val ctxt: Context, private val data: List<ArtistsItem>, private val layoutId: Int, private val onClickListener: View.OnClickListener) : BaseAdapter() {

    private val TAG = "ArtistsAdapter"

    override fun getItem(position: Int): ArtistsItem {
        Log.d(TAG, "getItem: position = $position")
        return data[position]
    }

    override fun getItemId(position: Int): Long {
        Log.d(TAG, "getItemId: position = $position")
        return data[position]._id
    }

    override fun getCount(): Int {
        Log.d(TAG, "getCount")
        return data.size
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        Log.d(TAG, "getView: position = $position")
        var viewHolder : ViewHolder
        var convertView = convertView
        if(convertView == null){
            convertView = (ctxt as Activity).layoutInflater.inflate(layoutId, parent, false)
            viewHolder = ViewHolder()
            viewHolder.mIcon = convertView!!.findViewById(R.id.imgV_item_icon)
            viewHolder.mName = convertView!!.findViewById(R.id.txtV_item_name)
            convertView.tag = viewHolder
            convertView.setOnClickListener(onClickListener)
        }else {
            viewHolder = convertView.tag as ViewHolder
        }

        viewHolder.mIcon.setImageResource(R.drawable.ic_folder)
        viewHolder.mName.text = data[position].name
        viewHolder.mMediaItems = data[position].items
        return convertView
    }

    class ViewHolder {
        lateinit var mIcon : ImageView
        lateinit var mName : TextView
        lateinit var mMediaItems : Collection<MediaItem>
    }
}