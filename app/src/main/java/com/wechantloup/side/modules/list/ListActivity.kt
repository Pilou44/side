package com.wechantloup.side.modules.list

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.maps.model.LatLng
import com.wechantloup.side.R
import com.wechantloup.side.modules.core.BaseActivity
import kotlinx.android.synthetic.main.activity_list.*

class ListActivity: BaseActivity<ListContract.Presenter>(), ListContract.View {

    companion object {
        const val EXTRA_FAVORITES = "favorites"
        const val EXTRA_POSITION = "position"
    }

    private lateinit var mAdapter: ListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.wechantloup.side.R.layout.activity_list)

        if (savedInstanceState == null) {
            val favorites = intent.getBooleanExtra(EXTRA_FAVORITES, false)
            val myPosition = intent.getParcelableExtra<LatLng?>(EXTRA_POSITION)
            mPresenter.retrieveToiletsList(favorites, myPosition)
        }

        mAdapter = ListAdapter(mPresenter)
        rv.setHasFixedSize(true)
        rv.layoutManager = LinearLayoutManager(this)
        rv.adapter = mAdapter
        rv.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        swipe.setOnRefreshListener { mPresenter.retrieveToiletsList() }
    }

    override fun notifyItemModified(position: Int) {
        mAdapter.notifyItemChanged(position)
    }

    override fun notifyItemRemoved(position: Int) {
        mAdapter.notifyItemRemoved(position)
    }

    override fun notifyToiletsListRetrieved() {
        swipe.isRefreshing = false
        mAdapter.notifyDataSetChanged()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.list_menu, menu)
        menu.findItem(R.id.sort_by_distance).isEnabled = mPresenter.getMyLocation() != null
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
            R.id.opens_first -> {
                mPresenter.sortByOpens()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}