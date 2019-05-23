package com.wechantloup.side.modules.core

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import dagger.android.AndroidInjection
import icepick.Icepick
import javax.inject.Inject

abstract class BaseActivity<T: BaseContract.Presenter>: AppCompatActivity(), BaseContract.View {

    @Inject
    protected lateinit var mPresenter: T

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidInjection.inject(this)
        mPresenter.subscribe(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        mPresenter.unsubscribe(this)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        Icepick.restoreInstanceState(this, savedInstanceState)
        mPresenter.onRestoreInstanceState(savedInstanceState)
    }

    public override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        Icepick.saveInstanceState(this, outState)
        mPresenter.onSaveInstanceState(outState)
    }
}