package com.wechantloup.side.modules.list

import com.google.android.gms.maps.model.LatLng
import com.wechantloup.side.domain.bean.ToiletBean
import com.wechantloup.side.modules.core.BaseContract

interface ListContract {

    interface View: BaseContract.View {
        fun notifyToiletsListRetrieved()
        fun notifyItemModified(position: Int)
        fun notifyItemRemoved(position: Int)
    }

    interface Presenter: BaseContract.Presenter {
        fun retrieveToiletsList(favorites: Boolean, myPosition: LatLng?)
        fun retrieveToiletsList()
        fun getToiletsList(): ArrayList<ToiletBean>?
        fun setFavorite(toilet: ToiletBean)
        fun sortByDistance()
        fun sortByOpens()
        fun showDetails(toilet: ToiletBean)
        fun getMyLocation(): LatLng?
    }

    interface Router: BaseContract.Router {
        fun openToilet(view: View, toilet: ToiletBean)
    }
}