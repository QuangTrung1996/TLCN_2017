package com.travel.phuc.trung.tlcn.tlcn.Home

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.travel.phuc.trung.tlcn.tlcn.R

/**
 * Created by Admin on 15/11/2017.
 */
class HomeFragment : Fragment() {
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