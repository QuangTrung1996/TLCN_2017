package com.travel.phuc.trung.tlcn.tlcn.Home

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.travel.phuc.trung.tlcn.tlcn.Home.TouristAttraction.HomeFragmentTouristDestination
import com.travel.phuc.trung.tlcn.tlcn.R

class HomeFragment : Fragment() {

    val sharedprperences : String="taikhoan";
    var id_USER :String?=null
    var ten:String? =null
    var ten_email:String? = null
    var hinhDaiDien:String?=null
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view =inflater!!.inflate(R.layout.home_fragment,container,false);
        val pageradapter: HomeAdapter = HomeAdapter(this.activity.supportFragmentManager);
        pageradapter.addfragment(HomeFragmentTouristDestination());
        pageradapter.addfragment(HomeFragmentFestivalLocation());

        val pager = view.findViewById<ViewPager>(R.id.viewpager);

        pager!!.adapter = pageradapter;

        return view;

    }
}