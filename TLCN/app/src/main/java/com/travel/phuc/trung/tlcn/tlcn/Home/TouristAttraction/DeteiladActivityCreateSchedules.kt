package com.travel.phuc.trung.tlcn.tlcn.Home.TouristAttraction

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.media.MediaPlayer
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.travel.phuc.trung.tlcn.tlcn.R
import java.util.*

// class này để tạo lịch trình
class DeteiladActivityCreateSchedules : AppCompatActivity() {

    private val sharedprperences : String="taikhoan";
    private val KEY_ID_USER = "ID_User"
    private var id_USER :String?=null
    private var ten:String? =null
    private var ten_email:String? = null
    private var hinhDaiDien:String?=null

    private var EDT_tieude: EditText?=null
    private var EDT_ghichu:EditText?=null
    private var btnCapNhat: Button?=null
    private var btnNgayBD:Button?=null
    private var btnNgayKT:Button? = null
    private var btnGioBD:Button?=null
    private var btnGioKT:Button?=null
    val database : DatabaseReference
    init {
        database    = FirebaseDatabase.getInstance().reference
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_deteilad_create_schedules)
        EDT_ghichu = findViewById<EditText>(R.id.nghichu)
        EDT_tieude = findViewById<EditText>(R.id.tieude)
        btnNgayBD = findViewById<Button>(R.id.ngayBD)
        btnNgayKT = findViewById<Button>(R.id.ngayKT)
        btnGioBD = findViewById<Button>(R.id.gioBD)
        btnGioKT = findViewById<Button>(R.id.gioKT)
        btnCapNhat = findViewById<Button>(R.id.CapNhat)
        val calender: Calendar = Calendar.getInstance()
        val thang:Int = calender.get(Calendar.MONTH) +1
        val Gio=calender.get(Calendar.HOUR_OF_DAY).toString() +":"+ calender.get(Calendar.MINUTE).toString()
        val Ngay = calender.get(Calendar.DAY_OF_MONTH).toString()+"/" + thang+"/" + calender.get(Calendar.YEAR).toString()
        btnGioKT!!.text = Gio
        btnGioBD!!.text =  Gio
        btnNgayKT!!.text=Ngay
        btnNgayBD!!.text = Ngay
        addNgayGio(this)
        var tt = intent
        var tendd:String= tt.getStringExtra("key")
        EDT_tieude!!.append(tendd)
        val btnHuy:Button=findViewById(R.id.HuyLT)
        supportActionBar!!.hide()
        val dm: DisplayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(dm)
        val width:Int = dm.widthPixels
        val height:Int = dm.heightPixels
        window.setLayout((width * .9).toInt(),(height * .5).toInt())
        btnHuy.setOnClickListener {
            finish()
        }
    }
    // tạo dữ liệu thwm vào lịch trình
    private fun addNgayGio(context: Context) {
        val calender: Calendar = Calendar.getInstance()
        var gio = calender.get(Calendar.HOUR_OF_DAY)
        var phut = calender.get(Calendar.MINUTE)
        var ngay = calender.get(Calendar.DAY_OF_MONTH)
        var thang = calender.get(Calendar.MONTH)
        var nam = calender.get(Calendar.YEAR)
        var giokt = calender.get(Calendar.HOUR_OF_DAY)
        var phutkt = calender.get(Calendar.MINUTE)
        var ngaykt = calender.get(Calendar.DAY_OF_MONTH)
        var thangkt = calender.get(Calendar.MONTH)
        var namkt = calender.get(Calendar.YEAR)
        btnNgayBD!!.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View?) {
                val dpd = DatePickerDialog(context, DatePickerDialog.OnDateSetListener { view_, year, monthOfYear, dayOfMonth ->
                    nam = year
                    thang = monthOfYear+1
                    ngay = dayOfMonth
                    var Ngay = nam.toString() +"/"+ thang.toString() +"/"+ngay.toString()
                    btnNgayBD!!.text=Ngay
                }, nam, thang, ngay)
                dpd.setTitle("Ngày bắt đầu")
                dpd.show()
            }
        })

        btnNgayKT!!.setOnClickListener(object : View.OnClickListener {
            override fun onClick(p0: View?) {
                val dpd = DatePickerDialog(context, DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                    //val a = GregorianCalendar(year, monthOfYear, dayOfMonth)
                    namkt = year
                    thangkt = monthOfYear+1
                    ngaykt = dayOfMonth
                    var Ngay = namkt.toString() +"/"+ thangkt.toString() +"/"+ngaykt.toString()
                    btnNgayKT!!.text=Ngay
                    //Toast.makeText(context,a.timeInMillis.toString(),Toast.LENGTH_LONG).show()
                }, nam, thang, ngay)
                dpd.setTitle("Ngày kết thúc")
                dpd.show()
            }

        })

        btnGioBD!!.setOnClickListener {
            val timeSPDL = TimePickerDialog(
                    context, TimePickerDialog.OnTimeSetListener { timePicker, g, p ->
                gio = g
                phut = p
                val G = gio.toString()+":"+phut.toString()
                btnGioBD!!.text=G
            }, gio, phut, true)
            timeSPDL.setTitle("Giờ bắt đầu")
            timeSPDL.show()
        }
        btnGioKT!!.setOnClickListener{
            val timeSPDLKT = TimePickerDialog(
                    context, TimePickerDialog.OnTimeSetListener { timePicker, g, p ->
                giokt = g
                phutkt = p
                val G = giokt.toString()+":"+phutkt.toString()
                btnGioKT!!.text=G
            }, gio, phut, true)
            timeSPDLKT.setTitle("Giờ bắt đầu")
            timeSPDLKT.show()
        }
        btnCapNhat!!.setOnClickListener{
            val NgayBatDau =GregorianCalendar(nam, thang, ngay,gio,phut)
            val NgayKetThuc =GregorianCalendar(namkt, thangkt, ngaykt,giokt,phutkt)
            //Toast.makeText(context,NgayBatDau.timeInMillis.toString(),Toast.LENGTH_LONG).show()
            var a=EDT_ghichu!!.text
            if (EDT_ghichu !=null && EDT_ghichu!!.text.toString()!=""&&EDT_tieude !=null && EDT_tieude!!.text.toString()!=""&&NgayBatDau < NgayKetThuc){
                doctaikhoan()
                if (doctaikhoan()){
                    val ref = database.child("lich").child(id_USER).push()
                    val key = ref.key
                    // 10/10/2017 - 20/10/2017
                    val lich = DeteiladSchedulesData(key,EDT_tieude!!.text.toString(), NgayBatDau.timeInMillis, NgayKetThuc.timeInMillis, EDT_ghichu!!.text.toString())
                    ref.setValue(lich,DatabaseReference.CompletionListener { databaseError, databaseReference ->
                        if (databaseError==null)
                        {
                            Toast.makeText(this,"Đã thêm vào Lịch Trình",Toast.LENGTH_LONG).show()
                        }
                        else
                        {
                            Toast.makeText(this,"thêm thất bại, hãy thử lại",Toast.LENGTH_LONG).show()
                        }
                    })


                }
                else{
                    Toast.makeText(this,"Bạn Phải đăng nhập để sử dụng tính năng này",Toast.LENGTH_LONG).show()
                }
            }
            else
            {
                Toast.makeText(this,"Hãy Điền Đày Đủ Thông Tin",Toast.LENGTH_LONG).show()
            }
        }
//        val NgayBatDau1 =GregorianCalendar(nam, thang, ngay,gio,phut)
//        Toast.makeText(context,NgayBatDau1.timeInMillis.toString(),Toast.LENGTH_LONG).show()
//

    }
    fun doctaikhoan():Boolean {
        val sharedpreferences = this.getSharedPreferences(sharedprperences, android.content.Context.MODE_PRIVATE)
        id_USER = sharedpreferences.getString("Uid", null)
        ten_email = sharedpreferences.getString("Uemail", null)
        hinhDaiDien = sharedpreferences.getString("UURLAnh", null)
        ten = sharedpreferences.getString("Uname", null)
        if (id_USER != null) {
            return true
            // truyen!!.truyenUser(uid,name,email,photoUrl)
        }
        return false
    }

}
