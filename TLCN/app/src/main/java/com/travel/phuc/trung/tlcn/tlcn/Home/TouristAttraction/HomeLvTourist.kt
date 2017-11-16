package com.travel.phuc.trung.tlcn.tlcn.Home.TouristAttraction

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.bumptech.glide.Glide
import com.travel.phuc.trung.tlcn.tlcn.Home.HomeActivityComment
import com.travel.phuc.trung.tlcn.tlcn.R
import de.hdodenhof.circleimageview.CircleImageView

/**
 * Created by Admin on 16/11/2017.
 */
// lớp adapter của địa điểm du lịch
class HomeLvTourist(var context: Context, var arrayList:ArrayList<HomeInformationTourisData>) : BaseAdapter() {
    class viewHolder(row : View) {
        var Khung_TTDL: LinearLayout
        var anhdaidien: CircleImageView
        var tenDD: TextView
        var diachi: TextView
        var btn_like: ImageButton
        var danhgia: RatingBar
        var soluoclike: TextView
        var btn_comment:ImageButton
        var soComment:TextView
        var anh:ImageView
        init {
            Khung_TTDL = row.findViewById(R.id.id_DuLich)
            anhdaidien=row.findViewById(R.id.AnhDaijDien_DL)
            tenDD=row.findViewById(R.id.TenDiaDiem_DL)
            diachi=row.findViewById(R.id.DiaChi_DL)
            anh=row.findViewById(R.id.Anh_DL)
            btn_like=row.findViewById(R.id.Btn_Like)
            danhgia=row.findViewById(R.id.DanhGia)
            soluoclike = row.findViewById(R.id.SoLuotLike)
            soComment=row.findViewById(R.id.SoComment)
            btn_comment=row.findViewById(R.id.Btn_Comment)

        }
    }
    override fun getView(position: Int, convertview: View?, p2: ViewGroup?): View {
        var view:View?
        var viewHolder: HomeLvTourist.viewHolder
        if (convertview==null)
        {
            var layoutInflater: LayoutInflater = LayoutInflater.from(context)
            view=layoutInflater.inflate(R.layout.home_row_information_tourisr, null)
            viewHolder= HomeLvTourist.viewHolder(view)
            view.tag=viewHolder
        }
        else{
            view=convertview
            viewHolder= convertview.tag as viewHolder
        }
        viewHolder.tenDD.text=arrayList.get(position).TenDiaDiem
        viewHolder.diachi.text = arrayList.get(position).DiaChi
        Glide.with(context).load(arrayList!!.get(position).url)
                .centerCrop()
                .placeholder(R.drawable.loading_image)
                .error(R.drawable.wellcom0)
                .into(viewHolder.anh)
        Glide.with(context).load(arrayList!!.get(position).url)
                .centerCrop()
                .error(R.drawable.wellcom0)
                .into(viewHolder.anhdaidien)
        viewHolder.soluoclike.text=arrayList.get(position).LuocLike.toString()
        viewHolder.danhgia.rating=arrayList.get(position).DanhGia
        viewHolder.soComment.text = arrayList.get(position).SoCmmt.toString()
        viewHolder.Khung_TTDL.setOnClickListener(){
            val intent = Intent(view!!.getContext(), HomeActivityDeteiladTourism::class.java)
            var Thongtin:HomeInformationTourisData
            Thongtin= arrayList.get(position)
            //Toast.makeText(view!!.context,Thongtin.toString(),Toast.LENGTH_SHORT).show()
            intent.putExtra("data",Thongtin)
            context.startActivity(intent)
        }
        viewHolder.btn_comment.setOnClickListener(){
            val intent = Intent(view!!.getContext(), HomeActivityComment::class.java)
            intent.putExtra("keyDL",arrayList.get(position).key)
            view!!.getContext().startActivity(intent)
        }
        return view as View
    }

    override fun getItem(p0: Int): Any {
        return arrayList.get(p0)
    }

    override fun getItemId(p0: Int): Long {
        return p0.toLong()
    }

    override fun getCount(): Int {
        return arrayList.size
    }
}