package com.wechantloup.side.modules.list

import android.os.Bundle
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import com.wechantloup.side.modules.core.BaseActivity
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_list.*
import javax.inject.Inject

class ListActivity: BaseActivity(), ListContract.View {

    companion object {
        const val EXTRA_FAVORITES = "favorites"
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
        mPresenter.retrieveToiletsList(favorites)

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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}