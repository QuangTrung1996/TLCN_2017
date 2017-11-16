package com.travel.phuc.trung.tlcn.tlcn.Home.TouristAttraction

import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ListView
import android.widget.Toast
import com.google.firebase.database.DatabaseReference
import com.travel.phuc.trung.tlcn.tlcn.Conect.CheckInternet
import com.travel.phuc.trung.tlcn.tlcn.Conect.CheckInternetInterface
import com.travel.phuc.trung.tlcn.tlcn.R
import kotlinx.android.synthetic.main.home_fragment_tourist_destination.*
import java.lang.Exception

/**
 * Created by Admin on 15/11/2017.
 */
class HomeFragmentTouristDestination: Fragment(),CheckInternetInterface {
    companion object{
        @JvmStatic
        var a:Double=0.9
    }
    //var stchinhanh:getLatlng? = getLatlng()

    private var BtnTheLoai: Button?=null
    private var BtnTinhThanh: Button?=null;


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater!!.inflate(R.layout.home_fragment_tourist_destination, container, false);

        BtnTheLoai=view.findViewById<Button>(R.id.btntheloai)
        BtnTinhThanh=view.findViewById<Button>(R.id.btntinhthanh)
        kiemtraInternet()
        adfragment()
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
            val fragmentManager =this.activity.supportFragmentManager
            val transaction = fragmentManager.beginTransaction()
            transaction.replace(R.id.content_dulich,HomeFragmentInformationTourist()).commit()        }
        else
        {
            Toast.makeText(this.activity,"false",Toast.LENGTH_SHORT).show()
        }
    }
    private fun adfragment() {
        BtnTinhThanh!!.setOnClickListener({
            Toast.makeText(this.activity,a.toString(), Toast.LENGTH_LONG).show()
            BtnTinhThanh!!.setTextColor(Color.RED)
            BtnTheLoai!!.setTextColor(Color.BLACK)
            val fragmentManager =this.activity.supportFragmentManager
            val transaction = fragmentManager.beginTransaction()
            transaction.replace(R.id.content_dulich, HomeFragmentProvinces()).commit()
        })
        BtnTheLoai!!.setOnClickListener({
            btntheloai.setTextColor(Color.RED)
            BtnTinhThanh!!.setTextColor(Color.BLACK)
            val fragmentManager =this.activity.supportFragmentManager
            val transaction = fragmentManager.beginTransaction()
            transaction.replace(R.id.content_dulich, HomeFragmentTourismType()).commit()
        })
    }

}