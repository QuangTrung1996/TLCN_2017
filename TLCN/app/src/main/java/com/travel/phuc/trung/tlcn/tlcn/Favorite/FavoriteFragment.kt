package com.travel.phuc.trung.tlcn.tlcn.Favorite

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.Toast
import com.google.firebase.database.*
import com.travel.phuc.trung.tlcn.tlcn.Home.TouristAttraction.GetDataTourist
import com.travel.phuc.trung.tlcn.tlcn.Home.TouristAttraction.HomeInformationTourisData
import com.travel.phuc.trung.tlcn.tlcn.Home.TouristAttraction.HomeLvTourist
import com.travel.phuc.trung.tlcn.tlcn.R

/**
 * Created by Admin on 15/11/2017.
 */
class FavoriteFragment : Fragment() {
    private var arrList_ThongTinDL:ArrayList<HomeInformationTourisData> = ArrayList()
    val sharedprperences : String="taikhoan";
    var id_USER :String?=null
    var ten:String? =null
    var ten_email:String? = null
    var hinhDaiDien:String?=null
    val database : DatabaseReference
    lateinit var adapter : FavoriteAdapter
    init {
        database    = FirebaseDatabase.getInstance().reference
    }
    var lvFavorist:ListView?=null
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view =inflater!!.inflate(R.layout.favorite_fragment,container,false);
        lvFavorist = view.findViewById<ListView>(R.id.listyeuthich)
         adapter = FavoriteAdapter(activity, arrList_ThongTinDL)
        addtt()
        lvFavorist!!.adapter = adapter
        return view;

    }

    private fun addtt() {
        if (doctaikhoan()){
            var t=0;
            database.child("YeuThich").child(id_USER).addChildEventListener(object : ChildEventListener {
                override fun onCancelled(p0: DatabaseError?) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

                override fun onChildMoved(p0: DataSnapshot?, p1: String?) {
                    for (i in 0 until arrList_ThongTinDL.size-1)
                    {
                        if (p0!!.key.toString()==arrList_ThongTinDL!!.get(i).key)
                        {
                            arrList_ThongTinDL.removeAt(i)
                            database.child("DiadiemDuLich").child(p0!!.key.toString()).addValueEventListener(object :ValueEventListener{
                                override fun onDataChange(p0: DataSnapshot?) {
                                    var data: GetDataTourist? = p0!!.getValue(GetDataTourist::class.java)
                                    var tt = HomeInformationTourisData(p0.key.toString(),data!!.Lat,data.Long,data.MoTa,data!!.tenDiaDiem, data.DiaChi, data.AnhDaiDien, 0, 0, 2.3f)
                                    arrList_ThongTinDL.add(tt)
                                   // var adapter = FavoriteAdapter(activity, arrList_ThongTinDL)
                                    adapter.notifyDataSetChanged()
                                   // lvFavorist!!.adapter = adapter
                                }

                                override fun onCancelled(p0: DatabaseError?) {
                                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                                }


                            })
                        }
                    }                }

                override fun onChildChanged(p0: DataSnapshot?, p1: String?) {
                    for (i in 0 until arrList_ThongTinDL.size-1)
                    {
                        if (p0!!.key.toString()==arrList_ThongTinDL!!.get(i).key)
                        {
                            arrList_ThongTinDL.removeAt(i)
                            adapter.notifyDataSetChanged()
                            database.child("DiadiemDuLich").child(p0!!.key.toString()).addValueEventListener(object :ValueEventListener{
                                override fun onDataChange(p0: DataSnapshot?) {
                                    var data: GetDataTourist? = p0!!.getValue(GetDataTourist::class.java)
                                    var tt = HomeInformationTourisData(p0.key.toString(),data!!.Lat,data.Long,data.MoTa,data!!.tenDiaDiem, data.DiaChi, data.AnhDaiDien, 0, 0, 2.3f)
                                    arrList_ThongTinDL.add(tt)
                                    //var adapter = FavoriteAdapter(activity, arrList_ThongTinDL)
                                    adapter.notifyDataSetChanged()
                                   // lvFavorist!!.adapter = adapter
                                }

                                override fun onCancelled(p0: DatabaseError?) {
                                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                                }


                            })
                        }
                    }
                }
                override fun onChildAdded(p0: DataSnapshot?, p1: String?) {
                    for (i in 0 until arrList_ThongTinDL.size-1)
                    {
                        if (p0!!.key.toString()== arrList_ThongTinDL[i].key)
                        {
                            t==1
                        }
                    }
                    if (t==0) {

                        database.child("DiadiemDuLich").child(p0!!.key.toString()).addValueEventListener(object : ValueEventListener {
                            override fun onDataChange(p0: DataSnapshot?) {
                                var data: GetDataTourist? = p0!!.getValue(GetDataTourist::class.java)
                                var tt = HomeInformationTourisData(p0.key.toString(), data!!.Lat, data.Long, data.MoTa, data!!.tenDiaDiem, data.DiaChi, data.AnhDaiDien, 0, 0, 2.3f)
                                arrList_ThongTinDL.add(tt)
                                //var adapter = FavoriteAdapter(activity, arrList_ThongTinDL)
                                adapter.notifyDataSetChanged()
                                //lvFavorist!!.adapter = adapter
                            }

                            override fun onCancelled(p0: DatabaseError?) {
                                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                            }


                        })
                    }
                }

                override fun onChildRemoved(p0: DataSnapshot?) {
                    for (i in 0 until arrList_ThongTinDL.size-1)
                    {
                       if (p0!!.key.toString()== arrList_ThongTinDL[i].key)
                       {
                           arrList_ThongTinDL.removeAt(i)
                           adapter.notifyDataSetChanged()
                       }
                       }
                    }
            })
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