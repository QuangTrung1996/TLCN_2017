package com.travel.phuc.trung.tlcn.tlcn.Home

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.ActionBar
import android.widget.ImageButton
import android.widget.ListView
import com.google.firebase.database.*
import com.travel.phuc.trung.tlcn.tlcn.R

class HomeActivityComment : AppCompatActivity() {

    private var database: DatabaseReference
    var keyDL :String?=null
    init {
        database    = FirebaseDatabase.getInstance().reference
    }
    private var Lv :ListView?=null
    private var Arrthongtincomment:ArrayList<HomeInfromationCommentData> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_comment)
        var actionBar : ActionBar? = getSupportActionBar()
        actionBar!!.hide()
        supportActionBar!!.hide()
        val intent = intent
        keyDL = intent.getStringExtra("keyDL")
        Lv=findViewById<ListView>(R.id.LV_TTComment)
        addcomment()
        var btnBack=findViewById<ImageButton>(R.id.thoatcomment)
        btnBack.setOnClickListener {
            finish()
        }
    }
    private fun addcomment() {
        database.child("Comment").child(keyDL).addChildEventListener(object : ChildEventListener {
            override fun onCancelled(p0: DatabaseError?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onChildMoved(p0: DataSnapshot?, p1: String?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onChildChanged(p0: DataSnapshot?, p1: String?) {
                val data:GetDataComment? = p0!!.getValue(GetDataComment::class.java)
                database.child("Users").child(data!!.id_User).child("AnhDaiDien").addValueEventListener(object :ValueEventListener{
                    override fun onCancelled(p0: DatabaseError?) {
                        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                    }

                    override fun onDataChange(p0: DataSnapshot?) {
                        Arrthongtincomment!!.add(HomeInfromationCommentData("cho",data.text,p0!!.value.toString(),data.time))
                        var adapter=HomeLvComment(this@HomeActivityComment,Arrthongtincomment)
                        adapter.notifyDataSetChanged()
                        Lv!!.adapter=adapter
                    }

                })            }

            override fun onChildAdded(p0: DataSnapshot?, p1: String?) {
                val data:GetDataComment? = p0!!.getValue(GetDataComment::class.java)
                database.child("Users").child(data!!.id_User).child("AnhDaiDien").addValueEventListener(object :ValueEventListener{
                    override fun onCancelled(p0: DatabaseError?) {
                        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                    }

                    override fun onDataChange(p0: DataSnapshot?) {
                        Arrthongtincomment!!.add(HomeInfromationCommentData("cho",data.text,p0!!.value.toString(),data.time))
                        var adapter=HomeLvComment(this@HomeActivityComment,Arrthongtincomment)
                        adapter.notifyDataSetChanged()
                        Lv!!.adapter=adapter
                    }

                })

            }

            override fun onChildRemoved(p0: DataSnapshot?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

        })
    }
}
