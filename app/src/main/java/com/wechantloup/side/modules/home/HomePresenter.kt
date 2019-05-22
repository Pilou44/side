package com.wechantloup.side.modules.home

import android.util.Log
import com.wechantloup.side.domain.bean.ToiletsBean
import com.wechantloup.side.domain.usecase.GetToiletsUseCase
import com.wechantloup.side.modules.core.BasePresenter
import io.reactivex.observers.ResourceObserver

class HomePresenter(router: HomeContract.Router, private val mGetToiletsUseCase: GetToiletsUseCase): BasePresenter<HomeContract.Router, HomeContract.View>(router), HomeContract.Presenter {

    private var mToilets: ArrayList<ToiletsBean>? = null

    override fun retrieveToiletsList() {
        mGetToiletsUseCase.execute(GetToiletsSubscriber())
    }

    companion object {
        private var TAG = HomePresenter::class.java.simpleName
    }

    override fun getToiletsList(): ArrayList<ToiletsBean>? {
        return mToilets
    }

    inner class GetToiletsSubscriber : ResourceObserver<ArrayList<ToiletsBean>>() {
        override fun onComplete() {
            // Nothing to do
        }

        override fun onNext(toilets: ArrayList<ToiletsBean>) {
            mToilets = toilets
            mView?.notifyToiletsListRetrieved()
        }

        override fun onError(e: Throwable) {
            Log.e(TAG, "Error getting toilets list", e)
            //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }
    }
}