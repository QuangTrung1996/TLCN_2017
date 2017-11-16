package com.travel.phuc.trung.tlcn.tlcn.Home.TouristAttraction

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.Toast
import com.google.firebase.database.*
import com.travel.phuc.trung.tlcn.tlcn.Conect.CheckInternet
import com.travel.phuc.trung.tlcn.tlcn.Conect.CheckInternetInterface
import com.travel.phuc.trung.tlcn.tlcn.R

/**
 * Created by Admin on 16/11/2017.
 */
class HomeFragmentInformationTourist :Fragment(),CheckInternetInterface{


    val database : DatabaseReference
    private var Lv_ThongTin: ListView? = null
    private var tong:Int=10
    private var listkeycomment:ArrayList<String> = ArrayList()
    companion object{
        var listkeyDDDL:ArrayList<String> = ArrayList()
    }
    init {
        database    = FirebaseDatabase.getInstance().reference
    }
    var arrList_ThongTinDL:ArrayList<HomeInformationTourisData> = ArrayList()
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater!!.inflate(R.layout.home_fragment_information_tourist, container, false);

        Lv_ThongTin =view.findViewById<ListView>(R.id.LV_ThongTinDuLich)

        try {

            val a = CheckInternet(this)
            a.checkConnection(this.context)
        }catch (e: Exception){}
        return view
    }
    override fun kiemtrainternet(flag: Boolean) {
        if (flag==true){
            //Toast.makeText(this.context,"có in ternet",Toast.LENGTH_LONG).show()
            addthongtin()

        }
        else{
            Toast.makeText(this.context,"đéo có internet",Toast.LENGTH_LONG).show()
        }

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
                var tongcm: ArrayList<String>
                tongcm = listkeycomment
                var data: GetDataTourist? = p0.getValue(GetDataTourist::class.java)
                var tt = HomeInformationTourisData(p0.key.toString(),data!!.Lat,data.Long,data.MoTa,data!!.tenDiaDiem, data.DiaChi, data.AnhDaiDien, 1, tongcm.size, 2.3f)
                arrList_ThongTinDL.add(tt)
                var adapter = HomeLvTourist(activity, arrList_ThongTinDL)
                adapter.notifyDataSetChanged()
                Lv_ThongTin!!.adapter = adapter
            }

            override fun onChildRemoved(p0: DataSnapshot?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

        })
    }
}