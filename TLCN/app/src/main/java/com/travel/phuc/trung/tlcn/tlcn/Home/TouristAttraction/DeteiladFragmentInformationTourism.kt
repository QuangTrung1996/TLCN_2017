package com.travel.phuc.trung.tlcn.tlcn.Home.TouristAttraction

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.google.firebase.database.*
import com.travel.phuc.trung.tlcn.tlcn.Conect.CheckInternet
import com.travel.phuc.trung.tlcn.tlcn.Home.HomeRatingData
import com.travel.phuc.trung.tlcn.tlcn.Home.TouristAttraction.HomeFragmentTouristDestination.Companion.a
import com.travel.phuc.trung.tlcn.tlcn.R
import kotlinx.android.synthetic.main.detailed_fragment_infromation_tourism.*

/**
 * Created by Admin on 16/11/2017.
 */
class DeteiladFragmentInformationTourism : Fragment() {
//    override fun check(flag: Boolean) {
//        if (flag == true)
//        {
//            Toast.makeText(activity,"ngủ",Toast.LENGTH_SHORT).show()
//        }
//    }

    private val sharedprperences : String="taikhoan";
    private var id_USER:String?=null

    var tenDD: TextView?=null
    var diaChi:TextView?=null
    var moTa:TextView?=null
    var btnLike: ImageButton?=null
    var btnComment:ImageButton?=null
    var xacsnhandanhgia:Button?=null
    var soComment:TextView?= null
    var btnYeuThich:ImageButton?=null
    var danhGia: RatingBar?=null
    var btn_Disklike : ImageButton?=null
    var idDL:String=""
    var database: DatabaseReference
    init {
        database    = FirebaseDatabase.getInstance().reference
    }
    var   TT: HomeInformationTourisData?=null
    var Arraychild:ArrayList<HomeDistrictsData>?= ArrayList();
    private lateinit var mDateSetListenner: DatePickerDialog.OnDateSetListener
    private var ThemVaoLichTrinh: Button? = null
    private var  viepager: ViewPager?=null
    companion object {
         var listrating:ArrayList<HomeRatingData>?= ArrayList()
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater!!.inflate(R.layout.detailed_fragment_infromation_tourism, container, false);

        viepager=view.findViewById<ViewPager>(R.id.ViewPager_Hinhanh_chitiet);
        ThemVaoLichTrinh = view.findViewById<Button>(R.id.ThemVaoLichTrinh)
        btnYeuThich = view.findViewById(R.id.Btn_YeuThich_chitiet)
        btnLike = view.findViewById(R.id.Bnt_like_chitiet)
        xacsnhandanhgia = view.findViewById(R.id.XacNhanDAnhGia)
        TT= arguments.getSerializable("data1") as HomeInformationTourisData
        anhxa(view)
        addTimepicker(view)
        Arraychild!!.add(HomeDistrictsData(TT!!.url))
        var adapter: PagerAdapter = DeteiladAdaprerImage(activity,Arraychild!!)
        adapter.notifyDataSetChanged()
        viepager!!.adapter=adapter
        idDL = TT!!.key
        addListAnh()
        ktYeuThich()
        kiemtraDislike()
        kiemtraLike()
        setRating()

        btnYeuThich!!.setOnClickListener(){
            setyeuthich()
        }
        btnLike!!.setOnClickListener {
            setlike()
        }
        xacsnhandanhgia!!.setOnClickListener{
            themdanhgia()
        }
        return view
    }

    private fun themdanhgia() {
        database.child("DanhGia").child(idDL).child(id_USER).setValue(danhGia!!.rating.toString())
    }

    private fun setRating() {
        var sum=0.0
        var i=0
        database.child("DanhGia").child(idDL).addChildEventListener(object : ChildEventListener{
            override fun onCancelled(p0: DatabaseError?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onChildMoved(p0: DataSnapshot?, p1: String?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onChildChanged(p0: DataSnapshot?, p1: String?) {
                var sum =0.0;
                for (i in 0 until listrating!!.size)
                {
                    if (p0!!.key == listrating!!.get(i).key)
                    {
                        var a = p0!!.value as String
                        listrating!!.get(i).danhgia = a.toFloat()

                    }
                    sum+= listrating!!.get(i).danhgia
                }
                //
                var tb = Math.round((sum/ listrating!!.size)*10)/10.toFloat()
                danhGia!!.rating = tb

            }

            override fun onChildAdded(p0: DataSnapshot?, p1: String?) {
               var a = p0!!.value as String
                i++
                sum  = sum + a.toDouble()
                listrating!!.add(HomeRatingData(p0!!.key,a.toFloat()))
                var tam = sum /i
                var TB =(Math.round(tam*10))/10.toFloat()
                danhGia!!.rating = TB
            }

            override fun onChildRemoved(p0: DataSnapshot?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

        })
    }

    private fun setlike() {
        if(doctaikhoan()){
            database.child("Like").child(idDL).child(id_USER).addListenerForSingleValueEvent(object :ValueEventListener{
                override fun onCancelled(p0: DatabaseError?) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

                override fun onDataChange(p0: DataSnapshot?) {
                    if (p0!!.exists()){
                        btnLike!!.setImageResource(R.drawable.like)
//                        var calender:Calendar= Calendar.getInstance()
                        database.child("Like").child(idDL).child(id_USER).removeValue()
                    }
                    else{
                        btnLike!!.setImageResource(R.drawable.liked)
                        database.child("Like").child(idDL).child(id_USER).setValue(id_USER)
                    }
                }

            })
        }
        else{
            Toast.makeText(this.activity,"Bạn cần đăng nhập",Toast.LENGTH_SHORT).show()
        }
    }

    private fun kiemtraLike() {
        if (doctaikhoan())
        {
            database.child("Like").child(idDL).child(id_USER).addValueEventListener(object :ValueEventListener{
                override fun onCancelled(p0: DatabaseError?) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

                override fun onDataChange(p0: DataSnapshot?) {
                    if (p0!!.value!=null){
                        btnLike!!.setImageResource(R.drawable.liked)

                    }
//                    else
//                    {
//                        btnLike!!.setImageResource(R.drawable.like)
//                    }
                }

            })
        }
    }

    private fun kiemtraDislike() {
        if (doctaikhoan())
        {
            database.child("DisLike").child(idDL).child(id_USER).addValueEventListener(object :ValueEventListener{
                override fun onCancelled(p0: DatabaseError?) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

                override fun onDataChange(p0: DataSnapshot?) {
                    if (p0!!.value!=null){
                        btn_Disklike!!.setImageResource(R.drawable.disliked)

                    }
//                    else
//                    {
//                        btn_Disklike!!.setImageResource(R.drawable.dislike)
//                    }
                }

            })
        }
    }

    private fun setyeuthich() {

        if(doctaikhoan()){
            database.child("YeuThich").child(id_USER).child(idDL).addListenerForSingleValueEvent(object :ValueEventListener{
                override fun onCancelled(p0: DatabaseError?) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

                override fun onDataChange(p0: DataSnapshot?) {
                    if (p0!!.exists()){
                        btnYeuThich!!.setImageResource(R.drawable.chuayeuthich)
//                        var calender:Calendar= Calendar.getInstance()
                        database.child("YeuThich").child(id_USER).child(idDL).removeValue()
                        Toast.makeText(activity,"Đã xóa khỏi mục yêu thích",Toast.LENGTH_LONG).show()
                    }
                    else{
                        btnYeuThich!!.setImageResource(R.drawable.tim)
                        database.child("YeuThich").child(id_USER).child(idDL).setValue(idDL)
                        Toast.makeText(activity,"Đã thêm vào mục yêu thích",Toast.LENGTH_LONG).show()
                    }
                }

            })
        }
        else{
            Toast.makeText(this.activity,"Bạn cần đăng nhập",Toast.LENGTH_SHORT).show()
        }

    }



    private fun ktYeuThich() {
        if(doctaikhoan()){
            database.child("YeuThich").child(id_USER).child(idDL).addValueEventListener(object :ValueEventListener{
                override fun onCancelled(p0: DatabaseError?) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

                override fun onDataChange(p0: DataSnapshot?) {
                    if (p0!!.value!=null){
                        btnYeuThich!!.setImageResource(R.drawable.tim)

                    }
                }

            })
        }
        else{
            Toast.makeText(this.activity,"Bạn cần đăng nhập",Toast.LENGTH_SHORT).show()
        }
    }

    // laays danh sach album tu firebase
    private fun addListAnh() {
        database.child("AlbumAnhDuLich").child(idDL).addChildEventListener(object : ChildEventListener {
            override fun onCancelled(p0: DatabaseError?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onChildMoved(p0: DataSnapshot?, p1: String?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onChildChanged(p0: DataSnapshot?, p1: String?) {

            }

            override fun onChildAdded(p0: DataSnapshot?, p1: String?) {
                if (p0!=null){
                    Arraychild!!.add(HomeDistrictsData(p0!!.value.toString()))
                    var adapter:PagerAdapter= DeteiladAdaprerImage(activity,Arraychild!!)
                    adapter.notifyDataSetChanged()
                    viepager!!.adapter=adapter  }

            }

            override fun onChildRemoved(p0: DataSnapshot?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

        })
    }

    private fun anhxa(view: View) {
        tenDD=view.findViewById(R.id.Ten_DDDL_ChiTiet)
        tenDD!!.text =TT!!.TenDiaDiem
        moTa=view.findViewById(R.id.mota)
        moTa!!.text = TT!!.Mota
        btn_Disklike = view.findViewById<ImageButton>(R.id.Btn_Dislike_chitiet)
        danhGia=view.findViewById(R.id.DanhGia_chitiet)
    }
    private fun addTimepicker(view: View) {
        ThemVaoLichTrinh!!.setOnClickListener({
            //            var a:frmDailogLichTrinh = frmDailogLichTrinh()
//            a.show(fragmentManager,"con heo")
            val intent = Intent(this.getContext(), DeteiladActivityCreateSchedules::class.java)
            this.getContext().startActivity(intent)
        })
        btn_Disklike!!.setOnClickListener {
            setDislike()


        }

    }

    private fun setDislike() {
        if(doctaikhoan()){
            database.child("DisLike").child(idDL).child(id_USER).addListenerForSingleValueEvent(object :ValueEventListener{
                override fun onCancelled(p0: DatabaseError?) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

                override fun onDataChange(p0: DataSnapshot?) {
                    if (p0!!.exists()){
                        btn_Disklike!!.setImageResource(R.drawable.dislike)
//                        var calender:Calendar= Calendar.getInstance()
                        database.child("DisLike").child(idDL).child(id_USER).removeValue()
                    }
                    else{
                        val intent = Intent(activity, HomeActivityCheckDislike::class.java)
                        intent.putExtra("keyDL", idDL)
                        intent.putExtra("idUser", id_USER)
                        this@DeteiladFragmentInformationTourism.startActivity(intent)
                    }
                }

            })
        }
        else{
            Toast.makeText(this.activity,"Bạn cần đăng nhập",Toast.LENGTH_SHORT).show()
        }    }


    fun doctaikhoan():Boolean {
        val sharedpreferences = this.activity.getSharedPreferences(sharedprperences, android.content.Context.MODE_PRIVATE)
        id_USER = sharedpreferences.getString("Uid", null)

        if (id_USER != null && !id_USER!!.equals("")) {
            return true
            // truyen!!.truyenUser(uid,name,email,photoUrl)
        }
        return false
    }
}
