package com.travel.phuc.trung.tlcn.tlcn.Home

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import com.google.firebase.database.*
import com.travel.phuc.trung.tlcn.tlcn.Home.TouristAttraction.DeteiladAdaprerImage
import com.travel.phuc.trung.tlcn.tlcn.Home.TouristAttraction.HomeDistrictsData
import com.travel.phuc.trung.tlcn.tlcn.R
import kotlinx.android.synthetic.main.activity_home_image.*

class HomeActivityImage : AppCompatActivity() {
//    var rclv:RecyclerView?=null
    var database: DatabaseReference
    init {
        database    = FirebaseDatabase.getInstance().reference
    }
    var ArraychildImage:ArrayList<HomeDistrictsData>?= ArrayList();
    private var  viepager: ViewPager?=null
private var iddl:String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_image)
        supportActionBar!!.hide()
       viepager=findViewById<ViewPager>(R.id.viewpageralbumanh);
//        rclv =findViewById<RecyclerView>(R.id.rcLvAnh)
        var intent = intent
        iddl = intent.getStringExtra("IDdl")
        var url = intent.getStringExtra("urlanh")

        //Toast.makeText(this,url,Toast.LENGTH_SHORT).show()
//        ArraychildImage!!.add(HomeDistrictsData(url))
//        var adapter: PagerAdapter = DeteiladAdaprerImage(this,ArraychildImage!!)
//       adapter.notifyDataSetChanged()
//        viepager!!.adapter=adapter
        addListAnh()
        donganh.setOnClickListener {
            finish()
        }
    }
//
    private fun addListAnh() {
        database.child("AlbumAnhDuLich").child(iddl).addChildEventListener(object : ChildEventListener {
            override fun onCancelled(p0: DatabaseError?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onChildMoved(p0: DataSnapshot?, p1: String?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onChildChanged(p0: DataSnapshot?, p1: String?) {

            }

            override fun onChildAdded(p0: DataSnapshot?, p1: String?) {
                if (p0!=null){
                    ArraychildImage!!.add(HomeDistrictsData(p0!!.value.toString()))
                    var adapter: PagerAdapter = DeteiladAdaprerImage(this@HomeActivityImage,ArraychildImage!!)
                    adapter.notifyDataSetChanged()
                    viepager!!.adapter=adapter

//                    rclv!!.layoutManager = LinearLayoutManager(this@HomeActivityImage ,LinearLayoutManager.HORIZONTAL ,false)
//                    rclv!!.adapter = adapter
//                    var adapter: RecrycleListViewAdapter = RecrycleListViewAdapter(this@HomeActivityImage,ArraychildImage!!)
                      //Toast.makeText(this@HomeActivityImage,p0.key.toString(),Toast.LENGTH_SHORT).show()
                }

            }

            override fun onChildRemoved(p0: DataSnapshot?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

        })
    }
}

