package com.travel.phuc.trung.tlcn.tlcn.AddInfromation

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.travel.phuc.trung.tlcn.tlcn.R

/**
 * Created by Admin on 22/12/2017.
 */
class AlbumImageAdapter constructor(var context: Context,var listAnh:ArrayList<ImageData>): BaseAdapter() {
    inner class ViewHolder (view : View){
        var image: ImageView

        init {
            image = view.findViewById(R.id.img_album)
        }
    }
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var view: View?
        val viewHolder: ViewHolder
        if (convertView == null) {
            var layoutInflater: LayoutInflater = LayoutInflater.from(context)
            view = layoutInflater.inflate(R.layout.album_fragment_item, null)
            viewHolder = ViewHolder(view)
            view.tag = viewHolder
        } else {
            view = convertView
            viewHolder = convertView.tag as ViewHolder
        }
        Glide.with(context).load(listAnh.get(position).link)
                .centerCrop()
                .override(300, 300)
                .into(viewHolder.image)
        return  return view as View
    }

    override fun getItem(position: Int): Any {
        return listAnh.get(position)
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return listAnh.size
    }
}