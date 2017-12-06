package com.travel.phuc.trung.tlcn.tlcn.Manager

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.travel.phuc.trung.tlcn.tlcn.R

class ManagerFragment : Fragment() {

    val databaseRef : DatabaseReference = FirebaseDatabase.getInstance().reference
    val storageRef  : StorageReference = FirebaseStorage.getInstance().reference

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view =inflater!!.inflate(R.layout.manager_fragment,container,false)

        return view
    }
}