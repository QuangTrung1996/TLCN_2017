package com.travel.phuc.trung.tlcn.tlcn.managers.tourist

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import com.google.firebase.database.*
import com.travel.phuc.trung.tlcn.tlcn.AddInfromation.InformationDataAdapter
import com.travel.phuc.trung.tlcn.tlcn.Home.TouristAttraction.GetDataTourist
import com.travel.phuc.trung.tlcn.tlcn.R

class ManagerTouristFragment : Fragment() {

    val databaseRef : DatabaseReference = FirebaseDatabase.getInstance().reference

    private lateinit var lvDL   : ListView
    private lateinit var adapter: ManagerTouristAdapter
    private var listThongTin    : ArrayList<InformationDataAdapter> = ArrayList()

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view =inflater!!.inflate(R.layout.manager_fragment_list,container,false)

        lvDL = view.findViewById(R.id.manager_fragment_listView)
        adapter = ManagerTouristAdapter(this.context, listThongTin)
        lvDL.adapter = adapter

        docThongTinDL()

        lvDL.setOnItemClickListener { _, _, position, _ ->
            val intent = Intent(activity, ManagerTouristConfirmInformation::class.java)
            intent.putExtra("key", listThongTin[position].key)
            startActivity(intent)
        }

        return view
    }

    private fun docThongTinDL() {
        databaseRef.child("Tam").child("DiaDiemDL").addChildEventListener(object :ChildEventListener{
            override fun onCancelled(p0: DatabaseError?) {}

            override fun onChildMoved(p0: DataSnapshot?, p1: String?) {}

            override fun onChildChanged(p0: DataSnapshot?, p1: String?) {}

            override fun onChildAdded(p0: DataSnapshot?, p1: String?) {
                if (p0 != null) {
                    val data: GetDataTourist? = p0.getValue(GetDataTourist::class.java)
                    listThongTin.add(InformationDataAdapter(data!!.tenDiaDiem, p0.key, 1,data.AnhDaiDien))
                    adapter.notifyDataSetChanged()
                }
            }

            override fun onChildRemoved(p0: DataSnapshot?) {
                for (i in 0 until listThongTin.size) {
                    if (p0!!.key == listThongTin[i].key) {
                        listThongTin.removeAt(i)
                        adapter.notifyDataSetChanged()
                        break
                    }
                }
            }
        })
    }
}