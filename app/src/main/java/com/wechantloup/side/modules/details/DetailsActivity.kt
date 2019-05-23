package com.wechantloup.side.modules.details

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import com.wechantloup.side.R
import com.wechantloup.side.domain.bean.ToiletsBean
import com.wechantloup.side.modules.core.BaseActivity
import kotlinx.android.synthetic.main.activity_details.*

class DetailsActivity: BaseActivity<DetailsContract.Presenter>(), DetailsContract.View {

    companion object {
        const val EXTRA_TOILET = "toilet"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.wechantloup.side.R.layout.activity_details)

        val toilet = intent.getParcelableExtra<ToiletsBean>(EXTRA_TOILET)
        mPresenter.setToilet(toilet)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        favorite.isChecked = toilet.isFavorite
        address.text = toilet.getAddress()
        administrator.text = toilet.getAdministrator()
        opening_time.text = toilet.getOpening()

        if (toilet.distanceToMe >= 0) {
            distance.visibility = View.VISIBLE
            distance.text = getString(R.string.distance, toilet.distanceToMe.toInt())
        } else {
            distance.visibility = View.GONE
        }

        favorite.setOnCheckedChangeListener { _, isChecked -> mPresenter.setFavorite(isChecked)}
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.details_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            R.id.share -> {
                mPresenter.share()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}