package com.travel.phuc.trung.tlcn.tlcn.albums

import android.support.v4.view.PagerAdapter
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView

class AlbumImagePagerAdapter : PagerAdapter {

    private lateinit var images: List<ImageView>

    constructor(images: List<ImageView>) : super() {
        this.images = images
    }

    constructor() : super()


    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val imageView = images[position]
        container.addView(imageView)
        return imageView
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(images[position])
    }

    override fun getCount(): Int {
        return images.size
    }

    override fun isViewFromObject(view: View, o: Any): Boolean {
        return view === o
    }
}