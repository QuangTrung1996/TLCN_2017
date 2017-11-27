package com.travel.phuc.trung.tlcn.tlcn.Home.TouristAttraction

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.DisplayMetrics
import android.widget.Button
import android.widget.Toast
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.travel.phuc.trung.tlcn.tlcn.R
import kotlinx.android.synthetic.main.activity_home_check_dislike.*

class HomeActivityCheckDislike : AppCompatActivity() {
    var database: DatabaseReference
    init {
        database    = FirebaseDatabase.getInstance().reference
    }
    var keyDl:String?=null
    var idUser:String?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var thongtin = intent
        keyDl = thongtin.getStringExtra("keyDL")
        idUser = thongtin.getStringExtra("idUser")

        setContentView(R.layout.activity_home_check_dislike)
        supportActionBar!!.hide()
        val dm: DisplayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(dm)
        val width:Int = dm.widthPixels
        val height:Int = dm.heightPixels
        window.setLayout((width * .7).toInt(),(height * .3).toInt())
        HuyDislike.setOnClickListener {
            finish()
        }
        Dislike.setOnClickListener({
            setdisklike()
        })
    }

    private fun setdisklike() {
        database.child("DisLike").child(keyDl).child(idUser).setValue(idUser, DatabaseReference.CompletionListener { databaseError, databaseReference ->
            if (databaseError ==null){
                this@HomeActivityCheckDislike.finish()
            }
            else{
                Toast.makeText(this,"thử lại",Toast.LENGTH_LONG).show()
            }
        })
    }
}
