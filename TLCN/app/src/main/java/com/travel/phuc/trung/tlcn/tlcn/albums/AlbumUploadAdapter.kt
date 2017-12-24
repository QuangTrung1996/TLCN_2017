package com.travel.phuc.trung.tlcn.tlcn.albums

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.travel.phuc.trung.tlcn.tlcn.R

class AlbumUploadAdapter(private var mContext: Context, private var arrayList : ArrayList<String>) : BaseAdapter() {

    class ViewHolder (view : View){
        var image: ImageView = view.findViewById(R.id.img_album)
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view : View?
        val holder: ViewHolder

        if (convertView == null){
            val layoutinflater : LayoutInflater = LayoutInflater.from(mContext)
            view = layoutinflater.inflate(R.layout.album_fragment_item, parent,false)
            holder = ViewHolder(view)
            view.tag = holder
        }
        else{
            holder = convertView.tag as ViewHolder
            view = convertView
        }

        Glide.with(mContext)
                .load(arrayList[position])
                .placeholder(R.drawable.default_error)
                .centerCrop()
                .into(holder.image)

        return view!!
    }

    override fun getItem(position: Int): Any {
        return arrayList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return arrayList.size
    }
}