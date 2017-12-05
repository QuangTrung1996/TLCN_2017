package com.travel.phuc.trung.tlcn.tlcn.Manager

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.travel.phuc.trung.tlcn.tlcn.R

class ManagerFragment : Fragment() {



    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view =inflater!!.inflate(R.layout.manager_fragment,container,false)



        return view
    }
}