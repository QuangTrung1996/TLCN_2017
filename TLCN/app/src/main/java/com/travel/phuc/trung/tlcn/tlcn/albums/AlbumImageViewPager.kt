package com.travel.phuc.trung.tlcn.tlcn.albums

import android.app.Activity
import android.os.Bundle
import android.support.v4.view.ViewPager
import android.widget.ImageView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.travel.phuc.trung.tlcn.tlcn.R
import java.util.*


class AlbumImageViewPager : Activity() {

    var position: Int = 0
    private var images  : ArrayList<ImageView> = ArrayList<ImageView>()
    private lateinit var listImageUrl : ArrayList<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.album_image_view_pager)

        val p = intent
        position = p.extras!!.getInt("id")
        listImageUrl = p.getStringArrayListExtra("list")

        for (i in 0 until listImageUrl.size) {

            val imageView = ImageView(this)
            Glide.with(this)
                    .load(listImageUrl[i])
                    .fitCenter()
                    .into(imageView)

            imageView.scaleType = ImageView.ScaleType.FIT_XY
            images.add(imageView)


        }

        val pagerAdapter = AlbumImagePagerAdapter(images)
        val viewpager : ViewPager = findViewById(R.id.pager)
        viewpager.adapter = pagerAdapter
        // Show images following the position
        viewpager.currentItem = position
    }

    // hien dong thong bao
    private fun shortToast(messsage : String) {
        val length : Int = Toast.LENGTH_SHORT
        Toast.makeText(this, messsage, length).show()
    }
}
