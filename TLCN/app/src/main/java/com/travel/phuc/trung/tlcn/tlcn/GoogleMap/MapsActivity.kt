package com.travel.phuc.trung.tlcn.tlcn.GoogleMap

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.view.View
import android.widget.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.firebase.database.*
import com.travel.phuc.trung.tlcn.tlcn.Home.HomeActivityComment
import com.travel.phuc.trung.tlcn.tlcn.Home.HomeActivityLike
import com.travel.phuc.trung.tlcn.tlcn.Home.HomeRatingData
import com.travel.phuc.trung.tlcn.tlcn.Home.TouristAttraction.*
import com.travel.phuc.trung.tlcn.tlcn.R
import docongphuc.pttravle.Maps.DirectionFinder
import docongphuc.pttravle.Maps.DirectionFinderListener
import docongphuc.pttravle.Maps.Route
import kotlinx.android.synthetic.main.activity_comfirn_information_touris.*
import kotlinx.android.synthetic.main.activity_festival_detail.*
import kotlinx.android.synthetic.main.activity_maps.*
import java.io.UnsupportedEncodingException

class MapsActivity : AppCompatActivity(), OnMapReadyCallback, DirectionFinderListener {

    private val sharedprperences : String="taikhoan";
    private var id_USER:String?=null

    var tenDDDL: TextView?=null
    var diaChi:TextView?=null
    var moTa:TextView?=null
    var btnLike: ImageButton?=null
    var btnComment: ImageButton?=null
    var xacsnhandanhgia: Button?=null
    var soComment:TextView?= null
    var btnYeuThich: ImageButton?=null
    var danhGia: RatingBar?=null
    var btn_Disklike : ImageButton?=null
    var idDL:String=""
    var Arraychild:ArrayList<HomeDistrictsData>?= ArrayList();
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
    private var nhanTT :HomeInformationTourisData? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        nhanTT = intent.getSerializableExtra("data") as HomeInformationTourisData

        viepager=findViewById<ViewPager>(R.id.ViewPager_Hinhanh_chitiet);
        ThemVaoLichTrinh = findViewById<Button>(R.id.ThemVaoLichTrinh)
        btnYeuThich = findViewById(R.id.Btn_YeuThich_chitiet)
        btnLike = findViewById(R.id.Bnt_like_chitiet)
        xacsnhandanhgia = findViewById(R.id.XacNhanDAnhGia)
//        TT= arguments.getSerializable("data1") as HomeInformationTourisData
//        anhxa(view)
        tenDDDL=findViewById(R.id.Ten_DDDL_ChiTiet)
        tenDDDL!!.text =nhanTT!!.TenDiaDiem
        moTa=findViewById(R.id.mota)
        moTa!!.text = nhanTT!!.Mota
        btn_Disklike = findViewById<ImageButton>(R.id.Btn_Dislike_chitiet)
        danhGia=findViewById(R.id.DanhGia_chitiet)
        Arraychild!!.add(HomeDistrictsData(nhanTT!!.url))
        var adapter: PagerAdapter = DeteiladAdaprerImage(this@MapsActivity,Arraychild!!)
        adapter.notifyDataSetChanged()
        viepager!!.adapter=adapter
        idDL = nhanTT!!.key
        addListAnh()
        ktYeuThich()
        kiemtraDislike()
        kiemtraLike()
        setRating()
        luoclikeDDDL.setOnClickListener {
            val intent = Intent(this@MapsActivity, HomeActivityLike::class.java)
            intent.putExtra("keyDDDL",nhanTT!!.key)
            this@MapsActivity.startActivity(intent)
        }
        binhluan_DDDL.setOnClickListener {
            val intent = Intent(this@MapsActivity, HomeActivityComment::class.java)
            intent.putExtra("keyDL",nhanTT!!.key.toString())
            intent.putExtra("key",1)
            this@MapsActivity.startActivity(intent)
        }
        btnYeuThich!!.setOnClickListener(){
            setyeuthich()
        }
        btnLike!!.setOnClickListener {
            setlike()
        }
        xacsnhandanhgia!!.setOnClickListener{
            themdanhgia()
        }
        ThemVaoLichTrinh!!.setOnClickListener({
            //            var a:frmDailogLichTrinh = frmDailogLichTrinh()
//            a.show(fragmentManager,"con heo")
            val intent = Intent(this@MapsActivity, DeteiladActivityCreateSchedules::class.java)
            this@MapsActivity.startActivity(intent)
        })
        btn_Disklike!!.setOnClickListener {
            setDislike()
        }
        addTimepicker()


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.

        Lat = nhanTT!!.Lat
        Long = nhanTT!!.Long
        tenDD = nhanTT!!.TenDiaDiem
//        val mapFragment = supportFragmentManager
//                .findFragmentById(R.id.map) as SupportMapFragment

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as WorkaroundMapFragment
        mapFragment.getMapAsync(this)
        mapFragment.setListener(object :WorkaroundMapFragment.OnTouchListener{
            override fun onTouch() {
                scrollmapDL.requestDisallowInterceptTouchEvent(true)
            }
        })


        btnFindPath.setOnClickListener(){
            sendRequest()
        }
        val adapterLoaiMap: ArrayAdapter<String> = ArrayAdapter(this, android.R.layout.simple_spinner_item, resources.getStringArray(R.array.maps_type))
        adapterLoaiMap.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        loaimap.adapter=adapterLoaiMap


        loaimap!!.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
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

    private fun sendRequest() {
        val origin = etOrigin.text.toString()
        val destination = etDestination.text.toString()
        if (origin.isEmpty()) {
            Toast.makeText(this, "Please enter origin address!", Toast.LENGTH_SHORT).show()
            return
        }
        if (destination.isEmpty()) {
            Toast.makeText(this, "Please enter destination address!", Toast.LENGTH_SHORT).show()
            return
        }

        try {
            DirectionFinder(this@MapsActivity, origin, destination).execute()
        } catch (e: UnsupportedEncodingException) {
            e.printStackTrace()
        }

    }


    @SuppressLint("MissingPermission")
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Add a marker in Sydney and move the camera
        val sydney = LatLng(Lat, Long)
        Toast.makeText(this,Lat.toString(),Toast.LENGTH_LONG).show()
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

    override fun onDirectionFinderStart() {
        progressDialog = ProgressDialog.show(this, "Please wait.",
                "Finding direction..!", true)

        if (originMarkers != null) {
            for (marker in originMarkers) {
                marker.remove()
            }
        }

        if (destinationMarkers != null) {
            for (marker in destinationMarkers) {
                marker.remove()
            }
        }

        if (polylinePaths != null) {
            for (polyline in polylinePaths) {
                polyline.remove()
            }
        }
    }

    override fun onDirectionFinderSuccess(routes: List<Route>) {
        progressDialog!!.dismiss()
        polylinePaths = ArrayList()
        originMarkers = ArrayList()
        destinationMarkers = ArrayList()

        for (route in routes) {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(route.startLocation, 16f))
            (findViewById<View>(R.id.tvDuration) as TextView).text = route.duration!!.text
            (findViewById<View>(R.id.tvDistance) as TextView).text = route.distance!!.text

            originMarkers!!.add(mMap.addMarker(MarkerOptions()
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.start_blue))
                    .title(route.startAddress)
                    .position(route.startLocation!!)))
            destinationMarkers!!.add(mMap.addMarker(MarkerOptions()
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.end_green))
                    .title(route.endAddress)
                    .position(route.endLocation!!)))

            val polylineOptions = PolylineOptions().geodesic(true).color(Color.BLUE).width(10f)

            for (i in route.points!!.indices)
                polylineOptions.add(route.points!![i])

            polylinePaths!!.add(mMap.addPolyline(polylineOptions))
        }
    }

    // bắt sự kiện chọn nut back trên thanh actionbar
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
            val intent = Intent(this@MapsActivity, DeteiladActivityCreateSchedules::class.java)
            intent.putExtra("key",nhanTT!!.TenDiaDiem)
            this@MapsActivity.startActivity(intent)
        })
        btn_Disklike!!.setOnClickListener {
            setDislike()

        }

    }
    private fun setDislike() {
        if(doctaikhoan()){
            database.child("DisLike").child(idDL).child(id_USER).addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError?) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

                override fun onDataChange(p0: DataSnapshot?) {
                    if (p0!!.exists()){
                        btn_Disklike!!.setImageResource(R.drawable.dislike)
//                        var calender:Calendar= Calendar.getInstance()
                        database.child("DisLike").child(idDL).child(id_USER).removeValue()
                    }
                    else{
                        val intent = Intent(this@MapsActivity, HomeActivityCheckDislike::class.java)
                        intent.putExtra("keyDL", idDL)
                        intent.putExtra("idUser", id_USER)
                        this@MapsActivity.startActivity(intent)
                    }
                }

            })
        }
        else{
            Toast.makeText(this@MapsActivity,"Bạn cần đăng nhập",Toast.LENGTH_SHORT).show()
        }
    }

    // lấy danh sach anh tu albul
    private fun addListAnh() {
        database.child("AlbumAnhDuLich").child(idDL).addChildEventListener(object : ChildEventListener {
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
                    Arraychild!!.add(HomeDistrictsData(p0!!.value.toString()))
                    var adapter:PagerAdapter= DeteiladAdaprerImage(this@MapsActivity,Arraychild!!)
                    adapter.notifyDataSetChanged()
                    CFViewPager_Hinhanh_chitiet!!.adapter=adapter  }

            }

            override fun onChildRemoved(p0: DataSnapshot?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

        })
    }
    // kiểm tra địa điểm có trong danh sách yêu thich hay không
    private fun ktYeuThich() {
        if(doctaikhoan()){
            database.child("YeuThich").child(id_USER).child(idDL).addValueEventListener(object :ValueEventListener{
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
            Toast.makeText(this@MapsActivity,"Bạn cần đăng nhập",Toast.LENGTH_SHORT).show()
        }
    }
    // kiểm tra địa điểm có trong bảng dislike khôgn
    private fun kiemtraDislike() {
        if (doctaikhoan())
        {
            database.child("DisLike").child(idDL).child(id_USER).addValueEventListener(object :ValueEventListener{
                override fun onCancelled(p0: DatabaseError?) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

                override fun onDataChange(p0: DataSnapshot?) {
                    if (p0!!.value!=null){
                        btn_Disklike!!.setImageResource(R.drawable.disliked)

                    }
//                    else
//                    {
//                        btn_Disklike!!.setImageResource(R.drawable.dislike)
//                    }
                }

            })
        }
    }
    // kiểm tra địa điểm có trong bản like hay không
    private fun kiemtraLike() {
        if (doctaikhoan())
        {
            database.child("Like").child(idDL).child(id_USER).addValueEventListener(object :ValueEventListener{
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
    // tính đánh giá của địa điểm
    private fun setRating() {
        var sum=0.0
        var i=0
        database.child("DanhGia").child(idDL).addChildEventListener(object : ChildEventListener{
            override fun onCancelled(p0: DatabaseError?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onChildMoved(p0: DataSnapshot?, p1: String?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onChildChanged(p0: DataSnapshot?, p1: String?) {
                var sum =0.0;
                for (i in 0 until listrating!!.size)
                {
                    if (p0!!.key ==listrating!!.get(i).key)
                    {
                        var a = p0!!.value as String
                        listrating!!.get(i).danhgia = a.toFloat()

                    }
                    sum+= listrating!!.get(i).danhgia
                }
                //
                var tb = Math.round((sum/ listrating!!.size)*10)/10.toFloat()
                danhGia!!.rating = tb

            }

            override fun onChildAdded(p0: DataSnapshot?, p1: String?) {
                var a = p0!!.value as String
                i++
                sum  = sum + a.toDouble()
                listrating!!.add(HomeRatingData(p0!!.key,a.toFloat()))
                var tam = sum /i
                var TB =(Math.round(tam*10))/10.toFloat()
                danhGia!!.rating = TB
            }

            override fun onChildRemoved(p0: DataSnapshot?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

        })
    }
    // thêm địa điểm vào yêu thích khi click like
    private fun setyeuthich() {

        if(doctaikhoan()){
            database.child("YeuThich").child(id_USER).child(idDL).addListenerForSingleValueEvent(object :ValueEventListener{
                override fun onCancelled(p0: DatabaseError?) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

                override fun onDataChange(p0: DataSnapshot?) {
                    if (p0!!.exists()){
                        btnYeuThich!!.setImageResource(R.drawable.chuayeuthich)
//                        var calender:Calendar= Calendar.getInstance()
                        database.child("YeuThich").child(id_USER).child(idDL).removeValue()
                        Toast.makeText(this@MapsActivity,"Đã xóa khỏi mục yêu thích",Toast.LENGTH_LONG).show()
                    }
                    else{
                        btnYeuThich!!.setImageResource(R.drawable.tim)
                        database.child("YeuThich").child(id_USER).child(idDL).setValue(idDL)
                        Toast.makeText(this@MapsActivity,"Đã thêm vào mục yêu thích",Toast.LENGTH_LONG).show()
                    }
                }

            })
        }
        else{
            Toast.makeText(this@MapsActivity,"Bạn cần đăng nhập",Toast.LENGTH_SHORT).show()
        }

    }
    // set like khi click vào image like
    private fun setlike() {
        if(doctaikhoan()){
            database.child("Like").child(idDL).child(id_USER).addListenerForSingleValueEvent(object :ValueEventListener{
                override fun onCancelled(p0: DatabaseError?) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

                override fun onDataChange(p0: DataSnapshot?) {
                    if (p0!!.exists()){
                        btnLike!!.setImageResource(R.drawable.like)
//                        var calender:Calendar= Calendar.getInstance()
                        database.child("Like").child(idDL).child(id_USER).removeValue()
                    }
                    else{
                        btnLike!!.setImageResource(R.drawable.liked)
                        database.child("Like").child(idDL).child(id_USER).setValue(id_USER)
                    }
                }

            })
        }
        else{
            Toast.makeText(this@MapsActivity,"Bạn cần đăng nhập",Toast.LENGTH_SHORT).show()
        }
    }
    private fun themdanhgia() {
        database.child("DanhGia").child(idDL).child(id_USER).setValue(danhGia!!.rating.toString())
    }


    //dọc tài khoản từ album
    fun doctaikhoan():Boolean {
        val sharedpreferences = this@MapsActivity.getSharedPreferences(sharedprperences, android.content.Context.MODE_PRIVATE)
        id_USER = sharedpreferences.getString("Uid", null)

        if (id_USER != null && !id_USER!!.equals("")) {
            return true
            // truyen!!.truyenUser(uid,name,email,photoUrl)
        }
        return false
    }


}
