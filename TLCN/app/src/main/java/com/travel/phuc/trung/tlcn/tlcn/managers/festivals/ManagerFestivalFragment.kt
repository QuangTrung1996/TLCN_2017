package com.travel.phuc.trung.tlcn.tlcn.managers.festivals

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import com.google.firebase.database.*
import com.travel.phuc.trung.tlcn.tlcn.AddInfromation.InformationDataAdapter
import com.travel.phuc.trung.tlcn.tlcn.Home.festivalVenues.getDataFestival
import com.travel.phuc.trung.tlcn.tlcn.R
import com.travel.phuc.trung.tlcn.tlcn.managers.tourist.ManagerTouristAdapter

class ManagerFestivalFragment : Fragment() {

    val databaseRef : DatabaseReference = FirebaseDatabase.getInstance().reference

    private lateinit var lvDL   : ListView
    private lateinit var adapter: ManagerTouristAdapter
    private var listThongTin    : ArrayList<InformationDataAdapter> = ArrayList()

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.manager_fragment_list,container,false)

        lvDL = view.findViewById(R.id.manager_fragment_listView)
        adapter = ManagerTouristAdapter(this.context, listThongTin)
        lvDL.adapter = adapter

        docThongTinLH()

        lvDL.setOnItemClickListener { _, _, position, _ ->
            val intent = Intent(activity, ManagerFestivalConfirmInformation::class.java)
            intent.putExtra("key", listThongTin[position].key)
            startActivity(intent)
        }

        return view
    }

    private fun docThongTinLH() {
        databaseRef.child("Tam").child("DiaDiemLH").addChildEventListener(object : ChildEventListener {
            override fun onCancelled(p0: DatabaseError?) {}
            override fun onChildMoved(p0: DataSnapshot?, p1: String?) {}
            override fun onChildChanged(p0: DataSnapshot?, p1: String?) {}

            override fun onChildAdded(p0: DataSnapshot?, p1: String?) {
                val data: getDataFestival? = p0!!.getValue(getDataFestival::class.java)
                listThongTin.add(InformationDataAdapter(data!!.TenLeHoi,p0.key,2,data.AnhDaiDien))
                adapter.notifyDataSetChanged()
            }

            override fun onChildRemoved(p0: DataSnapshot?) {
                for (i in 0 until listThongTin.size)
                {
                    if (p0!!.key == listThongTin[i].key)
                    {
                        listThongTin.removeAt(i)
                        adapter.notifyDataSetChanged()
                        break
                    }
                }
            }
        })
    }
}