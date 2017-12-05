package com.travel.phuc.trung.tlcn.tlcn.Favorite

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.bumptech.glide.Glide
import com.google.firebase.database.*
import com.travel.phuc.trung.tlcn.tlcn.GoogleMap.MapsActivity
import com.travel.phuc.trung.tlcn.tlcn.Home.TouristAttraction.HomeInformationTourisData
import com.travel.phuc.trung.tlcn.tlcn.R

/**
 * Created by Admin on 26/11/2017.
 */
class FavoriteAdapter(var context: Context, var arrayList:ArrayList<HomeInformationTourisData>) : BaseAdapter() {
    val sharedprperences : String="taikhoan";
    var id_USER :String?=null
    var ten:String? =null
    var ten_email:String? = null
    var hinhDaiDien:String?=null
    val database : DatabaseReference
    init {
        database    = FirebaseDatabase.getInstance().reference
    }
        class viewHolder(row : View) {

            var Khung_TTDL: ImageView
            var tenDD: TextView
            var btn_like: ImageView

            init {

                Khung_TTDL = row.findViewById(R.id.anhdaidienfavorist)
                tenDD=row.findViewById(R.id.tenddfavorist)
                btn_like=row.findViewById(R.id.Image_YeuThich)

            }
        }
        override fun getView(position: Int, convertview: View?, p2: ViewGroup?): View {


            var view: View?
            var viewHolder: FavoriteAdapter.viewHolder
            if (convertview==null)
            {
                var layoutInflater: LayoutInflater = LayoutInflater.from(context)
                view=layoutInflater.inflate(R.layout.favorite_row_tourist, null)
                viewHolder= FavoriteAdapter.viewHolder(view)
                view.tag=viewHolder

            }
            else{
                view=convertview
                viewHolder= convertview.tag as viewHolder
            }

            viewHolder.tenDD.text=arrayList.get(position).TenDiaDiem
            Glide.with(context).load(arrayList!!.get(position).url)
                    .centerCrop()
                    .bitmapTransform(RoundedCornersTransformation(context,15,2))
                    .error(R.drawable.wellcom0)
                    .into(viewHolder.Khung_TTDL)


            //viewHolder.soluoclike.text=arrayList.get(position).LuocLike.toString()

            viewHolder.Khung_TTDL.setOnClickListener(){
                    val intent = Intent(view!!.getContext(), MapsActivity::class.java)
                    var Thongtin: HomeInformationTourisData
                    Thongtin = arrayList.get(position)
                    //Toast.makeText(view!!.context,arrayList.get(position).SoCmmt.toString(),Toast.LENGTH_SHORT).show()
                    intent.putExtra("data", Thongtin)
                    context.startActivity(intent)

            }
            viewHolder.btn_like.setOnClickListener({
                if(doctaikhoan()){
                    database.child("YeuThich").child(id_USER).child(arrayList.get(position).key.toString()).addListenerForSingleValueEvent(object :ValueEventListener{
                        override fun onCancelled(p0: DatabaseError?) {
                            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                        }

                        override fun onDataChange(p0: DataSnapshot?) {
                            if (p0!!.exists()){
                                viewHolder!!.btn_like.setImageResource(R.drawable.chuathich)
//                        var calender:Calendar= Calendar.getInstance()
                                database.child("YeuThich").child(id_USER).child(arrayList.get(position).key.toString()).removeValue()
                            }
                            else{
                                viewHolder.btn_like!!.setImageResource(R.drawable.favorist)
                                database.child("YeuThich").child(id_USER).child(arrayList.get(position).key.toString()).setValue(arrayList.get(position).key.toString())
                            }
                        }

                    })
                }
                else{
                    Toast.makeText(context,"Bạn cần đăng nhập",Toast.LENGTH_SHORT).show()
                }
            })
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
    private fun doctaikhoan():Boolean {
        val sharedpreferences = context.getSharedPreferences(sharedprperences, android.content.Context.MODE_PRIVATE)
        id_USER = sharedpreferences.getString("Uid", null)
        ten_email = sharedpreferences.getString("Uemail", null)
        hinhDaiDien = sharedpreferences.getString("UURLAnh", null)
        ten = sharedpreferences.getString("Uname", null)
        if (id_USER != null) {

            return true
        }
        return false
    }


}