package com.travel.phuc.trung.tlcn.tlcn

import android.annotation.SuppressLint
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.travel.phuc.trung.tlcn.tlcn.Album.AlbumFragment
import com.travel.phuc.trung.tlcn.tlcn.Favorite.FavoriteFragment
import com.travel.phuc.trung.tlcn.tlcn.Home.HomeFragment
import com.travel.phuc.trung.tlcn.tlcn.Login.LoginFragment
import com.travel.phuc.trung.tlcn.tlcn.Schedule.ScheduleFragment
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    val sharedprperences : String="taikhoan";
    val KEY_ID_USER = "ID_User"
    var id_USER :String?=null
    var ten:String? =null
    var ten_email:String? = null
    var hinhDaiDien:String?=null
    private var actionBarDrawerToggle: ActionBarDrawerToggle? = null
    private var navigationView: NavigationView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        actionBarDrawerToggle = ActionBarDrawerToggle(this@MainActivity,drawer,R.string.open,R.string.close)
        drawer!!.addDrawerListener(actionBarDrawerToggle!!)
        actionBarDrawerToggle!!.syncState()
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        navigationView = findViewById<NavigationView>(R.id.navigation);
        initNavigationDrawer()
//        // mac dinh mo trang Home
        val fragmentManager = supportFragmentManager
        val transaction = fragmentManager.beginTransaction()
        transaction.replace(R.id.content,HomeFragment()).commit()
    }
    // lấy thông tin user từ sharedprperences
    private fun doctaikhoan() {
        val sharedpreferences=this.getSharedPreferences(sharedprperences,android.content.Context.MODE_PRIVATE)
        id_USER =sharedpreferences.getString("Uid",null)
        ten_email =sharedpreferences.getString("Uemail",null)
        hinhDaiDien=sharedpreferences.getString("UURLAnh",null)
        ten= sharedpreferences.getString("Uname",null)
        if (id_USER!=null){
            ShortToast(id_USER!!)
            val anhdaidien: CircleImageView = this.findViewById<CircleImageView>(R.id.profile_image)
            val TenDN: TextView = this.findViewById<TextView>(R.id.username)
            val Email: TextView = this.findViewById<TextView>(R.id.email)

            TenDN.text = ten
            Email.text = ten_email
            Glide.with(this).load(hinhDaiDien)
                    .centerCrop()
                    .error(R.drawable.wellcom0)
                    .into(anhdaidien)
        }


    }

    // bắt sự kiện đóng drawble khi nhấn nút back
    override fun onBackPressed() {
        if (drawer!!.isDrawerOpen(GravityCompat.START)) {
            drawer!!.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }
    // bắt sự kiện đóng mở drawble trên button menu actionbar
    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (actionBarDrawerToggle!!.onOptionsItemSelected(item)) {
            doctaikhoan()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
    
    @SuppressLint("CommitTransaction")
// chon item trong drowble
    fun initNavigationDrawer() {

        navigationView!!.setNavigationItemSelectedListener { menuItem ->
            val id = menuItem.itemId

            when (id) {
                R.id.home -> {
                    val fragmentManager = supportFragmentManager
                    val transaction = fragmentManager.beginTransaction()
                    transaction.replace(R.id.content,HomeFragment()).commit()
                    drawer!!.closeDrawer(GravityCompat.START)
                }
                R.id.schedule -> {
                    val fragmentManager = supportFragmentManager
                    val transaction = fragmentManager.beginTransaction()
                    transaction.replace(R.id.content,ScheduleFragment()).commit()
                    drawer!!.closeDrawer(GravityCompat.START)

                }
                R.id.album -> {
                    val fragmentManager = supportFragmentManager
                    val transaction = fragmentManager.beginTransaction()
                    transaction.replace(R.id.content,AlbumFragment()).commit()

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
                    transaction.replace(R.id.content,LoginFragment()).commit()
                    drawer!!.closeDrawer(GravityCompat.START)
                }
            }
            true
        }
        actionBarDrawerToggle!!.syncState()
    }
    private fun ShortToast(messsage : String) {
        val length : Int = Toast.LENGTH_SHORT
        Toast.makeText(this, messsage, length).show()
    }

}
