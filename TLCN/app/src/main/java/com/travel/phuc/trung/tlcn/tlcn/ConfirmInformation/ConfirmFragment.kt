package com.travel.phuc.trung.tlcn.tlcn.ConfirmInformation

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ListView
import com.google.firebase.database.*
import com.travel.phuc.trung.tlcn.tlcn.AddInfromation.InformationDataAdapter
import com.travel.phuc.trung.tlcn.tlcn.Home.TouristAttraction.GetDataTourist
import com.travel.phuc.trung.tlcn.tlcn.Home.festivalVenues.getDataFestival
import com.travel.phuc.trung.tlcn.tlcn.R

/**
 * Created by Admin on 23/12/2017.
 */
class ConfirmFragment : Fragment(){
    private val sharedprperences : String="taikhoan";
    private val KEY_ID_USER = "ID_User"
    private var id_USER :String?=null
    private val database : DatabaseReference
    init {
        database    = FirebaseDatabase.getInstance().reference
    }
    private var add: FloatingActionButton?=null
    private var xacnan:Button?=null
    private var lvDaDang: ListView?=null
    private var listthongtin:ArrayList<InformationDataAdapter> = ArrayList()
    private lateinit var adapter: ConfirnApter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.ifomation, container, false);
        add = view.findViewById(R.id.addthongtin)
        lvDaDang = view.findViewById(R.id.thongtindadang)
        adapter = ConfirnApter(this.context,listthongtin)
        xacnan = view.findViewById(R.id.xacnhananhcho)
        xacnan!!.visibility = Button.VISIBLE
        xacnan!!.setOnClickListener {
            val intent = Intent(view!!.getContext(), AlbumUnconfimred::class.java)
//                intent.putExtra("loai",2)
            this.getContext().startActivity(intent)
        }
        add!!.visibility = FloatingActionButton.INVISIBLE
        docthongtinDL()
        docthongtinLH()
        lvDaDang!!.setOnItemClickListener { parent, view, position, id ->
            if (listthongtin.get(position).loai ==1)
            {
                val intent = Intent(view!!.getContext(), ComfirnInformationTouris::class.java)
                intent.putExtra("key",listthongtin.get(position).key)
//                intent.putExtra("loai",2)
                view!!.getContext().startActivity(intent)
            }
            else
            {
                val intent = Intent(view!!.getContext(), ConfirnInformationFestival::class.java)
                intent.putExtra("key",listthongtin.get(position).key)
//                intent.putExtra("loai",2)
                view!!.getContext().startActivity(intent)
            }
        }
        return view
    }

    private fun docthongtinLH() {
        database.child("Tam").child("DiaDiemLH").addChildEventListener(object :ChildEventListener{
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
                        lvDaDang!!.adapter = adapter
                        break
                    }
                }
            }

        })
    }

    private fun docthongtinDL() {
        database.child("Tam").child("DiaDiemDL").addChildEventListener(object :ChildEventListener{
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
                if (p0!=null)
                {
                    var data: GetDataTourist? = p0!!.getValue(GetDataTourist::class.java)
                    // Toast.makeText(activity,data.toString(),Toast.LENGTH_SHORT).show()
                    listthongtin.add(InformationDataAdapter(data!!.tenDiaDiem, p0.key, 1,data.AnhDaiDien))
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
                        lvDaDang!!.adapter = adapter
                        break
                    }
                }
            }
        })
    }
}