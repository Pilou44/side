package com.wechantloup.side.modules.core

import android.os.Bundle

interface BaseContract {

    interface View {

    }

    interface Presenter {
        fun onRestoreInstanceState(savedInstanceState : Bundle)
        fun onSaveInstanceState(outState: Bundle)
        fun subscribe(view: View)
        fun unsubscribe(view: View)
        fun onContextRestored()
    }

    interface Router {

    }
}