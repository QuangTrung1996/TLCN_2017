package com.travel.phuc.trung.tlcn.tlcn.ConfirmInformation

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.view.PagerAdapter
import android.widget.FrameLayout
import android.widget.ProgressBar
import android.widget.Toast
import com.bumptech.glide.Glide

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.database.*
import com.travel.phuc.trung.tlcn.tlcn.Home.TouristAttraction.GetDataTourist
import com.travel.phuc.trung.tlcn.tlcn.Home.festivalVenues.getDataFestival
import com.travel.phuc.trung.tlcn.tlcn.R
import com.travel.phuc.trung.tlcn.tlcn.notifications.NotificationsData
import kotlinx.android.synthetic.main.activity_add_information_festival.*
import kotlinx.android.synthetic.main.activity_comfirn_information_touris.*
import kotlinx.android.synthetic.main.activity_confirn_information_festival.*
import java.text.SimpleDateFormat
import java.util.*

class ConfirnInformationFestival : AppCompatActivity(), OnMapReadyCallback {
    val databaseRef : DatabaseReference = FirebaseDatabase.getInstance().reference
    private var arrTheloai:IntArray = intArrayOf(0,0,0,0,0,0,0)
    private var giave:Int = 0
    private var Lat:Double =1.0
    private var Long:Double = 1.0
    private var key = ""
    private var tenDD:String = ""
    private var ttLH:getDataFestival = getDataFestival()
    var Arraychild:ArrayList<DataListImage>?= ArrayList();
    private lateinit var adapter: PagerAdapter
    private val sharedprperences : String="taikhoan";
    var ten:String? =null
    var ten_email:String? = null
    var hinhDaiDien:String?=null
    private var id_USER :String?=null
    private lateinit var mMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_confirn_information_festival)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        CFAddTenDDLH.isEnabled = false
        CFAddDiachiLH.isEnabled = false
        CFAddMotaLH.isEnabled = false
        var intent = getIntent()
        adapter= AdapterImage(this@ConfirnInformationFestival,Arraychild!!)
        key = intent.getStringExtra("key")
        doctaikhoan()
        if (key != "" && doctaikhoan()) {
            docthongtin()
            layanh();
//            xacnhan()
        }
        else{
            CFconfirmLH.visibility = FrameLayout.GONE
        }
        xacnhantt()
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.CFmapLH) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    private fun xacnhantt() {
        CFkhongxacnhanLH.setOnClickListener {
            CFaddtientrinhcapnhat.visibility = ProgressBar.VISIBLE
            if (CFthongtinphanhoiLH.text.toString()!="" && CFthongtinphanhoiLH !=null)
            {
                val time = System.currentTimeMillis()
                var ttCom = NotificationsData(ten!!, hinhDaiDien!!, CFthongtinphanhoiLH.text.toString(), time)
                databaseRef.child("Notification").child(ttLH.idUser).child(time.toString()).setValue(ttCom)
                finish()
            }
            else
            {
                val time = System.currentTimeMillis()
                var ttCom = NotificationsData(ten!!, hinhDaiDien!!, "không nhận địa điểm :".plus(ttLH.TenLeHoi), time)
                databaseRef.child("Notification").child(ttLH.idUser).child(time.toString()).setValue(ttCom)
                finish()
            }
        }
        CFAddcapnhatTTLH.setOnClickListener({
            CFaddtientrinhcapnhatLH.visibility = ProgressBar.VISIBLE
            databaseRef.child("DiaDiemLeHoi").child(key).setValue(ttLH, DatabaseReference.CompletionListener { databaseError, databaseReference ->
                if (databaseError ==null)
                {
                    for (i in 0 until Arraychild!!.size) {
                        databaseRef.child("AlbumAnhLeHoi").child(key).child(Arraychild!!.get(i).keyanh).setValue(Arraychild!!.get(i).linkanh)
                        databaseRef.child("Tam").child("Album").child(key).child(Arraychild!!.get(i).keyanh).removeValue()
                    }
                    databaseRef.child("Tam").child("DiaDiemLH").child(key).removeValue(DatabaseReference.CompletionListener { databaseError, databaseReference ->
                        if (databaseError ==null)
                        {
                            val time = System.currentTimeMillis()
                            var ttCom = NotificationsData(ten!!, hinhDaiDien!!, "đã xác nhận địa điểm :".plus(ttLH.TenLeHoi), time)
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
            override fun onCancelled(p0: DatabaseError?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(p0: DataSnapshot?) {
                val data: getDataFestival? = p0!!.getValue(getDataFestival::class.java)
                ttLH=if(data != null) data else throw NullPointerException("Expression 'data' must not be null")
                CFAddTenDDLH.append(data!!.TenLeHoi)
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
                    Arraychild!!.add(DataListImage(p0.key.toString(),p0.value.toString()))
                    adapter.notifyDataSetChanged()
                    CFViewPager_Hinhanh_chitietLH.adapter = adapter

                }
            }

            override fun onChildRemoved(p0: DataSnapshot?) {
                if (p0 != null)
                {
                    for (i in 0 until Arraychild!!.size)
                    {
                        if (p0!!.key == Arraychild!!.get(i).keyanh)
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
            // truyen!!.truyenUser(uid,name,email,photoUrl)
        }
        return false
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Add a marker in Sydney and move the camera
        val sydney = LatLng(Lat, Long)
        //Toast.makeText(this,Lat.toString(), Toast.LENGTH_LONG).show()
        mMap.addMarker(MarkerOptions().position(sydney).title(tenDD))
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney,14f))
    }
}
