package com.wechantloup.side.modules.core

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.wechantloup.side.R
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

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        Icepick.saveInstanceState(this, outState)
        mPresenter.onSaveInstanceState(outState)
    }

    override fun notifyError() {
        Toast.makeText(this, R.string.error, Toast.LENGTH_LONG).show()
    }
}