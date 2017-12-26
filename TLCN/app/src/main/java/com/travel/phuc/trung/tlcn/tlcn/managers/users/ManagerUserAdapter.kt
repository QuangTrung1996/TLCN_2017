package com.travel.phuc.trung.tlcn.tlcn.managers.users

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
import com.travel.phuc.trung.tlcn.tlcn.logins.UserData
import com.travel.phuc.trung.tlcn.tlcn.notifications.NotificationsData
import com.travel.phuc.trung.tlcn.tlcn.R
import de.hdodenhof.circleimageview.CircleImageView

class ManagerUserAdapter(var context : Context, private var array : ArrayList<ManagerUserData>) : BaseAdapter() {

    private val databaseRef : DatabaseReference = FirebaseDatabase.getInstance().reference

    class ViewHolder (row : View){
        var nameUser : TextView   = row.findViewById(R.id.txtNameUser)
        var email    : TextView   = row.findViewById(R.id.txtEmail)
        var img : CircleImageView = row.findViewById(R.id.profile_image)
        var check : CheckBox = row.findViewById(R.id.managerCheckBox)
    }

    override fun getView(position: Int, convertview: View?, parent: ViewGroup?): View {
        val view : View?
        val viewH : ViewHolder

        if (convertview == null){
            val layoutInflater : LayoutInflater = LayoutInflater.from(context)
            view = layoutInflater.inflate(R.layout.manager_fragment_user_item, parent,false)
            viewH = ViewHolder(view!!)
            view.tag = viewH
        }
        else{
            view = convertview
            viewH = convertview.tag as ViewHolder
        }

        val managerUser : ManagerUserData = getItem(position) as ManagerUserData
        val user : UserData = managerUser.userData

        viewH.nameUser.text = user.ten
        viewH.email.text    = user.email
        Glide.with(context).load(user.AnhDaiDien)
                .centerCrop()
                .error(R.drawable.wellcom0)
                .into(viewH.img)

        viewH.check.isChecked = managerUser.check

        viewH.check.setOnClickListener {
            databaseRef.child("Manager").child(user.id).setValue(viewH.check.isChecked)

            val sharedPreferences = context.getSharedPreferences("taikhoan",android.content.Context.MODE_PRIVATE)
            val hinhDaiDien =sharedPreferences.getString("UURLAnh",null)
            val ten         = sharedPreferences.getString("Uname",null)
            val time = System.currentTimeMillis()

            val string = if (viewH.check.isChecked){
                "Đã thêm quyền Quản lý cho bạn"
            } else{
                "Đã xóa quyền Quản lý của bạn"
            }

            databaseRef.child("Notification").child(user.id).child(time.toString()).setValue(NotificationsData(ten, hinhDaiDien, string, time))
        }

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