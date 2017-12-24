package com.travel.phuc.trung.tlcn.tlcn.AddInfromation

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import com.bumptech.glide.Glide
import com.google.firebase.database.*
import com.travel.phuc.trung.tlcn.tlcn.R
import kotlinx.android.synthetic.main.activity_add_image_detail.*

class AddImageDetail : AppCompatActivity() {
    val databaseRef : DatabaseReference = FirebaseDatabase.getInstance().reference
    private var key:String = ""
    private var loai:Int = 0
    private var loaiDD:Int = 0
    private var linkanh:String = ""
    private var keyDD:String= ""
    private val duoi:String = ".png"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_image_detail)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        val intent = intent
        key = intent.getStringExtra("key")
        loai = intent.getIntExtra("loai",0)
        loaiDD = intent.getIntExtra("loaiDD",0)
        keyDD = intent.getStringExtra("keyDD")
        linkanh = intent.getStringExtra("Anh")
        //Toast.makeText(this@AddImageDetail, loaiDD.toString(), Toast.LENGTH_SHORT).show()
        if (key!="" && loai !=0&& linkanh !="")
        {
            Glide.with(this@AddImageDetail)
                    .load(linkanh)
                    .into(anhalbum)

        }
        deleteanh.setOnClickListener({
            if (keyDD !="") {
                deleteAnh()
            }
        })

    }

    private fun deleteAnh() {
        if (loaiDD ==3)
        {
            databaseRef.child("DiadiemDuLich").child(keyDD).child("idAnh").addListenerForSingleValueEvent(object :ValueEventListener{
                override fun onCancelled(p0: DatabaseError?) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

                override fun onDataChange(p0: DataSnapshot?) {
                    if (p0!!.value.toString() != (key+duoi))
                    {
                        databaseRef.child("AlbumAnhDuLich").child(keyDD).child(key).removeValue(DatabaseReference.CompletionListener { databaseError, databaseReference ->
                            if (databaseError == null)
                            {
                                this@AddImageDetail.finish()
                            }
                            else
                            {
                                Toast.makeText(this@AddImageDetail, "thử lại", Toast.LENGTH_SHORT).show()
                            }
                        })
                    }
                    if (p0!!.value.toString() == (key+duoi))
                    {
                        Toast.makeText(this@AddImageDetail, "ko the xoa", Toast.LENGTH_SHORT).show()
                    }
                }
            })
        }
        if (loaiDD ==4)
        {
            databaseRef.child("DiaDiemLeHoi").child(keyDD).child("idAnh").addListenerForSingleValueEvent(object :ValueEventListener{
                override fun onCancelled(p0: DatabaseError?) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

                override fun onDataChange(p0: DataSnapshot?) {
                    if (p0!!.value.toString()!= (key+duoi))
                    {
                        databaseRef.child("AlbumAnhDuLich").child(keyDD).child(key).removeValue(DatabaseReference.CompletionListener { databaseError, databaseReference ->
                            if (databaseError == null)
                            {
                                this@AddImageDetail.finish()
                            }
                            else
                            {
                                Toast.makeText(this@AddImageDetail, "thử lại", Toast.LENGTH_SHORT).show()
                            }
                        })
                    }
                    if (p0!!.value.toString() == (key+duoi))
                    {
                        Toast.makeText(this@AddImageDetail, "ko the xoa", Toast.LENGTH_SHORT).show()
                    }
                }
            })
        }
        if (loaiDD ==1)
        {
            databaseRef.child("Tam").child("DiaDiemDL").child(keyDD).child("idAnh").addListenerForSingleValueEvent(object :ValueEventListener{
                override fun onCancelled(p0: DatabaseError?) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

                override fun onDataChange(p0: DataSnapshot?) {
                    if (p0!!.value.toString()!= (key+duoi))
                    {
                        databaseRef.child("Tam").child("Album").child(keyDD).child(key).removeValue(DatabaseReference.CompletionListener { databaseError, databaseReference ->
                            if (databaseError == null)
                            {
                                this@AddImageDetail.finish()
                            }
                            else
                            {
                                Toast.makeText(this@AddImageDetail, "thử lại", Toast.LENGTH_SHORT).show()
                            }
                        })
                    }
                    if (p0!!.value.toString() == (key+duoi))
                    {
                        Toast.makeText(this@AddImageDetail, "ko the xoa", Toast.LENGTH_SHORT).show()
                    }
                }

            })
        }
        if (loaiDD == 2)
        {
            databaseRef.child("Tam").child("DiaDiemLH").child(keyDD).child("idAnh").addListenerForSingleValueEvent(object :ValueEventListener{
                override fun onCancelled(p0: DatabaseError?) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

                override fun onDataChange(p0: DataSnapshot?) {
                    if (p0!!.value.toString()!= (key+duoi))
                    {
                        databaseRef.child("Tam").child("Album").child(keyDD).child(key).removeValue(DatabaseReference.CompletionListener { databaseError, databaseReference ->
                            if (databaseError == null)
                            {
                                this@AddImageDetail.finish()
                            }
                            else
                            {
                                Toast.makeText(this@AddImageDetail, "thử lại", Toast.LENGTH_SHORT).show()
                            }
                        })
                    }
                    if (p0!!.value.toString() == (key+duoi))
                    {
                        Toast.makeText(this@AddImageDetail, "ko the xoa", Toast.LENGTH_SHORT).show()
                    }
                }

            })
        }
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
}
