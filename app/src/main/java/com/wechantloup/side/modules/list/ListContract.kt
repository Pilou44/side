package com.wechantloup.side.modules.list

import com.google.android.gms.maps.model.LatLng
import com.wechantloup.side.domain.bean.ToiletsBean
import com.wechantloup.side.modules.core.BaseContract

interface ListContract {

    interface View: BaseContract.View {
        fun notifyToiletsListRetrieved()
    }

    interface Presenter: BaseContract.Presenter {
        fun retrieveToiletsList(favorites: Boolean, myPosition: LatLng?)
        fun getToiletsList(): ArrayList<ToiletsBean>?
        fun setFavorite(toilet: ToiletsBean, favorite: Boolean)
        fun sortByDistance()
        fun sortByOpens()
        fun showDetails(toilet: ToiletsBean)
    }

    interface Router: BaseContract.Router {
        fun openToilet(view: View, toilet: ToiletsBean)
    }
}