package com.travel.phuc.trung.tlcn.tlcn

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.widget.TextView
import com.bumptech.glide.Glide
import com.google.firebase.database.*
import com.travel.phuc.trung.tlcn.tlcn.AddInfromation.InformationFragment
import com.travel.phuc.trung.tlcn.tlcn.ConfirmInformation.ConfirmFragment
import com.travel.phuc.trung.tlcn.tlcn.Favorite.FavoriteFragment
import com.travel.phuc.trung.tlcn.tlcn.Home.HomeFragment
import com.travel.phuc.trung.tlcn.tlcn.albums.AlbumFragment
import com.travel.phuc.trung.tlcn.tlcn.logins.LoginFragment
import com.travel.phuc.trung.tlcn.tlcn.managers.ManagerFragmentUser
import com.travel.phuc.trung.tlcn.tlcn.notifications.NotificationsFragment
import com.travel.phuc.trung.tlcn.tlcn.schedules.ScheduleFragment
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(){

    private val databaseRef : DatabaseReference = FirebaseDatabase.getInstance().reference

    private val sharedPreferences : String="taikhoan"
    private var idUSER      :String? = null
    private var name        :String? = null
    private var nameEmail   :String? = null
    private var imgUser     :String? = null

    lateinit var anh    : CircleImageView
    lateinit var tenDN  : TextView
    lateinit var email  : TextView

    private var actionBarDrawerToggle: ActionBarDrawerToggle? = null
    private var navigationView: NavigationView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        actionBarDrawerToggle = ActionBarDrawerToggle(this@MainActivity,drawer,R.string.open,R.string.close)
        drawer!!.addDrawerListener(actionBarDrawerToggle!!)
        actionBarDrawerToggle!!.syncState()
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        navigationView = findViewById(R.id.navigation)
        initNavigationDrawer()

        // mac dinh mo trang Home
        openFragmentHome()

        //testAAAA()
    }

    private fun testAAAA() {
        val fragmentManager = supportFragmentManager
        val transaction = fragmentManager.beginTransaction()
        transaction.replace(R.id.content, AlbumFragment()).commit()
    }

    private fun openFragmentHome() {
        val fragmentManager = supportFragmentManager
        val transaction = fragmentManager.beginTransaction()
        transaction.replace(R.id.content,HomeFragment()).commit()
    }

    // bắt sự kiện đóng drawble khi nhấn nút back
    override fun onBackPressed() {
        if (drawer!!.isDrawerOpen(GravityCompat.START)) {
            drawer!!.closeDrawer(GravityCompat.START)
        }
        else {
            super.onBackPressed()
        }
    }

    // bắt sự kiện đóng mở drawble trên button menu actionbar
    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (actionBarDrawerToggle!!.onOptionsItemSelected(item)) {
            docTaiKhoan()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    // lấy thông tin user từ sharedprperences
    private fun docTaiKhoan() {

        val sharedPreferences = this.getSharedPreferences(sharedPreferences,android.content.Context.MODE_PRIVATE)

        idUSER      = sharedPreferences.getString("Uid",null)
        nameEmail   = sharedPreferences.getString("Uemail",null)
        imgUser     = sharedPreferences.getString("UURLAnh",null)
        name        = sharedPreferences.getString("Uname",null)

        if (idUSER != null){

            anh   = this.findViewById(R.id.profile_image)
            tenDN = this.findViewById(R.id.username)
            email = this.findViewById(R.id.email)

            tenDN.text = name
            email.text = nameEmail
            Glide.with(this).load(imgUser)
                    .centerCrop()
                    .error(R.drawable.anhao)
                    .into(anh)

            // ẩn menu login và hiện thông tin của những menu khác
            showMenu()
        }
        else{
            hideMenu()
        }
    }

    private fun hideMenu() {
        val navMenu = navigationView!!.menu
        navMenu.findItem(R.id.login).isVisible = true
        navMenu.setGroupVisible(R.id.group_menu, false)
    }

    private fun showMenu() {
        val navMenu = navigationView!!.menu
        navMenu.findItem(R.id.login).isVisible = false
        navMenu.setGroupVisible(R.id.group_menu, true)

        // kiem tra quyen quan ly
        checkManager()
    }

    // select item in drawable
    @SuppressLint("CommitTransaction")
    private fun initNavigationDrawer() {

        navigationView!!.setNavigationItemSelectedListener { menuItem ->
            val id = menuItem.itemId

            when (id) {
                R.id.home -> {
                    openFragmentHome()
                    drawer!!.closeDrawer(GravityCompat.START)
                }
                R.id.schedule -> {
                    val fragmentManager = supportFragmentManager
                    val transaction = fragmentManager.beginTransaction()

                    transaction.replace(R.id.content, ScheduleFragment()).commit()
                    drawer!!.closeDrawer(GravityCompat.START)
                }
                R.id.album -> {
                    val fragmentManager = supportFragmentManager
                    val transaction = fragmentManager.beginTransaction()

                    transaction.replace(R.id.content, AlbumFragment()).commit()
                    drawer!!.closeDrawer(GravityCompat.START)
                }
                R.id.favorite -> {
                    val fragmentManager = supportFragmentManager
                    val transaction = fragmentManager.beginTransaction()

                    transaction.replace(R.id.content,FavoriteFragment()).commit()
                    drawer!!.closeDrawer(GravityCompat.START)
                }
                R.id.login -> {
                    val fragmentManager = supportFragmentManager
                    val transaction = fragmentManager.beginTransaction()

                    transaction.replace(R.id.content, LoginFragment()).commit()
                    drawer!!.closeDrawer(GravityCompat.START)
                }
                R.id.notifications -> {
                    val fragmentManager = supportFragmentManager
                    val transaction = fragmentManager.beginTransaction()

                    transaction.replace(R.id.content, NotificationsFragment()).commit()
                    drawer!!.closeDrawer(GravityCompat.START)
                }
                R.id.manager -> {
                    val fragmentManager = supportFragmentManager
                    val transaction = fragmentManager.beginTransaction()

                    transaction.replace(R.id.content, ManagerFragmentUser()).commit()
                    drawer!!.closeDrawer(GravityCompat.START)
                }
                R.id.logout -> {
                    val sharedPreferences = getSharedPreferences(sharedPreferences,android.content.Context.MODE_PRIVATE)
                    val editor = sharedPreferences.edit()
                    editor.putString("Uid",null)
                    editor.putString("Uname",null)
                    editor.putString("Uemail",null)
                    editor.putString("UURLAnh",null)
                    editor.apply()

                    anh   = this.findViewById(R.id.profile_image)
                    tenDN = this.findViewById(R.id.username)
                    email = this.findViewById(R.id.email)

                    tenDN.text = ""
                    email.text = ""
                    Glide.with(this).load(R.drawable.anhao)
                            .centerCrop()
                            .into(anh)

                    openFragmentHome()
                    drawer!!.closeDrawer(GravityCompat.START)
                }
                R.id.addinfromation ->
                {
                    val fragmentManager = supportFragmentManager
                    val transaction = fragmentManager.beginTransaction()

                    transaction.replace(R.id.content, InformationFragment()).commit()
                    drawer!!.closeDrawer(GravityCompat.START)
                }
                R.id.confirm ->
                {

                    val fragmentManager = supportFragmentManager
                    val transaction = fragmentManager.beginTransaction()
                    transaction.replace(R.id.content, ConfirmFragment()).commit()
                    drawer!!.closeDrawer(GravityCompat.START)
                }
            }
            true
        }
        actionBarDrawerToggle!!.syncState()
    }

    private fun checkManager() {
        databaseRef.child("Manager").child(idUSER).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot?) {
                if (p0!!.value == true){
                    val navMenu = navigationView!!.menu
                    navMenu.findItem(R.id.manager).isVisible = true
                }
                else{
                    val navMenu = navigationView!!.menu
                    navMenu.findItem(R.id.manager).isVisible = false
                }
            }

            override fun onCancelled(p0: DatabaseError?) {}
        })
    }
}
