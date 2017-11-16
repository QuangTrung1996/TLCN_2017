package com.travel.phuc.trung.tlcn.tlcn.GoogleMap

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.travel.phuc.trung.tlcn.tlcn.R
import docongphuc.pttravle.Maps.DirectionFinder
import docongphuc.pttravle.Maps.DirectionFinderListener
import docongphuc.pttravle.Maps.Route
import kotlinx.android.synthetic.main.activity_maps.*
import java.io.UnsupportedEncodingException

class MapsActivity : AppCompatActivity(), OnMapReadyCallback, DirectionFinderListener {

    private lateinit var mMap: GoogleMap
    private var originMarkers:ArrayList<Marker> = ArrayList()
    private var destinationMarkers:ArrayList<Marker> = ArrayList()
    private var polylinePaths:ArrayList<Polyline> = ArrayList()
    private var progressDialog: ProgressDialog? = null
    private var Lat:Double=1.0
    private var Long:Double=1.0
    private var tenDD:String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        var TT = intent
        Lat = TT.getDoubleExtra("lat",1.0)
        Long = TT.getDoubleExtra("long",1.0)
        tenDD = TT.getStringExtra("TenDD")
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
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
}
