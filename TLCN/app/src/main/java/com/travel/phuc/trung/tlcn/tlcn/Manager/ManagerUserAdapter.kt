package com.travel.phuc.trung.tlcn.tlcn.Manager

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.CheckBox
import android.widget.TextView
import com.bumptech.glide.Glide
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.travel.phuc.trung.tlcn.tlcn.Login.UserData
import com.travel.phuc.trung.tlcn.tlcn.Notifications.NotificationsData
import com.travel.phuc.trung.tlcn.tlcn.R
import de.hdodenhof.circleimageview.CircleImageView

class ManagerUserAdapter(var context : Context, var mang : ArrayList<ManagerUserData>) : BaseAdapter() {

    val databaseRef : DatabaseReference = FirebaseDatabase.getInstance().reference

    class viewHolder (row : View){

        var id       : TextView   = row.findViewById(R.id.txtId)
        var nameUser : TextView   = row.findViewById(R.id.txtNameUser)
        var email    : TextView   = row.findViewById(R.id.txtEmail)
        var img : CircleImageView = row.findViewById<CircleImageView>(R.id.profile_image)
        var check : CheckBox = row.findViewById(R.id.managerCheckBox)
    }

    override fun getView(position: Int, convertview: View?, parent: ViewGroup?): View {
        val view : View?
        val viewH : viewHolder

        if (convertview == null){
            val layoutinflater : LayoutInflater = LayoutInflater.from(context)
            view = layoutinflater.inflate(R.layout.manager_fragment_user_item, parent,false)
            viewH = viewHolder(view!!)
            view.tag = viewH
        }
        else{
            view = convertview
            viewH = convertview.tag as viewHolder
        }

        val managerUser : ManagerUserData = getItem(position) as ManagerUserData
        val user : UserData = managerUser.userData

        viewH.id.text       = user.id
        viewH.nameUser.text = user.ten
        viewH.email.text    = user.email
        Glide.with(context).load(user.AnhDaiDien)
                .centerCrop()
                .error(R.drawable.wellcom0)
                .into(viewH.img)

        viewH.check.isChecked = managerUser.check

        viewH.check.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View) {
                databaseRef.child("Manager").child(user.id).setValue(viewH.check.isChecked)

                val sharedPreferences = context.getSharedPreferences("taikhoan",android.content.Context.MODE_PRIVATE)
                val hinhDaiDien =sharedPreferences.getString("UURLAnh",null)
                val ten         = sharedPreferences.getString("Uname",null)
                val time = System.currentTimeMillis()
                var string =""

                if (viewH.check.isChecked){
                    string = "Đã thêm quyền Quản lý cho bạn"
                }
                else{
                    string = "Đã xóa quyền Quản lý của bạn"
                }

                databaseRef.child("Notification").child(user.id).child(time.toString()).setValue(NotificationsData(ten, hinhDaiDien, string, time.toString()))

            }
        })

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