package com.travel.phuc.trung.tlcn.tlcn.AddInfromation

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.travel.phuc.trung.tlcn.tlcn.Home.HomeActivityImage
import com.travel.phuc.trung.tlcn.tlcn.Home.HomeLvComment
import com.travel.phuc.trung.tlcn.tlcn.R
import de.hdodenhof.circleimageview.CircleImageView

/**
 * Created by Admin on 8/12/2017.
 */
class InfrormationAddedAdapter constructor(var context:Context,var listThongtin:ArrayList<InformationDataAdapter>) : BaseAdapter() {
    inner class viewholder(row: View)
    {
        var anh:ImageView
        var Ten: TextView
        var ChinhSua: TextView
        var Album: TextView
        init {
            anh = row.findViewById(R.id.anhthongtindang)
            Ten=row.findViewById<TextView>(R.id.tenddadded)
            ChinhSua=row.findViewById<TextView>(R.id.chinhsuathongtin)
            Album =row.findViewById<TextView>(R.id.chinhsuaAlbum)
        }
    }
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var view: View?
        val viewHolder: viewholder
        if (convertView == null) {
            var layoutInflater: LayoutInflater = LayoutInflater.from(context)
            view = layoutInflater.inflate(R.layout.information_row_added, null)
            viewHolder = viewholder(view)
            view.tag = viewHolder
        } else {
            view = convertView
            viewHolder = convertView.tag as viewholder
        }

        viewHolder.Ten.text = listThongtin.get(position).ten
        viewHolder.ChinhSua.setOnClickListener {
            if (listThongtin.get(position).loai ==1) {
                val intent = Intent(view!!.getContext(), ChangeInformationTourist::class.java)
                intent.putExtra("key",listThongtin.get(position).key)
                intent.putExtra("loai",1)
                view!!.getContext().startActivity(intent)
            }
            if (listThongtin.get(position).loai ==2)
            {
                val intent = Intent(view!!.getContext(), ChangInfomationfestival::class.java)
                intent.putExtra("key",listThongtin.get(position).key)
                intent.putExtra("loai",2)
                view!!.getContext().startActivity(intent)
            }
            if (listThongtin.get(position).loai ==3) {
                val intent = Intent(view!!.getContext(), ChangeInformationTourist::class.java)
                intent.putExtra("key",listThongtin.get(position).key)
                intent.putExtra("loai",3)
                view!!.getContext().startActivity(intent)
            }
            if (listThongtin.get(position).loai ==4)
            {
                val intent = Intent(view!!.getContext(), ChangInfomationfestival::class.java)
                intent.putExtra("key",listThongtin.get(position).key)
                intent.putExtra("loai",4)
                view!!.getContext().startActivity(intent)
            }

        }
        viewHolder.Album.setOnClickListener({
            val intent = Intent(view!!.getContext(), AddAlbum::class.java)
            intent.putExtra("key",listThongtin.get(position).key)
            intent.putExtra("loaiDD",listThongtin.get(position).loai)
            view!!.getContext().startActivity(intent)
        })
        Glide.with(context).load(listThongtin.get(position).linkanh)
                .centerCrop()
                .into(viewHolder.anh)
        return view as View
    }

    override fun getItem(position: Int): Any {
        return listThongtin.get(position)    }

    override fun getItemId(position: Int): Long {
       return position.toLong()
    }

    override fun getCount(): Int {
        return listThongtin.size
    }
}