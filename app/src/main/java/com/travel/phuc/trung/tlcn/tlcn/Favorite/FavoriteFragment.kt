package com.travel.phuc.trung.tlcn.tlcn.Favorite

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.travel.phuc.trung.tlcn.tlcn.R

/**
 * Created by Admin on 15/11/2017.
 */
class FavoriteFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view =inflater!!.inflate(R.layout.favorite_fragment,container,false);

        return view;

    }
}