package com.travel.phuc.trung.tlcn.tlcn.ConfirmInformation

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.view.PagerAdapter
import android.view.FrameMetrics
import android.view.MenuItem
import android.widget.FrameLayout
import android.widget.ProgressBar
import com.google.firebase.database.*
import com.travel.phuc.trung.tlcn.tlcn.AddInfromation.ImageData
import com.travel.phuc.trung.tlcn.tlcn.R
import com.travel.phuc.trung.tlcn.tlcn.notifications.NotificationsData
import kotlinx.android.synthetic.main.activity_cofirm_image.*

class CofirmImage : AppCompatActivity() {
    val databaseRef : DatabaseReference = FirebaseDatabase.getInstance().reference
    var Arraychild:ArrayList<ImageData>?= ArrayList();
    private lateinit var adapter: ConfirmAdapterAlbum
    private val sharedprperences : String="taikhoan";
    var ten:String? =null
    var ten_email:String? = null
    var hinhDaiDien:String?=null
    private var id_USER :String?=null
    private var keyanh :String=""
    private var loai:Int =0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cofirm_image)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        adapter= ConfirmAdapterAlbum(this@CofirmImage,Arraychild!!)
        val intent = intent
        keyanh=intent.getStringExtra("keyAlbum")
        loai = intent.getIntExtra("loai",0)
        CAcapnhatanh.setOnClickListener({
            CAconfirm.visibility = FrameLayout.GONE
            tiengtrinhcapnhatalbum.visibility = ProgressBar.VISIBLE
            addanh()
        })
        if (keyanh!= null) {
            layanh()
        }

    }

    private fun addanh() {
        for (i in 0 until Arraychild!!.size) {
            databaseRef.child("AlbumAnhDuLich").child(keyanh).child(Arraychild!!.get(i).key).setValue(Arraychild!!.get(i).link)
            databaseRef.child("Tam").child("Album").child(keyanh.toString()).child(Arraychild!!.get(i).key).removeValue()
        }

        this.finish()
    }

    private fun layanh() {
        databaseRef.child("Tam").child("Album").child(keyanh).addChildEventListener(object :ChildEventListener{
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
                if (p0!=null) {
                    Arraychild!!.add(ImageData(p0!!.key.toString(),p0.value.toString(),1))
                    adapter.notifyDataSetChanged()
                    CAListanhxacnhan.adapter = adapter
                }
            }

            override fun onChildRemoved(p0: DataSnapshot?) {
                for (i in 0 until Arraychild!!.size)
                {
                    if (p0!!.key == Arraychild!!.get(i).key)
                    {
                        Arraychild!!.removeAt(i)
                        adapter.notifyDataSetChanged()
                        CAListanhxacnhan.adapter = adapter
                        break
                    }
                }
            }
        })
    }
    fun doctaikhoan():Boolean {
        val sharedpreferences=this.getSharedPreferences(sharedprperences,android.content.Context.MODE_PRIVATE)

        id_USER =sharedpreferences.getString("Uid",null)
        ten_email =sharedpreferences.getString("Uemail",null)
        hinhDaiDien=sharedpreferences.getString("UURLAnh",null)
        ten= sharedpreferences.getString("Uname",null)

        if (id_USER!=null){
            return true
            // truyen!!.truyenUser(uid,name,email,photoUrl)
        }
        return false
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
