package com.travel.phuc.trung.tlcn.tlcn.managers

import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.travel.phuc.trung.tlcn.tlcn.R
import com.travel.phuc.trung.tlcn.tlcn.managers.albumUpdate.ManagerAlbumUpdateFragment
import com.travel.phuc.trung.tlcn.tlcn.managers.festivals.ManagerFestivalFragment
import com.travel.phuc.trung.tlcn.tlcn.managers.tourist.ManagerTouristFragment
import com.travel.phuc.trung.tlcn.tlcn.managers.users.ManagerUserFragment


class ManagerFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view =inflater!!.inflate(R.layout.manager_fragment,container,false)

        val tabLayout: TabLayout = view.findViewById(R.id.tab_layout)
        val mPager: ViewPager = view.findViewById(R.id.pager)
        val manager = activity.supportFragmentManager
        val adapter = PagerAdapter(manager)
        mPager.adapter = adapter

        tabLayout.setupWithViewPager(mPager)
        mPager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayout))
        tabLayout.setTabsFromPagerAdapter(adapter)

        return view
    }

    inner class PagerAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm) {

        override fun getItem(position: Int): Fragment? {

            var frag: Fragment? = null
            when (position) {
                0 -> frag = ManagerTouristFragment()
                1 -> frag = ManagerFestivalFragment()
                2 -> frag = ManagerAlbumUpdateFragment()
                3 -> frag = ManagerUserFragment()
            }
            return frag
        }

        override fun getCount(): Int {
            return 4
        }

        override fun getPageTitle(position: Int): CharSequence {
            var title = ""
            when (position) {
                0 -> title = "Du Lịch"
                1 -> title = "Lễ Hội"
                2 -> title = "Ảnh Update"
                3 -> title = "Quyền"
            }

            return title
        }

    }
}