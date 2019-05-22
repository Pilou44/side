package com.wechantloup.side.modules.list

import android.util.Log
import com.wechantloup.side.domain.bean.FavoriteBean
import com.wechantloup.side.domain.bean.ToiletsBean
import com.wechantloup.side.domain.usecase.AddFavoriteUseCase
import com.wechantloup.side.domain.usecase.GetFavoritesUseCase
import com.wechantloup.side.domain.usecase.GetToiletsUseCase
import com.wechantloup.side.domain.usecase.RemoveFavoriteUseCase
import com.wechantloup.side.modules.core.BaseContract
import com.wechantloup.side.modules.core.BasePresenter
import io.reactivex.observers.ResourceObserver

class ListPresenter(private val mGetToiletsUseCase: GetToiletsUseCase,
                    private val mGetFavoritesUseCase: GetFavoritesUseCase,
                    private val mAddFavoriteUseCase: AddFavoriteUseCase,
                    private val mRemoveFavoriteUseCase: RemoveFavoriteUseCase
):
    BasePresenter<BaseContract.Router, ListContract.View>(null), ListContract.Presenter {

    companion object {
        private var TAG = ListPresenter::class.java.simpleName
    }

    private var mToilets: ArrayList<ToiletsBean>? = null

    override fun retrieveToiletsList(favorites: Boolean) {
        mGetFavoritesUseCase.execute(GetFavoritesSubscriber(favorites))
    }

    override fun getToiletsList(): ArrayList<ToiletsBean>? {
        return mToilets
    }

    private fun checkFavorites(toilets: ArrayList<ToiletsBean>, favorites: List<FavoriteBean>, showFavoritesOnly: Boolean) {
        val favoritesList = ArrayList<ToiletsBean>()
        for (toilet in toilets) {
            toilet.isFavorite = favorites.contains(FavoriteBean(toilet.id))
            if (showFavoritesOnly && toilet.isFavorite) {
                favoritesList.add(toilet)
            }
        }
        if (showFavoritesOnly) {
            mToilets = favoritesList
        }
    }

    override fun setFavorite(toilet: ToiletsBean, favorite: Boolean) {
        when (favorite) {
            true -> mAddFavoriteUseCase.execute(FavoriteSubscriber(toilet), FavoriteBean(toilet.id))
            false -> mRemoveFavoriteUseCase.execute(FavoriteSubscriber(toilet), FavoriteBean(toilet.id))
        }
    }

    inner class FavoriteSubscriber(private var toilet: ToiletsBean) : ResourceObserver<Void>() {
        override fun onComplete() {
            toilet.isFavorite = !toilet.isFavorite
        }

        override fun onNext(t: Void) {
            // Nothing to do
        }

        override fun onError(e: Throwable) {
            //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

    }

    inner class GetToiletsSubscriber(private val favorites: List<FavoriteBean>?,
                                     private val showFavoritesOnly: Boolean) : ResourceObserver<ArrayList<ToiletsBean>>() {
        override fun onComplete() {
            // Nothing to do
        }

        override fun onNext(toilets: ArrayList<ToiletsBean>) {
            mToilets = toilets
            favorites?.let {
                checkFavorites(mToilets!!, favorites, showFavoritesOnly)
            }
            mView?.notifyToiletsListRetrieved()
        }

        override fun onError(e: Throwable) {
            Log.e(TAG, "Error getting toilets list", e)
            //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }
    }

    inner class GetFavoritesSubscriber(private var showFavoritesOnly: Boolean) : ResourceObserver<List<FavoriteBean>>() {
        override fun onComplete() {
            // Nothing to do
        }

        override fun onNext(favorites: List<FavoriteBean>) {
            mGetToiletsUseCase.execute(GetToiletsSubscriber(favorites, showFavoritesOnly))
        }

        override fun onError(e: Throwable) {
            //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            mGetToiletsUseCase.execute(GetToiletsSubscriber(null, showFavoritesOnly))
        }

    }
}