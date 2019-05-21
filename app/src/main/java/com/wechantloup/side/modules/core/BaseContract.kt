package com.wechantloup.side.modules.core

interface BaseContract {

    interface View {

    }

    interface Presenter {
        fun subscribe(view: View)
        fun unsubscribe(view: View)
    }

    interface Router {

    }
}