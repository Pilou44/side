package com.wechantloup.side.utils

import com.google.android.gms.maps.model.LatLng

fun calculateDistance(StartP: LatLng, EndP: LatLng): Double {
    val radius = 6371000// radius of earth in m
    val lat1 = StartP.latitude
    val lat2 = EndP.latitude
    val lon1 = StartP.longitude
    val lon2 = EndP.longitude
    val dLat = Math.toRadians(lat2 - lat1)
    val dLon = Math.toRadians(lon2 - lon1)
    val a = Math.sin(dLat / 2) * Math.sin(dLat / 2) + (Math.cos(Math.toRadians(lat1))
            * Math.cos(Math.toRadians(lat2)) * Math.sin(dLon / 2)
            * Math.sin(dLon / 2))
    val c = 2 * Math.asin(Math.sqrt(a))

    return radius * c
}