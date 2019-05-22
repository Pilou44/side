package com.wechantloup.side.modules.home

import android.content.Intent
import com.wechantloup.side.domain.bean.ToiletsBean
import com.wechantloup.side.modules.core.BaseRouter
import com.wechantloup.side.modules.details.DetailsActivity
import com.wechantloup.side.modules.list.ListActivity

class HomeRouter: BaseRouter(), HomeContract.Router {

    override fun openToilet(view: HomeContract.View, toilet: ToiletsBean) {
        val intent = Intent(getActivity(view), DetailsActivity::class.java)
        intent.putExtra(DetailsActivity.EXTRA_TOILET, toilet)
        getActivity(view)?.startActivity(intent)
    }

    override fun goToList(view: HomeContract.View) {
        val intent = Intent(getActivity(view), ListActivity::class.java)
        getActivity(view)?.startActivity(intent)
        //getActivity(view)?.overridePendingTransition(R.anim.animation_enter_left, R.anim.animation_leave_right)
    }
}