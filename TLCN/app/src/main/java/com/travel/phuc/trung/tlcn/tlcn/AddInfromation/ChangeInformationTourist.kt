package com.travel.phuc.trung.tlcn.tlcn.AddInfromation

import android.annotation.SuppressLint
import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Bundle
import android.os.PersistableBundle
import android.provider.MediaStore
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.view.View
import android.widget.*
import com.bumptech.glide.Glide
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.travel.phuc.trung.tlcn.tlcn.Conect.ConectDatabaseSQLite
import com.travel.phuc.trung.tlcn.tlcn.Home.TouristAttraction.GetDataTourist
import com.travel.phuc.trung.tlcn.tlcn.R
import docongphuc.pttravle.Maps.getLatlng
import kotlinx.android.synthetic.main.activity_add_information_festival.*
import kotlinx.android.synthetic.main.activity_add_infromation_tourist.*
import java.io.ByteArrayOutputStream
import java.util.*

/**
 * Created by Admin on 18/12/2017.
 */
class ChangeInformationTourist : AppCompatActivity() {
    private var tt:GetDataTourist= GetDataTourist()
    val databaseRef : DatabaseReference = FirebaseDatabase.getInstance().reference
    val storageRef  : StorageReference = FirebaseStorage.getInstance().reference
    private var RequestCode:Int = 1;
    private var PICK_IMAGE_REQUEST :Int =2
    private var  keyDL:String=""
    private var loai:Int=0
    private val sharedprperences : String="taikhoan";
    private var doianh: Boolean = false
    private var id_USER :String?=null
    private var arrTheloai:IntArray = intArrayOf(0,0,0,0,0,0,0)
    private var giave:Int = 0
    private var khuvuc:Int=0
    private var tinh:Int=0
    private var huyen:Int=0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_infromation_tourist)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        var intent=intent
        keyDL = intent.getStringExtra("key")
        loai =intent.getIntExtra("loai",0)
        AddDiachi.isEnabled = false
        addkhuvuc.visibility = LinearLayout.GONE
        if (loai ==1){
        onclick()
        docthongtin()
        chontheloai()
        capnhatthongtin()}
        else
        {
            if (loai ==3)
            {
                chontheloai()
                onclick()
                docthongtin1()
                capnhatthongtin()
                capnhatthongtin()
            }
        }

    }
    // doc thong tin tu node đia diem DL
    private fun docthongtin1() {
        if (keyDL!="" && loai ==3)
        {
            doctaikhoan()
            databaseRef.child("DiadiemDuLich").child(keyDL).addListenerForSingleValueEvent(object :ValueEventListener {
                override fun onCancelled(p0: DatabaseError?) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

                override fun onDataChange(p0: DataSnapshot?) {
                    if (p0 != null) {
                        val data: GetDataTourist? = p0!!.getValue(GetDataTourist::class.java)
                        tt = if (data != null) data else throw NullPointerException("Expression 'data' must not be null")
                        AddTenDD.append(data!!.tenDiaDiem)
                        AddDiachi.append(data.DiaChi)
                        AddMota.append(data.MoTa)
                        Glide.with(this@ChangeInformationTourist).load(data.AnhDaiDien)
                                .centerCrop()
                                .error(R.drawable.wellcom0)
                                .into(Addanhdaidien)
                        khuvuc = data.KhuVuc
                        tinh = data.Tinh
                        huyen = data.Huyen
                    }
                }
            })
            doctheloai1()

        }    }

    private fun doctheloai1() {
        for (i in 0..6) {
            databaseRef.child("TheLoai").child(i.toString()).child(keyDL).addListenerForSingleValueEvent(object :ValueEventListener{
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

    // cập nhật thông tin đã chỉnh sửa
    private fun capnhatthongtin() {
        AddcapnhatTT.setOnClickListener({
            Toast.makeText(this@ChangeInformationTourist, arrTheloai[2].toString(), Toast.LENGTH_LONG).show()
            doctaikhoan()
            if (doctaikhoan()) {
                if (checktt() && checkTheloai()) {
                    addtientrinhcapnhat.visibility = ProgressBar.VISIBLE
                    AddcapnhatTT.visibility = Button.GONE
                    if (doianh == true) {
                        val time = System.currentTimeMillis()
                        val nameImage = id_USER + time.toString()
                        val ref: StorageReference = storageRef.child(keyDL + "/" + nameImage + ".png")
                        Addanhdaidien.setDrawingCacheEnabled(true)
                        Addanhdaidien.buildDrawingCache()
                        val bitmap = Addanhdaidien.getDrawingCache()
                        val baos = ByteArrayOutputStream()
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
                        val data = baos.toByteArray()

                        val uploadTask = ref.putBytes(data)
                        uploadTask.addOnFailureListener(OnFailureListener {
                            Toast.makeText(this@ChangeInformationTourist, "hãy thử lại", Toast.LENGTH_SHORT).show()
                        }).addOnSuccessListener(OnSuccessListener<UploadTask.TaskSnapshot> { taskSnapshot ->
                            // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                            val downloadUrl = taskSnapshot.downloadUrl
                            var getdata = GetDataTourist(downloadUrl!!.toString(), tt.DiaChi, tt.Huyen, tt.Tinh, tt.Lat, tt.Long, AddMota.text.toString(), tt.Tinh, nameImage.plus(".png"), id_USER!!, AddTenDD.text.toString())
                            databaseRef.child("Tam").child("DiaDiemDL").child(keyDL).setValue(getdata, DatabaseReference.CompletionListener { databaseError, databaseReference ->
                                if (databaseError == null) {
                                    for (i in 0 until arrTheloai.size) {
                                        if (arrTheloai.get(i) == 1) {
                                            databaseRef.child("Tam").child("TheLoai").child(keyDL).child(i.toString()).child(keyDL).setValue(0, DatabaseReference.CompletionListener { databaseError, databaseReference ->
                                                if (databaseError == null) {
                                                    databaseRef.child("Tam").child("Album").child(keyDL).child(nameImage.toString()).setValue(downloadUrl.toString(), DatabaseReference.CompletionListener { databaseError, databaseReference ->
                                                        if (databaseError == null) {

                                                        }
                                                    })

                                                }

                                            })
                                        } else {
                                            databaseRef.child("Tam").child("TheLoai").child(keyDL).child(i.toString()).child(keyDL).removeValue()
                                        }
                                    }
                                    if (arrTheloai.get(2) == 0) {
                                        this@ChangeInformationTourist.finish()
                                    }
                                    if (arrTheloai.get(2) == 1) {
                                        databaseRef.child("Tam").child("GiaVe").child(keyDL).setValue(docgiave(), DatabaseReference.CompletionListener { databaseError, databaseReference ->
                                            if (databaseError == null) {
                                                this@ChangeInformationTourist.finish()
                                            }
                                        })
                                    }
                                    //Toast.makeText(this@AddInfromationTourist, "thành công", Toast.LENGTH_LONG).show()
                                } else {
                                    Toast.makeText(this@ChangeInformationTourist, "thêm thất bại, hãy thử lại", Toast.LENGTH_LONG).show()
                                }
                            })
                        })
                    }
                    else
                    {
                        var getdata = GetDataTourist(tt.AnhDaiDien, tt.DiaChi, tt.Huyen, tt.KhuVuc, tt.Lat, tt.Long, AddMota.text.toString(), tt.Tinh, tt.idAnh, id_USER!!, AddTenDD.text.toString())
                        databaseRef.child("Tam").child("DiaDiemDL").child(keyDL).setValue(getdata, DatabaseReference.CompletionListener { databaseError, databaseReference ->
                            if (databaseError ==null)
                            {
                                for (i in 0 until arrTheloai.size) {
                                    if (arrTheloai.get(i) == 1) {
                                        databaseRef.child("Tam").child("TheLoai").child(keyDL).child(i.toString()).child(keyDL).setValue(0, DatabaseReference.CompletionListener { databaseError, databaseReference ->
                                            if (databaseError == null) {
                                               this@ChangeInformationTourist.finish()
                                            }

                                        })
                                    } else {
                                        databaseRef.child("Tam").child("TheLoai").child(keyDL).child(i.toString()).child(keyDL).removeValue()
                                    }
                                }
                                if (arrTheloai.get(2) == 0) {
                                    this@ChangeInformationTourist.finish()
                                }
                                if (arrTheloai.get(2) == 1) {
                                    databaseRef.child("Tam").child("GiaVe").child(keyDL).setValue(docgiave(), DatabaseReference.CompletionListener { databaseError, databaseReference ->
                                        if (databaseError == null) {
                                            this@ChangeInformationTourist.finish()
                                        }
                                    })
                                }
//                                //Toast.makeText(this@AddInfromationTourist, "thành công", Toast.LENGTH_LONG).show()
                            }
                        })
                    }
                }
                else {
                        Toast.makeText(this@ChangeInformationTourist, "Cập Nhật  Đủ Thông tin", Toast.LENGTH_SHORT).show()
                    }
            }
            if (arrTheloai.get(2) == 1) {
                databaseRef.child("Tam").child("GiaVe").child(keyDL).setValue(docgiave(), DatabaseReference.CompletionListener { databaseError, databaseReference ->
                    if (databaseError == null) {
                        this@ChangeInformationTourist.finish()
                    }
                })
            }
            else
            {
                databaseRef.child("Tam").child("GiaVe").child(keyDL).removeValue()
            }
        })

    }

    // set onclick các button
    private fun onclick(){
        laytuCMR.setOnClickListener({
            var intent: Intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            this@ChangeInformationTourist.startActivityForResult(intent,RequestCode)
        })
        laytuBST.setOnClickListener({
            val intent =  Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT

            startActivityForResult(Intent.createChooser(intent,"select picture"),PICK_IMAGE_REQUEST)
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode ==RequestCode && resultCode == RESULT_OK && data !=null)
        {
            doianh = true
            var bitmap: Bitmap = data.extras.get("data") as Bitmap
            Addanhdaidien.setImageBitmap(bitmap)

        }
        else
        {
            if (requestCode ==PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data!!.data !=null)
            {
                doianh =true
                val bm = BitmapFactory.decodeStream(this@ChangeInformationTourist.contentResolver.openInputStream(data.data))
                Addanhdaidien!!.setImageBitmap(bm)
            }
            else
            {
                Toast.makeText(this,"vui lòng thử lại",Toast.LENGTH_SHORT).show()
            }
        }

        super.onActivityResult(requestCode, resultCode, data)
    }


    // doc thong tin da dang
    private fun docthongtin() {
        if (keyDL!="" && loai ==1)
        {
            doctaikhoan()
            databaseRef.child("Tam").child("DiaDiemDL").child(keyDL).addListenerForSingleValueEvent(object :ValueEventListener {
                override fun onCancelled(p0: DatabaseError?) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

                override fun onDataChange(p0: DataSnapshot?) {
                    if (p0 != null) {
                        val data: GetDataTourist? = p0!!.getValue(GetDataTourist::class.java)
                        tt = if (data != null) data else throw NullPointerException("Expression 'data' must not be null")
                        AddTenDD.append(data!!.tenDiaDiem)
                        AddDiachi.append(data.DiaChi)
                        AddMota.append(data.MoTa)
                        Glide.with(this@ChangeInformationTourist).load(data.AnhDaiDien)
                                .centerCrop()
                                .error(R.drawable.wellcom0)
                                .into(Addanhdaidien)
                        khuvuc = data.KhuVuc
                        tinh = data.Tinh
                        huyen = data.Huyen
                    }
                }
            })
            doctheloai()

        }
    }

    private fun doctheloai() {
        for (i in 0..6) {
            databaseRef.child("Tam").child("TheLoai").child(keyDL).child(i.toString()).child(keyDL).addListenerForSingleValueEvent(object :ValueEventListener{
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
    // xác định theloai
    private fun checktheloai(i:Int) {
        when (i){
            0->{
                adduudai.setTextColor(Color.BLUE)
            }
            1->{
                addtudo.setTextColor(Color.BLUE)
            }
            2->{
                addbanve.setTextColor(Color.BLUE)
                addkhunbanggia.visibility = LinearLayout.VISIBLE
               if (loai ==1)
               {
                themve()
               }
                else if (loai ==3)
               {
                   themve1()
               }
            }
            3->{
                addbaotang.setTextColor(Color.BLUE)
            }
            4->{
                addditich.setTextColor(Color.BLUE)
            }
            5->{
                addcongvien.setTextColor(Color.BLUE)
            }
            6->{
                addthamhiem.setTextColor(Color.BLUE)
            }
        }
    }

    private fun themve1() {
        databaseRef.child("BanVe").child(keyDL).addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onCancelled(p0: DatabaseError?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(p0: DataSnapshot?) {
                if (p0!!.value !=null && p0.value !=""){
                    addgiave.append(p0.value.toString())
                }
            }
        })    }

    private fun themve()
    {
        databaseRef.child("Tam").child("GiaVe").child(keyDL).addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onCancelled(p0: DatabaseError?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(p0: DataSnapshot?) {
                if (p0!!.value !=null && p0.value !=""){
                    addgiave.append(p0.value.toString())
                }
            }
        })
    }

        // thay doi the loại
        private fun chontheloai() {
            adduudai.setOnClickListener {
                Toast.makeText(this@ChangeInformationTourist,tt.tenDiaDiem,Toast.LENGTH_SHORT).show()
                if (arrTheloai.get(0)==1)
                {

                    adduudai.setTextColor(Color.BLACK)
                    arrTheloai[0] =0
                }
                else
                {
                    adduudai.setTextColor(Color.BLUE)
                    arrTheloai[0] =1
                }
            }
            addtudo.setOnClickListener {
                if (arrTheloai.get(1)==1)
                {
                    addtudo.setTextColor(Color.BLACK)
                    arrTheloai[1] =0
                }
                else
                {
                    addtudo.setTextColor(Color.BLUE)
                    arrTheloai[1] =1
                }
            }
            addbanve.setOnClickListener {
                if (arrTheloai.get(2)==1)
                {
                    addbanve.setTextColor(Color.BLACK)
                    arrTheloai[2] =0
                    themgiave()
                }
                else
                {
                    addbanve.setTextColor(Color.BLUE)
                    arrTheloai[2] =1
                    themgiave()
                }
            }
            addbaotang.setOnClickListener {
                if (arrTheloai.get(3)==1)
                {
                    addbaotang.setTextColor(Color.BLACK)
                    arrTheloai[3] =0
                }
                else
                {
                    addbaotang.setTextColor(Color.BLUE)
                    arrTheloai[3] =1
                }
            }
            addditich.setOnClickListener {
                if (arrTheloai.get(4)==1)
                {
                    addditich.setTextColor(Color.BLACK)
                    arrTheloai[4] =0
                }
                else
                {
                    addditich.setTextColor(Color.BLUE)
                    arrTheloai[4] =1
                }
            }
            addcongvien.setOnClickListener {
                if (arrTheloai.get(5)==1)
                {
                    addcongvien.setTextColor(Color.BLACK)
                    arrTheloai[5] =0
                }
                else
                {
                    addcongvien.setTextColor(Color.BLUE)
                    arrTheloai[5] =1
                }
            }
            addthamhiem.setOnClickListener {
                if (arrTheloai.get(6)==1)
                {
                    addthamhiem.setTextColor(Color.BLACK)
                    arrTheloai[6] =0
                }
                else
                {
                    addthamhiem.setTextColor(Color.BLUE)
                    arrTheloai[6] =1
                }
            }
        }
    private fun checkTheloai():Boolean{
        if (arrTheloai.get(2)==1)
        {
            if (addgiave.text.toString() != "")
                return true
            else
                if (addgiave.text.toString() == "")
                    return false
        }

        for (i in 0 until arrTheloai.size)
        {
            if (arrTheloai.get(i)==1)
                return true
        }
        return false
    }
    // dọc giave từ editext
    private fun docgiave():Int{
        var gia=addgiave.text.toString()
        giave = gia.toInt()
        return giave
    }

    // kiem tra thông tin
    private fun checktt():Boolean{
        if (AddTenDD.text.toString()!="" && AddDiachi.text.toString()!=""&&AddMota.text.toString()!=""&&AddTenDD!=null && AddDiachi!=null&&AddMota!=null)
        {
            return true
        }

        return false
    }

    private fun themgiave() {
        if (arrTheloai.get(2) ==0)
        {
            addkhunbanggia.visibility = LinearLayout.GONE
        }
        else
            addkhunbanggia.visibility = LinearLayout.VISIBLE
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