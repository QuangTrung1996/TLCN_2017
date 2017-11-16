package com.travel.phuc.trung.tlcn.tlcn.Home

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.bumptech.glide.Glide
import com.travel.phuc.trung.tlcn.tlcn.R
import de.hdodenhof.circleimageview.CircleImageView
import java.text.SimpleDateFormat

/**
 * Created by Admin on 16/11/2017.
 */
class HomeLvComment constructor(var context: Context, var arayThongTinCoomnet:ArrayList<HomeInfromationCommentData> ) : BaseAdapter() {
    inner class viewholder(row: View)
    {
        var anhDD: CircleImageView
        var Ten: TextView
        var ThoiGianBL:TextView
        var NoiDung:TextView
        init {
            anhDD=row.findViewById<CircleImageView>(R.id.AnhDaidien_Cmmt)
            Ten=row.findViewById<TextView>(R.id.UserNameComment)
            ThoiGianBL=row.findViewById<TextView>(R.id.ThoiGianBinhLuan_DDDL)
            NoiDung =row.findViewById<TextView>(R.id.NoiDungCmmt)
        }
    }

    override fun getView(position: Int, convertview: View?, p2: ViewGroup?): View {
        var view:View?
        val viewHolder:viewholder
        if (convertview==null)
        {
            var layoutInflater: LayoutInflater = LayoutInflater.from(context)
            view=layoutInflater.inflate(R.layout.row_comment, null)
            viewHolder= viewholder(view)
            view.tag=viewHolder
        }
        else{
            view=convertview
            viewHolder= convertview.tag as viewholder
        }
        val sdf_date_bd : SimpleDateFormat = SimpleDateFormat("dd-MM-yyyy")
        var tg =sdf_date_bd.format(arayThongTinCoomnet.get(position).ThoiGian)
        viewHolder.ThoiGianBL.text=tg.toString()
        viewHolder.NoiDung.text = arayThongTinCoomnet.get(position).Comment
        viewHolder.Ten.text=arayThongTinCoomnet.get(position).UserName
        Glide.with(context).load(arayThongTinCoomnet.get(position).url)
                .centerCrop()
                .error(R.drawable.wellcom0)
                .into(viewHolder.anhDD)
        return view as View
    }

    override fun getItem(p0: Int): Any {
        return arayThongTinCoomnet.get(p0)
    }

    override fun getItemId(p0: Int): Long {
        return p0.toLong()
    }

    override fun getCount(): Int {
        return arayThongTinCoomnet.size
    }
}