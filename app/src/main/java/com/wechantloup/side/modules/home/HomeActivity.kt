package com.wechantloup.side.modules.home

import android.os.Bundle
import com.wechantloup.side.R
import com.wechantloup.side.modules.core.BaseActivity
import dagger.android.AndroidInjection
import javax.inject.Inject

class HomeActivity: BaseActivity(), HomeContract.View {

    @Inject
    internal lateinit var mPresenter: HomeContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        AndroidInjection.inject(this)

        mPresenter.subscribe(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        mPresenter.unsubscribe(this)
    }
}