package com.travel.phuc.trung.tlcn.tlcn.Home.TouristAttraction

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.ListView
import com.travel.phuc.trung.tlcn.tlcn.Home.HomeFragment
import com.travel.phuc.trung.tlcn.tlcn.Home.HomeTypeAdapter
import com.travel.phuc.trung.tlcn.tlcn.R


// fragment thể loại du lịch
class HomeFragmentTourismType: Fragment() {
    private var arraylist:ArrayList<HomeDistrictsData>?= ArrayList()
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view=inflater!!.inflate(R.layout.home_fragment_tourism_type,container,false)
        arraylist!!.add(HomeDistrictsData("Ưu Đãi"))
        arraylist!!.add(HomeDistrictsData("Tự Do"))
        arraylist!!.add(HomeDistrictsData("Bán Vé"))
        arraylist!!.add(HomeDistrictsData("Bảo Tàng"))
        arraylist!!.add(HomeDistrictsData("Di Tích"))
        arraylist!!.add(HomeDistrictsData("Công Viên"))
        arraylist!!.add(HomeDistrictsData("Thám Hiểm"))
        arraylist!!.add(HomeDistrictsData("Tất Cả"))
        var Lv = view.findViewById<ListView>(R.id.lv_theloai_dulich)
        var adapter =  HomeTypeAdapter(this.context,arraylist!!,HomeFragmentInformationTourist.theloai)
        adapter.notifyDataSetChanged()
        Lv.adapter= adapter
        Lv.setOnItemClickListener { parent, view, position, id ->
            HomeFragmentInformationTourist.theloai = position
            val fragmentManager = this.activity.supportFragmentManager
            val transaction = fragmentManager.beginTransaction()
            transaction.replace(R.id.content, HomeFragment()).commit()
        }
        return view;
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
//        try {
//            interfaceType = context as HomeTypeInterface
//        } catch (e: ClassCastException) {
//            throw ClassCastException(context.toString() + " must implement OnSelectedListener")
//        }

    }
}