package com.travel.phuc.trung.tlcn.tlcn.Home.TouristAttraction

import android.content.Context
import android.os.Parcel
import android.os.Parcelable
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.travel.phuc.trung.tlcn.tlcn.R

/**
 * Created by Admin on 24/11/2017.
 */
class RecrycleListViewAdapter constructor( var context:Context, var listAnh:ArrayList<HomeDistrictsData>) : RecyclerView.Adapter<RecrycleListViewAdapter.MyViewHolder>() {
    override fun onBindViewHolder(holder: MyViewHolder?, position: Int) {
        val  data = listAnh.get(position)

        Glide.with(context).load(data.TenQuanHuyen)
                .centerCrop()
                .error(R.drawable.wellcom0)
                .into(holder!!.anh)


    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): MyViewHolder {
        val v = LayoutInflater.from(parent!!.context).inflate(R.layout.home_image, parent, false)
        return MyViewHolder(v)    }


    override fun getItemCount(): Int {
       return listAnh.size
    }

    inner class MyViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {

            var anh:ImageView = itemView!!.findViewById<ImageView>(R.id.thu)


    }

}