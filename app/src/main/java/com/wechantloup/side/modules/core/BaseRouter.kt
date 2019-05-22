package com.wechantloup.side.modules.core

import android.app.Activity
import androidx.fragment.app.Fragment

abstract class BaseRouter: BaseContract.Router {

    protected fun getActivity(view: BaseContract.View): Activity? {
        return when (view) {
            is BaseActivity -> view
            is Fragment -> (view as Fragment).activity
            else -> null
        }
    }
}