package com.travel.phuc.trung.tlcn.tlcn.albums

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.*
import android.widget.AdapterView
import android.widget.GridView
import android.widget.Toast
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.travel.phuc.trung.tlcn.tlcn.R
import java.util.*

class AlbumFragment : Fragment(){

    private val databaseRef : DatabaseReference = FirebaseDatabase.getInstance().reference
    private val storageRef  : StorageReference = FirebaseStorage.getInstance().reference

    private var idUser : String = "null"
    private val listImageUrl : ArrayList<String> = ArrayList()
    private val listKeyImage : ArrayList<String> = ArrayList()

    private lateinit var gridAlbum : GridView
    private lateinit var albumLoadAdapter : AlbumAdapter

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater!!.inflate(R.layout.album_fragment,container,false)
        val sharedPreferences = activity.getSharedPreferences("taikhoan",android.content.Context.MODE_PRIVATE)
        idUser = sharedPreferences.getString("Uid",null)
        setHasOptionsMenu(true)

        gridAlbum   = view.findViewById(R.id.grid_album)

        if (idUser != "null"){

            loadAlbum()

            albumLoadAdapter = AlbumAdapter(activity, listImageUrl)
            gridAlbum.adapter = albumLoadAdapter

            // su kien click vao gridview load
            gridAlbum.onItemClickListener = AdapterView.OnItemClickListener({ _, _, position, _ ->
                val i = Intent(activity, AlbumImageViewPager::class.java)
                i.putExtra("id", position)
                i.putExtra("list", listImageUrl)
                startActivity(i)
            })

            gridAlbum.onItemLongClickListener = AdapterView.OnItemLongClickListener({_, _, position, _->
                deleteStorage(position)
                true
            })
        }
        else{
            shortToast("Chưa đăng nhập")
        }

        return view
    }

    // menu
    override fun onPrepareOptionsMenu(menu: Menu?) {
        super.onPrepareOptionsMenu(menu)
        menu!!.findItem(R.id.add_menu).isVisible = true
    }
    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        activity.menuInflater.inflate(R.menu.album_menu, menu)
    }
    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        val id = item!!.itemId
        when(id){
            R.id.add_menu ->{
                routeAlbumFragmentUpload()
            }
        }

        return super.onOptionsItemSelected(item)
    }
    private fun routeAlbumFragmentUpload() {
        val fragmentManager = activity.supportFragmentManager
        val transaction = fragmentManager.beginTransaction()
        transaction.replace(R.id.content,AlbumUploadFragment()).commit()
    }

    // lay url anh tu firebase ve may
    private fun loadAlbum() {
        databaseRef.child("Album").child(idUser).addChildEventListener(object : ChildEventListener {
            override fun onCancelled(p0: DatabaseError?) {}
            override fun onChildMoved(p0: DataSnapshot?, p1: String?) {}
            override fun onChildChanged(p0: DataSnapshot?, p1: String?) {}
            override fun onChildRemoved(p0: DataSnapshot?) {
                val key = p0!!.key
                for (i in 0..(listKeyImage.size-1)){
                    if (key == listKeyImage[i]){
                        deleteList(i)
                    }
                }
            }

            override fun onChildAdded(p0: DataSnapshot?, p1: String?) {
                val key = p0!!.key
                val url = p0.value.toString()

                listImageUrl.add(url)
                listKeyImage.add(key)
                albumLoadAdapter.notifyDataSetChanged()
            }
        })
    }

    // xoa hinh
    private fun deleteStorage(position: Int) {
        val ref : StorageReference = storageRef.child(idUser).child(listKeyImage[position] + ".png")
        ref.delete()
                .addOnSuccessListener({
                    deleteImageAlbum(idUser,listKeyImage[position])
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

    // hien dong thong bao
    private fun shortToast(messsage : String) {
        val length : Int = Toast.LENGTH_SHORT
        Toast.makeText(activity, messsage, length).show()
    }
}