package com.travel.phuc.trung.tlcn.tlcn.AddInfromation

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.MenuItem
import android.view.View
import android.widget.*
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.travel.phuc.trung.tlcn.tlcn.Conect.ConectDatabaseSQLite
import com.travel.phuc.trung.tlcn.tlcn.GoogleMap.getlatlongFestival
import com.travel.phuc.trung.tlcn.tlcn.Home.TouristAttraction.GetDataTourist
import com.travel.phuc.trung.tlcn.tlcn.Home.festivalVenues.getDataFestival
import com.travel.phuc.trung.tlcn.tlcn.R
import docongphuc.pttravle.Maps.getLatlng
import kotlinx.android.synthetic.main.activity_add_information_festival.*
import kotlinx.android.synthetic.main.activity_add_infromation_tourist.*
import java.io.ByteArrayOutputStream
import java.util.*

class AddInformationFestival : AppCompatActivity() {
    val databaseRef : DatabaseReference = FirebaseDatabase.getInstance().reference
    val storageRef  : StorageReference = FirebaseStorage.getInstance().reference
    private val sharedprperences : String="taikhoan";
    private val KEY_ID_USER = "ID_User"
    private var id_USER :String?=null

    private var listHuyen:ArrayList<String> = ArrayList()
    private var listTinh:ArrayList<String> = ArrayList()
    private val DATABASENAME:String="TinhThanhPho.sqlite"
    private var database: SQLiteDatabase?=null;
    private var KhuVuc:Int=1;
    private var idtinh:Int = 1
    private var idhuyen:Int = 1
    private var mangtinh: Cursor?=null
    private var manghuyen: Cursor?=null
    var uri : Uri? = null
    private var RequestCode:Int = 3;
    private var PICK_IMAGE_REQUEST :Int =4
    private var laynh : Boolean = false
    companion object {
        var latLH:Double? = null
        var longLH:Double? = null
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_information_festival)
        val adapterKhuVuc: ArrayAdapter<String> = ArrayAdapter(this@AddInformationFestival, android.R.layout.simple_spinner_item, resources.getStringArray(R.array.KhuVuc))
        adapterKhuVuc.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        AddkhuvucLH!!.adapter=adapterKhuVuc
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        val calender: Calendar = Calendar.getInstance()
        val thang:Int = calender.get(Calendar.MONTH) +1
        val Gio=calender.get(Calendar.HOUR_OF_DAY).toString() +":"+ calender.get(Calendar.MINUTE).toString()
        val Ngay = calender.get(Calendar.DAY_OF_MONTH).toString()+"/" + thang+"/" + calender.get(Calendar.YEAR).toString()
        AddgioKTLH!!.text = Gio
        AddgioBDLH!!.text =  Gio
        AddngayKTLH!!.text=Ngay
        AddngayBDLH!!.text = Ngay
        addngaygio()
        onclick()
        doikhuvuc()
        addtinh(KhuVuc)
        addhuyen(idtinh)
    }

    private fun addhuyen(idtinh: Int) {
        database = ConectDatabaseSQLite().initDatabase(this@AddInformationFestival,DATABASENAME);
        val cursor : Cursor = database!!.rawQuery("SELECT * FROM Huyen where Huyen.Tinh="+idtinh,null)
        listHuyen.clear()
        manghuyen = cursor
        for (i in 0 until cursor.count)
        {
            cursor.moveToPosition(i)
            listHuyen.add(cursor.getString(1))
        }
        val adapterHuyen: ArrayAdapter<String> = ArrayAdapter(this@AddInformationFestival, android.R.layout.simple_spinner_item, listHuyen)
        adapterHuyen.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        AddhuyenLH!!.adapter=adapterHuyen
    }

    private fun addtinh(idkhuvuc: Int) {
        database = ConectDatabaseSQLite().initDatabase(this@AddInformationFestival,DATABASENAME);
        val cursor : Cursor = database!!.rawQuery("SELECT * FROM Tinh_TP where Tinh_TP.Vung="+idkhuvuc,null)
        mangtinh = cursor
        listTinh.clear();
        //cursor.moveToPosition(2)
        for (i in 0 until cursor.count){
            cursor.moveToPosition(i)
            listTinh.add((cursor.getString(1)))
        }
        val adapterTinh: ArrayAdapter<String> = ArrayAdapter(this@AddInformationFestival, android.R.layout.simple_spinner_item, listTinh)
        adapterTinh.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        AddtinhLH!!.adapter=adapterTinh
    }

    private fun doikhuvuc() {
        AddkhuvucLH!!.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                KhuVuc=p2+1;
                addtinh(KhuVuc)
                when (p2) {
                    0 ->{
                    }
                }

            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }
        AddtinhLH!!.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                mangtinh!!.moveToPosition(p2)
                idtinh=mangtinh!!.getInt(0);
                addhuyen(idtinh)
                when (p2) {
                    0 ->{
                    }
                }

            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }
        AddhuyenLH!!.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                manghuyen!!.moveToPosition(p2)
                idhuyen = manghuyen!!.getInt(0)
                when (p2) {
                    0 ->{
                    }
                }

            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }

    }

    private fun addngaygio() {
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
        AddngayBDLH!!.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View?) {
                val dpd = DatePickerDialog(this@AddInformationFestival, DatePickerDialog.OnDateSetListener { view_, year, monthOfYear, dayOfMonth ->
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
                val dpd = DatePickerDialog(this@AddInformationFestival, DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
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
                    this@AddInformationFestival, TimePickerDialog.OnTimeSetListener { timePicker, g, p ->
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
                    this@AddInformationFestival, TimePickerDialog.OnTimeSetListener { timePicker, g, p ->
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
                if (checktt() && (NgayBatDau < NgayKetThuc) && laynh == true) {
                    AddcapnhatTTLH.visibility = Button.GONE
                    addtientrinhcapnhatLH.visibility = ProgressBar.VISIBLE

                    val a: getlatlongFestival = getlatlongFestival()
                    a.execute(AddDiachiLH.text.toString())
                    val refLH= databaseRef!!.child("Tam").child("DiaDiemLH").push()
                    val key = refLH.key

//            Toast.makeText(this@AddInfromationTourist, lat.toString(),Toast.LENGTH_SHORT).show()
                    val time = System.currentTimeMillis()
                    val nameImage = id_USER + time.toString()
                    val ref: StorageReference = storageRef.child(key + "/" + nameImage + ".png")
                    AddanhdaidienLH.setDrawingCacheEnabled(true)
                    AddanhdaidienLH.buildDrawingCache()
                    val bitmap = AddanhdaidienLH.getDrawingCache()
                    val baos = ByteArrayOutputStream()
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
                    val data = baos.toByteArray()

                    val uploadTask = ref.putBytes(data)
                    uploadTask.addOnFailureListener(OnFailureListener {
                        Toast.makeText(this@AddInformationFestival, "hãy thử lại", Toast.LENGTH_SHORT).show()
                    }).addOnSuccessListener(OnSuccessListener<UploadTask.TaskSnapshot> { taskSnapshot ->
                        // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                        val downloadUrl = taskSnapshot.downloadUrl
                        val getdata = getDataFestival(downloadUrl!!.toString(),  AddDiachiLH.text.toString(), idhuyen, KhuVuc, latLH!!, longLH!!, AddMotaLH.text.toString(),NgayBatDau.timeInMillis,NgayKetThuc.timeInMillis,AddTenDDLH.text.toString(), idtinh, nameImage .plus(".png"),id_USER!!)
                        refLH.setValue(getdata, DatabaseReference.CompletionListener { databaseError, databaseReference ->
                            if (databaseError == null) {
                                databaseRef.child("Tam").child("Album").child(key).child(nameImage).setValue(downloadUrl.toString(), DatabaseReference.CompletionListener { databaseError1, databaseReference ->
                                    if (databaseError1 == null)
                                    {
                                        finish()
                                    }
                                })
                            } else {
                                Toast.makeText(this@AddInformationFestival, "thêm thất bại, hãy thử lại", Toast.LENGTH_LONG).show()
                            }
                        })
                    })
                }
                else
                {
                    Toast.makeText(this@AddInformationFestival, "Cập Nhật  Đủ Thông tin", Toast.LENGTH_SHORT).show()

                }
            }
        })
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
    // gọi lớp lấy ảnh
    private fun onclick(){
        laytuCMRLH.setOnClickListener({
            val intent: Intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            this@AddInformationFestival.startActivityForResult(intent,RequestCode)
        })
        laytuBSTLH.setOnClickListener({
            val intent =  Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT

            this@AddInformationFestival.startActivityForResult(Intent.createChooser(intent,"select picture"),PICK_IMAGE_REQUEST)
        })
    }
 // lấy ảnh
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode ==RequestCode && resultCode == RESULT_OK && data !=null)
        {
            val bitmap: Bitmap = data.extras.get("data") as Bitmap
            AddanhdaidienLH.setImageBitmap(bitmap)
            laynh = true

        }
        else
        {
            if (requestCode ==PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data!!.data !=null)
            {
                val bm = BitmapFactory.decodeStream(this@AddInformationFestival.contentResolver.openInputStream(data.data))
                AddanhdaidienLH!!.setImageBitmap(bm)
                laynh = true
            }
            else
            {
                Toast.makeText(this,"vui lòng thử lại", Toast.LENGTH_SHORT).show()
            }
        }

        super.onActivityResult(requestCode, resultCode, data)
    }
    private fun checktt():Boolean{
        if (AddTenDDLH.text.toString()!="" && AddDiachiLH.text.toString()!=""&&AddMotaLH.text.toString()!=""&&AddTenDDLH!=null && AddDiachiLH!=null&&AddMotaLH!=null)
        {
            return true
        }

        return false
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
