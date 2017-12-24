package com.travel.phuc.trung.tlcn.tlcn.notifications

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.bumptech.glide.Glide
import com.travel.phuc.trung.tlcn.tlcn.R
import de.hdodenhof.circleimageview.CircleImageView
import java.util.*

class NotificationsAdapter(var context : Context, var mang : ArrayList<NotificationsData>) : BaseAdapter() {

    // khai bao cho event
    var mYear  : Int = 0
    var mMonth : Int = 0
    var mDay   : Int = 0
    var mHour  : Int = 0
    var mMinute: Int = 0
    var mSecond: Int = 0

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
            val layoutInflater : LayoutInflater = LayoutInflater.from(context)
            view = layoutInflater.inflate(R.layout.notifications_fragment_item, parent,false)
            viewH = viewHolder(view!!)
            view.tag = viewH
        }
        else{
            view = convertview
            viewH = convertview.tag as viewHolder
        }

        val data : NotificationsData = getItem(position) as NotificationsData
        val c = Calendar.getInstance()

        viewH.tenNguoiGui.text = data.tenNguoiGui

        c.timeInMillis = data.thoiGian
        getTimeDate(c)
        viewH.thoigian.text    = (mHour.toString() + ":"+ mMinute + ":"+ mSecond +
                ", ngày "+ mDay + " tháng "+ mMonth + " năm " + mYear)

        viewH.noiDung.text     = data.noiDung

        Glide.with(context).load(data.hinhNguoiGui)
                .centerCrop()
                .error(R.drawable.wellcom0)
                .into(viewH.img)

        return view
    }

    private fun getTimeDate(c: Calendar) {
        mHour   = c.get(Calendar.HOUR_OF_DAY)
        mMinute = c.get(Calendar.MINUTE)
        mSecond = c.get(Calendar.SECOND)

        mYear   = c.get(Calendar.YEAR)
        mMonth  = c.get(Calendar.MONTH) + 1
        mDay    = c.get(Calendar.DAY_OF_MONTH)
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