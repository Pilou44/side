package com.wechantloup.side.modules.home

import com.wechantloup.side.domain.bean.ToiletsBean
import com.wechantloup.side.modules.core.BaseContract

interface HomeContract {

    interface View: BaseContract.View {
        fun notifyToiletsListRetrieved()
        fun notifyItemModified()
    }

    interface Presenter: BaseContract.Presenter {
        fun retrieveToiletsList()
        fun getToiletsList(): ArrayList<ToiletsBean>?
        fun showAsList()
        fun openToilet(toilet: ToiletsBean)
        fun showFavorites()
    }

    interface Router: BaseContract.Router {
        fun goToList(view: View, favorites: Boolean)
        fun openToilet(view: View, toilet: ToiletsBean)
    }
}