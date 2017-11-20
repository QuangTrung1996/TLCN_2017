package com.travel.phuc.trung.tlcn.tlcn.Album

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Point
import android.graphics.Rect
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.*
import android.view.animation.DecelerateInterpolator
import android.widget.*
import com.bumptech.glide.Glide
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.travel.phuc.trung.tlcn.tlcn.R
import java.util.*

class AlbumFragment : Fragment() {

    val databaseRef : DatabaseReference = FirebaseDatabase.getInstance().reference
    val storageRef  : StorageReference = FirebaseStorage.getInstance().reference

    var idUser : String = "null"
    var filePath : Uri? = null
    var FilePathArray: ArrayList<Uri> = ArrayList()
    val listImageUrl : ArrayList<String> = ArrayList()
    val listKeyImage : ArrayList<String> = ArrayList()

    val PICK_IMAGE_REQUEST : Int = 71
    var selectItemDeleteLoad = -1
    var selectItemDeleteUp = -1

    lateinit var viewSwitcher : ViewSwitcher
    lateinit var myFirstView : RelativeLayout
    lateinit var mySecondView : RelativeLayout
    lateinit var gridAlbumLoad : GridView
    lateinit var gridAlbumUp   : GridView
    lateinit var imageLoad     : ImageView
    lateinit var imageUp       : ImageView
    lateinit var albumLoadAdapter : AlbumLoadAdapter
    lateinit var albumUpAdapter : AlbumUpAdapter

    // zoom image when onclick gridview
    private var mCurrentAnimator: Animator? = null
    private var mShortAnimationDuration: Int = 0

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater!!.inflate(R.layout.album_fragment,container,false)

        setHasOptionsMenu(true)

        val sharedPreferences = activity.getSharedPreferences("taikhoan",android.content.Context.MODE_PRIVATE)
        idUser = sharedPreferences.getString("Uid",null)

        viewSwitcher = view.findViewById(R.id.viewSwitcher_album)
        myFirstView = view.findViewById(R.id.container_load)
        mySecondView = view.findViewById(R.id.container_up)
        gridAlbumLoad   = view.findViewById(R.id.grid_album_load)
        gridAlbumUp     = view.findViewById(R.id.grid_album_up)
        imageLoad = view.findViewById(R.id.expanded_image)
        imageUp = view.findViewById(R.id.expanded_image_up)
        mShortAnimationDuration = resources.getInteger(android.R.integer.config_longAnimTime)

        if (idUser != "null"){

            loadAlbum()

            albumUpAdapter = AlbumUpAdapter(activity, FilePathArray)
            albumLoadAdapter = AlbumLoadAdapter(activity, listImageUrl)
            gridAlbumLoad.adapter = albumLoadAdapter
            gridAlbumUp.adapter   = albumUpAdapter

            // su kien click vao gridview load
            gridAlbumLoad.onItemClickListener = AdapterView.OnItemClickListener({ parent, view1, position, id ->
                selectItemDeleteLoad = position
                zoomImageFromThumb(position)
            })

            gridAlbumLoad.onItemLongClickListener = AdapterView.OnItemLongClickListener({parent, view1, position, id->
                deleteStorage(position)
                true
            })

            // su kien click vao gridview up
            gridAlbumUp.onItemClickListener = AdapterView.OnItemClickListener({ parent, view1, position, id ->
                selectItemDeleteUp = position
                zoomImageFromThumb2(position)
            })

            gridAlbumUp.onItemLongClickListener = AdapterView.OnItemLongClickListener({parent, view1, position, id->
                FilePathArray.removeAt(position)
                albumUpAdapter.notifyDataSetChanged()
                true
            })
        }
        else{
            shortToast("Chưa đăng nhập")
        }

        return view
    }




    // menu
    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        activity.menuInflater.inflate(R.menu.album_menu, menu)
    }
    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        val id = item!!.itemId
        when(id){
            R.id.add_menu ->{
                if (viewSwitcher.currentView != mySecondView){
                    viewSwitcher.showNext()
                    chooseImage()
                }
                else{
                    if (viewSwitcher.currentView == mySecondView){
                        chooseImage()
                    }
                }
            }

            R.id.upload_menu ->{
                if (viewSwitcher.currentView == mySecondView){
                    val temp = FilePathArray.size-1
                    for(i in temp downTo 0){
                        uploadImage(i, temp)
                        if (i == 0 && viewSwitcher.currentView != myFirstView){
                            viewSwitcher.showPrevious()
                            FilePathArray.clear()
                            albumUpAdapter.notifyDataSetChanged()
                        }
                    }
                    if (temp == -1 && viewSwitcher.currentView != myFirstView){
                        viewSwitcher.showPrevious()
                    }
                }
                else{
                    shortToast("trang 1")
                }
            }

            R.id.delete_menu ->{
                val temp = viewSwitcher.currentView
                when (temp){
                    myFirstView ->{
                        if (selectItemDeleteLoad == -1){
                            shortToast("Chưa chọn hình để xóa.")
                        }
                        else{
                            deleteStorage(selectItemDeleteLoad)
                            selectItemDeleteLoad = -1
                        }
                    }
                    mySecondView ->{
                        if (selectItemDeleteUp == -1){
                            shortToast("Chưa chọn hình để xóa.")
                        }
                        else{
                            FilePathArray.removeAt(selectItemDeleteUp)
                            albumUpAdapter.notifyDataSetChanged()
                            selectItemDeleteUp = -1
                        }
                    }
                }
            }
        }

        return super.onOptionsItemSelected(item)
    }

    // lay url anh tu firebase ve may
    private fun loadAlbum() {
        databaseRef.child("Album").child(idUser).addChildEventListener(object : ChildEventListener {
            override fun onCancelled(p0: DatabaseError?) {}
            override fun onChildMoved(p0: DataSnapshot?, p1: String?) {}
            override fun onChildChanged(p0: DataSnapshot?, p1: String?) {}
            override fun onChildRemoved(p0: DataSnapshot?) {}

            override fun onChildAdded(p0: DataSnapshot?, p1: String?) {
                val key = p0!!.key
                val url = p0.value.toString()

                listImageUrl.add(url)
                listKeyImage.add(key)
                albumLoadAdapter.notifyDataSetChanged()
            }
        })
    }

    // lay uri tu trong may
    private fun chooseImage() {
        val intent =  Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT

        startActivityForResult(Intent.createChooser(intent,"select picture"),PICK_IMAGE_REQUEST)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data!!.data != null){
            filePath = data.data
            FilePathArray.add(filePath!!)
            albumUpAdapter.notifyDataSetChanged()

            Log.d("AAA",filePath.toString())
        }
    }

    // bat dau up len
    private fun uploadImage(position: Int, temp: Int) {
        val time = System.currentTimeMillis()
        val nameImage = "image_" + time
        val dialog: ProgressDialog = ProgressDialog(activity)
        dialog.setTitle("Uploading...")
        dialog.show()

        //val ref : StorageReference = storageref.child("image/" + id_random)
        val ref : StorageReference = storageRef.child(idUser + "/" + nameImage + ".png")
        ref.putFile(FilePathArray[position])
                .addOnSuccessListener ({ taskSnapshot ->
                    if (position == 0){
                        dialog.dismiss()
                    }
                    uploadImageAlbum(idUser,nameImage,taskSnapshot.downloadUrl.toString())
                })
                .addOnFailureListener({
                    dialog.dismiss()
                    shortToast("Fail")
                })
                .addOnProgressListener({ taskSnapshot ->
                    val progress = 100.0 * taskSnapshot.bytesTransferred / taskSnapshot.totalByteCount
                    dialog.setMessage("" + (position + 1) + "/" + (temp + 1)+ "\nLoading " + progress + " %")
                })
    }
    private fun uploadImageAlbum(idUser: String,nameImage: String,imageURL: String ) {
        databaseRef.child("Album").child(idUser).child(nameImage).setValue(imageURL)
    }

    // xoa hinh
    private fun deleteStorage(position: Int) {
        val ref : StorageReference = storageRef.child(idUser).child(listKeyImage[position] + ".png")
        ref.delete()
                .addOnSuccessListener({
                    deleteImageAlbum(idUser,listKeyImage[position])
                    deleteList(position)

                    shortToast("Xóa thành công.")
                })
                .addOnFailureListener({
                    shortToast("Xóa thất bại.")
                })
    }
    private fun deleteImageAlbum(idUser: String, key: String) {
        databaseRef.child("Album").child(idUser).child(key).removeValue()
    }
    private fun deleteList(position: Int) {
        listImageUrl.removeAt(position)
        listKeyImage.removeAt(position)
        albumLoadAdapter.notifyDataSetChanged()
    }

    // phong lon anh khi click vao hinh trong gridview
    private fun zoomImageFromThumb(position: Int) {

        if (mCurrentAnimator != null) {
            mCurrentAnimator!!.cancel()
        }

        Glide.with(activity)
                .load(listImageUrl[position])
                .into(imageLoad)

        val startBounds = Rect()
        val finalBounds = Rect()
        val globalOffset = Point()

        gridAlbumLoad.getGlobalVisibleRect(startBounds)
        activity.findViewById<View>(R.id.container_load).getGlobalVisibleRect(finalBounds, globalOffset)
        startBounds.offset(-globalOffset.x, -globalOffset.y)
        finalBounds.offset(-globalOffset.x, -globalOffset.y)

        // cho nay co loi 'startBounds.left -= deltaWidth.toInt()' do rect() khong co kieu float ma chi co kieu Int
        val startScale: Float
        if (finalBounds.width().toFloat() / finalBounds.height() > startBounds.width().toFloat()/ startBounds.height()) {
            // Extend start bounds horizontally
            startScale = startBounds.height().toFloat()/ finalBounds.height()
            val startWidth : Float = startScale * finalBounds.width()
            val deltaWidth : Float = (startWidth - startBounds.width()) / 2

            startBounds.left -= deltaWidth.toInt()
            startBounds.right += deltaWidth.toInt()
        }
        else {
            // Extend start bounds vertically
            startScale = startBounds.width().toFloat() / finalBounds.width()
            val startHeight = startScale * finalBounds.height()
            val deltaHeight = (startHeight - startBounds.height()) / 2
            startBounds.top -= deltaHeight.toInt()
            startBounds.bottom += deltaHeight.toInt()
        }

        gridAlbumLoad.alpha = 0f
        imageLoad.visibility = View.VISIBLE

        imageLoad.pivotX = 0f
        imageLoad.pivotY = 0f

        val animatorSet1 = AnimatorSet()
        animatorSet1.play(ObjectAnimator.ofFloat(imageLoad, View.X, startBounds.left.toFloat(), finalBounds.left.toFloat()))
                .with(ObjectAnimator.ofFloat(imageLoad, View.Y, startBounds.top.toFloat(), finalBounds.top.toFloat()))
                .with(ObjectAnimator.ofFloat(imageLoad, View.SCALE_X, startScale, 1f))
                .with(ObjectAnimator.ofFloat(imageLoad, View.SCALE_Y, startScale, 1f))

        animatorSet1.duration = mShortAnimationDuration.toLong()
        animatorSet1.interpolator = DecelerateInterpolator()
        animatorSet1.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                mCurrentAnimator = null
            }

            override fun onAnimationCancel(animation: Animator) {
                mCurrentAnimator = null
            }
        })
        animatorSet1.start()
        mCurrentAnimator = animatorSet1

        imageLoad.setOnClickListener( {
            if (mCurrentAnimator != null) {
                mCurrentAnimator!!.cancel()
            }

            // Animate the four positioning/sizing properties in parallel,
            // back to their
            // original values.
            val animatorSet2 = AnimatorSet()
            animatorSet2.play(
                    ObjectAnimator.ofFloat(imageLoad, View.X, startBounds.left.toFloat()))
                    .with(ObjectAnimator.ofFloat(imageLoad, View.Y, startBounds.top.toFloat()))
                    .with(ObjectAnimator.ofFloat(imageLoad, View.SCALE_X, startScale))
                    .with(ObjectAnimator.ofFloat(imageLoad, View.SCALE_Y, startScale))

            animatorSet2.duration = mShortAnimationDuration.toLong()
            animatorSet2.interpolator = DecelerateInterpolator()
            animatorSet2.addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    gridAlbumLoad.alpha = 1f
                    imageLoad.visibility = View.GONE
                    mCurrentAnimator = null
                }

                override fun onAnimationCancel(animation: Animator) {
                    gridAlbumLoad.alpha = 1f
                    imageLoad.visibility = View.GONE
                    mCurrentAnimator = null
                }
            })
            animatorSet2.start()
            mCurrentAnimator = animatorSet2
        })
    }
    private fun zoomImageFromThumb2(position: Int) {

        if (mCurrentAnimator != null) {
            mCurrentAnimator!!.cancel()
        }

        // chon hinh trong mang
        val bm = BitmapFactory.decodeStream(activity.contentResolver.openInputStream(FilePathArray[position]))
        imageUp.setImageBitmap(bm)

        val startBounds = Rect()
        val finalBounds = Rect()
        val globalOffset = Point()

        gridAlbumUp.getGlobalVisibleRect(startBounds)
        activity.findViewById<View>(R.id.container_up).getGlobalVisibleRect(finalBounds, globalOffset)
        startBounds.offset(-globalOffset.x, -globalOffset.y)
        finalBounds.offset(-globalOffset.x, -globalOffset.y)

        // cho nay co loi 'startBounds.left -= deltaWidth.toInt()' do rect() khong co kieu float ma chi co kieu Int
        val startScale: Float
        if (finalBounds.width().toFloat() / finalBounds.height() > startBounds.width().toFloat()/ startBounds.height()) {
            // Extend start bounds horizontally
            startScale = startBounds.height().toFloat()/ finalBounds.height()
            val startWidth : Float = startScale * finalBounds.width()
            val deltaWidth : Float = (startWidth - startBounds.width()) / 2

            startBounds.left -= deltaWidth.toInt()
            startBounds.right += deltaWidth.toInt()
        }
        else {
            // Extend start bounds vertically
            startScale = startBounds.width().toFloat() / finalBounds.width()
            val startHeight = startScale * finalBounds.height()
            val deltaHeight = (startHeight - startBounds.height()) / 2
            startBounds.top -= deltaHeight.toInt()
            startBounds.bottom += deltaHeight.toInt()
        }

        gridAlbumUp.alpha = 0f
        imageUp.visibility = View.VISIBLE

        imageUp.pivotX = 0f
        imageUp.pivotY = 0f

        val animatorSet1 = AnimatorSet()
        animatorSet1.play(ObjectAnimator.ofFloat(imageUp, View.X, startBounds.left.toFloat(), finalBounds.left.toFloat()))
                .with(ObjectAnimator.ofFloat(imageUp, View.Y, startBounds.top.toFloat(), finalBounds.top.toFloat()))
                .with(ObjectAnimator.ofFloat(imageUp, View.SCALE_X, startScale, 1f))
                .with(ObjectAnimator.ofFloat(imageUp, View.SCALE_Y, startScale, 1f))

        animatorSet1.duration = mShortAnimationDuration.toLong()
        animatorSet1.interpolator = DecelerateInterpolator()
        animatorSet1.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                mCurrentAnimator = null
            }

            override fun onAnimationCancel(animation: Animator) {
                mCurrentAnimator = null
            }
        })
        animatorSet1.start()
        mCurrentAnimator = animatorSet1

        imageUp.setOnClickListener( {
            if (mCurrentAnimator != null) {
                mCurrentAnimator!!.cancel()
            }

            // Animate the four positioning/sizing properties in parallel,
            // back to their
            // original values.
            val animatorSet2 = AnimatorSet()
            animatorSet2.play(
                    ObjectAnimator.ofFloat(imageUp, View.X, startBounds.left.toFloat()))
                    .with(ObjectAnimator.ofFloat(imageUp, View.Y, startBounds.top.toFloat()))
                    .with(ObjectAnimator.ofFloat(imageUp, View.SCALE_X, startScale))
                    .with(ObjectAnimator.ofFloat(imageUp, View.SCALE_Y, startScale))

            animatorSet2.duration = mShortAnimationDuration.toLong()
            animatorSet2.interpolator = DecelerateInterpolator()
            animatorSet2.addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    gridAlbumUp.alpha = 1f
                    imageUp.visibility = View.GONE
                    mCurrentAnimator = null
                }

                override fun onAnimationCancel(animation: Animator) {
                    gridAlbumUp.alpha = 1f
                    imageUp.visibility = View.GONE
                    mCurrentAnimator = null
                }
            })
            animatorSet2.start()
            mCurrentAnimator = animatorSet2
        })
    }

    // hien dong thong bao
    private fun shortToast(messsage : String) {
        val length : Int = Toast.LENGTH_SHORT
        Toast.makeText(activity, messsage, length).show()
    }
}