package com.wechantloup.side.modules.home

import android.os.Bundle
import com.wechantloup.side.R
import com.wechantloup.side.modules.core.BaseActivity
import dagger.android.AndroidInjection
import javax.inject.Inject
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment

class HomeActivity: BaseActivity(), HomeContract.View, OnMapReadyCallback {

    @Inject
    internal lateinit var mPresenter: HomeContract.Presenter

    private var mMap: GoogleMap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        AndroidInjection.inject(this)

        mPresenter.subscribe(this)

        mPresenter.retrieveToiletsList()

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment!!.getMapAsync(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        mPresenter.unsubscribe(this)
    }

    override fun onMapReady(map: GoogleMap?) {
        mMap = map
    }

}