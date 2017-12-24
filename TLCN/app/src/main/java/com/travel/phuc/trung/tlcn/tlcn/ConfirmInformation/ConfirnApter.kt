package com.travel.phuc.trung.tlcn.tlcn.ConfirmInformation

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.travel.phuc.trung.tlcn.tlcn.AddInfromation.InformationDataAdapter
import com.travel.phuc.trung.tlcn.tlcn.AddInfromation.InfrormationAddedAdapter
import com.travel.phuc.trung.tlcn.tlcn.R

/**
 * Created by Admin on 23/12/2017.
 */
class ConfirnApter constructor(var context: Context, var listThongtin:ArrayList<InformationDataAdapter>): BaseAdapter() {
    inner class viewholder(row: View)
    {
        var Ten: TextView
        init {
            Ten=row.findViewById<TextView>(R.id.CFtenDD)
        }
    }
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var view: View?
        val viewHolder: viewholder
        if (convertView == null) {
            var layoutInflater: LayoutInflater = LayoutInflater.from(context)
            view = layoutInflater.inflate(R.layout.confirm_information_row, null)
            viewHolder = viewholder(view)
            view.tag = viewHolder
        } else {
            view = convertView
            viewHolder = convertView.tag as viewholder
        }
        viewHolder.Ten.text = listThongtin.get(position).ten
        return view as View
    }

    override fun getItem(position: Int): Any {
        return listThongtin.get(position)
    }

    override fun getItemId(position: Int): Long {
       return position.toLong()
    }

    override fun getCount(): Int {
        return listThongtin.size
    }
}