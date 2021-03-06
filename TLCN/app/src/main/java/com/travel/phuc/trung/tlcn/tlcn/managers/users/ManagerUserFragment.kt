package com.travel.phuc.trung.tlcn.tlcn.managers.users

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import com.google.firebase.database.*
import com.travel.phuc.trung.tlcn.tlcn.R
import com.travel.phuc.trung.tlcn.tlcn.logins.UserData

class ManagerUserFragment : Fragment() {

    private val databaseRef : DatabaseReference = FirebaseDatabase.getInstance().reference
    private var idUser : String = "null"
    private lateinit var listUser : ListView
    private val managerUserData : ArrayList<ManagerUserData> = ArrayList()
    private lateinit var adapter : ManagerUserAdapter

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view =inflater!!.inflate(R.layout.manager_fragment_list,container,false)

        val sharedPreferences = activity.getSharedPreferences("taikhoan",android.content.Context.MODE_PRIVATE)
        idUser = sharedPreferences.getString("Uid","null")

        addListUser()
        listUser = view.findViewById(R.id.manager_fragment_listView)
        adapter = ManagerUserAdapter(activity, managerUserData)
        listUser.adapter = adapter

        return view
    }

    private fun addListUser() {
        databaseRef.child("Users").orderByChild("ten").addChildEventListener(object : ChildEventListener {
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
                    val manager = ManagerUserData(user, p0.value as Boolean)

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
                    val manager = ManagerUserData(user, false)
                    managerUserData.add(manager)
                }
                adapter.notifyDataSetChanged()
            }
        })
    }
}