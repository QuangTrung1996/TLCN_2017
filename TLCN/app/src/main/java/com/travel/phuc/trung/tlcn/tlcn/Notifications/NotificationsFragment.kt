package com.travel.phuc.trung.tlcn.tlcn.Notifications

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.Toast
import com.google.firebase.database.*
import com.travel.phuc.trung.tlcn.tlcn.R

class NotificationsFragment : Fragment() {

    val databaseRef : DatabaseReference = FirebaseDatabase.getInstance().reference

    var idUser : String = "null"

    lateinit var list : ListView
    val arrayData : ArrayList<NotificationsData> = ArrayList()
    lateinit var adapter : NotificationsAdapter

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view =inflater!!.inflate(R.layout.notifications_fragment,container,false)

        val sharedPreferences = activity.getSharedPreferences("taikhoan",android.content.Context.MODE_PRIVATE)
        idUser = sharedPreferences.getString("Uid","null")

        addList()
        list = view.findViewById(R.id.notifications_listView)
        adapter = NotificationsAdapter(activity, arrayData)
        list.adapter = adapter

        return view
    }

    private fun addList() {
        databaseRef.child("Notification").child(idUser).addChildEventListener(object : ChildEventListener {
            override fun onCancelled(p0: DatabaseError?) {}

            override fun onChildMoved(p0: DataSnapshot?, p1: String?) {}

            override fun onChildChanged(p0: DataSnapshot?, p1: String?) {}

            override fun onChildAdded(p0: DataSnapshot?, p1: String?) {
                val notification = p0!!.getValue(NotificationsData::class.java)
                arrayData.add(notification!!)
                adapter.notifyDataSetChanged()
            }

            override fun onChildRemoved(p0: DataSnapshot?) { }
        })
    }

    //thong bao
    private fun shortToast(str : String) {
        val length : Int = Toast.LENGTH_SHORT
        Toast.makeText(this.activity, str, length).show()
    }
}