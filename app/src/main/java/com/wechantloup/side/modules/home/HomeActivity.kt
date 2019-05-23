package com.wechantloup.side.modules.home

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.wechantloup.side.R
import com.wechantloup.side.domain.bean.ToiletsBean
import com.wechantloup.side.modules.core.BaseActivity

class HomeActivity: BaseActivity<HomeContract.Presenter>(), HomeContract.View, OnMapReadyCallback, LocationListener,
    GoogleMap.OnInfoWindowClickListener {

    companion object {
        private const val PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 2702
        private const val LOCATION_REFRESH_TIME = 2000L // ms
        private const val LOCATION_REFRESH_DISTANCE = 2F // m
    }

    private var mMap: GoogleMap? = null
    private var mLocationManager: LocationManager? = null
    private var mPositionMarker: Marker? = null
    private var mDataReady = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment!!.getMapAsync(this)

        mLocationManager = getSystemService(LOCATION_SERVICE) as LocationManager

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION
            )
        }

        if (savedInstanceState == null) {
            mPresenter.retrieveToiletsList()
        }
    }

    override fun onResume() {
        super.onResume()
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mLocationManager?.requestLocationUpdates(
                LocationManager.GPS_PROVIDER, LOCATION_REFRESH_TIME,
                LOCATION_REFRESH_DISTANCE, this)
        }
    }

    override fun onPause() {
        super.onPause()
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mLocationManager?.removeUpdates(this)
        }
    }

    @SuppressLint("MissingPermission")
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (requestCode == PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                mLocationManager?.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER, LOCATION_REFRESH_TIME,
                    LOCATION_REFRESH_DISTANCE, this)
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }

    override fun onMapReady(map: GoogleMap?) {
        mMap = map
        mMap!!.setInfoWindowAdapter(CustomInfoWindowAdapter(this))
        mMap!!.setOnInfoWindowClickListener(this)
        if (mDataReady) {
            displayToilets()
        }
    }

    override fun onLocationChanged(location: Location) {
        val position = LatLng(location.latitude, location.longitude)
        if (mPositionMarker == null) {
            mPositionMarker = mMap?.addMarker(MarkerOptions().position(position).icon(BitmapDescriptorFactory.fromResource(R.drawable.position_marker)))
            mMap?.moveCamera(CameraUpdateFactory.newLatLng(position))
        } else {
            mPositionMarker?.position = position
        }
    }

    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
        // Nothing to do
    }

    override fun onProviderEnabled(provider: String?) {
        // Nothing to do
    }

    override fun onProviderDisabled(provider: String?) {
        // Nothing to do
    }

    override fun notifyToiletsListRetrieved() {
        mDataReady = true
        mMap?.let {
            displayToilets()
        }
    }

    private fun displayToilets() {
        mMap?.clear()
        mPositionMarker = null
        val toilets = mPresenter.getToiletsList()
        for (toilet in toilets!!) {
            val marker = mMap!!.addMarker(MarkerOptions().position(toilet.getPosition()))
            marker.tag = toilet
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            val location = mLocationManager!!.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            val position = LatLng(location.latitude, location.longitude)
            mPositionMarker = mMap?.addMarker(MarkerOptions().position(position).icon(BitmapDescriptorFactory.fromResource(R.drawable.position_marker)))
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.home_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.refresh -> {
                refresh()
                true
            }
            R.id.list -> {
                val position = when (mPositionMarker) {
                    null -> null
                    else -> mPositionMarker!!.position
                }
                mPresenter.showAsList(position)
                true
            }
            R.id.favorites -> {
                val position = when (mPositionMarker) {
                    null -> null
                    else -> mPositionMarker!!.position
                }
                mPresenter.showFavorites(position)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun refresh() {
        mMap?.clear()
        mPositionMarker = null
        mPresenter.retrieveToiletsList()
    }

    override fun onInfoWindowClick(marker: Marker) {
        mPresenter.openToilet(marker.tag as ToiletsBean)
    }

    override fun notifyItemModified() {
        mMap?.let { displayToilets() }
    }
}