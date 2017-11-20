package com.travel.phuc.trung.tlcn.tlcn.Album

import android.content.Context
import android.graphics.BitmapFactory
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import com.travel.phuc.trung.tlcn.tlcn.R

class AlbumUpAdapter(var mContext: Context, var arrayList : ArrayList<Uri>) : BaseAdapter() {

    class ViewHolder (view : View){
        var image: ImageView

        init {
            image = view.findViewById(R.id.img_album)
        }
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

        val bm = BitmapFactory.decodeStream(this.mContext.contentResolver.openInputStream(arrayList[position]))
        holder.image.setImageBitmap(bm)

        return view!!
    }

    override fun getItem(position: Int): Any {
        return arrayList.get(position)
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return arrayList.size
    }
}