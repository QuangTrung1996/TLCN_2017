package com.travel.phuc.trung.tlcn.tlcn.Home.TouristAttraction

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import com.travel.phuc.trung.tlcn.tlcn.R


// fragment thể loại du lịch
class HomeFragmentTourismType: Fragment() {
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view=inflater!!.inflate(R.layout.home_fragment_tourism_type,container,false)
        var Lv = view.findViewById<ListView>(R.id.lv_theloai_dulich)
        Lv.adapter= ArrayAdapter(this.context,android.R.layout.simple_list_item_1,resources.getStringArray(R.array.theloai))

        return view;
    }
}