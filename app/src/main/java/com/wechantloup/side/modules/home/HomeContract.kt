package com.wechantloup.side.modules.home

import com.wechantloup.side.modules.core.BaseContract

interface HomeContract {

    interface View: BaseContract.View {

    }

    interface Presenter: BaseContract.Presenter {
        fun retrieveToiletsList()
    }

    interface Router: BaseContract.Router {

    }
}