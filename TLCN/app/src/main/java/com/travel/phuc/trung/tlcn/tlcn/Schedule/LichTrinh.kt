package com.travel.phuc.trung.tlcn.tlcn.Schedule

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.view.*
import android.widget.*
import com.github.sundeepk.compactcalendarview.CompactCalendarView
import com.github.sundeepk.compactcalendarview.domain.Event
import com.google.firebase.database.*
import com.travel.phuc.trung.tlcn.tlcn.R
import java.text.SimpleDateFormat
import java.util.*

class LichTrinh : Fragment() {

    val KEY_ID_USER = "ID_User"
    val key : String = "$1$2$3$4$5$6$"
    var id_User = "null"
    var key_value = ""
    var itemLongClickListener = -1
    lateinit var dateclick : Date

    // khai bao cho event
    private var mYear: Int = 0
    var mMonth: Int = 0
    var mDay: Int = 0
    var mHour: Int = 0
    var mMinute: Int = 0

    val sdf      : SimpleDateFormat = SimpleDateFormat("dd-MM-yyyy 'at' HH:mm")
    val sdf_date : SimpleDateFormat = SimpleDateFormat("dd-MM-yyyy")
    val sdf_time : SimpleDateFormat = SimpleDateFormat("HH:mm")

    val database : DatabaseReference
    init {
        database    = FirebaseDatabase.getInstance().reference
    }

    // khai bao cho lich
    lateinit var compactCalendar : CompactCalendarView
    lateinit var lvLich : ListView
    lateinit var adapter : LichTrinh_list_day_adapter
    val list_lich_event : ArrayList<LichTrinh_data_event> = ArrayList()
    val list_lich_list : ArrayList<LichTrinh_data_list> = ArrayList()

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater!!.inflate(R.layout.schedule_fragment, container, false)

        val sharedPreferences = activity.getSharedPreferences("taikhoan",android.content.Context.MODE_PRIVATE)
        id_User = sharedPreferences.getString("Uid","null")

        lvLich  = view.findViewById(R.id.list_lichtrinh_day)
        compactCalendar = view.findViewById(R.id.compactcalendar_view)
        compactCalendar.setLocale(TimeZone.getTimeZone("GMT+07:00"), Locale.ENGLISH)
        compactCalendar.setUseThreeLetterAbbreviation(true)
        compactCalendar.setFirstDayOfWeek(Calendar.MONDAY)

        // listview khi keo len cung co the cuon len
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            lvLich.setNestedScrollingEnabled(true);
        }
        /** Registering context menu for the listview */
        registerForContextMenu(lvLich)

        if (id_User != "null"){
            AddList()
            adapter = LichTrinh_list_day_adapter(activity, list_lich_list)
            lvLich.adapter = adapter
        }
        else{
            ShortToast("Chưa đăng nhập")
        }

        lvLich.onItemLongClickListener = AdapterView.OnItemLongClickListener { p0, p1, p2, p3 ->
            val lich = list_lich_list[p2]
            val size = list_lich_event.size - 1
            var event : LichTrinh_data_event

            for (i in 0..size){
                event = list_lich_event[i]

                if(event.key == lich.key){
                    key_value = event.key
                    itemLongClickListener = i
                }
            }
            false
        }

        // define a listener to receive callbacks when certain events happen.
        compactCalendar.setListener(object : CompactCalendarView.CompactCalendarViewListener {
            override fun onDayClick(dateClicked: Date) {
                dateclick = dateClicked
                LayNgayNay(dateClicked)
            }

            override fun onMonthScroll(firstDayOfNewMonth: Date) {
                ShortToast(firstDayOfNewMonth.toString())
            }
        })

        //addLich_test()

        return view
    }

    private fun addLich_test() {
        val ref = database.child("lich").child(id_User).push()
        val key = ref.key
        // 10/10/2017 - 20/10/2017
        val lich = LichTrinh_data_event(key, "trung", 1507568400000, 1508432400000, "chu thich")
        ref.setValue(lich)
    }

    // lay du lieu tren firebase va them vao list va compactCalendar
    private fun AddList() {
        database.child("lich").child(id_User).addChildEventListener(object : ChildEventListener {
            override fun onCancelled(p0: DatabaseError?) {}

            override fun onChildMoved(p0: DataSnapshot?, p1: String?) {}

            override fun onChildChanged(p0: DataSnapshot?, p1: String?) {
                key_value = p0!!.key
                val list = p0.getValue(LichTrinh_data_event::class.java)
                var lich = LichTrinh_data_event()

                for (i in 0..(list_lich_event.size-1)){
                    if(key_value == list_lich_event[i].key){
                        lich = list_lich_event[i]
                        list_lich_event[i] = list!!
                    }
                }
                val events_truoc = getEvents(lich.time_BD, lich.time_KT,lich.ten,lich.note,lich.key)
                val events_sau = getEvents(list!!.time_BD, list.time_KT,list.ten,list.note,lich.key)

                val arr: MutableList<String> = mutableListOf("0")  // vi tri 0 : ""
                list_lich_list.clear()

                for (i in 0..(list_lich_event.size-1)){
                    lich = list_lich_event[i]
                    val events = getEvents(lich.time_BD, lich.time_KT,lich.ten,lich.note,lich.key)
                    for (booking in events) {
                        TachEvent(arr,booking.data.toString())
                    }
                }

                loadEvents(arr)
                adapter.notifyDataSetChanged()

                compactCalendar.removeEvents(events_truoc)
                compactCalendar.addEvents(events_sau)
            }

            override fun onChildAdded(p0: DataSnapshot?, p1: String?) {
                val lich = p0!!.getValue(LichTrinh_data_event::class.java)
                val events = getEvents(lich!!.time_BD, lich.time_KT,lich.ten,lich.note,lich.key)
                val arr: MutableList<String> = mutableListOf("0")  // vi tri 0 : ""

                list_lich_event.add(lich)

                compactCalendar.addEvents(events)

                for (booking in events) {
                    TachEvent(arr,booking.data.toString())
                }
                loadEvents(arr)
                adapter.notifyDataSetChanged()
            }

            override fun onChildRemoved(p0: DataSnapshot?) {
                key_value = p0!!.key
                var lich = list_lich_event[itemLongClickListener]
                val events = getEvents(lich.time_BD, lich.time_KT,lich.ten,lich.note,lich.key)

                list_lich_event.removeAt(itemLongClickListener)     // xóa của biến tạm
                compactCalendar.removeEvents(events)

                val arr: MutableList<String> = mutableListOf("0")  // vi tri 0 : ""
                list_lich_list.clear()

                for (i in 0..(list_lich_event.size-1)){
                    lich = list_lich_event[i]
                    val events = getEvents(lich.time_BD, lich.time_KT,lich.ten,lich.note,lich.key)
                    for (booking in events) {
                        TachEvent(arr,booking.data.toString())
                    }
                }

                loadEvents(arr)
                adapter.notifyDataSetChanged()
            }
        })
    }

    // lay events co trong ngay duoc chon
    private fun LayNgayNay(date: Date) {
        val events = compactCalendar.getEvents(date)
        val arr: MutableList<String> = mutableListOf("0")  // vi tri 0 : ""

        if (events != null) {
            list_lich_list.clear()
            for (booking in events) {
                TachEvent(arr,booking.data.toString())
            }
            loadEvents(arr)
            adapter.notifyDataSetChanged()
        }
    }

    // lay su kien khi lick vao ngay tren lich
    private fun loadEvents(arr: MutableList<String>) {

        val temp = arr.size-1
        if (temp != 0){
            for (i in 0..(temp/5-1)){

                val mangLich = LichTrinh_data_list()
                val dateBD = java.sql.Date(arr[i*5+2].toLong())
                val dateKT = java.sql.Date(arr[i*5+3].toLong())
                val timeBD = java.sql.Time(arr[i*5+2].toLong())
                val timeKT = java.sql.Time(arr[i*5+3].toLong())

                mangLich.ten     = arr[i*5+1]
                mangLich.ngayBD  = sdf_date.format(dateBD)
                mangLich.gioBD   = sdf_time.format(timeBD)
                mangLich.ngayKT  = sdf_date.format(dateKT)
                mangLich.gioKT   = sdf_time.format(timeKT)
                mangLich.note    = arr[i*5+4]
                mangLich.key     = arr[i*5+5]

                list_lich_list.add(mangLich)
            }
        }
        else{
            ShortToast("Không có lịch")
        }
    }

    // tach chuoi event truyen vao
    private fun TachEvent(arr: MutableList<String>, event_data : String) {
        val substr : List<String> = event_data.split(key)
        for (j in 0..(substr.size-1)){
            arr.add(substr[j])
        }
    }

    // goi event de truyen vao lich
    private fun getEvents(timeInMillisBD: Long, timeInMillisKT: Long, ten: String, note: String, ma: String): List<Event> {

        // doi tu mili giay sang date
        val dateBD = java.sql.Date(timeInMillisBD)
        val dateKT = java.sql.Date(timeInMillisKT)

        // lay lai thong tin ngay bat dau va ngay ket thuc
        val date1 : Date = sdf.parse(sdf_date.format(dateBD).toString() + " at " + "23:59")
        val date2 : Date = sdf.parse(sdf_date.format(dateKT).toString() + " at " + "0:0")

        // chuoi ngay dau tien va cuoi cung cua su kien
        val string1 = ten +key+ timeInMillisBD +key+ date1.time +key+ note +key+ ma
        val string2 = ten +key+ date2.time +key+ timeInMillisKT +key+ note +key+ ma

        // tinh so ngay trong event
        val noDay = (timeInMillisKT - timeInMillisBD) / (24 * 3600 * 1000)

        // phan thuc hien so sanh
        if (noDay.toInt() == 0) {

            // chi co mot ngay
            val string : String = ten +key+ timeInMillisBD +key+ timeInMillisKT +key+ note +key+ ma
            return Arrays.asList(Event(Color.RED, timeInMillisBD, string))
        }
        else {
            if (noDay.toInt() == 1) {

                //co 2 ngay
                return Arrays.asList(
                        Event(Color.BLUE, timeInMillisBD, string1),
                        Event(Color.BLUE, timeInMillisKT, string2))
            }
            else {

                val event = Event(Color.GREEN, timeInMillisBD, string1)
                val mutableList : MutableList<Event> = mutableListOf(event)  // event dau tien

                // thoi gian bat dau va ket thuc cua 1 ngay trong 1 su kien nhieu ngay : bat dau luc 0h va ket thuc luc 23:59
                var date_time_BD : Date
                var date_time_KT : Date

                // lay ngay de co the tang len 1 ngay, tao vong lap
                var d : Date
                val c = Calendar.getInstance()
                c.time = dateBD

                // lay chuoi vao mang
                for (i in 1..(noDay-1)){
                    c.add(Calendar.DATE, 1)
                    d = c.time

                    // doi tu mili giay sang date
                    val date = java.sql.Date(d.time)

                    // thoi gian bat dau va ket thuc cua 1 ngay trong 1 su kien nhieu ngay : bat dau luc 0h va ket thuc luc 23:59
                    date_time_BD = sdf.parse(sdf_date.format(date).toString() + " at " + "0:0")
                    date_time_KT = sdf.parse(sdf_date.format(date).toString() + " at " + "23:59")

                    // chuoi ngay dau tien va cuoi cung cua su kien
                    val string_date_time = ten +key+ date_time_BD.time +key+ date_time_KT.time +key+ note +key+ ma
                    mutableList.add(Event(Color.GREEN, date_time_BD.time, string_date_time))
                }

                // lay chuoi ngay cuoi cung
                mutableList.add(Event(Color.GREEN, timeInMillisKT, string2))

                val list : List<Event> = mutableList
                return list
            }
        }
    }

    // phan su ly list
    override fun onCreateContextMenu(menu: ContextMenu?, v: View?, menuInfo: ContextMenu.ContextMenuInfo?) {
        super.onCreateContextMenu(menu, v, menuInfo)
        activity.menuInflater.inflate(R.menu.lichtrinh_list_menu,menu)
    }

    // chay khi chon add , edit , delete
    override fun onContextItemSelected(item: MenuItem?): Boolean {
        when (item!!.getItemId()) {
            R.id.cnt_mnu_add ->{
                val mBuuilder : AlertDialog.Builder = AlertDialog.Builder(activity)
                val mView : View = layoutInflater.inflate(R.layout.lichtrinh_list_edit,null)

                val edit_tieude : EditText = mView.findViewById(R.id.edit_tieude)
                val edit_ngayBD : EditText = mView.findViewById(R.id.edit_ngayBD)
                val edit_gioBD  : EditText = mView.findViewById(R.id.edit_gioBD)
                val edit_ngayKT : EditText = mView.findViewById(R.id.edit_ngayKT)
                val edit_gioKT  : EditText = mView.findViewById(R.id.edit_gioKT)
                val edit_note   : EditText = mView.findViewById(R.id.edit_note)

                val btnAdd          : Button = mView.findViewById(R.id.btn_add)
                val btnNhapNgayBD   : Button = mView.findViewById(R.id.btn_set_ngayBD)
                val btnNhapNgayKT   : Button = mView.findViewById(R.id.btn_set_ngayKT)
                val btnNhapGioBD    : Button = mView.findViewById(R.id.btn_set_gioBD)
                val btnNhapGioKT    : Button = mView.findViewById(R.id.btn_set_gioKT)

                mBuuilder.setView(mView)
                val dialog : AlertDialog = mBuuilder.create()
                dialog.show()

                btnNhapNgayBD.setOnClickListener(object : View.OnClickListener{
                    override fun onClick(p0: View?) {
                        getNgay()
                        val datePickerDialog = DatePickerDialog(activity,
                                DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                                    edit_ngayBD.setText(dayOfMonth.toString() + "-" + (monthOfYear + 1) + "-" + year)
                                }, mYear, mMonth, mDay)
                        datePickerDialog.show()
                    }
                })

                btnNhapNgayKT.setOnClickListener(object : View.OnClickListener{
                    override fun onClick(p0: View?) {
                        getNgay()
                        val datePickerDialog = DatePickerDialog(activity,
                                DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                                    edit_ngayKT.setText(
                                            dayOfMonth.toString() + "-" + (monthOfYear + 1) + "-" + year)
                                }, mYear, mMonth, mDay)
                        datePickerDialog.show()
                    }
                })

                btnNhapGioBD.setOnClickListener(object : View.OnClickListener{
                    override fun onClick(p0: View?) {
                        getGio()
                        val timePickerDialog = TimePickerDialog(activity,
                                TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
                                    edit_gioBD.setText(hourOfDay.toString() + ":" + minute)
                                }, mHour, mMinute, true)
                        timePickerDialog.show()
                    }
                })

                btnNhapGioKT.setOnClickListener(object : View.OnClickListener{
                    override fun onClick(p0: View?) {
                        getGio()
                        val timePickerDialog = TimePickerDialog(activity,
                                TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
                                    edit_gioKT.setText(hourOfDay.toString() + ":" + minute)
                                }, mHour, mMinute, true)
                        timePickerDialog.show()
                    }
                })

                btnAdd.setOnClickListener(object : View.OnClickListener{
                    override fun onClick(p0: View?) {
                        if(edit_ngayBD.text.isEmpty() || edit_ngayKT.text.isEmpty() ||
                                edit_gioBD.text.isEmpty() || edit_gioKT.text.isEmpty()){
                            ShortToast("Empty")
                        }
                        else{

                            val date1 : Date = sdf.parse(
                                    edit_ngayBD.text.toString() + " at " + edit_gioBD.text.toString())
                            val date2 : Date = sdf.parse(
                                    edit_ngayKT.text.toString() + " at " + edit_gioKT.text.toString())

                            // ngay BD co truoc hoac cung thoi gian voi ngay KT
                            if (date1.before(date2) || date1.equals(date2)) {
                                val ref = database.child("lich").child(id_User).push()
                                val key = ref.key

                                val lich = LichTrinh_data_event(
                                        key,
                                        edit_tieude.text.toString(),
                                        date1.time,
                                        date2.time,
                                        edit_note.text.toString())

                                ref.setValue(lich)
                                dialog.dismiss()
                            }
                            else{
                                ShortToast("Thoi gian ban nhap khong hop ly")
                            }
                        }
                    }
                })

                return true
            }

            R.id.cnt_mnu_edit ->{
                return true
            }

            R.id.cnt_mnu_delete ->{
                val ref = database.child("lich").child(id_User).child(""+ key_value)
                if (key_value != ""){
                    ref.removeValue()
                }else{
                    ShortToast("không tìm thấy")
                }
                return true
            }

            else -> {
                return super.onContextItemSelected(item)
            }
        }
    }

    // lay gio hien tai
    private fun getGio() {
        // Get Current Time
        val c = Calendar.getInstance();
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);
    }

    // lay ngay hien tai
    private fun getNgay() {
        // Get Current Date
        val c = Calendar.getInstance()
        mYear = c.get(Calendar.YEAR)
        mMonth = c.get(Calendar.MONTH)
        mDay = c.get(Calendar.DAY_OF_MONTH)
    }

    // hien dong thong bao
    private fun ShortToast(messsage : String) {
        val length : Int = Toast.LENGTH_SHORT
        Toast.makeText(activity, messsage, length).show()
    }
}