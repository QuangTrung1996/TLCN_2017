package com.travel.phuc.trung.tlcn.tlcn.albums

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.travel.phuc.trung.tlcn.tlcn.R

class AlbumAdapter : BaseAdapter {

    lateinit var context: Context
    lateinit var imageUrls: ArrayList<String>

    constructor(context: Context, imageUrls: ArrayList<String>) : super() {
        this.context = context
        this.imageUrls = imageUrls
    }

    constructor() : super()

    constructor(context: Context) : super() {
        this.context = context
    }

    override fun getItem(position: Int): Any {
        return imageUrls[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return imageUrls.size
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View? {
        val holder: ViewHolder
        val view : View

        if(convertView == null){
            view = LayoutInflater.from(context).inflate(R.layout.album_fragment_item, parent, false)
            holder = ViewHolder(view)
            view.tag = holder //error in this line

        } else {
            holder = convertView.tag as ViewHolder
            view = convertView
        }

        Glide.with(context)
                .load(imageUrls[position])
                .override(300, 300)
                .centerCrop()
                .into(holder.image)

        return view
    }

    class ViewHolder (view : View){
        var image: ImageView = view.findViewById(R.id.img_album)
    }
}