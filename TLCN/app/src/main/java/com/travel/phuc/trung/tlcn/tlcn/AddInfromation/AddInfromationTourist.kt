package com.travel.phuc.trung.tlcn.tlcn.AddInfromation

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.travel.phuc.trung.tlcn.tlcn.Conect.ConectDatabaseSQLite
import com.travel.phuc.trung.tlcn.tlcn.Home.TouristAttraction.HomeDistrictsData
import com.travel.phuc.trung.tlcn.tlcn.R
import com.travel.phuc.trung.tlcn.tlcn.R.id.Addkhuvuc
import docongphuc.pttravle.Maps.getLatlng
import kotlinx.android.synthetic.main.activity_add_infromation_tourist.*
import com.google.firebase.storage.UploadTask
import com.google.android.gms.tasks.OnSuccessListener
import android.support.annotation.NonNull
import android.view.MenuItem
import android.widget.*
import com.google.android.gms.tasks.OnFailureListener
import com.travel.phuc.trung.tlcn.tlcn.Home.TouristAttraction.GetDataTourist
import java.io.ByteArrayOutputStream


class AddInfromationTourist : AppCompatActivity() {
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
    private var mangtinh:Cursor?=null
    private var manghuyen:Cursor?=null
    var uri : Uri? = null
    private var RequestCode:Int = 1;
    private var layanh:Boolean = false
    private var PICK_IMAGE_REQUEST :Int =2
    companion object {
        var lat:Double? = null
        var long:Double? = null
    }
    private var arrTheloai:IntArray = intArrayOf(0,0,0,0,0,0,0)
    private var giave:Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_infromation_tourist)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        val adapterKhuVuc: ArrayAdapter<String> = ArrayAdapter(this@AddInfromationTourist, android.R.layout.simple_spinner_item, resources.getStringArray(R.array.KhuVuc))
        adapterKhuVuc.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        Addkhuvuc!!.adapter=adapterKhuVuc
        doikhuvuc()
        addtinh(KhuVuc)
        addhuyen(idtinh)
        ThemThongTin()
        chontheloai()
        onclick()
    }

    private fun chontheloai() {
        adduudai.setOnClickListener {
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

    private fun themgiave() {
        if (arrTheloai.get(2) ==0)
        {
            addkhunbanggia.visibility = LinearLayout.GONE
        }
        else
            addkhunbanggia.visibility = LinearLayout.VISIBLE
    }

    // lấy thông tin từ tỉnh đổ vào spiner tinh
    @SuppressLint("Recycle")
    private fun addtinh(idkhuvuc:Int){
        database = ConectDatabaseSQLite().initDatabase(this@AddInfromationTourist,DATABASENAME);
        var cursor : Cursor = database!!.rawQuery("SELECT * FROM Tinh_TP where Tinh_TP.Vung="+idkhuvuc,null)
        mangtinh = cursor
        listTinh!!.clear();
        //cursor.moveToPosition(2)
        for (i in 0 until cursor.count){
            cursor.moveToPosition(i)
            listTinh!!.add((cursor.getString(1)))
        }
        val adapterTinh: ArrayAdapter<String> = ArrayAdapter(this@AddInfromationTourist, android.R.layout.simple_spinner_item, listTinh)
        adapterTinh.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        Addtinh!!.adapter=adapterTinh

    }
    // lấy thông tin từ huyen đổ vào spiner huyen
    @SuppressLint("Recycle")
    private fun addhuyen(idtinh:Int){
        database = ConectDatabaseSQLite().initDatabase(this@AddInfromationTourist,DATABASENAME);
        var cursor : Cursor = database!!.rawQuery("SELECT * FROM Huyen where Huyen.Tinh="+idtinh,null)
        listHuyen.clear()
        manghuyen = cursor
        for (i in 0 until cursor.count)
        {
            cursor.moveToPosition(i)
            listHuyen!!.add(cursor.getString(1))
        }
        val adapterHuyen: ArrayAdapter<String> = ArrayAdapter(this@AddInfromationTourist, android.R.layout.simple_spinner_item, listHuyen)
        adapterHuyen.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        Addhuyen!!.adapter=adapterHuyen
    }
    // set thay dổi tinh huyen khi click spiner
    private fun doikhuvuc() {
        Addkhuvuc!!.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
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
        Addtinh!!.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
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
        Addhuyen!!.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
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
   // set onclick các button
    private fun onclick(){
       laytuCMR.setOnClickListener({
           var intent:Intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
           this@AddInfromationTourist.startActivityForResult(intent,RequestCode)
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
            var bitmap:Bitmap = data.extras.get("data") as Bitmap
            Addanhdaidien.setImageBitmap(bitmap)
            layanh =true

        }
        else
        {
            if (requestCode ==PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data!!.data !=null)
            {
                val bm = BitmapFactory.decodeStream(this@AddInfromationTourist.contentResolver.openInputStream(data.data))
                Addanhdaidien!!.setImageBitmap(bm)
                layanh = true
            }
            else
            {
                Toast.makeText(this,"vui lòng thử lại",Toast.LENGTH_SHORT).show()
            }
        }

        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun ThemThongTin() {
        AddcapnhatTT.setOnClickListener({

            doctaikhoan()
            if (doctaikhoan()) {
                if (checktt() && checkTheloai() && layanh == true) {
                        addtientrinhcapnhat.visibility = ProgressBar.VISIBLE
                        AddcapnhatTT.visibility = Button.GONE
                        val a: getLatlng = getLatlng()
                        a.execute(AddDiachi.text.toString())
                        val refDl = databaseRef!!.child("Tam").child("DiaDiemDL").push()
                        val key = refDl.key

//            Toast.makeText(this@AddInfromationTourist, lat.toString(),Toast.LENGTH_SHORT).show()
                        val time = System.currentTimeMillis()
                        val nameImage = id_USER + time.toString()
                        val ref: StorageReference = storageRef.child(key + "/" + nameImage + ".png")
                        Addanhdaidien.setDrawingCacheEnabled(true)
                        Addanhdaidien.buildDrawingCache()
                        val bitmap = Addanhdaidien.getDrawingCache()
                        val baos = ByteArrayOutputStream()
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
                        val data = baos.toByteArray()

                        val uploadTask = ref.putBytes(data)
                        uploadTask.addOnFailureListener(OnFailureListener {
                            Toast.makeText(this@AddInfromationTourist, "hãy thử lại", Toast.LENGTH_SHORT).show()
                        }).addOnSuccessListener(OnSuccessListener<UploadTask.TaskSnapshot> { taskSnapshot ->
                            // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                            val downloadUrl = taskSnapshot.downloadUrl
                            var getdata = GetDataTourist(downloadUrl!!.toString(), AddDiachi.text.toString(), idhuyen, KhuVuc, lat!!, long!!, AddMota.text.toString(), idtinh, nameImage.plus(".png"), id_USER!!, AddTenDD.text.toString())
                            refDl.setValue(getdata, DatabaseReference.CompletionListener { databaseError, databaseReference ->
                                if (databaseError == null) {
                                    for (i in 0 until arrTheloai.size)
                                    {
                                        if (arrTheloai.get(i)==1) {
                                            databaseRef.child("Tam").child("TheLoai").child(key).child(i.toString()).child(key).setValue(0, DatabaseReference.CompletionListener { databaseError, databaseReference ->
                                                if (databaseError ==null){
                                                    databaseRef.child("Tam").child("Album").child(key).child(nameImage.toString()).setValue(downloadUrl.toString(), DatabaseReference.CompletionListener { databaseError, databaseReference ->
                                                        if (databaseError ==null)
                                                        {

                                                        }
                                                    })

                                                }

                                            })
                                        }
                                    }
                                    if (arrTheloai.get(2)==0) {
                                        this@AddInfromationTourist.finish()
                                    }
                                    if (arrTheloai.get(2)==1)
                                    {
                                        databaseRef.child("Tam").child("GiaVe").child(key).setValue(docgiave(), DatabaseReference.CompletionListener { databaseError, databaseReference ->
                                            if (databaseError ==null)
                                            {
                                                this@AddInfromationTourist.finish()
                                            }
                                        })
                                    }
                                    //Toast.makeText(this@AddInfromationTourist, "thành công", Toast.LENGTH_LONG).show()
                                } else {
                                    Toast.makeText(this@AddInfromationTourist, "thêm thất bại, hãy thử lại", Toast.LENGTH_LONG).show()
                                }
                            })
                        })
                }
                else
                {
                    Toast.makeText(this@AddInfromationTourist, "Cập Nhật  Đủ Thông tin", Toast.LENGTH_SHORT).show()

                }
            }
        })
    }
    // kiêm tra nguoi dung nhập thể laoi chua
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

    // đóng trang
    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        val id = item!!.itemId
        when (id) {
            android.R.id.home ->{
                finish()
            }

        }

        return true
    }
    private fun checktt():Boolean{
        if (AddTenDD.text.toString()!="" && AddDiachi.text.toString()!=""&&AddMota.text.toString()!=""&&AddTenDD!=null && AddDiachi!=null&&AddMota!=null)
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

