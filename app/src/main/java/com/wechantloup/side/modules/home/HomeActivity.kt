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
import dagger.android.AndroidInjection
import javax.inject.Inject

class HomeActivity: BaseActivity(), HomeContract.View, OnMapReadyCallback, LocationListener,
    GoogleMap.OnInfoWindowClickListener {

    companion object {
        private const val PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 2702
        private const val LOCATION_REFRESH_TIME = 2000L // ms
        private const val LOCATION_REFRESH_DISTANCE = 2F // m
    }

    @Inject
    internal lateinit var mPresenter: HomeContract.Presenter

    private var mMap: GoogleMap? = null
    private var mLocationManager: LocationManager? = null
    private var mPositionMarker: Marker? = null
    private var mDataReady = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        AndroidInjection.inject(this)

        mPresenter.subscribe(this)

        mPresenter.retrieveToiletsList()

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment!!.getMapAsync(this)

        mLocationManager = getSystemService(LOCATION_SERVICE) as LocationManager
    }

    override fun onDestroy() {
        super.onDestroy()
        mPresenter.unsubscribe(this)
    }

    override fun onResume() {
        super.onResume()

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION
            )
        } else {
            mLocationManager?.requestLocationUpdates(
                LocationManager.GPS_PROVIDER, LOCATION_REFRESH_TIME,
                LOCATION_REFRESH_DISTANCE, this)
        }
    }

    override fun onPause() {
        super.onPause()
        mLocationManager?.removeUpdates(this)
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
        //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onProviderEnabled(provider: String?) {
        //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onProviderDisabled(provider: String?) {
        //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun notifyToiletsListRetrieved() {
        mDataReady = true
        mMap?.let { displayToilets() }
    }

    private fun displayToilets() {
        val toilets = mPresenter.getToiletsList()
        for (toilet in toilets!!) {
            val marker = mMap!!.addMarker(MarkerOptions().position(toilet.getPosition()))//.title("je suis la").icon(BitmapDescriptorFactory.fromResource(R.drawable.position_marker)))
            marker.tag = toilet
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
                mPresenter.showAsList()
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
        mMap?.clear()
        mMap?.let { displayToilets() }
    }
}