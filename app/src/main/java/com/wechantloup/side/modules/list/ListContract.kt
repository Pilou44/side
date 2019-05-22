package com.wechantloup.side.modules.list

import com.wechantloup.side.domain.bean.ToiletsBean
import com.wechantloup.side.modules.core.BaseContract

interface ListContract {

    interface View: BaseContract.View {
        fun notifyToiletsListRetrieved()
    }

    interface Presenter: BaseContract.Presenter {
        fun retrieveToiletsList()
        fun getToiletsList(): ArrayList<ToiletsBean>?
        fun setFavorite(toilet: ToiletsBean, favorite: Boolean)
    }
}