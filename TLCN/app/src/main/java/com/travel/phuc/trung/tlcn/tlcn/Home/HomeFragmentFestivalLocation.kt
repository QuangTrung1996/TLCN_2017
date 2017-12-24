package com.travel.phuc.trung.tlcn.tlcn.Home

import android.annotation.SuppressLint
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Spinner
import com.google.firebase.database.*
import com.travel.phuc.trung.tlcn.tlcn.Conect.ConectDatabaseSQLite
import com.travel.phuc.trung.tlcn.tlcn.Home.TouristAttraction.*
import com.travel.phuc.trung.tlcn.tlcn.Home.festivalVenues.DataFestival
import com.travel.phuc.trung.tlcn.tlcn.Home.festivalVenues.HomeFestivalListAdapter
import com.travel.phuc.trung.tlcn.tlcn.Home.festivalVenues.getDataFestival
import com.travel.phuc.trung.tlcn.tlcn.R

class HomeFragmentFestivalLocation : Fragment() {
    private var khuvuc: Spinner?=null
    private var Tinh: Spinner?=null
    private val DATABASENAME:String="TinhThanhPho.sqlite"
    private var database: SQLiteDatabase?=null;
    private var idkhuvuc:Int = 1;
    private var listTinh:ArrayList<String> = ArrayList()
    private var ArrlistFestival:ArrayList<DataFestival> = ArrayList()
    private lateinit var adapterLH:HomeFestivalListAdapter
    private var lvFestival:ListView?=null
    private var databaseFB: DatabaseReference
    init {
        databaseFB    = FirebaseDatabase.getInstance().reference
    }
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater!!.inflate(R.layout.home_fragment_festival_location, container, false);
        khuvuc =view.findViewById<Spinner>(R.id.SP_khuvuc_LH)
        Tinh = view.findViewById<Spinner>(R.id.SP_Tinh_LH)
        lvFestival = view.findViewById<ListView>(R.id.LV_ThongTinLeHoi)
        adapterLH = HomeFestivalListAdapter(this.activity,ArrlistFestival)
        ArrlistFestival.add(DataFestival("djndsbfdsf",13.343244,6.432434,"bibuhsabdgsytadvsgdvsgdcsadsadsadsad0","làng tre năm nay","117/12213/34324","https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcS_muC2O0PYHfosGb2HQJiMCqc3xGK85W_twwZKQxW2qDD8O-6y",32123213213,244324235125453455))
        ArrlistFestival.add(DataFestival("ssdsasafsd",13.343244,6.432434,"bibuhsabdgsytadvsgdvsgdcsadsadsadsad0","festival hoa đà lạt","117/12213/34324","https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcS_muC2O0PYHfosGb2HQJiMCqc3xGK85W_twwZKQxW2qDD8O-6y",32123213213,2443))

        adapterLH.notifyDataSetChanged()
        lvFestival!!.adapter = adapterLH

        addTTLeHoi()
        val adapterKhuVuc: ArrayAdapter<String> = ArrayAdapter(this.activity, android.R.layout.simple_spinner_item, resources.getStringArray(R.array.KhuVuc))
        adapterKhuVuc.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        khuvuc!!.adapter=adapterKhuVuc
        addtinh(idkhuvuc)
        doikhuvuc()
        return view
    }

    private fun addTTLeHoi() {
        databaseFB.child("DiaDiemLeHoi").addChildEventListener(object : ChildEventListener{
            override fun onCancelled(p0: DatabaseError?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onChildMoved(p0: DataSnapshot?, p1: String?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onChildChanged(p0: DataSnapshot?, p1: String?) {
                var data: getDataFestival? = p0!!.getValue(getDataFestival::class.java)
                var tt = DataFestival(p0.key.toString(),data!!.Lat,data.Long,data.MoTa,data!!.TenLeHoi, data.DiaChi, data.AnhDaiDien, data.NgayBD, data.NgayKT)
                for (i in 0 until ArrlistFestival.size)
                {
                    if (ArrlistFestival.get(i).key==p0!!.key)
                    {
                        ArrlistFestival[i]=tt
                        adapterLH.notifyDataSetChanged()
                    }
                }
            }

            override fun onChildAdded(p0: DataSnapshot?, p1: String?) {
                var data: getDataFestival? = p0!!.getValue(getDataFestival::class.java)

                var tt = DataFestival(p0.key.toString(),data!!.Lat,data.Long,data.MoTa,data!!.TenLeHoi, data.DiaChi, data.AnhDaiDien, data.NgayBD, data.NgayKT)
                ArrlistFestival.add(tt)
                adapterLH.notifyDataSetChanged()
                lvFestival!!.adapter = adapterLH
            }

            override fun onChildRemoved(p0: DataSnapshot?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        })
    }


    @SuppressLint("Recycle")
    private fun addtinh(idkhuvuc:Int){
        database = ConectDatabaseSQLite().initDatabase(this.activity,DATABASENAME);
        var cursor : Cursor = database!!.rawQuery("SELECT * FROM Tinh_TP where Tinh_TP.Vung="+idkhuvuc,null)
        listTinh!!.clear();
        //cursor.moveToPosition(2)
        for (i in 0 until cursor.count){
            cursor.moveToPosition(i)
            listTinh!!.add((cursor.getString(1)))
        }
        val adapterTinh: ArrayAdapter<String> = ArrayAdapter(this.activity, android.R.layout.simple_spinner_item, listTinh)
        adapterTinh.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        Tinh!!.adapter=adapterTinh

    }
    private fun doikhuvuc() {
        khuvuc!!.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                idkhuvuc = p2+1
                addtinh(idkhuvuc)
                when (p2) {
                    0 ->{ }

                    1 ->{ }
                    2 ->{ }
                    3 -> { }
                    4 ->{ }
                    5 -> { }
                    6 ->{ } }
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }

    }
}