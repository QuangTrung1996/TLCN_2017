package com.travel.phuc.trung.tlcn.tlcn.Home

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.ActionBar
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.ListView
import android.widget.Toast
import com.google.firebase.database.*
import com.travel.phuc.trung.tlcn.tlcn.R
import kotlinx.android.synthetic.main.activity_home_comment.*

class HomeActivityComment : AppCompatActivity() {
    private var role:Int = 0
    private var database: DatabaseReference
    var keyDL :String?=null
    var socomment:Int=0;
    init {
        database    = FirebaseDatabase.getInstance().reference
    }
    private var Lv :ListView?=null
    private var Arrthongtincomment:ArrayList<HomeInfromationCommentData> = ArrayList()

    val sharedprperences : String="taikhoan";
    var id_USER :String?=null
    var ten:String? =null
    var ten_email:String? = null
    var hinhDaiDien:String?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_comment)
        var actionBar : ActionBar? = getSupportActionBar()
        actionBar!!.hide()
        supportActionBar!!.hide()
        val intent = intent
        keyDL = intent.getStringExtra("keyDL")
        role = intent.getIntExtra("key",0)
        if (role !=1)
        {
            dangBL.visibility = FrameLayout.GONE
        }
        //tongsocomment.text = socomment.toString()+" "+" Comment"
        Lv=findViewById<ListView>(R.id.LV_TTComment)
        addcomment()
        var btnBack=findViewById<ImageButton>(R.id.thoatcomment)
        btnBack.setOnClickListener {
            finish()
        }
        dang()

    }

    private fun dang() {
        dangcomment.setOnClickListener {
            if (role==1){
                if (commenttext.text.toString() != "" || commenttext !=null)
                {
                    doctaikhoan()
                    val time = System.currentTimeMillis()
                    val data = GetDataComment(id_USER!!,commenttext.text.toString(),time)
                    database.child("Comment").child(keyDL).child(time.toString()).setValue(data, DatabaseReference.CompletionListener { databaseError, databaseReference ->
                        if (databaseError != null)
                        {
                            Toast.makeText(this@HomeActivityComment,"Thử lại",Toast.LENGTH_SHORT).show()
                        }
                    })
                }
            }
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
                socomment ++
                tongsocomment.text = socomment.toString().plus(" Comment")
                val data:GetDataComment? = p0!!.getValue(GetDataComment::class.java)
                database.child("Users").child(data!!.id_User).child("anhDaiDien").addValueEventListener(object :ValueEventListener{
                    override fun onCancelled(p0: DatabaseError?) {
                        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                    }

                    override fun onDataChange(p0: DataSnapshot?) {
                        database.child("Users").child(data!!.id_User).child("ten").addValueEventListener(object :ValueEventListener{
                            override fun onCancelled(p1: DatabaseError?) {

                            }

                            override fun onDataChange(p1: DataSnapshot?) {
                                Arrthongtincomment.add(HomeInfromationCommentData(p1!!.value.toString(),data.text,p0!!.value.toString(),data.time))
                                var adapter=HomeLvComment(this@HomeActivityComment,Arrthongtincomment)
                                adapter.notifyDataSetChanged()
                                Lv!!.adapter=adapter
                            }

                        })

                    }

                })

            }

            override fun onChildAdded(p0: DataSnapshot?, p1: String?) {
                socomment ++
                tongsocomment.text = socomment.toString().plus(" Comment")
                val data:GetDataComment? = p0!!.getValue(GetDataComment::class.java)
                database.child("Users").child(data!!.id_User).child("anhDaiDien").addValueEventListener(object :ValueEventListener{
                    override fun onCancelled(p0: DatabaseError?) {
                        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                    }

                    override fun onDataChange(p0: DataSnapshot?) {
                        database.child("Users").child(data!!.id_User).child("ten").addValueEventListener(object :ValueEventListener{
                            override fun onCancelled(p1: DatabaseError?) {

                            }

                            override fun onDataChange(p1: DataSnapshot?) {
                                Arrthongtincomment.add(HomeInfromationCommentData(p1!!.value.toString(),data.text,p0!!.value.toString(),data.time))
                                var adapter=HomeLvComment(this@HomeActivityComment,Arrthongtincomment)
                                adapter.notifyDataSetChanged()
                                Lv!!.adapter=adapter
                            }

                        })

                    }

                })

            }

            override fun onChildRemoved(p0: DataSnapshot?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

        })
    }
    private fun doctaikhoan():Boolean {
        val sharedpreferences = this@HomeActivityComment.getSharedPreferences(sharedprperences, android.content.Context.MODE_PRIVATE)
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
