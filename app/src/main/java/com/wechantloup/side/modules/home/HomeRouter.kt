package com.wechantloup.side.modules.home

import android.content.Intent
import com.google.android.gms.maps.model.LatLng
import com.wechantloup.side.domain.bean.ToiletBean
import com.wechantloup.side.modules.core.BaseRouter
import com.wechantloup.side.modules.details.DetailsActivity
import com.wechantloup.side.modules.list.ListActivity

class HomeRouter: BaseRouter(), HomeContract.Router {

    override fun openToilet(view: HomeContract.View, toilet: ToiletBean) {
        val intent = Intent(getActivity(view), DetailsActivity::class.java)
        intent.putExtra(DetailsActivity.EXTRA_TOILET, toilet)
        getActivity(view)?.startActivity(intent)
    }

    override fun goToList(view: HomeContract.View, favorites: Boolean, position: LatLng?) {
        val intent = Intent(getActivity(view), ListActivity::class.java)
        intent.putExtra(ListActivity.EXTRA_FAVORITES, favorites)
        intent.putExtra(ListActivity.EXTRA_POSITION, position)
        getActivity(view)?.startActivity(intent)
        //getActivity(view)?.overridePendingTransition(R.anim.animation_enter_left, R.anim.animation_leave_right)
    }
}