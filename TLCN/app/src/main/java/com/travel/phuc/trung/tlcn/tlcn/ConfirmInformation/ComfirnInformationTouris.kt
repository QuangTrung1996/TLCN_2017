package com.travel.phuc.trung.tlcn.tlcn.ConfirmInformation

import android.content.Intent
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.view.PagerAdapter
import android.view.MenuItem
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.Toast

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.database.*
import com.travel.phuc.trung.tlcn.tlcn.Home.TouristAttraction.DeteiladAdaprerImage
import com.travel.phuc.trung.tlcn.tlcn.Home.TouristAttraction.GetDataTourist
import com.travel.phuc.trung.tlcn.tlcn.Home.TouristAttraction.HomeDistrictsData
import com.travel.phuc.trung.tlcn.tlcn.Home.festivalVenues.getDataFestival
import com.travel.phuc.trung.tlcn.tlcn.R
import kotlinx.android.synthetic.main.activity_add_infromation_tourist.*
import kotlinx.android.synthetic.main.activity_comfirn_information_touris.*
import kotlinx.android.synthetic.main.confirm_information_row.*

class ComfirnInformationTouris : AppCompatActivity(), OnMapReadyCallback {
    val databaseRef : DatabaseReference = FirebaseDatabase.getInstance().reference
    private var arrTheloai:IntArray = intArrayOf(0,0,0,0,0,0,0)
    private var giave:Int = 0
    private var Lat:Double =1.0
    private var Long:Double = 1.0
    private var key = ""
    private var tenDD:String = ""
    private var tt: GetDataTourist = GetDataTourist()
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
        setContentView(R.layout.activity_comfirn_information_touris)
        CFAddDiachi.isEnabled = false
        CFAddTenDD.isEnabled = false
        CFAddMota.isEnabled = false
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        var intent = getIntent()
        adapter= AdapterImage(this@ComfirnInformationTouris,Arraychild!!)
        key = intent.getStringExtra("key")
        doctaikhoan()
        if (key != "" && doctaikhoan()) {
            docthongtin()
            layanh();
            xacnhan()
        }
        else{
            CFconfirm.visibility = FrameLayout.GONE
        }
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.CFmap) as SupportMapFragment
        mapFragment.getMapAsync(this)
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

    private fun xacnhan() {
        CFkhongxacnhan.setOnClickListener {
            if (CFthongtinphanhoi.text.toString()!="" &&CFthongtinphanhoi.text.toString()!=null)
            {
                finish()
            }
            else
            {
                Toast.makeText(this@ComfirnInformationTouris,"cần gởi thông tin phản hồi",Toast.LENGTH_LONG).show()
            }
        }
        CFAddcapnhatTT.setOnClickListener {
            CFaddtientrinhcapnhat.visibility = ProgressBar.VISIBLE
            databaseRef.child("DiadiemDuLich").child(key).setValue(tt, DatabaseReference.CompletionListener { databaseError, databaseReference ->
                if (databaseError == null) {
                    for (i in 0 until arrTheloai.size) {
                        if (arrTheloai.get(i) == 1) {
                            databaseRef.child("TheLoai").child(i.toString()).child(key).setValue(0, DatabaseReference.CompletionListener { databaseError, databaseReference ->
                                if (databaseError == null) {
                                    databaseRef.child("Tam").child("TheLoai").child(key).child(i.toString()).child(key).removeValue()
                                }

                            })
                        } else {
                            databaseRef.child("TheLoai").child(i.toString()).child(key).removeValue()
                        }
                    }
                    if (arrTheloai.get(2) == 1) {
                        databaseRef.child("BanVe").child(key).setValue(docgiave(), DatabaseReference.CompletionListener { databaseError, databaseReference ->
                            if (databaseError == null) {
                                databaseRef.child("Tam").child("GiaVe").child(key).removeValue()
                            }
                        })
                    }
                    for (i in 0 until Arraychild!!.size) {
                        databaseRef.child("AlbumAnhDuLich").child(Arraychild!!.get(i).keyanh).setValue(Arraychild!!.get(i).linkanh)
                    }
                    databaseRef.child("Tam").child("DiaDiemDL").child(key).removeValue(DatabaseReference.CompletionListener { databaseError, databaseReference ->
                        if (databaseError ==null)
                        {
                            val time = System.currentTimeMillis()
                            databaseRef.child("Notification").child(id_USER).child(time.toString()).setValue("testthooi")
                            finish()
                        }
                    })
                }
            })
        }
    }

    // dọc gia ve
    private fun docgiave():Int{
        var gia=addgiave.text.toString()
        giave = gia.toInt()
        return giave
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
                    CFViewPager_Hinhanh_chitiet.adapter = adapter

                }
            }

            override fun onChildRemoved(p0: DataSnapshot?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

        })
    }

    private fun docthongtin() {
        databaseRef.child("Tam").child("DiaDiemDL").child(key).addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onCancelled(p0: DatabaseError?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(p0: DataSnapshot?) {
                if (p0!=null) {
                    val data: GetDataTourist? = p0!!.getValue(GetDataTourist::class.java)
                    tt = if (data != null) data else throw NullPointerException("Expression 'data' must not be null")
                    CFAddMota.append(data.MoTa)
                    CFAddDiachi.append(data.DiaChi)
                    CFAddTenDD.append(data.tenDiaDiem)
                    Lat =tt.Lat
                    Long = tt.Long
                    tenDD = tt.tenDiaDiem
                }
            }
        })
        doctheloai()
    }
    private fun doctheloai() {
        for (i in 0..6) {
            databaseRef.child("Tam").child("TheLoai").child(key).child(i.toString()).child(key).addListenerForSingleValueEvent(object :ValueEventListener{
                override fun onCancelled(p0: DatabaseError?) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

                override fun onDataChange(p0: DataSnapshot?) {
                    if (p0!!.value !=null)
                    {
                        //Toast.makeText(this@ChangeInformationTourist,i.toString(),Toast.LENGTH_SHORT).show()
                        arrTheloai[i]=1
                        checktheloai(i)

                    }
//                    else
//                    {
//                        Toast.makeText(this@ChangeInformationTourist,"no",Toast.LENGTH_SHORT).show()
//
//                    }
                }
            })
        }

    }
    private fun checktheloai(i:Int) {
        when (i){
            0->{
                CFadduudai.setTextColor(Color.BLUE)
            }
            1->{
                CFaddtudo.setTextColor(Color.BLUE)
            }
            2->{
                CFaddbanve.setTextColor(Color.BLUE)
                CFaddkhunbanggia.visibility = LinearLayout.VISIBLE
                themve()
            }
            3->{
                CFaddbaotang.setTextColor(Color.BLUE)
            }
            4->{
                CFaddditich.setTextColor(Color.BLUE)
            }
            5->{
                CFaddcongvien.setTextColor(Color.BLUE)
            }
            6->{
                CFaddthamhiem.setTextColor(Color.BLUE)
            }
        }
    }
    private fun themve() {
        databaseRef.child("Tam").child("GiaVe").child(key).addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onCancelled(p0: DatabaseError?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(p0: DataSnapshot?) {
                if (p0!!.value !=null && p0.value !=""){
                    CFaddgiave.append(p0.value.toString())
                }
            }
        })    }

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
        Toast.makeText(this,Lat.toString(), Toast.LENGTH_LONG).show()
        mMap.addMarker(MarkerOptions().position(sydney).title(tenDD))
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney,14f))
    }
}
