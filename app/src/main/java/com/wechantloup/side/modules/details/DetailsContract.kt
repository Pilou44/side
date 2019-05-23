package com.wechantloup.side.modules.details

import com.wechantloup.side.domain.bean.ToiletBean
import com.wechantloup.side.modules.core.BaseContract

interface DetailsContract {

    interface View: BaseContract.View {
        fun notifyErrorModifyingFavorite(toilet: ToiletBean)
    }

    interface Presenter: BaseContract.Presenter {
        fun setToilet(toilet: ToiletBean)
        fun setFavorite(favorite: Boolean)
        fun share()
    }

    interface Router: BaseContract.Router {
        fun share(view: View, text: String)
    }
}