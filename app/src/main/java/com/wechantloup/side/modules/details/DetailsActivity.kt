package com.wechantloup.side.modules.details

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import com.wechantloup.side.R
import com.wechantloup.side.domain.bean.ToiletsBean
import com.wechantloup.side.modules.core.BaseActivity
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_details.*
import javax.inject.Inject

class DetailsActivity: BaseActivity(), DetailsContract.View {

    companion object {
        const val EXTRA_TOILET = "toilet"
    }

    @Inject
    internal lateinit var mPresenter: DetailsContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.wechantloup.side.R.layout.activity_details)

        AndroidInjection.inject(this)

        mPresenter.subscribe(this)

        val toilet = intent.getParcelableExtra<ToiletsBean>(EXTRA_TOILET)
        mPresenter.setToilet(toilet)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        favorite.isChecked = toilet.isFavorite
        address.text = toilet.getAddress()
        administrator.text = toilet.getAdministrator()
        opening_time.text = toilet.getOpening()

        favorite.setOnCheckedChangeListener { _, isChecked -> mPresenter.setFavorite(isChecked)}
    }

    override fun onDestroy() {
        super.onDestroy()
        mPresenter.unsubscribe(this)
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