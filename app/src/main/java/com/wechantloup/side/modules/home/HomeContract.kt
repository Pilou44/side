package com.wechantloup.side.modules.home

import com.wechantloup.side.domain.bean.ToiletsBean
import com.wechantloup.side.modules.core.BaseContract

interface HomeContract {

    interface View: BaseContract.View {
        fun notifyToiletsListRetrieved()
    }

    interface Presenter: BaseContract.Presenter {
        fun retrieveToiletsList()
        fun getToiletsList(): ArrayList<ToiletsBean>?
    }

    interface Router: BaseContract.Router {

    }
}