package com.travel.phuc.trung.tlcn.tlcn.Home.TouristAttraction

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.google.firebase.database.*
import com.travel.phuc.trung.tlcn.tlcn.Conect.CheckInternet
import com.travel.phuc.trung.tlcn.tlcn.Conect.CheckInternetInterface
import com.travel.phuc.trung.tlcn.tlcn.Home.HomeAdapter
import com.travel.phuc.trung.tlcn.tlcn.R
import de.hdodenhof.circleimageview.CircleImageView

/**
 * Created by Admin on 16/11/2017.
 */
class HomeFragmentInformationTourist :Fragment(),CheckInternetInterface{
    var loading: FrameLayout? = null
    val sharedprperences : String="taikhoan";
    var id_USER :String?=null
    var ten:String? =null
    var ten_email:String? = null
    var hinhDaiDien:String?=null

    val database : DatabaseReference
    private var Lv_ThongTin: ListView? = null
    private var tong:Int=10
    private var listkeycomment:ArrayList<String> = ArrayList()
    private lateinit var adapter: HomeLvTourist
    companion object{
        var listkeyDDDL:ArrayList<String> = ArrayList()
        var theloai:Int = -1;
        var tinh:Int = -1;
        var huyen:Int=-1;
    }
    init {
        database    = FirebaseDatabase.getInstance().reference
    }
    var arrList_ThongTinDL:ArrayList<HomeInformationTourisData> = ArrayList()

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater!!.inflate(R.layout.home_fragment_information_tourist, container, false);
        Lv_ThongTin =view.findViewById<ListView>(R.id.LV_ThongTinDuLich)
        loading = view.findViewById<FrameLayout>(R.id.loading)
        adapter = HomeLvTourist(this.context,arrList_ThongTinDL)
        try {

            val a = CheckInternet(this)
            a.checkConnection(this.context)
        }catch (e: Exception){}
        return view
    }


    override fun kiemtrainternet(flag: Boolean) {
        if (flag==true){
            //Toast.makeText(this.context,"có in ternet",Toast.LENGTH_LONG).show()
            if(doctaikhoan()) {
                if (theloai==-1 ){
               addthongtin()
                    //test()
                }
                else
                {
                    addtheotheloai()
                }
            }
            else
            {
                addthongtin1()
            }

        }
        else{
            Toast.makeText(this.context,"đéo có internet",Toast.LENGTH_LONG).show()
        }

    }
    // lấy thoogn tiin địa điểm theo thể loại
    private fun addtheotheloai() {
        database.child("TheLoai").child(theloai.toString()).addChildEventListener(object :ChildEventListener {
            override fun onCancelled(p0: DatabaseError?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onChildMoved(p0: DataSnapshot?, p1: String?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onChildChanged(p0: DataSnapshot?, p1: String?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onChildAdded(p0: DataSnapshot?, p1: String?) {
                if (p0!=null && p0!!.key!=null)
                {
                    docthongtintheotheloai(p0.key.toString())
                }

            }

            override fun onChildRemoved(p0: DataSnapshot?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

        })
    }

    private fun docthongtintheotheloai(key:String) {
        database.child("DiadiemDuLich").child(key).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(p0: DataSnapshot?) {
                listkeyDDDL.add(p0!!.key.toString())

                var data: GetDataTourist? = p0.getValue(GetDataTourist::class.java)
                database.child("DisLike").child(p0.key.toString()).child(id_USER).addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onCancelled(p1: DatabaseError?) {
                        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                    }

                    override fun onDataChange(p1: DataSnapshot?) {
                        if (p1!!.value == null) {
                            var tt = HomeInformationTourisData(p0.key.toString(), data!!.Lat, data.Long, data.MoTa, data!!.tenDiaDiem, data.DiaChi, data.AnhDaiDien, 0, 0, 2.3f)
                            arrList_ThongTinDL.add(tt)
                            adapter.notifyDataSetChanged()
                            Lv_ThongTin!!.adapter = adapter
                            loading!!.visibility = FrameLayout.GONE
                        }
                    }
                })
            }
        })
    }

    private fun addthongtin1() {
        var i:Int=0;
        database.child("DiadiemDuLich").addChildEventListener(object : ChildEventListener {
            override fun onCancelled(p0: DatabaseError?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onChildMoved(p0: DataSnapshot?, p1: String?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onChildChanged(p0: DataSnapshot?, p1: String?) {

            }

            override fun onChildAdded(p0: DataSnapshot?, p1: String?) {

                listkeyDDDL.add(p0!!.key.toString())

                var data: GetDataTourist? = p0.getValue(GetDataTourist::class.java)

                var tt = HomeInformationTourisData(p0.key.toString(),data!!.Lat,data.Long,data.MoTa,data!!.tenDiaDiem, data.DiaChi, data.AnhDaiDien, 0, 0, 2.3f)
                arrList_ThongTinDL.add(tt)
                adapter.notifyDataSetChanged()
                Lv_ThongTin!!.adapter = adapter


            }

            override fun onChildRemoved(p0: DataSnapshot?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

        })
    }

    private fun addthongtin() {
        var i:Int=0;
        database.child("DiadiemDuLich").addChildEventListener(object : ChildEventListener {
            override fun onCancelled(p0: DatabaseError?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onChildMoved(p0: DataSnapshot?, p1: String?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onChildChanged(p0: DataSnapshot?, p1: String?) {

            }

            override fun onChildAdded(p0: DataSnapshot?, p1: String?) {

                listkeyDDDL.add(p0!!.key.toString())

                var data: GetDataTourist? = p0.getValue(GetDataTourist::class.java)
                database.child("DisLike").child(p0.key.toString()).child(id_USER).addListenerForSingleValueEvent(object :ValueEventListener{
                    override fun onCancelled(p1: DatabaseError?) {
                        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                    }

                    override fun onDataChange(p1: DataSnapshot?) {
                        if (p1!!.value==null)
                        {
                            var tt = HomeInformationTourisData(p0.key.toString(),data!!.Lat,data.Long,data.MoTa,data!!.tenDiaDiem, data.DiaChi, data.AnhDaiDien, 0, 0, 2.3f)
                            arrList_ThongTinDL.add(tt)
                            adapter.notifyDataSetChanged()
                            Lv_ThongTin!!.adapter = adapter
                            loading!!.visibility = FrameLayout.GONE
                        }
                    }

                })

            }

            override fun onChildRemoved(p0: DataSnapshot?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

        })
    }
    private fun test(){
        database.child("DiadiemDuLich").orderByChild("Huyen").equalTo(13.0).addChildEventListener(object :ChildEventListener{
            override fun onChildMoved(p0: DataSnapshot?, p1: String?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onChildChanged(p0: DataSnapshot?, p1: String?) {
                Toast.makeText(activity,p0!!.toString(),Toast.LENGTH_LONG).show()
            }

            override fun onChildAdded(p0: DataSnapshot?, p1: String?) {
                var data: GetDataTourist? = p0!!.getValue(GetDataTourist::class.java)
                database.child("DisLike").child(p0.key.toString()).child(id_USER).addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onCancelled(p1: DatabaseError?) {
                        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                    }

                    override fun onDataChange(p1: DataSnapshot?) {
                        if (p1!!.value == null) {
                            var tt = HomeInformationTourisData(p0.key.toString(), data!!.Lat, data.Long, data.MoTa, data!!.tenDiaDiem, data.DiaChi, data.AnhDaiDien, 0, 0, 2.3f)
                            arrList_ThongTinDL.add(tt)
                            adapter.notifyDataSetChanged()
                            Lv_ThongTin!!.adapter = adapter
                            loading!!.visibility = FrameLayout.GONE
                        }
                    }
                })                    }

            override fun onChildRemoved(p0: DataSnapshot?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onCancelled(p0: DatabaseError?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }


        })
    }

    private fun doctaikhoan():Boolean {
        val sharedpreferences = this.activity.getSharedPreferences(sharedprperences, Context.MODE_PRIVATE)
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