package com.travel.phuc.trung.tlcn.tlcn.Notifications

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.bumptech.glide.Glide
import com.travel.phuc.trung.tlcn.tlcn.R
import de.hdodenhof.circleimageview.CircleImageView

class NotificationsAdapter(var context : Context, var mang : ArrayList<NotificationsData>) : BaseAdapter() {

    class viewHolder (row : View){

        var tenNguoiGui : TextView   = row.findViewById(R.id.notification_tenNguoiGui)
        var noiDung     : TextView   = row.findViewById(R.id.notification_noiDung)
        var thoigian    : TextView   = row.findViewById(R.id.notification_thoiGian)
        var img : CircleImageView = row.findViewById<CircleImageView>(R.id.profile_image)
    }

    override fun getView(position: Int, convertview: View?, parent: ViewGroup?): View {
        val view : View?
        val viewH : viewHolder

        if (convertview == null){
            val layoutinflater : LayoutInflater = LayoutInflater.from(context)
            view = layoutinflater.inflate(R.layout.notifications_fragment_item, parent,false)
            viewH = viewHolder(view!!)
            view.tag = viewH
        }
        else{
            view = convertview
            viewH = convertview.tag as viewHolder
        }

        val data : NotificationsData = getItem(position) as NotificationsData

        viewH.tenNguoiGui.text       = data.tenNguoiGui
        viewH.thoigian.text       = data.thoiGian
        viewH.noiDung.text       = data.noiDung

        Glide.with(context).load(data.hinhNguoiGui)
                .centerCrop()
                .error(R.drawable.wellcom0)
                .into(viewH.img)

        return view
    }

    override fun getItem(p0: Int): Any {
        return mang[p0]
    }

    override fun getItemId(p0: Int): Long {
        return p0.toLong()
    }

    override fun getCount(): Int {
        return mang.size
    }
}