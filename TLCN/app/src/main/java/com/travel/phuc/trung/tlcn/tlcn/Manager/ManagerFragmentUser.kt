package com.travel.phuc.trung.tlcn.tlcn.Manager

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.Toast
import com.google.firebase.database.*
import com.travel.phuc.trung.tlcn.tlcn.Login.UserData
import com.travel.phuc.trung.tlcn.tlcn.R

class ManagerFragmentUser : Fragment() {

    val databaseRef : DatabaseReference = FirebaseDatabase.getInstance().reference

    var idUser : String = "null"

    lateinit var listUser : ListView
    val managerUserData : ArrayList<ManagerUserData> = ArrayList()
    lateinit var adapter : ManagerUserAdapter

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view =inflater!!.inflate(R.layout.manager_fragment_user,container,false)

        val sharedPreferences = activity.getSharedPreferences("taikhoan",android.content.Context.MODE_PRIVATE)
        idUser = sharedPreferences.getString("Uid","null")

        addListUser()
        listUser = view.findViewById(R.id.manager_fragment_users_listView)
        adapter = ManagerUserAdapter(activity, managerUserData)
        listUser.adapter = adapter

        return view
    }

    private fun addListUser() {
        databaseRef.child("Users").addChildEventListener(object : ChildEventListener {
            override fun onCancelled(p0: DatabaseError?) {}

            override fun onChildMoved(p0: DataSnapshot?, p1: String?) {}

            override fun onChildChanged(p0: DataSnapshot?, p1: String?) {}

            override fun onChildAdded(p0: DataSnapshot?, p1: String?) {
                val user = p0!!.getValue(UserData::class.java)

                getManagerUser(user!!)
                adapter.notifyDataSetChanged()
            }

            override fun onChildRemoved(p0: DataSnapshot?) {}
        })
    }

    private fun getManagerUser(user: UserData) {
        databaseRef.child("Manager").child(user.id).addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError?) {}

            override fun onDataChange(p0: DataSnapshot?) {

                if (p0!!.value != null){
                    val manager = ManagerUserData(user,p0.value as Boolean)

                    var temp = -1
                    for (i in 0..(managerUserData.size-1)){
                        if (user == managerUserData[i].userData){
                            temp = i
                        }
                    }

                    if (temp == -1){
                        managerUserData.add(manager)
                    }
                    else{
                        managerUserData[temp] = manager
                    }
                }
                else{
                    val manager = ManagerUserData(user,false)
                    managerUserData.add(manager)
                }
                adapter.notifyDataSetChanged()
            }
        })
    }

    // hien dong thong bao
    private fun shortToast(str : String) {
        val length : Int = Toast.LENGTH_SHORT
        Toast.makeText(activity, str, length).show()
    }
}