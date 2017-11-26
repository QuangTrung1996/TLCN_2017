package com.travel.phuc.trung.tlcn.tlcn.Schedule

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.travel.phuc.trung.tlcn.tlcn.R

class ScheduleAdapter (var context : Context, var mang : ArrayList<ScheduleEventData>) : BaseAdapter() {

    class viewHolder (row : View){
        var key = ""
        var txtten : TextView
        var txtgioKT : TextView
        var txtgioBD : TextView
        var txtnote : TextView

        init {
            txtten = row.findViewById(R.id.txt_lichtrinh_day_tile)
            txtnote = row.findViewById(R.id.txt_lichtrinh_day_note)
            txtgioKT = row.findViewById(R.id.txt_lichtrinh_day_timeKT)
            txtgioBD = row.findViewById(R.id.txt_lichtrinh_day_timeBD)
        }
    }

    override fun getView(position: Int, convertview: View?, parent: ViewGroup?): View {
        val view : View?
        val viewholder : viewHolder

        if (convertview == null){
            val layoutinflater : LayoutInflater = LayoutInflater.from(context)
            view = layoutinflater.inflate(R.layout.schedule_list_item, parent,false)
            viewholder = viewHolder(view!!)
            view.tag = viewholder
        }
        else{
            view = convertview
            viewholder = convertview.tag as viewHolder
        }

        val lich : ScheduleEventData = getItem(position) as ScheduleEventData

        viewholder.key = lich.key
        viewholder.txtten.text = lich.title
        viewholder.txtnote.text = lich.detail
        viewholder.txtgioKT.text = lich.ngayKT
        viewholder.txtgioBD.text = lich.ngayBD

        return view
    }

    override fun getItem(p0: Int): Any {
        return mang.get(p0)
    }

    override fun getItemId(p0: Int): Long {
        return p0.toLong()
    }

    override fun getCount(): Int {
        return mang.size
    }
}