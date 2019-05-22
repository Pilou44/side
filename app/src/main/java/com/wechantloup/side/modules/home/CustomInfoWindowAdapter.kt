package com.wechantloup.side.modules.home

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker
import com.wechantloup.side.R
import com.wechantloup.side.domain.bean.ToiletsBean
import kotlinx.android.synthetic.main.item_marker_details.view.*


class CustomInfoWindowAdapter(private var context: Context): GoogleMap.InfoWindowAdapter {
    override fun getInfoContents(marker: Marker): View? {
        val info = marker.tag as ToiletsBean?

        return when (info) {
            null -> null
            else -> {
                val view = LayoutInflater.from(context).inflate(R.layout.item_marker_details, null, false);

                val addressTv = view.address
                val administratorTv = view.administrator
                val openingTv = view.opening_time
                val favorite = view.favorite

                addressTv.text = info.getAddress()
                administratorTv.text = info.getAdministrator()
                openingTv.text = info.getOpening()
                favorite.isChecked = info.isFavorite

                view
            }
        }
    }

    override fun getInfoWindow(p0: Marker?): View? {
        return null
    }
}
