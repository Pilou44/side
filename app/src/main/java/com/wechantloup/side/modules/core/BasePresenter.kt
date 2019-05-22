package com.wechantloup.side.modules.core

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
}