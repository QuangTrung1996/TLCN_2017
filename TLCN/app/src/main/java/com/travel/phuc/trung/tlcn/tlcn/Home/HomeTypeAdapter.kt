package com.travel.phuc.trung.tlcn.tlcn.Home

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.travel.phuc.trung.tlcn.tlcn.Home.TouristAttraction.HomeDistrictsData
import com.travel.phuc.trung.tlcn.tlcn.R

/**
 * Created by Admin on 28/11/2017.
 */
class HomeTypeAdapter constructor(var context:Context,var arrayList: ArrayList<HomeDistrictsData>,var a:Int) : BaseAdapter() {
    inner class viewholder(row: View) {
        var tenTL: TextView
        var tick: ImageView


        init {
            tenTL = row.findViewById<TextView>(R.id.theloai)
            tick = row.findViewById<ImageView>(R.id.tick)

        }
    }
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var view: View?
        val viewHolder: HomeTypeAdapter.viewholder
        if (convertView == null) {
            var layoutInflater: LayoutInflater = LayoutInflater.from(context)
            view = layoutInflater.inflate(R.layout.home_type_row, null)
            viewHolder = viewholder(view)
            view.tag = viewHolder
        } else {
            view = convertView
            viewHolder = convertView.tag as viewholder
        }
        if(a!=0 && position !=0|| position == arrayList.size-1)
        {
            viewHolder.tick.visibility = ImageView.INVISIBLE
        }
        if(position ==a && a!=-1){
            viewHolder.tick.visibility = ImageView.VISIBLE
        }
        viewHolder.tenTL.text = arrayList.get(position).TenQuanHuyen.toString()

        return view as View
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