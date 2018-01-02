package com.travel.phuc.trung.tlcn.tlcn.Home.festivalVenues

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.bumptech.glide.Glide
import com.travel.phuc.trung.tlcn.tlcn.GoogleMap.MapsActivity
import com.travel.phuc.trung.tlcn.tlcn.Home.HomeActivityComment
import com.travel.phuc.trung.tlcn.tlcn.Home.HomeActivityLike
import com.travel.phuc.trung.tlcn.tlcn.Home.HomeTypeAdapter
import com.travel.phuc.trung.tlcn.tlcn.Home.TouristAttraction.HomeInformationTourisData
import com.travel.phuc.trung.tlcn.tlcn.Home.TouristAttraction.HomeLvTourist
import com.travel.phuc.trung.tlcn.tlcn.R
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by Admin on 4/12/2017.
 */
class HomeFestivalListAdapter constructor(var context:Context,var listFestival:ArrayList<DataFestival>): BaseAdapter() {
    class viewHolder(row : View) {
        var NgayBD: TextView
        var Khung_DDLH: LinearLayout
        var anhdaidien: ImageView
        var tenDD: TextView
        var diachi: TextView
        var like: TextView
        var comment: TextView
        var canhbao:ImageView
        init {
            NgayBD = row.findViewById<TextView>(R.id.ngayBDLH)
            Khung_DDLH = row.findViewById(R.id.id_LeHoi)
            anhdaidien=row.findViewById(R.id.AnhDaijDien_LH)
            tenDD=row.findViewById(R.id.Ten_LH)
            diachi=row.findViewById(R.id.DiaChi_LH)
            like=row.findViewById(R.id.yeuthichLH)
            comment=row.findViewById(R.id.binhluanLH)
            canhbao = row.findViewById(R.id.thoigian)

        }
    }
    override fun getView(position: Int, convertview: View?, p2: ViewGroup?): View {
        var view:View?
        var viewHolder: HomeFestivalListAdapter.viewHolder
        if (convertview==null)
        {
            var layoutInflater: LayoutInflater = LayoutInflater.from(context)
            view=layoutInflater.inflate(R.layout.home_row_fragment_information_festival, null)
            viewHolder= HomeFestivalListAdapter.viewHolder(view)
            view.tag=viewHolder

        }
        else{
            view=convertview
            viewHolder= convertview.tag as HomeFestivalListAdapter.viewHolder
        }
        var calendar:Calendar = Calendar.getInstance()
        var a =calendar.timeInMillis
        if (a> listFestival.get(position).NgayKT)
        {
            viewHolder.canhbao.setImageResource(R.drawable.daqua)
        }
        else
        {
            viewHolder.canhbao.setImageResource(R.drawable.chuatoi)
        }
        val sdf_date_bd : SimpleDateFormat = SimpleDateFormat("dd-MM-yyyy")
        val sdf_date_bd1 : SimpleDateFormat = SimpleDateFormat("dd-MM-yyyy")
        var ngaybd =sdf_date_bd.format(listFestival.get(position).NgayBD)
        val ngaykt=sdf_date_bd1.format(listFestival.get(position).NgayKT)
        viewHolder.NgayBD.text = ngaybd.toString().plus(" - ").plus(ngaykt.toString())
        viewHolder.diachi.text = listFestival.get(position).DiaChi
        viewHolder.tenDD.text = listFestival.get(position).TenDiaDiem
        Glide.with(context).load(listFestival.get(position).url)
                .centerCrop()
                .error(R.drawable.wellcom0)
                .into(viewHolder.anhdaidien)
        viewHolder.Khung_DDLH.setOnClickListener({
            val intent = Intent(view!!.getContext(),FestivalDetail::class.java)
            var Thongtin: DataFestival
            Thongtin = listFestival.get(position)
            //Toast.makeText(view!!.context,arrayList.get(position).SoCmmt.toString(),Toast.LENGTH_SHORT).show()
            intent.putExtra("data", Thongtin)
            context.startActivity(intent)
        })
        viewHolder.like.setOnClickListener {
            val intent = Intent(view!!.getContext(), HomeActivityLike::class.java)
            intent.putExtra("keyDDDL",listFestival.get(position).key)
            view!!.getContext().startActivity(intent)
        }
        viewHolder.comment.setOnClickListener {
            val intent = Intent(view!!.getContext(), HomeActivityComment::class.java)
            intent.putExtra("keyDL",listFestival.get(position).key)
            intent.putExtra("key",0)
            intent.putExtra("tendd",listFestival.get(position).TenDiaDiem)
            view!!.getContext().startActivity(intent)
        }
         return view as View
    }

    override fun getItem(position: Int): Any {
        return  listFestival.get(position)

    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
       return listFestival.size
    }
}