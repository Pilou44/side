package com.wechantloup.side.modules.list

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.maps.model.LatLng
import com.wechantloup.side.R
import com.wechantloup.side.modules.core.BaseActivity
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_list.*
import javax.inject.Inject

class ListActivity: BaseActivity(), ListContract.View {

    companion object {
        const val EXTRA_FAVORITES = "favorites"
        const val EXTRA_POSITION = "position"
    }

    @Inject
    internal lateinit var mPresenter: ListContract.Presenter

    private lateinit var mAdapter: ListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.wechantloup.side.R.layout.activity_list)

        AndroidInjection.inject(this)

        mPresenter.subscribe(this)

        val favorites = intent.getBooleanExtra(EXTRA_FAVORITES, false)
        val myPosition = intent.getParcelableExtra<LatLng?>(EXTRA_POSITION)
        mPresenter.retrieveToiletsList(favorites, myPosition)

        mAdapter = ListAdapter(mPresenter)
        rv.setHasFixedSize(true)
        rv.layoutManager = LinearLayoutManager(this)
        rv.adapter = mAdapter

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
    }

    override fun onDestroy() {
        super.onDestroy()
        mPresenter.unsubscribe(this)
    }

    override fun notifyToiletsListRetrieved() {
        mAdapter.notifyDataSetChanged()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.list_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            R.id.sort_by_distance -> {
                mPresenter.sortByDistance()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}