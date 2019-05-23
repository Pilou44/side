package com.wechantloup.side.modules.list

import android.content.Intent
import com.wechantloup.side.domain.bean.ToiletsBean
import com.wechantloup.side.modules.core.BaseRouter
import com.wechantloup.side.modules.details.DetailsActivity

class ListRouter: BaseRouter(), ListContract.Router {

    override fun openToilet(view: ListContract.View, toilet: ToiletsBean) {
        val intent = Intent(getActivity(view), DetailsActivity::class.java)
        intent.putExtra(DetailsActivity.EXTRA_TOILET, toilet)
        getActivity(view)?.startActivity(intent)
    }
}