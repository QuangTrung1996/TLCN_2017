package com.travel.phuc.trung.tlcn.tlcn.Notifications

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.travel.phuc.trung.tlcn.tlcn.R

class NotificationsFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view =inflater!!.inflate(R.layout.notifications_fragment,container,false)



        return view
    }
}