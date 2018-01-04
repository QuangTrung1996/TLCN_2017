package com.travel.phuc.trung.tlcn.tlcn.managers.festivals

import android.os.Bundle
import android.support.v4.view.PagerAdapter
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.widget.FrameLayout
import android.widget.ProgressBar
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.database.*
import com.travel.phuc.trung.tlcn.tlcn.Home.festivalVenues.getDataFestival
import com.travel.phuc.trung.tlcn.tlcn.R
import com.travel.phuc.trung.tlcn.tlcn.notifications.NotificationsData
import kotlinx.android.synthetic.main.activity_comfirn_information_touris.*
import kotlinx.android.synthetic.main.activity_confirn_information_festival.*
import java.text.SimpleDateFormat
import java.util.*

class ManagerFestivalConfirmInformation : AppCompatActivity(), OnMapReadyCallback {
    val databaseRef : DatabaseReference = FirebaseDatabase.getInstance().reference

    private var Lat  : Double = 0.0
    private var Long : Double = 0.0
    private var key  = ""
    private var tenDD:String = ""
    private var ttLH:getDataFestival = getDataFestival()
    private var Arraychild:ArrayList<ManagerFestivalImageData>?= ArrayList();
    private lateinit var adapter: PagerAdapter
    private val sharedprperences : String="taikhoan"
    private var ten:String? =null
    private var ten_email:String? = null
    private var hinhDaiDien:String?=null
    private var id_USER :String?=null
    private lateinit var mMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_confirn_information_festival)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        CFAddTenDDLH.isEnabled = false
        CFAddDiachiLH.isEnabled = false
        CFAddMotaLH.isEnabled = false
        val intent = getIntent()
        adapter= ManagerFestivalImageAdapter(this@ManagerFestivalConfirmInformation,Arraychild!!)
        key = intent.getStringExtra("key")
        doctaikhoan()
        if (key != "" && doctaikhoan()) {
            docthongtin()
            layanh()

            val mapFragment = supportFragmentManager.findFragmentById(R.id.CFmapLH) as SupportMapFragment
            mapFragment.getMapAsync(this)
        }
        else{
            CFconfirmLH.visibility = FrameLayout.GONE
        }
        xacnhantt()
    }

    private fun xacnhantt() {
        CFkhongxacnhanLH.setOnClickListener {
            CFconfirmLH.visibility = FrameLayout.GONE
            CFaddtientrinhcapnhat.visibility = ProgressBar.VISIBLE
            if (CFthongtinphanhoiLH.text.toString()!="" && CFthongtinphanhoiLH !=null)
            {
                val time = System.currentTimeMillis()
                val ttCom = NotificationsData(ten!!, hinhDaiDien!!, CFthongtinphanhoiLH.text.toString(), time)
                databaseRef.child("Notification").child(ttLH.idUser).child(time.toString()).setValue(ttCom)
                finish()
            }
            else
            {
                val time = System.currentTimeMillis()
                val ttCom = NotificationsData(ten!!, hinhDaiDien!!, "không nhận địa điểm :".plus(ttLH.TenLeHoi), time)
                databaseRef.child("Notification").child(ttLH.idUser).child(time.toString()).setValue(ttCom)
                finish()
            }
        }
        CFAddcapnhatTTLH.setOnClickListener({
            CFconfirmLH.visibility = FrameLayout.GONE
            CFaddtientrinhcapnhatLH.visibility = ProgressBar.VISIBLE
            databaseRef.child("DiaDiemLeHoi").child(key).setValue(ttLH, DatabaseReference.CompletionListener { databaseError, databaseReference ->
                if (databaseError ==null)
                {
                    for (i in 0 until Arraychild!!.size) {
                        databaseRef.child("AlbumAnhLeHoi").child(key).child(Arraychild!![i].key).setValue(Arraychild!![i].link)
                        databaseRef.child("Tam").child("Album").child(key).child(Arraychild!!.get(i).key).removeValue()
                    }
                    databaseRef.child("Tam").child("DiaDiemLH").child(key).removeValue(DatabaseReference.CompletionListener { databaseError, databaseReference ->
                        if (databaseError ==null)
                        {
                            val time = System.currentTimeMillis()
                            val ttCom = NotificationsData(ten!!, hinhDaiDien!!, "đã xác nhận địa điểm :".plus(ttLH.TenLeHoi), time)
                            databaseRef.child("Notification").child(ttLH.idUser.toString()).child(time.toString()).setValue(ttCom)
                            finish()
                        }
                    })
                }
            })
        })
    }

    private fun docthongtin() {
        databaseRef.child("Tam").child("DiaDiemLH").child(key).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError?) {}

            override fun onDataChange(p0: DataSnapshot?) {
                val data: getDataFestival? = p0!!.getValue(getDataFestival::class.java)
                ttLH=if(data != null) data else throw NullPointerException("Expression 'data' must not be null")
                CFAddTenDDLH.append(data.TenLeHoi)
                CFAddMotaLH.append(data.MoTa)
                CFAddDiachiLH.append(data.DiaChi)
                val sdf_date : SimpleDateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
                val sdf_time : SimpleDateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())

                val Tgbd = sdf_time.format(data.NgayBD)
                val NgBD=sdf_date.format(data.NgayBD)
                val Tgkt = sdf_time.format(data.NgayKT)
                val Ngkt=sdf_date.format(data.NgayKT)

                CFAddgioBDLH.text = Tgbd.toString()
                CFAddgioKTLH.text = Tgkt.toString()
                CFAddngayBDLH.text = NgBD.toString()
                CFAddngayKTLH.text = Ngkt.toString()

                Lat =ttLH.Lat
                Long = ttLH.Long
                tenDD = ttLH.TenLeHoi
            }

        })
    }
    private fun layanh() {
        databaseRef.child("Tam").child("Album").child(key).addChildEventListener(object :ChildEventListener{
            override fun onCancelled(p0: DatabaseError?) {}
            override fun onChildMoved(p0: DataSnapshot?, p1: String?) {}
            override fun onChildChanged(p0: DataSnapshot?, p1: String?) {}

            override fun onChildAdded(p0: DataSnapshot?, p1: String?) {
                if (p0!=null)
                {
                    Arraychild!!.add(ManagerFestivalImageData(p0.key.toString(),p0.value.toString()))
                    adapter.notifyDataSetChanged()
                    CFViewPager_Hinhanh_chitietLH.adapter = adapter

                }
            }

            override fun onChildRemoved(p0: DataSnapshot?) {
                if (p0 != null)
                {
                    for (i in 0 until Arraychild!!.size)
                    {
                        if (p0.key == Arraychild!![i].key)
                        {
                            Arraychild!!.removeAt(i)
                            adapter.notifyDataSetChanged()
                        }
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

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        val sydney = LatLng(Lat, Long)
        mMap.addMarker(MarkerOptions().position(sydney).title(tenDD))
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney,14f))
    }
}
