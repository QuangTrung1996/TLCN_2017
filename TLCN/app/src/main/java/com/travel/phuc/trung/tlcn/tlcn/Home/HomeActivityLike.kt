package com.travel.phuc.trung.tlcn.tlcn.Home

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.DisplayMetrics
import android.widget.Toast
import com.google.firebase.database.*
import com.travel.phuc.trung.tlcn.tlcn.logins.UserData
import com.travel.phuc.trung.tlcn.tlcn.R
import kotlinx.android.synthetic.main.activity_home_like.*
class HomeActivityLike : AppCompatActivity() {
    val database : DatabaseReference
    init {
        database    = FirebaseDatabase.getInstance().reference
    }
    private var keyDL:String? = null
    private var soLike:Int = 0
    private var ArrayListThongTin:ArrayList<UserData> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_like)
        supportActionBar!!.hide()
        val dm: DisplayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(dm)
        val width:Int = dm.widthPixels
        val height:Int = dm.heightPixels
        window.setLayout((width * .8).toInt(),(height * .65).toInt())
        var intent = intent
        keyDL=intent.getStringExtra("keyDDDL")
        thoatLike.setOnClickListener {
            finish()
        }
        addThongTinLike()
    }

    private fun addThongTinLike() {
        database.child("Like").child(keyDL).addChildEventListener(object :ChildEventListener{
            override fun onCancelled(p0: DatabaseError?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onChildMoved(p0: DataSnapshot?, p1: String?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onChildChanged(p0: DataSnapshot?, p1: String?) {
            }

            override fun onChildAdded(p0: DataSnapshot?, p1: String?) {
                soLike++
                tongsolike.text = soLike.toString().plus("lược like")
                database.child("Users").child(p0!!.value.toString()).addValueEventListener(object : ValueEventListener {
                    override fun onCancelled(p0: DatabaseError?) {
                        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                    }

                    override fun onDataChange(p0: DataSnapshot?) {
                        var tt:UserData ?= p0!!.getValue(UserData::class.java)
                        ArrayListThongTin!!.add(tt!!)
                        var adapter = HomeLvAdapterLike(this@HomeActivityLike,ArrayListThongTin)
                        adapter.notifyDataSetChanged()
                        Lv_Like.adapter = adapter
                    }
                })
            }

            override fun onChildRemoved(p0: DataSnapshot?) {
                soLike --
                tongsolike.text = soLike.toString().plus("lược like")
                for (i in 0 until ArrayListThongTin.size-1)
                {
                    if (ArrayListThongTin.get(i).id ==p0!!.value.toString())
                    {
                        ArrayListThongTin.removeAt(i)
                        var adapter = HomeLvAdapterLike(this@HomeActivityLike,ArrayListThongTin)
                        adapter.notifyDataSetChanged()
                        Lv_Like.adapter = adapter

                    }
                }
            }

        })
    }
}
