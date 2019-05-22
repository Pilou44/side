package com.wechantloup.side.modules.home

import android.content.Intent
import com.wechantloup.side.modules.core.BaseRouter
import com.wechantloup.side.modules.list.ListActivity

class HomeRouter: BaseRouter(), HomeContract.Router {

    override fun goToList(view: HomeContract.View) {
        val intent = Intent(getActivity(view), ListActivity::class.java)
        getActivity(view)?.startActivity(intent)
    }
}