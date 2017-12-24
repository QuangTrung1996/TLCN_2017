package com.travel.phuc.trung.tlcn.tlcn.schedules

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.travel.phuc.trung.tlcn.tlcn.R

class ScheduleAdapter (var context : Context, private var array: ArrayList<ScheduleEventData>) : BaseAdapter() {

    class ViewHolder (row : View){
        var key = ""
        var txtTen: TextView = row.findViewById(R.id.txt_lichtrinh_day_tile)
        var txtTimeKT: TextView = row.findViewById(R.id.txt_lichtrinh_day_timeKT)
        var txtTimeBD: TextView = row.findViewById(R.id.txt_lichtrinh_day_timeBD)
        var txtNote: TextView = row.findViewById(R.id.txt_lichtrinh_day_note)
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view : View?
        val viewHolder : ViewHolder

        if (convertView == null){
            val layoutInflater : LayoutInflater = LayoutInflater.from(context)
            view = layoutInflater.inflate(R.layout.schedule_list_item, parent,false)
            viewHolder = ViewHolder(view!!)
            view.tag = viewHolder
        }
        else{
            view = convertView
            viewHolder = convertView.tag as ViewHolder
        }

        val schedule : ScheduleEventData = getItem(position) as ScheduleEventData

        viewHolder.key = schedule.key
        viewHolder.txtTen.text = schedule.title
        viewHolder.txtNote.text = schedule.detail
        viewHolder.txtTimeKT.text = schedule.ngayKT
        viewHolder.txtTimeBD.text = schedule.ngayBD

        return view
    }

    override fun getItem(p0: Int): Any {
        return array[p0]
    }

    override fun getItemId(p0: Int): Long {
        return p0.toLong()
    }

    override fun getCount(): Int {
        return array.size
    }
}