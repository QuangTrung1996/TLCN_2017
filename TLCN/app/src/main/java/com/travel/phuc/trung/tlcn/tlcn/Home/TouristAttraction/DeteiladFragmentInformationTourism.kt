package com.travel.phuc.trung.tlcn.tlcn.Home.TouristAttraction

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
import com.travel.phuc.trung.tlcn.tlcn.R

/**
 * Created by Admin on 16/11/2017.
 */
class DeteiladFragmentInformationTourism : Fragment() {

    private val sharedprperences : String="taikhoan";
    private var id_USER:String?=null
    var database: DatabaseReference
    var tenDD: TextView?=null
    var diaChi:TextView?=null
    var moTa:TextView?=null
    var btnLike: ImageButton?=null
    var btnComment:ImageButton?=null
    var soLuocLike:TextView?=null
    var soComment:TextView?= null
    var btnYeuThich:ImageButton?=null
    var danhGia: RatingBar?=null
    var idDL:String=""
    init {
        database    = FirebaseDatabase.getInstance().reference
    }
    var   TT: HomeInformationTourisData?=null
    var Arraychild:ArrayList<HomeDistrictsData>?= ArrayList();
    private lateinit var mDateSetListenner: DatePickerDialog.OnDateSetListener
    private var ThemVaoLichTrinh: Button? = null
    private var  viepager: ViewPager?=null

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater!!.inflate(R.layout.detailed_fragment_infromation_tourism, container, false);

        viepager=view.findViewById<ViewPager>(R.id.ViewPager_Hinhanh_chitiet);
        ThemVaoLichTrinh = view.findViewById<Button>(R.id.ThemVaoLichTrinh)
        btnYeuThich = view.findViewById(R.id.Btn_YeuThich_chitiet)

        //val TT:ThongTinDiaDiem_DL
        TT= arguments.getSerializable("data1") as HomeInformationTourisData
        anhxa(view)
        addTimepicker()
        Arraychild!!.add(HomeDistrictsData(TT!!.url))
        var adapter: PagerAdapter = DeteiladAdaprerImage(activity,Arraychild!!)
        adapter.notifyDataSetChanged()
        viepager!!.adapter=adapter
        idDL = TT!!.key
        addListAnh()
        ktYeuThich()
        btnYeuThich!!.setOnClickListener(){
            setyeuthich()
        }


        return view
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
//                        var calender:Calendar= Calendar.getInstance()
//                        database.child("YeuThich").child(id_USER).child(idDL).setValue(idDL)

                    }
//                    else{
//
//                    }
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
    }

    private fun addTimepicker() {
        ThemVaoLichTrinh!!.setOnClickListener({
            //            var a:frmDailogLichTrinh = frmDailogLichTrinh()
//            a.show(fragmentManager,"con heo")
            val intent = Intent(this.getContext(), DeteiladActivityCreateSchedules::class.java)
            this.getContext().startActivity(intent)
        })

    }
    fun doctaikhoan():Boolean {
        val sharedpreferences = this.activity.getSharedPreferences(sharedprperences, android.content.Context.MODE_PRIVATE)
        id_USER = sharedpreferences.getString("Uid", null)

        if (id_USER != null) {
            return true
            // truyen!!.truyenUser(uid,name,email,photoUrl)
        }
        return false
    }
}