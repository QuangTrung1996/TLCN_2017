package com.travel.phuc.trung.tlcn.tlcn.Home

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.Toast
import com.google.firebase.database.DatabaseReference
import com.travel.phuc.trung.tlcn.tlcn.Conect.CheckInternet
import com.travel.phuc.trung.tlcn.tlcn.Conect.CheckInternetInterface
import com.travel.phuc.trung.tlcn.tlcn.R
import java.lang.Exception

/**
 * Created by Admin on 15/11/2017.
 */
class HomeFragmentTouristDestination: Fragment(),CheckInternetInterface {

    private var Lv_ThongTin: ListView? = null
    private var tong:Int=10
    private var listkeycomment:ArrayList<String> = ArrayList()

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater!!.inflate(R.layout.home_fragment_tourist_destination, container, false);
        kiemtraInternet()


        return view
    }
    // gọi class Checkinternet
    private fun kiemtraInternet() {
        try {

        val a = CheckInternet(this)
        a.checkConnection(this.context)
    }catch (e: Exception){}
    }

    override fun kiemtrainternet(flag: Boolean) {
        if (flag)
        {
            Toast.makeText(this.activity,"true",Toast.LENGTH_SHORT).show()
        }
        else
        {
            Toast.makeText(this.activity,"false",Toast.LENGTH_SHORT).show()
        }
    }

}