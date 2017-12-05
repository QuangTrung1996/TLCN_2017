package com.travel.phuc.trung.tlcn.tlcn.Home.TouristAttraction

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import android.widget.ExpandableListView
import android.widget.TextView
import com.travel.phuc.trung.tlcn.tlcn.R


// adapter cho expandable tinh thành quận huyện
class HomeExPdbProvinces constructor(context: Context, mHeaderGroup:ArrayList<HomeProvincesData>, mDataChild: HashMap<HomeProvincesData,ArrayList<HomeDistrictsData>>) : BaseExpandableListAdapter() {
    private var context:Context?=context;
    private var mHeaderGroup:ArrayList<HomeProvincesData>?=mHeaderGroup
    private var mDataChild: HashMap<HomeProvincesData,ArrayList<HomeDistrictsData>>?= mDataChild;
    override fun getGroup(groupPosition: Int): Any {
        return mHeaderGroup!!.get(groupPosition)    }

    override fun isChildSelectable(p0: Int, p1: Int): Boolean {
        return true;
    }

    override fun hasStableIds(): Boolean {
        return false
    }

    override fun getGroupView(groupPosition: Int, isExpanded: Boolean, convertView: View?, parent: ViewGroup?): View {
        var view:View?
        view=convertView
        if (convertView == null) {
            val inflater = this.context!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            view = inflater.inflate(R.layout.row_provinces, null)

        }
        val tenquan = view!!.findViewById<TextView>(R.id.tinhthanh) as TextView
        val quanhuyen = view.findViewById<TextView>(R.id.soquanhuyen) as TextView
        tenquan.text=mHeaderGroup!!.get(groupPosition).Tinh.toString()
        quanhuyen.text = mHeaderGroup!!.get(groupPosition).QuanHuyen.toString().plus(" Quận Huyện")
        // set su kien khi click vao so duong trong expendlbe listview
        quanhuyen.setOnClickListener {
            if (isExpanded)
                (parent as ExpandableListView).collapseGroup(groupPosition)
            else
                (parent as ExpandableListView).expandGroup(groupPosition, true)
        }
        return view;
    }

    override fun getChildrenCount(groupPosition: Int): Int {
        return mDataChild!!.get(mHeaderGroup!!.get(groupPosition))!!.size    }

    override fun getChild(groupPosition: Int, childPosition: Int): Any {
        return mDataChild!!.get(mHeaderGroup!!.get(groupPosition))!!.get(childPosition)
    }

    override fun getGroupId(groupPosition: Int): Long {
        return groupPosition.toLong();
    }

    override fun getChildView(groupPosition: Int, childPosition: Int, isLastChild: Boolean, convertView: View?, parent: ViewGroup?): View {
        var view:View?;
        view=convertView
        if (view==null){
            val inflater = LayoutInflater.from(context)
            view = inflater!!.inflate(R.layout.row_districts, parent, false)
        }
        val tenduong = view!!.findViewById<TextView>(R.id.tenquanhuyen) as TextView
        tenduong.text=(getChild(groupPosition, childPosition) as HomeDistrictsData).TenQuanHuyen
        return view    }

    override fun getChildId(groupPosition: Int, childPosition: Int): Long {
        return childPosition.toLong()
    }

    override fun getGroupCount(): Int {
        return mHeaderGroup!!.size
    }

}