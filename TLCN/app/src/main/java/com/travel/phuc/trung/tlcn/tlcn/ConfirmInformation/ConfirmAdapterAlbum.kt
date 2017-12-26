package com.travel.phuc.trung.tlcn.tlcn.ConfirmInformation

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.travel.phuc.trung.tlcn.tlcn.AddInfromation.ImageData
import com.travel.phuc.trung.tlcn.tlcn.Favorite.FavoriteAdapter
import com.travel.phuc.trung.tlcn.tlcn.R

/**
 * Created by Admin on 26/12/2017.
 */
class ConfirmAdapterAlbum constructor(var context:Context, var listanh:ArrayList<ImageData>) : BaseAdapter() {
    class viewHolder(row : View) {

        var anh: ImageView

        init {

            anh = row.findViewById(R.id.Anh_Pager_DL)


        }
    }
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        var view: View?
        var viewHolder: viewHolder
        if (convertView==null)
        {
            var layoutInflater: LayoutInflater = LayoutInflater.from(context)
            view=layoutInflater.inflate(R.layout.image, null)
            viewHolder= viewHolder(view)
            view.tag=viewHolder

        }
        else{
            view=convertView
            viewHolder= convertView.tag as viewHolder
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