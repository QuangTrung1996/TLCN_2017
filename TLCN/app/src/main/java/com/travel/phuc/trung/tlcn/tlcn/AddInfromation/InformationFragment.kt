package com.travel.phuc.trung.tlcn.tlcn.AddInfromation

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ListView
import android.widget.Toast
import com.google.firebase.database.*
import com.travel.phuc.trung.tlcn.tlcn.Favorite.FavoriteAdapter
import com.travel.phuc.trung.tlcn.tlcn.GoogleMap.MapsActivity
import com.travel.phuc.trung.tlcn.tlcn.Home.TouristAttraction.DeteiladSchedulesData
import com.travel.phuc.trung.tlcn.tlcn.Home.TouristAttraction.GetDataTourist
import com.travel.phuc.trung.tlcn.tlcn.Home.TouristAttraction.HomeInformationTourisData
import com.travel.phuc.trung.tlcn.tlcn.Home.festivalVenues.getDataFestival
import com.travel.phuc.trung.tlcn.tlcn.R

/**
 * Created by Admin on 6/12/2017.
 */
class InformationFragment : Fragment() {

    private val sharedprperences : String="taikhoan";
    private val KEY_ID_USER = "ID_User"
    private var id_USER :String?=null
    private val database : DatabaseReference
    init {
        database    = FirebaseDatabase.getInstance().reference
    }


    private var add: FloatingActionButton?=null
    private var tourisr: FloatingActionButton?=null
    private var festival: FloatingActionButton?=null
   var flag =true
    var lvDaDang:ListView?=null
    var listthongtin:ArrayList<InformationDataAdapter> = ArrayList()
    private lateinit var adapter: InfrormationAddedAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view =inflater!!.inflate(R.layout.ifomation,container,false);
        add = view.findViewById(R.id.addthongtin)
        tourisr = view.findViewById(R.id.flb_tourist)
        festival = view.findViewById(R.id.flb_festival)
        lvDaDang = view.findViewById(R.id.thongtindadang)
//        listthongtin.add(InformationDataAdapter("khu du lịch tân cảng ","dfdsfsdg",1))
         adapter = InfrormationAddedAdapter(this.context,listthongtin)
//
        doctaikhoan()
        setonclick()
        layTTdangDL()
        LayTTdangLH()
        layTTdangDL1()
        LayTTdangLH1()
        //lvDaDang!!.adapter = adapter
        return view;

    }

    private fun LayTTdangLH1() {
        database.child("DiaDiemLeHoi").orderByChild("idUser").equalTo(id_USER.toString()).addChildEventListener(object :ChildEventListener{
            override fun onCancelled(p0: DatabaseError?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onChildMoved(p0: DataSnapshot?, p1: String?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onChildChanged(p0: DataSnapshot?, p1: String?) {
                var data: getDataFestival? = p0!!.getValue(getDataFestival::class.java)
                for (i in 0 until listthongtin.size)
                {
                    if (p0!!.key == listthongtin.get(i).key)
                    {
                        val  a =InformationDataAdapter(data!!.TenLeHoi,p0.key,4,data.AnhDaiDien)
                        listthongtin[i] = a!!
                    }
                }            }

            override fun onChildAdded(p0: DataSnapshot?, p1: String?) {
                var  a =0
                for (i in 0 until listthongtin.size)
                {
                    if (listthongtin.get(i).key == p0!!.key.toString()){
                        a=1
                        break
                    }

                }
                if (a==0) {
                    var data: getDataFestival? = p0!!.getValue(getDataFestival::class.java)
                    // Toast.makeText(activity,data.toString(),Toast.LENGTH_SHORT).show()
                    listthongtin.add(InformationDataAdapter(data!!.TenLeHoi, p0.key, 4,data.AnhDaiDien))
                    adapter.notifyDataSetChanged()
                    lvDaDang!!.adapter = adapter
                }
            }

            override fun onChildRemoved(p0: DataSnapshot?) {
                for (i in 0 until listthongtin.size)
                {
                    if (p0!!.key == listthongtin.get(i).key)
                    {
                        listthongtin.removeAt(i)
                        adapter.notifyDataSetChanged()
                        break
                    }
                }
            }
        })
    }

    // lấy thông tin từ dịa diểm dulich
    private fun layTTdangDL1() {
        database.child("DiadiemDuLich").orderByChild("idUser").equalTo(id_USER.toString()).addChildEventListener(object :ChildEventListener{
            override fun onCancelled(p0: DatabaseError?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onChildMoved(p0: DataSnapshot?, p1: String?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onChildChanged(p0: DataSnapshot?, p1: String?) {
                var data: GetDataTourist? = p0!!.getValue(GetDataTourist::class.java)
                for (i in 0 until listthongtin.size)
                {
                    if (p0!!.key == listthongtin.get(i).key)
                    {
                        val  a =InformationDataAdapter(data!!.tenDiaDiem,p0.key,3,data.AnhDaiDien)
                        listthongtin[i] = a!!
                        adapter.notifyDataSetChanged()
                        lvDaDang!!.adapter = adapter
                        break
                    }
                }
            }

            override fun onChildAdded(p0: DataSnapshot?, p1: String?) {
                var  a =0
                for (i in 0 until listthongtin.size)
                {
                    if (listthongtin.get(i).key == p0!!.key.toString()){
                        a=1
                        break
                    }

                }
                if (a==0) {
                    var data: GetDataTourist? = p0!!.getValue(GetDataTourist::class.java)
                    // Toast.makeText(activity,data.toString(),Toast.LENGTH_SHORT).show()
                    listthongtin.add(InformationDataAdapter(data!!.tenDiaDiem, p0.key, 3,data.AnhDaiDien))
                    adapter.notifyDataSetChanged()
                    lvDaDang!!.adapter = adapter
                }
            }

            override fun onChildRemoved(p0: DataSnapshot?) {
                for (i in 0 until listthongtin.size)
                {
                    if (p0!!.key == listthongtin.get(i).key)
                    {
                        listthongtin.removeAt(i)
                        adapter.notifyDataSetChanged()
                        break
                    }
                }

            }
        })
    }

    private fun LayTTdangLH() {
        database.child("Tam").child("DiaDiemLH").orderByChild("idUser").equalTo(id_USER.toString()).addChildEventListener(object :ChildEventListener{
            override fun onCancelled(p0: DatabaseError?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onChildMoved(p0: DataSnapshot?, p1: String?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onChildChanged(p0: DataSnapshot?, p1: String?) {
                var data: getDataFestival? = p0!!.getValue(getDataFestival::class.java)
                for (i in 0 until listthongtin.size)
                {
                    if (p0!!.key == listthongtin.get(i).key)
                    {
                        val  a =InformationDataAdapter(data!!.TenLeHoi,p0.key,2,data.AnhDaiDien)
                        listthongtin[i] = a!!
                        break
                    }
                }
            }

            override fun onChildAdded(p0: DataSnapshot?, p1: String?) {
                var data: getDataFestival? = p0!!.getValue(getDataFestival::class.java)
                // Toast.makeText(activity,data.toString(),Toast.LENGTH_SHORT).show()
                listthongtin.add(InformationDataAdapter(data!!.TenLeHoi,p0.key,2,data.AnhDaiDien))
                adapter.notifyDataSetChanged()
                lvDaDang!!.adapter = adapter
            }

            override fun onChildRemoved(p0: DataSnapshot?) {
                for (i in 0 until listthongtin.size)
                {
                    if (p0!!.key == listthongtin.get(i).key)
                    {
                        listthongtin.removeAt(i)
                        adapter.notifyDataSetChanged()
                        break
                    }
                }
            }
        })
    }

    private fun layTTdangDL() {
        database.child("Tam").child("DiaDiemDL").orderByChild("idUser").equalTo(id_USER.toString()).addChildEventListener(object :ChildEventListener{
            override fun onCancelled(p0: DatabaseError?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onChildMoved(p0: DataSnapshot?, p1: String?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onChildChanged(p0: DataSnapshot?, p1: String?) {
                var data: GetDataTourist? = p0!!.getValue(GetDataTourist::class.java)
                for (i in 0 until listthongtin.size)
                {
                    if (p0!!.key == listthongtin.get(i).key)
                    {
                        val  a =InformationDataAdapter(data!!.tenDiaDiem,p0.key,1,data.AnhDaiDien)
                        listthongtin[i] = a!!
                        adapter.notifyDataSetChanged()
                        break
                    }
                }
            }

            override fun onChildAdded(p0: DataSnapshot?, p1: String?) {

                var data: GetDataTourist? = p0!!.getValue(GetDataTourist::class.java)
               // Toast.makeText(activity,data.toString(),Toast.LENGTH_SHORT).show()
                listthongtin.add(InformationDataAdapter(data!!.tenDiaDiem,p0.key,1,data.AnhDaiDien))
                adapter.notifyDataSetChanged()
                lvDaDang!!.adapter = adapter
            }

            override fun onChildRemoved(p0: DataSnapshot?) {
                for (i in 0 until listthongtin.size)
                {
                    if (p0!!.key == listthongtin.get(i).key)
                    {
                        listthongtin.removeAt(i)
                        adapter.notifyDataSetChanged()
                        break
                    }
                }
            }
        })
    }
    private fun setonclick(){
        add!!.setOnClickListener {
            if (flag == true) {
                flag = false
                hien()

            }
            else{
                flag = true
                an()

            }
        }
        tourisr!!.setOnClickListener({
            val intent = Intent(this.activity, AddInfromationTourist::class.java)
            this.activity.startActivity(intent)
        })
        festival!!.setOnClickListener({
            val intent = Intent(this.activity, AddInformationFestival::class.java)
            this.activity.startActivity(intent)
        })
    }
    private fun hien() {
        tourisr!!.show()
        festival!!.show()
    }
    private fun an(){
        tourisr!!.hide()
        festival!!.hide()
    }

    private fun doctaikhoan():Boolean {
        val sharedpreferences = this.activity.getSharedPreferences(sharedprperences, Context.MODE_PRIVATE)
        id_USER = sharedpreferences.getString("Uid", null)
        if (id_USER != null) {
            return true
        }
        return false

    }
}