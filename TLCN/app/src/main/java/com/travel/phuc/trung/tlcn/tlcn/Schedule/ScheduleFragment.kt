package com.travel.phuc.trung.tlcn.tlcn.Schedule

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.bumptech.glide.Glide
import com.travel.phuc.trung.tlcn.tlcn.Home.TouristAttraction.HomeTypeInterface
import com.travel.phuc.trung.tlcn.tlcn.R
import de.hdodenhof.circleimageview.CircleImageView

/**
 * Created by Admin on 15/11/2017.
 */
class ScheduleFragment : Fragment() {
    var a :HomeTypeInterface?=null
    val sharedprperences: String = "taikhoan";
    var id_USER: String? = null
    var ten: String? = null
    var ten_email: String? = null
    var hinhDaiDien: String? = null
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view =inflater!!.inflate(R.layout.schedule_fragment,container,false);
        doctaikhoan()
        return view;

    }
    private fun doctaikhoan() {
        val sharedpreferences = this.activity.getSharedPreferences(sharedprperences, android.content.Context.MODE_PRIVATE)
        id_USER = sharedpreferences.getString("Uid", null)
        ten_email = sharedpreferences.getString("Uemail", null)
        hinhDaiDien = sharedpreferences.getString("UURLAnh", null)
        ten = sharedpreferences.getString("Uname", null)
        if (id_USER != null) {
            val anhdaidien: CircleImageView = activity.findViewById<CircleImageView>(R.id.profile_image)
            val TenDN: TextView = activity.findViewById<TextView>(R.id.username)
            val Email: TextView = activity.findViewById<TextView>(R.id.email)

            TenDN.text = ten
            Email.text = ten_email
            Glide.with(context).load(hinhDaiDien)
                    .centerCrop()
                    .error(R.drawable.wellcom0)
                    .into(anhdaidien)
        }

    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        a=activity as HomeTypeInterface
    }
}