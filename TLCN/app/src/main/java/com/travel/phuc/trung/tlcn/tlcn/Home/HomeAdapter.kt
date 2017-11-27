package com.travel.phuc.trung.tlcn.tlcn.Home

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.view.PagerAdapter

// lớp adapter của faragment dulich và lễ hội
class HomeAdapter (fm: FragmentManager) : FragmentStatePagerAdapter(fm) {
    private val tabTitles = arrayOf("Du Lịch", "Lễ Hội");
    private  var FrmItem: ArrayList<Fragment> = ArrayList();

    override fun getItem(position: Int): Fragment {
        return FrmItem[position];
    }

    override fun getCount(): Int {
        return FrmItem.size
    }
    // them fragment vào pageradapter
    fun addfragment(frmitem:Fragment){

        FrmItem.add(frmitem);
    }
    override fun getPageTitle(position: Int): CharSequence {
        return tabTitles[position];
    }
}