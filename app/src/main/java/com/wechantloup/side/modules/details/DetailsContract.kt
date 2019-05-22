package com.wechantloup.side.modules.details

import com.wechantloup.side.domain.bean.ToiletsBean
import com.wechantloup.side.modules.core.BaseContract

interface DetailsContract {

    interface View: BaseContract.View {

    }

    interface Presenter: BaseContract.Presenter {
        fun setToilet(toilet: ToiletsBean)
        fun setFavorite(favorite: Boolean)
    }
}