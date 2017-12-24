package com.travel.phuc.trung.tlcn.tlcn.albums

import android.Manifest
import android.app.ProgressDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.view.*
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.AdapterView
import android.widget.GridView
import android.widget.Toast
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.travel.phuc.trung.tlcn.multipleimagepickerlibrary.MultiImageSelector
import com.travel.phuc.trung.tlcn.tlcn.R
import java.io.File

class AlbumUploadFragment : Fragment(), View.OnClickListener {

    private val databaseRef : DatabaseReference = FirebaseDatabase.getInstance().reference
    private val storageRef  : StorageReference = FirebaseStorage.getInstance().reference

    private var idUser : String = "null"
    private val MAX_IMAGE_SELECTION_LIMIT = 100
    private val REQUEST_ID_MULTIPLE_PERMISSIONS = 401
    private val REQUEST_IMAGE = 301

    private var isFabOpen: Boolean = false
    private lateinit var gridView   : GridView
    private lateinit var fabMain    : FloatingActionButton
    private lateinit var fabSelect  : FloatingActionButton
    private lateinit var fabUpload  : FloatingActionButton
    private lateinit var fab_open   : Animation
    private lateinit var fab_close  : Animation
    private lateinit var rotate_forward: Animation
    private lateinit var rotate_backward: Animation

    private var mSelectedImagesList : ArrayList<String> =  ArrayList()
    private lateinit var mMultiImageSelector: MultiImageSelector
    private lateinit var mImagesAdapter: AlbumUploadAdapter

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater!!.inflate(R.layout.album_fragment_upload,container,false)

        init(view)
        setHasOptionsMenu(true)
        val sharedPreferences = activity.getSharedPreferences("taikhoan",android.content.Context.MODE_PRIVATE)
        idUser = sharedPreferences.getString("Uid",null)

        mMultiImageSelector = MultiImageSelector.create()

        gridView.onItemLongClickListener = AdapterView.OnItemLongClickListener({ _, _, position, _ ->
            mSelectedImagesList.removeAt(position)
            mImagesAdapter.notifyDataSetChanged()
            true
        })

        openMultiImageSelector()

        return view
    }

    private fun init(view: View) {
        gridView   = view.findViewById(R.id.grid_album_upload)
        fabMain = view.findViewById(R.id.fab_Main)
        fabSelect = view.findViewById(R.id.fab_Select)
        fabUpload = view.findViewById(R.id.fab_Upload)
        fab_open = AnimationUtils.loadAnimation(activity, R.anim.fab_open)
        fab_close = AnimationUtils.loadAnimation(activity,R.anim.fab_close)
        rotate_forward = AnimationUtils.loadAnimation(activity,R.anim.rotate_forward)
        rotate_backward = AnimationUtils.loadAnimation(activity,R.anim.rotate_backward)

        fabMain.setOnClickListener(this)
        fabSelect.setOnClickListener(this)
        fabUpload.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        val id = v!!.id
        when (id) {
            R.id.fab_Main ->{
                animateFAB()
            }
            R.id.fab_Select ->{
                openMultiImageSelector()
            }
            R.id.fab_Upload ->{
                openUpload()
            }
        }
    }

    private fun openUpload() {
        val temp = mSelectedImagesList.size-1
        if (temp != -1){
            for(i in 0..temp){
                uploadImage(i, temp)
            }
            routeAlbumFragment()
        }
        else{
            shortToast("No image!!!")
        }
    }

    private fun openMultiImageSelector() {
        if (checkAndRequestPermissions()) {
            mMultiImageSelector.showCamera(true)
            mMultiImageSelector.count(MAX_IMAGE_SELECTION_LIMIT)
            mMultiImageSelector.multi()
            mMultiImageSelector.origin(mSelectedImagesList)
            mMultiImageSelector.start(this@AlbumUploadFragment, REQUEST_IMAGE)
        }
    }

    // menu
    override fun onPrepareOptionsMenu(menu: Menu?) {
        super.onPrepareOptionsMenu(menu)
        val register = menu!!.findItem(R.id.back_menu)
        register.isVisible = true
    }
    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        activity.menuInflater.inflate(R.menu.album_menu, menu)
    }
    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        val id = item!!.itemId
        when(id){
            R.id.back_menu ->{
                routeAlbumFragment()
            }
        }

        return super.onOptionsItemSelected(item)
    }

    private fun routeAlbumFragment() {
        val fragmentManager = activity.supportFragmentManager
        val transaction = fragmentManager.beginTransaction()
        transaction.replace(R.id.content,AlbumFragment()).commit()
    }

    // bat dau up len
    private fun uploadImage(position: Int, temp: Int) {
        val time = System.currentTimeMillis()
        val nameImage = "image_" + time
        val dialog: ProgressDialog = ProgressDialog(activity)
        dialog.setTitle("Uploading...")
        dialog.show()

        val uriFromPath: Uri = Uri.fromFile(File(mSelectedImagesList[position]))
        val ref : StorageReference = storageRef.child(idUser + "/" + (nameImage + ".png"))
        ref.putFile(uriFromPath)
                .addOnSuccessListener ({ taskSnapshot ->
                    uploadImageAlbum(idUser,nameImage,taskSnapshot.downloadUrl.toString())
                    dialog.dismiss()
                })
                .addOnFailureListener({
                    dialog.dismiss()
                    shortToast("Fail")
                })
                .addOnProgressListener({ taskSnapshot ->
                    val progress = 100.0 * taskSnapshot.bytesTransferred / taskSnapshot.totalByteCount
                    dialog.setMessage("" + (temp + 1 - position) + "/" + (temp + 1)+ "\nLoading " + progress + " %")
                })
    }
    private fun uploadImageAlbum(idUser: String,nameImage: String,imageURL: String ) {
        databaseRef.child("Album").child(idUser).child(nameImage).setValue(imageURL)
    }

    private fun animateFAB() {
        if(isFabOpen){

            fabMain.startAnimation(rotate_backward)
            fabSelect.startAnimation(fab_close)
            fabUpload.startAnimation(fab_close)
            fabSelect.isClickable = false //setClickable(false)
            fabUpload.isClickable = false
            isFabOpen = false
        }
        else {
            fabMain.startAnimation(rotate_forward)
            fabSelect.startAnimation(fab_open)
            fabUpload.startAnimation(fab_open)
            fabSelect.isClickable = true
            fabUpload.isClickable = true
            isFabOpen = true
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE) {
            try {
                mSelectedImagesList = data.getStringArrayListExtra(MultiImageSelector.EXTRA_RESULT)
                mImagesAdapter = AlbumUploadAdapter(activity, mSelectedImagesList)
                gridView.adapter = mImagesAdapter
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
    }

    private fun checkAndRequestPermissions(): Boolean {
        val externalStoragePermission = ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE)

        val listPermissionsNeeded : ArrayList<String> = ArrayList()
        if (externalStoragePermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }

        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(activity, listPermissionsNeeded.toTypedArray(), REQUEST_ID_MULTIPLE_PERMISSIONS)
            return false
        }
        return true
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (requestCode == REQUEST_ID_MULTIPLE_PERMISSIONS) {
            fabMain.performClick()
        }
    }

    // hien dong thong bao
    private fun shortToast(messsage : String) {
        val length : Int = Toast.LENGTH_SHORT
        Toast.makeText(activity, messsage, length).show()
    }
}