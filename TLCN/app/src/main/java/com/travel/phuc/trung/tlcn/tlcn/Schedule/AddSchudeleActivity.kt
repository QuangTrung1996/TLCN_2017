package com.travel.phuc.trung.tlcn.tlcn.Schedule

import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.*
import android.widget.DatePicker
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.travel.phuc.trung.tlcn.tlcn.R
import java.text.SimpleDateFormat
import java.util.*

class AddSchudeleActivity : AppCompatActivity(), View.OnClickListener {

    val database : DatabaseReference = FirebaseDatabase.getInstance().reference
    val sdf      : SimpleDateFormat = SimpleDateFormat("HH:mm 'at' dd/MM/yyyy",Locale.getDefault())
    var idUser   : String = "null"
    var keyValue : String = ""

    // khai bao cho event
    var mYear  : Int = 0
    var mMonth : Int = 0
    var mDay   : Int = 0
    var mHour  : Int = 0
    var mMinute: Int = 0

    lateinit var dialog  : AlertDialog
    lateinit var time    : TimePicker
    lateinit var date    : DatePicker
    lateinit var cancel  : Button
    lateinit var ok      : Button
    lateinit var date_BD : Date
    lateinit var date_KT : Date

    lateinit var txtTitle       : EditText
    lateinit var txtDetail      : EditText
    lateinit var linearLayoutBD : LinearLayout
    lateinit var linearLayoutKT : LinearLayout
    lateinit var txtBD          : TextView
    lateinit var txtKT          : TextView
    lateinit var btnCancel      : Button
    lateinit var btnAdd         : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.schudele_add_activity)

        val string : String = intent.getStringExtra("work_menu")
        val sharedPreferences = this@AddSchudeleActivity.getSharedPreferences("taikhoan",android.content.Context.MODE_PRIVATE)
        idUser = sharedPreferences.getString("Uid","null")

        init()
        onClickListener()
        getWork(string)
    }

    private fun init() {
        txtTitle        = findViewById(R.id.scheduleEventTilte)
        txtDetail       = findViewById(R.id.scheduleEventDetail)
        linearLayoutBD  = findViewById(R.id.eventLayoutBD)
        linearLayoutKT  = findViewById(R.id.eventLayoutKT)
        txtBD           = findViewById(R.id.txt_eventBD)
        txtKT           = findViewById(R.id.txt_eventKT)
        btnCancel       = findViewById(R.id.btnEventCancel)
        btnAdd          = findViewById(R.id.btnEventAdd)
    }

    private fun onClickListener() {
        linearLayoutBD.setOnClickListener(this)
        linearLayoutKT.setOnClickListener(this)
        btnCancel.setOnClickListener(this)
        btnAdd.setOnClickListener(this)
    }

    private fun getWork(string: String) {
        when(string){
            "add" ->{
                val c = Calendar.getInstance()
                getTimeDate(c)

                txtBD.text = (mHour.toString() + ":"+ mMinute + ", ngày "+ mDay + " tháng "+ mMonth + " năm " + mYear)
                date_BD = sdf.parse(mHour.toString() + ":"+ mMinute + " at " + mDay + "/"+ mMonth + "/" + mYear)

                txtKT.text = txtBD.text
                date_KT = date_BD
            }
            "edit" -> {
                keyValue = intent.getStringExtra("schedule_key")
                txtTitle.setText(intent.getStringExtra("schedule_title"))
                txtDetail.setText(intent.getStringExtra("schedule_detail"))

                // thoi gian bat dau
                val c = Calendar.getInstance()
                c.timeInMillis = intent.getStringExtra("schedule_ngayBD").toLong()
                getTimeDate(c)
                txtBD.text = (mHour.toString() + ":"+ mMinute + ", ngày "+ mDay + " tháng "+ mMonth + " năm " + mYear)
                date_BD = sdf.parse(mHour.toString() + ":"+ mMinute + " at " + mDay + "/"+ mMonth + "/" + mYear)

                // thoi gian ket thuc
                c.timeInMillis = intent.getStringExtra("schedule_ngayKT").toLong()
                getTimeDate(c)
                txtKT.text = (mHour.toString() + ":"+ mMinute + ", ngày "+ mDay + " tháng "+ mMonth + " năm " + mYear)
                date_KT = sdf.parse(mHour.toString() + ":"+ mMinute + " at " + mDay + "/"+ mMonth + "/" + mYear)
            }
        }
    }

    private fun getTimeDate(c: Calendar) {
        mHour   = c.get(Calendar.HOUR_OF_DAY)
        mMinute = c.get(Calendar.MINUTE)

        mYear   = c.get(Calendar.YEAR)
        mMonth  = c.get(Calendar.MONTH) + 1
        mDay    = c.get(Calendar.DAY_OF_MONTH)
    }

    override fun onClick(view: View) {
        val id = view.id
        when(id){
            R.id.eventLayoutBD -> {

                showDialog()

                ok.setOnClickListener({
                    val mString = (mHour.toString() + ":"+ mMinute + ", ngày "+ mDay + " tháng "+ mMonth + " năm " + mYear)
                    val date = sdf.parse(mHour.toString() + ":"+ mMinute + " at " + mDay + "/"+ mMonth + "/" + mYear)

                    if (date.after(date_KT)){
                        txtBD.text = mString
                        txtKT.text = mString
                        date_BD = date
                        date_KT = date
                        dialog.dismiss()
                    }
                    else {
                        txtBD.text = mString
                        date_BD = date
                        dialog.dismiss()
                    }
                })
            }
            R.id.eventLayoutKT -> {

                showDialog()

                ok.setOnClickListener({
                    val mString = (mHour.toString() + ":"+ mMinute + ", ngày "+ mDay + " tháng "+ mMonth + " năm " + mYear)
                    val date = sdf.parse(mHour.toString() + ":"+ mMinute + " at " + mDay + "/"+ mMonth + "/" + mYear)

                    if (date.before(date_BD)){
                        dialog.dismiss()
                    }
                    else {
                        txtKT.text = mString
                        date_KT = date
                        dialog.dismiss()
                    }
                })
            }
            R.id.btnEventCancel -> {
                finish()
            }
            R.id.btnEventAdd -> {
                if(txtTitle.text.isEmpty() || txtDetail.text.isEmpty()){
                    shortToast("Empty")
                }
                else{

                    if (idUser != "null"){

                        val time = System.currentTimeMillis()
                        val ref = database.child("schedule").child(idUser)
                        var key = ""

                        if(keyValue != ""){
                            key = keyValue
                        }
                        else{
                            key = time.toString()
                        }

                        ref.child(key).setValue(
                                ScheduleEventData(key,
                                        txtTitle.text.toString(), txtDetail.text.toString(),
                                        date_BD.time.toString(), date_KT.time.toString()))

                        shortToast("Luu thanh cong")
                    }
                    else{
                        shortToast("Chưa đăng nhập")
                    }

                    finish()
                }
            }
        }
    }

    private fun showDialog() {
        val mBuilder : AlertDialog.Builder = AlertDialog.Builder(this@AddSchudeleActivity)
        val mView : View = layoutInflater.inflate(R.layout.schedule_picker,null)

        time = mView.findViewById(R.id.form_time)
        date = mView.findViewById(R.id.form_date)
        cancel = mView.findViewById(R.id.btnCancel)
        ok = mView.findViewById(R.id.btnOK)

        mBuilder.setView(mView)
        dialog = mBuilder.create()
        dialog.show()

        timeDate()

        cancel.setOnClickListener({
            dialog.dismiss()
        })
    }

    private fun timeDate() {

        time.setIs24HourView(false)
        time.currentHour = mHour
        time.currentMinute = mMinute

        time.setOnTimeChangedListener({timeView,hour,minute->
            mHour = hour
            mMinute = minute
        })

        date.init(mYear, mMonth, mDay, DatePicker.OnDateChangedListener { datePicker, year, month, dayOfMonth ->
            mYear = year
            mMonth = month
            mDay = dayOfMonth
        })
    }

    // hien dong thong bao
    private fun shortToast(string : String) {
        val length : Int = Toast.LENGTH_SHORT
        Toast.makeText(this@AddSchudeleActivity, string, length).show()
    }
}
