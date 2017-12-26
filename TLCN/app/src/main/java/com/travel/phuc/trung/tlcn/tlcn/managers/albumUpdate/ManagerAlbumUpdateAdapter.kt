package com.travel.phuc.trung.tlcn.tlcn.managers.albumUpdate

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.travel.phuc.trung.tlcn.tlcn.AddInfromation.ImageData
import com.travel.phuc.trung.tlcn.tlcn.R

class ManagerAlbumUpdateAdapter constructor(var context: Context, var listanh:ArrayList<ImageData>) : BaseAdapter() {

    class ViewHolder(row : View) {
        var anh: ImageView = row.findViewById(R.id.Anh_Pager_DL)
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        val view: View?
        val viewHolder: ViewHolder
        if (convertView==null)
        {
            val layoutInflater: LayoutInflater = LayoutInflater.from(context)
            view=layoutInflater.inflate(R.layout.image, null)
            viewHolder= ViewHolder(view)
            view.tag=viewHolder

        }
        else{
            view=convertView
            viewHolder= convertView.tag as ViewHolder
        }
        Glide.with(context).load(listanh.get(position).link)
                .centerCrop()
                .into(viewHolder.anh)
        return view as View
    }

    override fun getItem(position: Int): Any {
        return listanh.get(position)
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return listanh.size
    }
}