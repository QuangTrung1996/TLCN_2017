package com.travel.phuc.trung.tlcn.tlcn.notifications

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ListView
import android.widget.Toast
import com.google.firebase.database.*
import com.travel.phuc.trung.tlcn.tlcn.R

class NotificationsFragment : Fragment() {

    val databaseRef : DatabaseReference = FirebaseDatabase.getInstance().reference

    var idUser : String = "null"
    var itemLongClick = 0

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

        list.onItemLongClickListener = object : AdapterView.OnItemLongClickListener{
            override fun onItemLongClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long): Boolean {
                val key = arrayData[position].thoiGian.toString()
                databaseRef.child("Notification").child(idUser).child(key).removeValue()
                itemLongClick = position
                return true
            }
        }

        return view
    }

    private fun addList() {
        databaseRef.child("Notification").child(idUser).orderByChild("order").addChildEventListener(object : ChildEventListener {
            override fun onCancelled(p0: DatabaseError?) {}

            override fun onChildMoved(p0: DataSnapshot?, p1: String?) {}

            override fun onChildChanged(p0: DataSnapshot?, p1: String?) {}

            override fun onChildAdded(p0: DataSnapshot?, p1: String?) {
                val notification = p0!!.getValue(NotificationsData::class.java)
                arrayData.add(notification!!)
                adapter.notifyDataSetChanged()
            }

            override fun onChildRemoved(p0: DataSnapshot?) {
                arrayData.removeAt(itemLongClick)
                adapter.notifyDataSetChanged()
            }
        })
    }
}