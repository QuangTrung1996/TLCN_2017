package com.travel.phuc.trung.tlcn.tlcn.Album

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.travel.phuc.trung.tlcn.tlcn.R

class AlbumLoadAdapter(context: Context, imageUrls: ArrayList<String>) :
        ArrayAdapter<String>(context, R.layout.album_fragment_item,imageUrls) {

    private val inflater : LayoutInflater
    private val imageUrl : ArrayList<String>
    init {
        inflater = LayoutInflater.from(context)
        imageUrl = imageUrls
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View? {
        val holder: ViewHolder
        val view : View

        if(convertView == null){
            view = inflater.inflate(R.layout.album_fragment_item, parent, false)
            holder = ViewHolder(view)
            view.tag = holder //error in this line

        } else {
            holder = convertView.tag as ViewHolder
            view = convertView
        }

        Glide.with(context)
                .load(imageUrl[position])
                .override(300, 300)
                .centerCrop()
                .into(holder.image)

        return view
    }

    class ViewHolder (view : View){
        var image: ImageView

        init {
            image = view.findViewById(R.id.img_album)
        }
    }
}