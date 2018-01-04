package com.travel.phuc.trung.tlcn.tlcn.AddInfromation

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.Toast
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.travel.phuc.trung.tlcn.tlcn.Home.festivalVenues.getDataFestival
import com.travel.phuc.trung.tlcn.tlcn.R
import kotlinx.android.synthetic.main.activity_add_chang_image.*
import kotlinx.android.synthetic.main.activity_add_information_festival.*
import kotlinx.android.synthetic.main.activity_add_infromation_tourist.*
import java.io.ByteArrayOutputStream

class AddChangImage : AppCompatActivity() {
    private var key:String=""
    private val sharedprperences : String="taikhoan";
    private val KEY_ID_USER = "ID_User"
    private var id_USER :String?=null
    val storageRef  : StorageReference = FirebaseStorage.getInstance().reference
    val databaseRef : DatabaseReference = FirebaseDatabase.getInstance().reference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_chang_image)
        doctaikhoan()
        val intent = intent
        key = intent.getStringExtra("keyDD")
        val data = intent.getParcelableExtra<Uri>("data")
        val bm = BitmapFactory.decodeStream(this@AddChangImage.contentResolver.openInputStream(data))
        anhthem!!.setImageBitmap(bm)
        huydanganh.setOnClickListener({
            this.finish()
        })
        danganh.setOnClickListener({
            danganhALbum.visibility = LinearLayout.GONE
            tiengtrinhdangalbum.visibility = ProgressBar.VISIBLE
            val time = System.currentTimeMillis()
            val nameImage = key + time.toString()
            val ref: StorageReference = storageRef.child(key + "/" + nameImage + ".png")
            anhthem.setDrawingCacheEnabled(true)
            anhthem.buildDrawingCache()
            val bitmap = anhthem.getDrawingCache()
            val baos = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
            val data = baos.toByteArray()

            val uploadTask = ref.putBytes(data)
            uploadTask.addOnFailureListener(OnFailureListener {
                Toast.makeText(this@AddChangImage, "hãy thử lại", Toast.LENGTH_SHORT).show()
            }).addOnSuccessListener(OnSuccessListener<UploadTask.TaskSnapshot> { taskSnapshot ->
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                val downloadUrl:String = taskSnapshot.downloadUrl.toString()
                databaseRef.child("Tam").child("Album").child(key).child(nameImage).setValue(downloadUrl, DatabaseReference.CompletionListener { databaseError, databaseReference ->
                    if (databaseError ==null)
                    {
                        this@AddChangImage.finish()
                    }
                    else
                    {
                        Toast.makeText(this,"thử lại!",Toast.LENGTH_SHORT).show()
                    }

                })

            })
        })
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

