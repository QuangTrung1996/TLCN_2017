package com.travel.phuc.trung.tlcn.tlcn.AddInfromation

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.travel.phuc.trung.tlcn.tlcn.R
import kotlinx.android.synthetic.main.activity_album.*

class AddAlbum : AppCompatActivity() {
    val databaseRef : DatabaseReference = FirebaseDatabase.getInstance().reference
    val storageRef  : StorageReference = FirebaseStorage.getInstance().reference
    private var key:String=""
    private var loai:Int=1
    private var PICK_IMAGE_REQUEST :Int =4
   private var Arraychild:ArrayList<ImageData> = ArrayList();
   private lateinit var adapter:AlbumImageAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_album)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        val intent = intent
        key = intent.getStringExtra("key")
        val loaiDD = intent.getIntExtra("loaiDD",0)
        adapter = AlbumImageAdapter(this@AddAlbum,Arraychild)
        if (key!="")
        {
            loadanh()
            loadanh1()
        }
        grid_album.setOnItemClickListener { parent, view, position, id ->
            val intent = Intent(view!!.getContext(), AddImageDetail::class.java)
            intent.putExtra("keyDD",key)
            intent.putExtra("loaiDD",loaiDD)
            intent.putExtra("key",Arraychild.get(position).key)
            intent.putExtra("loai",Arraychild.get(position).loai)
            intent.putExtra("Anh",Arraychild.get(position).link)
            view!!.getContext().startActivity(intent)
        }
        themanh.setOnClickListener {
            val intent =  Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT

            this@AddAlbum.startActivityForResult(Intent.createChooser(intent,"select picture"),PICK_IMAGE_REQUEST)
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null){
            val intent = Intent(this@AddAlbum, AddChangImage::class.java)
            intent.putExtra("data",data.data)
            intent.putExtra("keyDD",key)
            startActivity(intent)
        }
    }
    // load alnum tu node tam
    private fun loadanh1() {

        databaseRef.child("Tam").child("Album").child(key.toString()).addChildEventListener(object :ChildEventListener{
            override fun onCancelled(p0: DatabaseError?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onChildMoved(p0: DataSnapshot?, p1: String?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onChildChanged(p0: DataSnapshot?, p1: String?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onChildAdded(p0: DataSnapshot?, p1: String?) {
                if (p0!=null)
                {
                    Arraychild!!.add(ImageData(p0.key.toString(),p0.value.toString(),2))
                    adapter.notifyDataSetChanged()
                    grid_album.adapter = adapter
                }            }

            override fun onChildRemoved(p0: DataSnapshot?) {
                for (i in 0 until Arraychild.size)
                {
                    if (p0!!.key.toString()==Arraychild.get(i).key)
                    {
                        Arraychild.removeAt(i)
                        adapter.notifyDataSetChanged()
                        grid_album.adapter=adapter
                        break
                    }
                }
            }

        })
    }

    // laod anh tu tam
    private fun loadanh() {
        databaseRef.child("AlbumAnhDuLich").child(key).addChildEventListener(object :ChildEventListener{
            override fun onCancelled(p0: DatabaseError?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onChildMoved(p0: DataSnapshot?, p1: String?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onChildChanged(p0: DataSnapshot?, p1: String?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onChildAdded(p0: DataSnapshot?, p1: String?) {
                if (p0!=null)
                {
                    Arraychild!!.add(ImageData(p0.key.toString(),p0.value.toString(),1))
                    adapter.notifyDataSetChanged()
                    grid_album.adapter = adapter
                }
            }

            override fun onChildRemoved(p0: DataSnapshot?) {
                for (i in 0 until Arraychild.size)
                {
                    if (p0!!.key.toString()==Arraychild.get(i).key)
                    {
                        Arraychild.removeAt(i)
                        adapter.notifyDataSetChanged()
                        grid_album.adapter=adapter
                        break
                    }
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
}
