package com.travel.phuc.trung.tlcn.tlcn.Home.festivalVenues

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.ProgressDialog
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.view.MenuItem
import android.view.View
import android.widget.*

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.Polyline
import com.google.firebase.database.*
import com.travel.phuc.trung.tlcn.tlcn.Home.HomeRatingData
import com.travel.phuc.trung.tlcn.tlcn.Home.TouristAttraction.DeteiladAdaprerImage
import com.travel.phuc.trung.tlcn.tlcn.Home.TouristAttraction.HomeDistrictsData
import com.travel.phuc.trung.tlcn.tlcn.R
import kotlinx.android.synthetic.main.activity_festival_detail.*
import java.text.SimpleDateFormat
import com.travel.phuc.trung.tlcn.tlcn.GoogleMap.WorkaroundMapFragment
import com.travel.phuc.trung.tlcn.tlcn.Home.HomeActivityComment
import com.travel.phuc.trung.tlcn.tlcn.Home.HomeActivityLike
import com.travel.phuc.trung.tlcn.tlcn.Home.TouristAttraction.DeteiladActivityCreateSchedules
import com.travel.phuc.trung.tlcn.tlcn.Home.TouristAttraction.HomeActivityCheckDislike
import kotlinx.android.synthetic.main.activity_maps.*


class FestivalDetail : AppCompatActivity(), OnMapReadyCallback {

    private val sharedprperences : String="taikhoan";
    private var id_USER:String?=null

    private var tenDDDL: TextView?=null
    private var diaChi: TextView?=null
    private var moTa: TextView?=null
    private var btnLike: ImageButton?=null
    private var btnComment: ImageButton?=null
    private var xacsnhandanhgia: Button?=null
    private var soComment: TextView?= null
    private var btnYeuThich: ImageButton?=null
    private var danhGia: RatingBar?=null
    private var btn_Disklike : ImageButton?=null
    private var idLH:String=""
    private var Arraychild:ArrayList<HomeDistrictsData>?= ArrayList();
    private lateinit var mDateSetListenner: DatePickerDialog.OnDateSetListener
    private var ThemVaoLichTrinh: Button? = null
    private var  viepager: ViewPager?=null
    companion object {
        var listrating:ArrayList<HomeRatingData>?= ArrayList()
    }
    var database: DatabaseReference
    init {
        database    = FirebaseDatabase.getInstance().reference
    }

    private lateinit var mMap: GoogleMap
    private var originMarkers:ArrayList<Marker> = ArrayList()
    private var destinationMarkers:ArrayList<Marker> = ArrayList()
    private var polylinePaths:ArrayList<Polyline> = ArrayList()
    private var progressDialog: ProgressDialog? = null
    private var Lat:Double=1.0
    private var Long:Double=1.0
    private var tenDD:String = ""
    private var nhanTT : DataFestival? = null
    private lateinit var adapter: PagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_festival_detail)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        nhanTT = intent.getSerializableExtra("data") as DataFestival

        viepager=findViewById<ViewPager>(R.id.ViewPager_Hinhanh_LH_chitiet);
        ThemVaoLichTrinh = findViewById<Button>(R.id.ThemVaoLichTrinh_LH)
        btnYeuThich = findViewById(R.id.Btn_YeuThich_chitiet_LH)
        btnLike = findViewById(R.id.Bnt_like_chitiet_LH)
        tenDDDL=findViewById(R.id.Ten_DDLH_ChiTiet)
        tenDDDL!!.text =nhanTT!!.TenDiaDiem
        moTa=findViewById(R.id.mota_LH)
        moTa!!.text = nhanTT!!.Mota
        btn_Disklike = findViewById<ImageButton>(R.id.Btn_Dislike_chitiet_LH)

        idLH = nhanTT!!.key
        Lat = nhanTT!!.Lat
        Long = nhanTT!!.Long
        tenDD = nhanTT!!.TenDiaDiem
        setngayBD()
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map_LH) as WorkaroundMapFragment
        mapFragment.getMapAsync(this)
        mapFragment.setListener(object :WorkaroundMapFragment.OnTouchListener{
            override fun onTouch() {
                scrollmapLH.requestDisallowInterceptTouchEvent(true)
            }
        })
        addlistanh()
        loadmapType()
        addTimepicker()
        ktYeuThich()
        kiemtraLike()
    }

    private fun loadmapType() {
        val adapterLoaiMap: ArrayAdapter<String> = ArrayAdapter(this, android.R.layout.simple_spinner_item, resources.getStringArray(R.array.maps_type))
        adapterLoaiMap.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        loaimap_LH.adapter=adapterLoaiMap
        loaimap_LH!!.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                var type:Int =GoogleMap.MAP_TYPE_NORMAL;
                when (p2) {

                    1 ->{
                        type=GoogleMap.MAP_TYPE_NORMAL;
                    }
                    2 ->{
                        type=GoogleMap.MAP_TYPE_SATELLITE;
                    }
                    3 -> {
                        type=GoogleMap.MAP_TYPE_TERRAIN;
                    }
                    4 ->{
                        type=GoogleMap.MAP_TYPE_HYBRID;
                    }

                }
                mMap.mapType=type
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }
    }
// lấy d/s ảnh
    private fun addlistanh() {
        Arraychild!!.add(HomeDistrictsData(nhanTT!!.url))
        adapter = DeteiladAdaprerImage(this@FestivalDetail,Arraychild!!)
        adapter.notifyDataSetChanged()
        viepager!!.adapter=adapter
    }

// set ngày BD le hoi
    private fun setngayBD() {
        val sdf_date_bd : SimpleDateFormat = SimpleDateFormat("dd-MM-yyyy")
        val sdf_date_bd1 : SimpleDateFormat = SimpleDateFormat("dd-MM-yyyy")
        var ngaybd =sdf_date_bd.format(nhanTT!!.NgayBD)
        val ngaykt=sdf_date_bd1.format(nhanTT!!.NgayKT)
        ngayBDLH_detail.text = "ngày bắt đầu: ".plus( ngaybd.toString())
        ngayKTLH_detail.text = "ngày kết thúc: ".plus(ngaykt.toString())

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
    @SuppressLint("MissingPermission")
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Add a marker in Sydney and move the camera
        val sydney = LatLng(Lat, Long)
        Toast.makeText(this,Lat.toString(), Toast.LENGTH_LONG).show()
        mMap.addMarker(MarkerOptions().position(sydney).title(tenDD))
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney,17f))
//        val locationManager: LocationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
//            val criteria  = Criteria()
//            val lastLocation: Location =locationManager.getLastKnownLocation(locationManager.getBestProvider(criteria,false))
//            if (lastLocation!=null) {
//                val  latLng:LatLng=LatLng(lastLocation.getLatitude(), lastLocation.getLongitude())
//                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(LatLng(lastLocation.altitude, lastLocation.longitude), 17f))
//                val cameraPosition: CameraPosition? = CameraPosition.Builder()
//                        .target(LatLng(lastLocation.getLatitude(), lastLocation.getLongitude()))
//                        .zoom(17f)
//                        .bearing(90f)
//                        .tilt(40f)
//                        .build()
//                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
////                //Thêm MarketOption cho Map:
//                var option:MarkerOptions= MarkerOptions();
//                option.title("Vị Trí Của Bạn");
//                //option.snippet("Gần làng SOS");
//                option.position(latLng);
//                 var currentMarker:Marker= mMap.addMarker(option);
//                currentMarker.showInfoWindow();
//            }
//
//       etOrigin.append(lastLocation.accuracy.toString())

        mMap.setMyLocationEnabled(true);
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
    private fun addTimepicker() {
        ThemVaoLichTrinh!!.setOnClickListener({
            //            var a:frmDailogLichTrinh = frmDailogLichTrinh()
//            a.show(fragmentManager,"con heo")
            val intent = Intent(this@FestivalDetail, DeteiladActivityCreateSchedules::class.java)
            intent.putExtra("key",nhanTT!!.TenDiaDiem)
            this@FestivalDetail.startActivity(intent)
        })
        btn_Disklike!!.setOnClickListener {
            //setDislike()

        }
        btnLike!!.setOnClickListener {
            setlike()
        }
        btnYeuThich!!.setOnClickListener(){
            setyeuthich()
        }
        btn_Disklike!!.setOnClickListener {
            setDislike()
        }
        binhluan_DDDL_LH.setOnClickListener {
            val intent = Intent(this@FestivalDetail, HomeActivityComment::class.java)
            intent.putExtra("keyDL",nhanTT!!.key)
            intent.putExtra("key",1)
            this@FestivalDetail.startActivity(intent)
        }
        luoclikeDDDL_LH.setOnClickListener {
            val intent = Intent(this@FestivalDetail, HomeActivityLike::class.java)
            intent.putExtra("keyDDDL",nhanTT!!.key)
            this@FestivalDetail.startActivity(intent)
        }

    }
    // set người dùng dislike
    private fun setDislike() {
        if(doctaikhoan()){
            database.child("DisLike").child(idLH).child(id_USER).addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError?) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

                override fun onDataChange(p0: DataSnapshot?) {
                    if (p0!!.exists()){
                        btn_Disklike!!.setImageResource(R.drawable.dislike)
//                        var calender:Calendar= Calendar.getInstance()
                        database.child("DisLike").child(idLH).child(id_USER).removeValue()
                    }
                    else{
                        val intent = Intent(this@FestivalDetail, HomeActivityCheckDislike::class.java)
                        intent.putExtra("keyDL", idLH)
                        intent.putExtra("idUser", id_USER)
                        this@FestivalDetail.startActivity(intent)
                    }
                }

            })
        }
        else{
            Toast.makeText(this@FestivalDetail,"Bạn cần đăng nhập",Toast.LENGTH_SHORT).show()
        }
    }

    // set yêu thich khi click vào biểu tượng trái tim
    private fun setyeuthich() {

        if(doctaikhoan()){
            database.child("YeuThich").child(id_USER).child(idLH).addListenerForSingleValueEvent(object :ValueEventListener{
                override fun onCancelled(p0: DatabaseError?) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

                override fun onDataChange(p0: DataSnapshot?) {
                    if (p0!!.exists()){
                        btnYeuThich!!.setImageResource(R.drawable.chuayeuthich)
//                        var calender:Calendar= Calendar.getInstance()
                        database.child("YeuThich").child(id_USER).child(idLH).removeValue()
                        Toast.makeText(this@FestivalDetail,"Đã xóa khỏi mục yêu thích",Toast.LENGTH_LONG).show()
                    }
                    else{
                        btnYeuThich!!.setImageResource(R.drawable.tim)
                        database.child("YeuThich").child(id_USER).child(idLH).setValue(idLH)
                        Toast.makeText(this@FestivalDetail,"Đã thêm vào mục yêu thích",Toast.LENGTH_LONG).show()
                    }
                }

            })
        }
        else{
            Toast.makeText(this@FestivalDetail,"Bạn cần đăng nhập",Toast.LENGTH_SHORT).show()
        }

    }

    private fun setlike() {
        if(doctaikhoan()){
            database.child("Like").child(idLH).child(id_USER).addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError?) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

                override fun onDataChange(p0: DataSnapshot?) {
                    if (p0!!.exists()){
                        btnLike!!.setImageResource(R.drawable.like)
//                        var calender:Calendar= Calendar.getInstance()
                        database.child("Like").child(idLH).child(id_USER).removeValue()
                    }
                    else{
                        btnLike!!.setImageResource(R.drawable.liked)
                        database.child("Like").child(idLH).child(id_USER).setValue(id_USER)
                    }
                }

            })
        }
        else{
            Toast.makeText(this@FestivalDetail,"Bạn cần đăng nhập",Toast.LENGTH_SHORT).show()
        }
    }
    // kiem tra đã like chưa để set hinh anh cho imagebuuton like

    private fun kiemtraLike() {
        if (doctaikhoan())
        {
            database.child("Like").child(idLH).child(id_USER).addValueEventListener(object :ValueEventListener{
                override fun onCancelled(p0: DatabaseError?) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

                override fun onDataChange(p0: DataSnapshot?) {
                    if (p0!!.value!=null){
                        btnLike!!.setImageResource(R.drawable.liked)

                    }
//                    else
//                    {
//                        btnLike!!.setImageResource(R.drawable.like)
//                    }
                }

            })
        }
    }

    // kiểm tra địa điểm có trong danh sách yêu thich hay không
    private fun ktYeuThich() {
        if(doctaikhoan()){
            database.child("YeuThich").child(id_USER).child(idLH).addValueEventListener(object :ValueEventListener{
                override fun onCancelled(p0: DatabaseError?) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

                override fun onDataChange(p0: DataSnapshot?) {
                    if (p0!!.value!=null){
                        btnYeuThich!!.setImageResource(R.drawable.tim)

                    }
                }

            })
        }
        else{
            Toast.makeText(this@FestivalDetail,"Bạn cần đăng nhập",Toast.LENGTH_SHORT).show()
        }
    }
    fun doctaikhoan():Boolean {
        val sharedpreferences = this@FestivalDetail.getSharedPreferences(sharedprperences, android.content.Context.MODE_PRIVATE)
        id_USER = sharedpreferences.getString("Uid", null)

        if (id_USER != null && !id_USER!!.equals("")) {
            return true
            // truyen!!.truyenUser(uid,name,email,photoUrl)
        }
        return false
    }
}
