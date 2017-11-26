package com.travel.phuc.trung.tlcn.tlcn.Schedule

import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.*
import android.widget.AdapterView
import android.widget.ListView
import android.widget.Toast
import com.github.sundeepk.compactcalendarview.CompactCalendarView
import com.github.sundeepk.compactcalendarview.domain.Event
import com.google.firebase.database.*
import com.travel.phuc.trung.tlcn.tlcn.R
import java.text.SimpleDateFormat
import java.util.*



class ScheduleFragment : Fragment() {

    val sdf      : SimpleDateFormat = SimpleDateFormat("dd-MM-yyyy 'at' HH:mm", Locale.getDefault())
    val sdf_date : SimpleDateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
    val sdf_time : SimpleDateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
    var idUser : String = "null"
    val key : String = "$1$2$3$4$5$6$"
    var itemLongClickListener = -1
    lateinit var dateClick : Date
    var keyValue = ""

    val database : DatabaseReference = FirebaseDatabase.getInstance().reference

    // khai bao cho lich
    lateinit var compactCalendar : CompactCalendarView
    lateinit var lvLich : ListView
    lateinit var adapter : ScheduleAdapter
    val scheduleEvents : ArrayList<ScheduleEventData> = ArrayList() // dung lam su lien truyen len adapter
    val listEvents : ArrayList<ScheduleEventData> = ArrayList()     // luu cac gia tri lay tu firebase ve
    val date : Date = Date()

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater!!.inflate(R.layout.schedule_fragment,container,false)
        val sharedPreferences = activity.getSharedPreferences("taikhoan",android.content.Context.MODE_PRIVATE)
        idUser = sharedPreferences.getString("Uid","null")
        setHasOptionsMenu(true)

        lvLich  = view.findViewById(R.id.list_lichtrinh_day)
        compactCalendar = view.findViewById(R.id.compactcalendar_view)
        compactCalendar.setLocale(TimeZone.getTimeZone("GMT+07:00"), Locale.ENGLISH)
        compactCalendar.setUseThreeLetterAbbreviation(true)
        compactCalendar.setFirstDayOfWeek(Calendar.MONDAY)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            lvLich.isNestedScrollingEnabled = true
        }

        /** Registering context menu for the listView */
        registerForContextMenu(lvLich)

        if (idUser != "null"){
            addList()
            adapter = ScheduleAdapter(activity, scheduleEvents)
            lvLich.adapter = adapter
        }
        else{
            shortToast("Chưa đăng nhập")
        }

        // define a listener to receive callbacks when certain events happen.
        compactCalendar.setListener(object : CompactCalendarView.CompactCalendarViewListener {
            override fun onDayClick(dateClicked: Date) {
                getDate(dateClicked)
            }

            override fun onMonthScroll(firstDayOfNewMonth: Date) {
                shortToast(firstDayOfNewMonth.toString())
            }
        })

        lvLich.onItemLongClickListener = AdapterView.OnItemLongClickListener{ parent, mView, position, id ->
            keyValue = scheduleEvents[position].key
            false
        }

        return view
    }

    private fun getDate(dateClicked: Date) {
        dateClick = dateClicked
        val events = compactCalendar.getEvents(dateClicked)
        val arr: MutableList<String> = mutableListOf("0")  // vi tri 0 : ""

        if (events != null) {
            scheduleEvents.clear()
            for (booking in events) {
                splitEvent(arr,booking.data.toString())
            }
            loadEvents(arr)
            adapter.notifyDataSetChanged()
        }
    }

    // get calendar FireBase ve , luu vao ListView va CompactCalendarView
    private fun addList() {
        database.child("schedule").child(idUser).addChildEventListener(object : ChildEventListener {
            override fun onCancelled(p0: DatabaseError?) {

            }

            override fun onChildMoved(p0: DataSnapshot?, p1: String?) {

            }

            override fun onChildChanged(p0: DataSnapshot?, p1: String?) {
                val scheduleAfter = p0!!.getValue(ScheduleEventData::class.java)
                var schedule = ScheduleEventData()

                // thay doi phan tu co cung key trong danh sach 'luu cac gia tri lay tu firebase ve'
                for (i in 0..(listEvents.size-1)){
                    if(scheduleAfter!!.key == listEvents[i].key){
                        schedule = listEvents[i]
                        listEvents[i] = scheduleAfter
                    }
                }

                val eventsBefore = getEvents(schedule.ngayBD.toLong(), schedule.ngayKT.toLong(), schedule.title,schedule.detail,schedule.key)
                val eventsAfter = getEvents(scheduleAfter!!.ngayBD.toLong(), scheduleAfter.ngayKT.toLong(), scheduleAfter.title,scheduleAfter.detail,scheduleAfter.key)
                compactCalendar.removeEvents(eventsBefore)
                compactCalendar.addEvents(eventsAfter)

                getDate(dateClick)
            }

            override fun onChildAdded(p0: DataSnapshot?, p1: String?) {
                val schedule = p0!!.getValue(ScheduleEventData::class.java)

                listEvents.add(schedule!!)

                val events = getEvents(schedule.ngayBD.toLong(), schedule.ngayKT.toLong(), schedule.title,schedule.detail,schedule.key)

                compactCalendar.addEvents(events)

                getDate(date)
            }

            override fun onChildRemoved(p0: DataSnapshot?) {
                var temp = -1
                var schedule = ScheduleEventData()

                // thay doi phan tu co cung key trong danh sach 'luu cac gia tri lay tu firebase ve'
                for (i : Int in 0..(listEvents.size-1)){
                    if(p0!!.key == listEvents[i].key){
                        schedule = listEvents[i]
                        temp = i
                    }
                }
                listEvents.removeAt(temp)

                val events = getEvents(schedule.ngayBD.toLong(), schedule.ngayKT.toLong(), schedule.title,schedule.detail,schedule.key)
                compactCalendar.removeEvents(events)
                getDate(dateClick)
            }
        })
    }

    // get event into list
    private fun loadEvents(arr: MutableList<String>) {
        val temp = arr.size-1
        if (temp != 0){
            for (i in 0..(temp/5-1)){

//                val dateBD = java.sql.Date(arr[i*5+2].toLong())
//                val dateKT = java.sql.Date(arr[i*5+3].toLong())
                val timeBD = java.sql.Time(arr[i*5+2].toLong())
                val timeKT = java.sql.Time(arr[i*5+3].toLong())

                val mangLich = ScheduleEventData()
                mangLich.title   = arr[i*5+1]

                if (sdf_time.format(timeBD) == "00:00" && sdf_time.format(timeKT) == "23:59"){
                    mangLich.ngayBD  = "Cả ngày"
                    mangLich.ngayKT  = ""
                }
                else {
                    if (sdf_time.format(timeBD) == "00:00"){
                        mangLich.ngayBD  = "Ngày mới"
                        mangLich.ngayKT  = sdf_time.format(timeKT)
                    }
                    else {
                        if (sdf_time.format(timeKT) == "23:59"){
                            mangLich.ngayBD  = sdf_time.format(timeBD)
                            mangLich.ngayKT  = "Hết ngày"
                        }
                        else {
                            mangLich.ngayBD  = sdf_time.format(timeBD)
                            mangLich.ngayKT  = sdf_time.format(timeKT)
                        }
                    }
                }
//                mangLich.ngayBD  = sdf_date.format(dateBD) + " " + sdf_time.format(timeBD)
//                mangLich.ngayKT  = sdf_date.format(dateKT) + " " + sdf_time.format(timeKT)

                mangLich.detail  = arr[i*5+4]
                mangLich.key     = arr[i*5+5]

                scheduleEvents.add(mangLich)
            }
        }
        else{
            if(dateClick != date){
                shortToast("Không có lịch")
            }
        }
    }

    // tach chuoi event truyen list view, 1 chuoi 5 phan tu
    private fun splitEvent(arr: MutableList<String>, event_data : String) {
        val str : List<String> = event_data.split(key)
        for (j in 0..(str.size-1)){
            arr.add(str[j])
        }
    }

    // goi event de into compactCalendar
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
                var dateTimeBD : Date
                var dateTimeKT : Date

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
                    dateTimeBD = sdf.parse(sdf_date.format(date).toString() + " at " + "0:0")
                    dateTimeKT = sdf.parse(sdf_date.format(date).toString() + " at " + "23:59")

                    // chuoi ngay dau tien va cuoi cung cua su kien
                    val stringDateTime = ten +key+ dateTimeBD.time +key+ dateTimeKT.time +key+ note +key+ ma
                    mutableList.add(Event(Color.GREEN, dateTimeBD.time, stringDateTime))
                }

                // lay chuoi ngay cuoi cung
                mutableList.add(Event(Color.GREEN, timeInMillisKT, string2))

                return mutableList
            }
        }
    }

    // su ly menu cua actionBar
    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?){
        super.onCreateOptionsMenu(menu, inflater)
        activity.menuInflater.inflate(R.menu.schedule_menu, menu)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        when (id) {
            R.id.action_today -> {
                getDate(date)
                return true
            }
            R.id.action_add -> {
                val intent = Intent(activity, AddSchudeleActivity::class.java)
                intent.putExtra("work_menu","add")
                startActivity(intent)
                return true
            }
            R.id.action_day_view -> {
                return true
            }
            R.id.action_three_day_view -> {
                return true
            }
            R.id.action_week_view -> {
                shortToast(listEvents.size.toString())
                return true
            }
        }

        return super.onOptionsItemSelected(item)
    }

    // su ly menu cua list
    override fun onCreateContextMenu(menu: ContextMenu?, v: View?, menuInfo: ContextMenu.ContextMenuInfo?) {
        super.onCreateContextMenu(menu, v, menuInfo)
        activity.menuInflater.inflate(R.menu.lichtrinh_list_menu,menu)
    }
    override fun onContextItemSelected(item: MenuItem?): Boolean {
        when (item!!.itemId) {
            R.id.cnt_mnu_add ->{
                val intent = Intent(activity, AddSchudeleActivity::class.java)
                intent.putExtra("work_menu","add")
                startActivity(intent)
                return true
            }

            R.id.cnt_mnu_edit ->{

                val intent = Intent(activity, AddSchudeleActivity::class.java)
                intent.putExtra("work_menu","edit")

                for (i : Int in 0..(listEvents.size-1)){
                    if(keyValue == listEvents[i].key){
                        intent.putExtra("schedule_key", listEvents[i].key)
                        intent.putExtra("schedule_title", listEvents[i].title)
                        intent.putExtra("schedule_detail", listEvents[i].detail)
                        intent.putExtra("schedule_ngayBD", listEvents[i].ngayBD)
                        intent.putExtra("schedule_ngayKT", listEvents[i].ngayKT)
                    }
                }
                startActivity(intent)
                return true
            }

            R.id.cnt_mnu_delete ->{

                val ref = database.child("schedule").child(idUser).child(keyValue)
                if (keyValue != ""){
                    ref.removeValue()
                    keyValue = ""
                }
                else{
                    shortToast("không tìm thấy")
                }
                return true
            }

            else -> {
                return super.onContextItemSelected(item)
            }
        }
    }

    //thong bao
    private fun shortToast(str : String) {
        val length : Int = Toast.LENGTH_SHORT
        Toast.makeText(this.activity, str, length).show()
    }
}