package com.travel.phuc.trung.tlcn.tlcn.Schedule

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.travel.phuc.trung.tlcn.tlcn.Home.HomeAdapter
import com.travel.phuc.trung.tlcn.tlcn.Home.HomeFragmentFestivalLocation
import com.travel.phuc.trung.tlcn.tlcn.Home.HomeFragmentTouristDestination
import com.travel.phuc.trung.tlcn.tlcn.R

/**
 * Created by Admin on 15/11/2017.
 */
class ScheduleFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view =inflater!!.inflate(R.layout.schedule_fragment,container,false);

        return view;

    }
}