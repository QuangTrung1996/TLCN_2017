package com.travel.phuc.trung.tlcn.tlcn.Home

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.bumptech.glide.Glide
import com.travel.phuc.trung.tlcn.tlcn.logins.UserData
import com.travel.phuc.trung.tlcn.tlcn.R
import de.hdodenhof.circleimageview.CircleImageView
import java.text.SimpleDateFormat

/**
 * Created by Admin on 19/11/2017.
 */
class HomeLvAdapterLike constructor(var context: Context, var arayThongTinLike:ArrayList<UserData> ) : BaseAdapter() {
    inner class viewholder(row: View) {
        var anhDD: CircleImageView
        var Ten: TextView


        init {
            anhDD = row.findViewById<CircleImageView>(R.id.AnhDaidien_Like)
            Ten = row.findViewById<TextView>(R.id.UserNameLike)

        }
    }

    override fun getView(position: Int, convertview: View?, p2: ViewGroup?): View {
        var view: View?
        val viewHolder: viewholder
        if (convertview == null) {
            var layoutInflater: LayoutInflater = LayoutInflater.from(context)
            view = layoutInflater.inflate(R.layout.home_row_like, null)
            viewHolder = viewholder(view)
            view.tag = viewHolder
        } else {
            view = convertview
            viewHolder = convertview.tag as viewholder
        }

        viewHolder.Ten.text = arayThongTinLike.get(position).ten
        Glide.with(context).load(arayThongTinLike.get(position).AnhDaiDien)
                .centerCrop()
                .error(R.drawable.wellcom0)
                .into(viewHolder.anhDD)
        return view as View
    }

    override fun getItem(p0: Int): Any {
        return arayThongTinLike.get(p0)
    }

    override fun getItemId(p0: Int): Long {
        return p0.toLong()
    }

    override fun getCount(): Int {
        return arayThongTinLike.size
    }
}