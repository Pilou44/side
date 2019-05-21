package com.wechantloup.side.modules.home

import com.wechantloup.side.modules.core.BasePresenter

class HomePresenter(router: HomeContract.Router): BasePresenter<HomeContract.Router, HomeContract.View>(router), HomeContract.Presenter {
}