package com.wechantloup.side.modules.home

import android.util.Log
import com.wechantloup.side.domain.bean.FavoriteBean
import com.wechantloup.side.domain.bean.ToiletsBean
import com.wechantloup.side.domain.usecase.GetFavoritesUseCase
import com.wechantloup.side.domain.usecase.GetToiletsUseCase
import com.wechantloup.side.modules.core.BasePresenter
import io.reactivex.observers.ResourceObserver

class HomePresenter(router: HomeContract.Router,
                    private val mGetToiletsUseCase: GetToiletsUseCase,
                    private val mGetFavoritesUseCase: GetFavoritesUseCase):
    BasePresenter<HomeContract.Router, HomeContract.View>(router), HomeContract.Presenter {

    companion object {
        private var TAG = HomePresenter::class.java.simpleName
    }

    private var mToilets: ArrayList<ToiletsBean>? = null

    override fun retrieveToiletsList() {
        mGetFavoritesUseCase.execute(GetFavoritesSubscriber())
    }

    inner class GetFavoritesSubscriber : ResourceObserver<List<FavoriteBean>>() {
        override fun onComplete() {
            // Nothing to do
        }

        override fun onNext(favorites: List<FavoriteBean>) {
            mGetToiletsUseCase.execute(GetToiletsSubscriber(favorites))
        }

        override fun onError(e: Throwable) {
            //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            mGetToiletsUseCase.execute(GetToiletsSubscriber(null))
        }

    }

    override fun getToiletsList(): ArrayList<ToiletsBean>? {
        return mToilets
    }

    override fun showAsList() {
        mRouter?.goToList(mView!!)
    }

    inner class GetToiletsSubscriber(private val favorites: List<FavoriteBean>?) : ResourceObserver<ArrayList<ToiletsBean>>() {
        override fun onComplete() {
            // Nothing to do
        }

        override fun onNext(toilets: ArrayList<ToiletsBean>) {
            mToilets = toilets
            favorites?.let {
                checkFavorites(mToilets!!, favorites)
            }
            mView?.notifyToiletsListRetrieved()
        }

        override fun onError(e: Throwable) {
            Log.e(TAG, "Error getting toilets list", e)
            //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }
    }

    private fun checkFavorites(toilets: ArrayList<ToiletsBean>, favorites: List<FavoriteBean>) {
        for (toilet in toilets) {
            toilet.isFavorite = favorites.contains(FavoriteBean(toilet.id))
        }
    }
}