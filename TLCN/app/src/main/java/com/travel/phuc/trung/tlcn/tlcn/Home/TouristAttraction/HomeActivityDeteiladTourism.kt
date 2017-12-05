package com.travel.phuc.trung.tlcn.tlcn.Home.TouristAttraction

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.Toast
import com.travel.phuc.trung.tlcn.tlcn.GoogleMap.MapsActivity
import com.travel.phuc.trung.tlcn.tlcn.R
// activity xem thông tin chi tiết địa điểm du lich
class HomeActivityDeteiladTourism : AppCompatActivity() {

    var btn_ThongTin: Button?=null
    var btn_QuanAn:Button?=null
    var btn_NoiO:Button?=null
    var nhanTT:HomeInformationTourisData? =null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_deteilad_tourism)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        btn_ThongTin=findViewById<Button>(R.id.btn_ThongTinCT)
        btn_QuanAn=findViewById<Button>(R.id.btn_QuanAn)
        btn_NoiO=findViewById<Button>(R.id.btn_NoiO)

        val intent = intent

        nhanTT = intent.getSerializableExtra("data") as HomeInformationTourisData
       // Toast.makeText(this,nhanTT!!.SoCmmt.toString(),Toast.LENGTH_SHORT).show()

        val bundle : Bundle = Bundle()
        bundle.putSerializable("data1", nhanTT)
        val TTCT = DeteiladFragmentInformationTourism()
        TTCT.arguments = bundle
        val fragmentManager = supportFragmentManager
        val transaction = fragmentManager.beginTransaction()
        transaction.replace(R.id.TTCT_DDDL, TTCT).commit()
        adfragment()
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater!!.inflate(R.menu.buttonactionbar,menu)
        return true
    }


    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        val id = item!!.itemId
        when (id) {
            R.id.map -> {
                val intent = Intent(this, MapsActivity::class.java)
                intent.putExtra("lat",nhanTT!!.Lat)
                intent.putExtra("long",nhanTT!!.Long)
                intent.putExtra("TenDD",nhanTT!!.TenDiaDiem)
                startActivity(intent)
            }
            android.R.id.home ->{
                finish()
            }

        }

        return true
    }
    private fun adfragment() {
//        btn_QuanAn!!.setOnClickListener({
//            val fragmentManager =supportFragmentManager
//            val transaction = fragmentManager.beginTransaction()
//            transaction.replace(R.id.TTCT_DDDL, DeteiladFragmentInformationTourism()).commit()
//        })
//        btn_ThongTin!!.setOnClickListener({
//            val bundle : Bundle = Bundle()
//            bundle.putSerializable("data1", nhanTT)
//            val TTCT = frm_thongtin_chitiet()
//            TTCT.arguments = bundle
//            val fragmentManager = supportFragmentManager
//            val transaction = fragmentManager.beginTransaction()
//            transaction.replace(R.id.TTCT_DDDL, TTCT).commit()
//        })
//        btn_NoiO!!.setOnClickListener({
//            val fragmentManager =supportFragmentManager
//            val transaction = fragmentManager.beginTransaction()
//            transaction.replace(R.id.TTCT_DDDL, frm_ThongTinNoiO()).commit()
//        })
    }
}
