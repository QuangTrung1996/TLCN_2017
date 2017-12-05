package com.travel.phuc.trung.tlcn.tlcn.Home.TouristAttraction

import android.annotation.SuppressLint
import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.travel.phuc.trung.tlcn.tlcn.Conect.ConectDatabaseSQLite
import com.travel.phuc.trung.tlcn.tlcn.R

// fragment điaiểm
class HomeFragmentProvinces : Fragment() {
    private var ExpandbleLV: ExpandableListView?=null
    private var khuvuc: Spinner?=null
    private val DATABASENAME:String="TinhThanhPho.sqlite"
    private var database: SQLiteDatabase?=null;
    private var KhuVuc:Int=1;
    private var Arraychild:ArrayList<HomeDistrictsData>?= ArrayList();
    private var HasMap:HashMap<HomeProvincesData,ArrayList<HomeDistrictsData>>?= HashMap()
    private var ArrayHeader : ArrayList<HomeProvincesData>?= ArrayList()
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view=inflater!!.inflate(R.layout.home_fragment_provinces,container,false)
        ExpandbleLV=view.findViewById<ExpandableListView>(R.id.expd_tinhthanh_dulich)
        khuvuc =view.findViewById<Spinner>(R.id.Sp_KhuVuc)
        addheader(KhuVuc)

        ExpandbleLV!!.setOnGroupClickListener { parent, v, groupPosition, id ->
            Toast.makeText(this.activity, groupPosition.toString(), Toast.LENGTH_SHORT).show();
//            val intent = Intent(this.activity, ThongTinChiTiet_DDDL::class.java)
//            startActivity(intent)
            true
        }
        val adapterKhuVuc: ArrayAdapter<String> = ArrayAdapter(this.activity, android.R.layout.simple_spinner_item, resources.getStringArray(R.array.KhuVuc))
        adapterKhuVuc.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        khuvuc!!.adapter=adapterKhuVuc
       doikhuvuc()
        return view;
    }
// thay đôit khu vực từ spinner
    private fun doikhuvuc() {
        khuvuc!!.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {

                when (p2) {
                    0 ->{
                        KhuVuc=1;
                        addheader(KhuVuc)
                    }

                    1 ->{
                        KhuVuc=2;
                        addheader(KhuVuc)
                    }
                    2 ->{
                        KhuVuc=3;
                        addheader(KhuVuc)
                    }
                    3 -> {
                        KhuVuc=4;
                        addheader(KhuVuc)
                    }
                    4 ->{
                        KhuVuc=5;
                        addheader(KhuVuc)
                    }
                    5 -> {
                        KhuVuc=6;
                        addheader(KhuVuc)
                    }
                    6 ->{
                        KhuVuc=7;
                        addheader(KhuVuc)
                    }


                }

            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }

    }
// đọc dữ liệu từ sqlite và đổ vào ArrayHeader
    @SuppressLint("Recycle")
    private fun addheader(idkhuvuc:Int){
        database = ConectDatabaseSQLite().initDatabase(this.activity,DATABASENAME);
        var cursor : Cursor = database!!.rawQuery("SELECT * FROM Tinh_TP where Tinh_TP.Vung="+idkhuvuc,null)
        ArrayHeader!!.clear();
        cursor.moveToPosition(2)
        for (i in 0 until cursor.count){
            cursor.moveToPosition(i)
            Arraychild= ArrayList()
            Arraychild=addchild(cursor.getInt(0))
            ArrayHeader!!.add(HomeProvincesData(cursor.getString(1),Arraychild!!.size))
            HasMap!!.put(ArrayHeader!![i],Arraychild!!)
            val adapter= HomeExPdbProvinces(this.activity,ArrayHeader!!,HasMap!!)
            adapter.notifyDataSetChanged();
            ExpandbleLV!!.setAdapter(adapter)

        }

    }
   // đọc dữ liệu từ sqlite và đổ vào Arraychild
    @SuppressLint("Recycle")
    private fun addchild(idtinh:Int):ArrayList<HomeDistrictsData> {
        database = ConectDatabaseSQLite().initDatabase(this.activity,DATABASENAME);
        var cursor : Cursor = database!!.rawQuery("SELECT * FROM Huyen where Huyen.Tinh="+idtinh,null)
        Arraychild!!.clear()
        for (i in 0 until cursor.count)
        {
            cursor.moveToPosition(i)
            Arraychild!!.add(HomeDistrictsData(cursor.getString(1)))
        }
        return Arraychild!!
    }
}