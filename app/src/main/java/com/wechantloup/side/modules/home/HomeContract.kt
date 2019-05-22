package com.wechantloup.side.modules.home

import com.google.android.gms.maps.model.LatLng
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
        fun showAsList(myPosition: LatLng?)
        fun openToilet(toilet: ToiletsBean)
        fun showFavorites(myPosition: LatLng?)
    }

    interface Router: BaseContract.Router {
        fun goToList(view: View, favorites: Boolean, position: LatLng?)
        fun openToilet(view: View, toilet: ToiletsBean)
    }
}