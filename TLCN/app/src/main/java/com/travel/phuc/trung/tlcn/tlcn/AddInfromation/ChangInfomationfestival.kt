package com.travel.phuc.trung.tlcn.tlcn.AddInfromation

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.provider.MediaStore
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import com.bumptech.glide.Glide
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.travel.phuc.trung.tlcn.tlcn.GoogleMap.getlatlongFestival
import com.travel.phuc.trung.tlcn.tlcn.Home.festivalVenues.getDataFestival
import com.travel.phuc.trung.tlcn.tlcn.R
import kotlinx.android.synthetic.main.activity_add_information_festival.*
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by Admin on 20/12/2017.
 */
class ChangInfomationfestival : AppCompatActivity() {

    val databaseRef : DatabaseReference = FirebaseDatabase.getInstance().reference
    val storageRef  : StorageReference = FirebaseStorage.getInstance().reference
    private val sharedprperences : String="taikhoan";
    private val KEY_ID_USER = "ID_User"
    private var id_USER :String?=null

    private var ttLH:getDataFestival = getDataFestival()
    private var  keyLH:String=""
    private var loai:Int=0
    private var RequestCode:Int = 3;
    private var PICK_IMAGE_REQUEST :Int =4
    private var doianh:Boolean=false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_information_festival)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        var intent=intent
        doctaikhoan()
        keyLH = intent.getStringExtra("key")
        loai =intent.getIntExtra("loai",0)
        ankhuvucLH.visibility = LinearLayout.GONE
        AddDiachiLH.isEnabled = false

        if (loai ==2)
        {
            docthongtin()
            onclick()
            thaydoithongtin()
        }
        else
            if (loai ==4)
            {
                docthongtin1()
                onclick()
                thaydoithongtin()
            }
    }

    private fun docthongtin1() {

        databaseRef.child("DiaDiemLeHoi").child(keyLH).addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onCancelled(p0: DatabaseError?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(p0: DataSnapshot?) {
                if (p0 != null) {
                    val data: getDataFestival? = p0!!.getValue(getDataFestival::class.java)
                    ttLH = if (data != null) data else throw NullPointerException("Expression 'data' must not be null")
                    AddTenDDLH.append(data!!.TenLeHoi)
                    AddMotaLH.append(data.MoTa)
                    AddDiachiLH.append(data.DiaChi)
                    val sdf_date: SimpleDateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
                    val sdf_time: SimpleDateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
                    val Tgbd = sdf_time.format(data.NgayBD)
                    val NgBD = sdf_date.format(data.NgayBD)
                    val Tgkt = sdf_time.format(data.NgayKT)
                    val Ngkt = sdf_date.format(data.NgayKT)
                    AddgioBDLH.text = Tgbd.toString()
                    AddgioKTLH.text = Tgkt.toString()
                    AddngayBDLH.text = NgBD.toString()
                    AddngayKTLH.text = Ngkt.toString()
                    Glide.with(this@ChangInfomationfestival)
                            .load(data.AnhDaiDien)
                            .centerCrop()
                            .error(R.drawable.anhbia)
                            .into(AddanhdaidienLH)
                }
            }

        })
    }

    private fun thaydoithongtin() {
        Toast.makeText(this,ttLH.NgayBD.toString(),Toast.LENGTH_SHORT).show()
        val calender: Calendar = Calendar.getInstance()
        calender.timeInMillis = ttLH.NgayBD
        val calender1: Calendar = Calendar.getInstance()
        calender1.timeInMillis = ttLH.NgayKT
        var gio = calender.get(Calendar.HOUR_OF_DAY)
        var phut = calender.get(Calendar.MINUTE)
        var ngay = calender.get(Calendar.DAY_OF_MONTH)
        var thang = calender.get(Calendar.MONTH)
        var nam = calender.get(Calendar.YEAR)
        var giokt = calender1.get(Calendar.HOUR_OF_DAY)
        var phutkt = calender1.get(Calendar.MINUTE)
        var ngaykt = calender1.get(Calendar.DAY_OF_MONTH)
        var thangkt = calender1.get(Calendar.MONTH)
        var namkt = calender1.get(Calendar.YEAR)
        AddngayBDLH!!.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View?) {
                val dpd = DatePickerDialog(this@ChangInfomationfestival, DatePickerDialog.OnDateSetListener { view_, year, monthOfYear, dayOfMonth ->
                    nam = year
                    thang = monthOfYear+1
                    ngay = dayOfMonth
                    val Ngay = nam.toString() +"/"+ thang.toString() +"/"+ngay.toString()
                    AddngayBDLH!!.text=Ngay
                }, nam, thang, ngay)
                dpd.setTitle("Ngày bắt đầu")
                dpd.show()
            }
        })
        AddngayKTLH!!.setOnClickListener(object : View.OnClickListener {
            override fun onClick(p0: View?) {
                val dpd = DatePickerDialog(this@ChangInfomationfestival, DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                    //val a = GregorianCalendar(year, monthOfYear, dayOfMonth)
                    namkt = year
                    thangkt = monthOfYear+1
                    ngaykt = dayOfMonth
                    val Ngay = namkt.toString() +"/"+ thangkt.toString() +"/"+ngaykt.toString()
                    AddngayKTLH!!.text=Ngay
                    //Toast.makeText(context,a.timeInMillis.toString(),Toast.LENGTH_LONG).show()
                }, nam, thang, ngay)
                dpd.setTitle("Ngày kết thúc")
                dpd.show()
            }

        })
        AddgioBDLH!!.setOnClickListener {
            val timeSPDL = TimePickerDialog(
                    this@ChangInfomationfestival, TimePickerDialog.OnTimeSetListener { timePicker, g, p ->
                gio = g
                phut = p
                val G = gio.toString()+":"+phut.toString()
                AddgioBDLH!!.text=G
            }, gio, phut, true)
            timeSPDL.setTitle("Giờ bắt đầu")
            timeSPDL.show()
        }
        AddgioKTLH!!.setOnClickListener{
            val timeSPDLKT = TimePickerDialog(
                    this@ChangInfomationfestival, TimePickerDialog.OnTimeSetListener { timePicker, g, p ->
                giokt = g
                phutkt = p
                val G = giokt.toString()+":"+phutkt.toString()
                AddgioKTLH!!.text=G
            }, gio, phut, true)
            timeSPDLKT.setTitle("Giờ bắt đầu")
            timeSPDLKT.show()
        }
        AddcapnhatTTLH.setOnClickListener({
            val NgayBatDau =GregorianCalendar(nam, thang, ngay,gio,phut)
            val NgayKetThuc =GregorianCalendar(namkt, thangkt, ngaykt,giokt,phutkt)
            doctaikhoan()
            if (doctaikhoan()) {
                if (checktt() && (NgayBatDau <= NgayKetThuc)) {
                    if (doianh == true) {
                        val a: getlatlongFestival = getlatlongFestival()
                        a.execute(AddDiachiLH.text.toString())
//                  Toast.makeText(this@AddInfromationTourist, lat.toString(),Toast.LENGTH_SHORT).show()
                        val time = System.currentTimeMillis()
                        val nameImage = "test" + time.toString()
                        val ref: StorageReference = storageRef.child(keyLH + "/" + nameImage + ".png")
                        AddanhdaidienLH.setDrawingCacheEnabled(true)
                        AddanhdaidienLH.buildDrawingCache()
                        val bitmap = AddanhdaidienLH.getDrawingCache()
                        val baos = ByteArrayOutputStream()
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
                        val data = baos.toByteArray()

                        val uploadTask = ref.putBytes(data)
                        uploadTask.addOnFailureListener(OnFailureListener {
                            Toast.makeText(this@ChangInfomationfestival, "hãy thử lại", Toast.LENGTH_SHORT).show()
                        }).addOnSuccessListener(OnSuccessListener<UploadTask.TaskSnapshot> { taskSnapshot ->
                            // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                            val downloadUrl = taskSnapshot.downloadUrl
                            val getdata = getDataFestival(downloadUrl!!.toString(), AddDiachiLH.text.toString(), ttLH.Huyen, ttLH.KhuVuc, ttLH.Lat, ttLH.Long, AddMotaLH.text.toString(), NgayBatDau.timeInMillis, NgayKetThuc.timeInMillis, AddTenDDLH.text.toString(), ttLH.Tinh, nameImage.plus(".png"), ttLH.idUser)
                            databaseRef.child("Tam").child("DiaDiemLH").child(keyLH).setValue(getdata, DatabaseReference.CompletionListener { databaseError, databaseReference ->
                                if (databaseError == null) {
                                    databaseRef.child("Tam").child("Album").child(keyLH).child(nameImage).setValue(downloadUrl.toString(), DatabaseReference.CompletionListener { databaseError, databaseReference ->
                                        if (databaseError == null) {
                                            finish()
                                        }
                                    })
                                } else {
                                    Toast.makeText(this@ChangInfomationfestival, "thêm thất bại, hãy thử lại", Toast.LENGTH_LONG).show()
                                }
                            })
                        })
                    }
                    else{
                        val getdata = getDataFestival(ttLH.AnhDaiDien, AddDiachiLH.text.toString(), ttLH.Huyen, ttLH.KhuVuc, ttLH.Lat, ttLH.Long, AddMotaLH.text.toString(), NgayBatDau.timeInMillis, NgayKetThuc.timeInMillis, AddTenDDLH.text.toString(), ttLH.Tinh, ttLH.idAnh, ttLH.idUser)
                        databaseRef.child("Tam").child("DiaDiemLH").child(keyLH).setValue(getdata, DatabaseReference.CompletionListener { databaseError, databaseReference ->
                            if (databaseError == null) {
                                finish()
                            } else {
                                Toast.makeText(this@ChangInfomationfestival, "thêm thất bại, hãy thử lại", Toast.LENGTH_LONG).show()
                            }
                        })
                    }
                }
                else
                {
                    Toast.makeText(this@ChangInfomationfestival, "Cập Nhật  Đủ Thông tin", Toast.LENGTH_SHORT).show()

                }
            }
        })
    }
    // kiem tra thong tin
    private fun checktt():Boolean{
        if (AddTenDDLH.text.toString()!="" && AddDiachiLH.text.toString()!=""&&AddMotaLH.text.toString()!=""&&AddTenDDLH!=null && AddDiachiLH!=null&&AddMotaLH!=null)
        {
            return true
        }

        return false
    }

    // đọc thong tin tu node tam tu firebase
    private fun docthongtin() {
        databaseRef.child("Tam").child("DiaDiemLH").child(keyLH).addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onCancelled(p0: DatabaseError?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(p0: DataSnapshot?) {
                val data:getDataFestival? = p0!!.getValue(getDataFestival::class.java)
                ttLH=if(data != null) data else throw NullPointerException("Expression 'data' must not be null")
                AddTenDDLH.append(data!!.TenLeHoi)
                AddMotaLH.append(data.MoTa)
                AddDiachiLH.append(data.DiaChi)
                val sdf_date : SimpleDateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
                val sdf_time : SimpleDateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
                val Tgbd = sdf_time.format(data.NgayBD)
                val NgBD=sdf_date.format(data.NgayBD)
                val Tgkt = sdf_time.format(data.NgayKT)
                val Ngkt=sdf_date.format(data.NgayKT)
                AddgioBDLH.text = Tgbd.toString()
                AddgioKTLH.text = Tgkt.toString()
                AddngayBDLH.text = NgBD.toString()
                AddngayKTLH.text = Ngkt.toString()
                Glide.with(this@ChangInfomationfestival)
                        .load(data.AnhDaiDien)
                        .centerCrop()
                        .error(R.drawable.anhbia)
                        .into(AddanhdaidienLH)            }

        })
    }
    // gọi lớp lấy ảnh
    private fun onclick(){
        laytuCMRLH.setOnClickListener({
            val calender: Calendar = Calendar.getInstance()
            calender.timeInMillis = ttLH.NgayBD
            val nam = calender.get(Calendar.YEAR)
            val thang = calender.get(Calendar.MONTH);
            Toast.makeText(this,nam.toString(),Toast.LENGTH_SHORT).show()
//            val intent: Intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
//            this@ChangInfomationfestival.startActivityForResult(intent,RequestCode)
        })
        laytuBSTLH.setOnClickListener({
            val intent =  Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT

            this@ChangInfomationfestival.startActivityForResult(Intent.createChooser(intent,"select picture"),PICK_IMAGE_REQUEST)
        })
    }
    // lấy ảnh
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode ==RequestCode && resultCode == RESULT_OK && data !=null)
        {
            doianh =true
            val bitmap: Bitmap = data.extras.get("data") as Bitmap
            AddanhdaidienLH.setImageBitmap(bitmap)

        }
        else
        {
            if (requestCode ==PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data!!.data !=null)
            {
                doianh = true
                val bm = BitmapFactory.decodeStream(this@ChangInfomationfestival.contentResolver.openInputStream(data.data))
                AddanhdaidienLH!!.setImageBitmap(bm)
            }
            else
            {
                Toast.makeText(this,"vui lòng thử lại", Toast.LENGTH_SHORT).show()
            }
        }

        super.onActivityResult(requestCode, resultCode, data)
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
    fun doctaikhoan():Boolean {
        val sharedpreferences = this.getSharedPreferences(sharedprperences, android.content.Context.MODE_PRIVATE)
        id_USER = sharedpreferences.getString("Uid", null)
        if (id_USER != null) {
            return true
            // truyen!!.truyenUser(uid,name,email,photoUrl)
        }
        return false
    }
}