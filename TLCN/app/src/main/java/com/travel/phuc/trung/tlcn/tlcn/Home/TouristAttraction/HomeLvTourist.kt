package com.travel.phuc.trung.tlcn.tlcn.Home.TouristAttraction

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.bumptech.glide.Glide
import com.google.firebase.database.*
import com.travel.phuc.trung.tlcn.tlcn.GoogleMap.MapsActivity
import com.travel.phuc.trung.tlcn.tlcn.Home.HomeActivityComment
import com.travel.phuc.trung.tlcn.tlcn.Home.HomeActivityImage
import com.travel.phuc.trung.tlcn.tlcn.Home.HomeActivityLike
import com.travel.phuc.trung.tlcn.tlcn.Home.HomeRatingData
import com.travel.phuc.trung.tlcn.tlcn.R

/**
 * Created by Admin on 16/11/2017.
 */
// lớp adapter của địa điểm du lịch
class HomeLvTourist(var context: Context, var arrayList : ArrayList<HomeInformationTourisData>) : BaseAdapter() {
    var ListRT:ArrayList<HomeRatingData>?= ArrayList()
    val sharedprperences : String="taikhoan";
    var id_USER :String?=null
    var ten:String? =null
    var ten_email:String? = null
    var hinhDaiDien:String?=null
    var sum:Int =0
    val database : DatabaseReference
    init {
        database    = FirebaseDatabase.getInstance().reference
    }


    @SuppressLint("InflateParams")
    override fun getView(position: Int, convertview: View?, p2: ViewGroup?): View {

        val view:View
        val viewHolder: viewHolder

        if (convertview==null)
        {
            val layoutInflater: LayoutInflater = LayoutInflater.from(context)
            view        = layoutInflater.inflate(R.layout.row_information_tourisr, null)
            viewHolder  = viewHolder(view)
            view.tag    = viewHolder

        }
        else{
            view        = convertview
            viewHolder  = convertview.tag as viewHolder
        }

        ListRT!!.clear()

        database.child("DanhGia").child(arrayList[position].key).addChildEventListener(object : ChildEventListener{
            var i=0
            var tong = 0.0

            override fun onCancelled(p0: DatabaseError?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onChildMoved(p0: DataSnapshot?, p1: String?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onChildChanged(p0: DataSnapshot?, p1: String?) {
                var sum =0.0;
                for (i in 0 until ListRT!!.size)
                {
                    if (p0!!.key == ListRT!!.get(i).key)
                    {
                        val a = p0.value as String
                        ListRT!!.get(i).danhgia = a.toFloat()

                    }
                    sum+= ListRT!!.get(i).danhgia
                }
                val tb = Math.round((sum/ ListRT!!.size)*10)/10.toFloat()
                viewHolder.danhgia.rating = tb
            }

            override fun onChildAdded(p0: DataSnapshot?, p1: String?) {
                val a = p0!!.value as String
                i++
                tong += a.toDouble()
                ListRT!!.add(HomeRatingData(p0.key,a.toFloat()))
                val tam = tong /i
                val TB =(Math.round((tam*10).toDouble()))/10.toFloat()
                viewHolder.danhgia.rating = TB
            }

            override fun onChildRemoved(p0: DataSnapshot?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

        })

        arrayList[position].SoCmmt   = 0
        arrayList[position].LuocLike = 0

        viewHolder.tenDD.text  = arrayList[position].TenDiaDiem
        viewHolder.diachi.text = arrayList[position].DiaChi

        Glide.with(context).load(arrayList[position].url)
                .centerCrop()
                .error(R.drawable.wellcom0)
                .into(viewHolder.anhdaidien)

        if (doctaikhoan()){
            database.child("Like").child(arrayList[position].key).child(id_USER).addValueEventListener(object :ValueEventListener{
                override fun onCancelled(p0: DatabaseError?) {
                }

                override fun onDataChange(p0: DataSnapshot?) {
                    if (p0!!.exists())
                    {
                        viewHolder.btn_like.setImageResource(R.drawable.liked)
                    }
                    else{
                        viewHolder.btn_like.setImageResource(R.drawable.like)
                    }
                }
            })
        }

        database.child("BanVe").child(arrayList[position].key).addValueEventListener(object :ValueEventListener{
            override fun onCancelled(p0: DatabaseError?) {}

            override fun onDataChange(p0: DataSnapshot?) {
                if (p0!!.value != null && p0.key !=""){
                    viewHolder.Banve.visibility = LinearLayout.VISIBLE
                    viewHolder.sotien.text = p0.value.toString().plus(" đ")
                }
                else
                {
                    viewHolder.Banve.visibility = LinearLayout.GONE
                }
            }
        })

        viewHolder.Khung_TTDL.setOnClickListener(){
            if (doctaikhoan()) {
                val intent = Intent(view.context, MapsActivity::class.java)
                val Thongtin: HomeInformationTourisData
                Thongtin = arrayList.get(position)
                intent.putExtra("data", Thongtin)
                context.startActivity(intent)
            }
            else
            {
                Toast.makeText(context,"Bạn Cần Dăng Nhập",Toast.LENGTH_LONG).show()
            }
        }

        viewHolder.btn_comment.setOnClickListener(){
            val intent = Intent(view.context, HomeActivityComment::class.java)
            intent.putExtra("keyDL",arrayList.get(position).key)
            intent.putExtra("key",0)
            view.context.startActivity(intent)
        }

        viewHolder.btn_like.setOnClickListener {
            val intent = Intent(view.context, HomeActivityLike::class.java)
            intent.putExtra("keyDDDL",arrayList.get(position).key)
            view.getContext().startActivity(intent)
        }

        viewHolder.anhdaidien.setOnClickListener {
            val intent = Intent(view.getContext(), HomeActivityImage::class.java)
            intent.putExtra("IDdl",arrayList.get(position).key.toString())
            intent.putExtra("urlanh",arrayList.get(position).url)
            view.getContext().startActivity(intent)
        }
        return view as View
    }

    override fun getItem(p0: Int): Any {
        return arrayList.get(p0)
    }

    override fun getItemId(p0: Int): Long {
        return p0.toLong()
    }

    override fun getCount(): Int {
        return arrayList.size
    }
    inner class viewHolder(row : View) {
        var sotien      : TextView
        var Banve       : LinearLayout
        var Khung_TTDL  : LinearLayout
        var anhdaidien  : ImageView
        var tenDD       : TextView
        var diachi      : TextView
        var btn_like    : ImageButton
        var danhgia     : RatingBar
        var btn_comment : ImageButton

        init {
            sotien      = row.findViewById(R.id.sotien)
            Banve       = row.findViewById(R.id.banve)
            Khung_TTDL  = row.findViewById(R.id.id_DuLich)
            anhdaidien  = row.findViewById(R.id.AnhDaijDien_DL)
            tenDD       = row.findViewById(R.id.TenDiaDiem_DL)
            diachi      = row.findViewById(R.id.DiaChi_DL)
            btn_like    = row.findViewById(R.id.Btn_Like)
            danhgia     = row.findViewById(R.id.DanhGia)
            btn_comment = row.findViewById(R.id.Btn_Comment)

        }
    }

    private fun doctaikhoan():Boolean {
        val sharedpreferences = context.getSharedPreferences(sharedprperences, android.content.Context.MODE_PRIVATE)
        id_USER = sharedpreferences.getString("Uid", null)
        ten_email = sharedpreferences.getString("Uemail", null)
        hinhDaiDien = sharedpreferences.getString("UURLAnh", null)
        ten = sharedpreferences.getString("Uname", null)
        if (id_USER != null) {

            return true
        }
        return false
    }
}