package com.wechantloup.side.modules.core

import android.os.Bundle
import icepick.Icepick

abstract class BasePresenter<R: BaseContract.Router, V: BaseContract.View>(protected val mRouter: R?): BaseContract.Presenter {

    protected var mView: V? = null

    override fun subscribe(view: BaseContract.View) {
        mView = view as V
    }

    override fun unsubscribe(view: BaseContract.View) {
        if (mView == view) {
            mView = null
        }
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        Icepick.restoreInstanceState(this, savedInstanceState)
        onContextRestored()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        Icepick.saveInstanceState(this, outState)
    }
}