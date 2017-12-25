package com.travel.phuc.trung.tlcn.tlcn.ConfirmInformation

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import com.google.firebase.database.*
import com.travel.phuc.trung.tlcn.tlcn.AddInfromation.InformationDataAdapter
import com.travel.phuc.trung.tlcn.tlcn.Home.TouristAttraction.GetDataTourist
import com.travel.phuc.trung.tlcn.tlcn.Home.festivalVenues.getDataFestival
import com.travel.phuc.trung.tlcn.tlcn.R
import kotlinx.android.synthetic.main.activity_album__unconfimred.*

class AlbumUnconfimred : AppCompatActivity() {
    val databaseRef : DatabaseReference = FirebaseDatabase.getInstance().reference
    private var listthongtin:ArrayList<InformationDataAdapter> = ArrayList()
    private lateinit var adapter: ConfirnApter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        setContentView(R.layout.activity_album__unconfimred)
        adapter = ConfirnApter(this@AlbumUnconfimred,listthongtin)
        addthongtinAlbum()
    }
    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        val id = item!!.itemId
        when (id) {
            android.R.id.home ->{
                finish()
            }

        }

        return true
    }
    private fun addthongtinAlbum() {
        databaseRef.child("Tam").child("Album").addChildEventListener(object :ChildEventListener{
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
               // Toast.makeText(this@AlbumUnconfimred,p0!!.key.toString(),Toast.LENGTH_SHORT).show()
                databaseRef.child("DiadiemDuLich").child(p0!!.key.toString()).addValueEventListener(object : ValueEventListener{
                    override fun onDataChange(p1: DataSnapshot?) {
                        if (p1!!.value!= null) {
                            var data: GetDataTourist? = p1!!.getValue(GetDataTourist::class.java)
                            listthongtin.add(InformationDataAdapter(data!!.tenDiaDiem, p0.key, 1, data.AnhDaiDien))
                            adapter.notifyDataSetChanged()
                            Albumchuaxacnhan!!.adapter = adapter

                        } else {
                            databaseRef.child("DiaDiemLeHoi").child(p0!!.key.toString()).addValueEventListener(object : ValueEventListener {
                                override fun onDataChange(p2: DataSnapshot?) {
                                    if (p2!!.value != null) {
                                        var data: getDataFestival? = p0!!.getValue(getDataFestival::class.java)
                                        listthongtin.add(InformationDataAdapter(data!!.TenLeHoi, p0.key, 1, data.AnhDaiDien))
                                        adapter.notifyDataSetChanged()
                                        Albumchuaxacnhan!!.adapter = adapter
                                    } else {
                                        Toast.makeText(this@AlbumUnconfimred, "không có album nào", Toast.LENGTH_SHORT).show()

                                    }
                                }

                                override fun onCancelled(p0: DatabaseError?) {
                                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                                }

                            })
                        }
                    }

                    override fun onCancelled(p0: DatabaseError?) {
                        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                    }


                })


            }

            override fun onChildRemoved(p0: DataSnapshot?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        })
    }



}
