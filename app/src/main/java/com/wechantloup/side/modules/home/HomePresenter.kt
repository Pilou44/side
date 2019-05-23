package com.wechantloup.side.modules.home

import android.util.Log
import com.google.android.gms.maps.model.LatLng
import com.wechantloup.side.domain.bean.FavoriteBean
import com.wechantloup.side.domain.bean.ToiletBean
import com.wechantloup.side.domain.usecase.GetFavoritesUseCase
import com.wechantloup.side.domain.usecase.GetToiletsUseCase
import com.wechantloup.side.events.ModifyFavoriteEvent
import com.wechantloup.side.modules.core.BaseContract
import com.wechantloup.side.modules.core.BasePresenter
import icepick.State
import io.reactivex.observers.ResourceObserver
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe

class HomePresenter(router: HomeContract.Router,
                    private val mGetToiletsUseCase: GetToiletsUseCase,
                    private val mGetFavoritesUseCase: GetFavoritesUseCase):
    BasePresenter<HomeContract.Router, HomeContract.View>(router), HomeContract.Presenter {

    companion object {
        private var TAG = HomePresenter::class.java.simpleName
    }

    @State @JvmField
    var mToilets: ArrayList<ToiletBean> = ArrayList()

    override fun subscribe(view: BaseContract.View) {
        super.subscribe(view)
        EventBus.getDefault().register(this)
    }

    override fun unsubscribe(view: BaseContract.View) {
        super.unsubscribe(view)
        EventBus.getDefault().unregister(this)
    }

    @Subscribe
    fun onModifyFavoriteEvent(event: ModifyFavoriteEvent) {
        val index = mToilets.indexOf(event.toilet)
        mToilets[index].isFavorite = event.toilet.isFavorite
        mView?.notifyItemModified()
    }

    override fun openToilet(toilet: ToiletBean) {
        mRouter?.openToilet(mView!!, toilet)
    }

    override fun retrieveToiletsList() {
        mGetFavoritesUseCase.execute(GetFavoritesSubscriber())
    }

    override fun onContextRestored() {
        mView?.notifyToiletsListRetrieved()
    }

    inner class GetFavoritesSubscriber : ResourceObserver<List<FavoriteBean>>() {
        override fun onComplete() {
            // Nothing to do
        }

        override fun onNext(favorites: List<FavoriteBean>) {
            mGetToiletsUseCase.execute(GetToiletsSubscriber(favorites))
        }

        override fun onError(e: Throwable) {
            Log.e(TAG, "Error getting favorites list", e)
            mGetToiletsUseCase.execute(GetToiletsSubscriber(null))
            mView?.notifyError()
        }

    }

    override fun getToiletsList(): ArrayList<ToiletBean>? {
        return mToilets
    }

    override fun showAsList(myPosition: LatLng?) {
        mRouter?.goToList(mView!!, false, myPosition)
    }

    override fun showFavorites(myPosition: LatLng?) {
        mRouter?.goToList(mView!!, true, myPosition)
    }

    inner class GetToiletsSubscriber(private val favorites: List<FavoriteBean>?) : ResourceObserver<List<ToiletBean>>() {
        override fun onComplete() {
            // Nothing to do
        }

        override fun onNext(toilets: List<ToiletBean>) {
            mToilets.clear()
            mToilets.addAll(toilets)
            favorites?.let {
                checkFavorites(mToilets, favorites)
            }
            mView?.notifyToiletsListRetrieved()
        }

        override fun onError(e: Throwable) {
            Log.e(TAG, "Error getting toilets list", e)
            mView?.notifyError()
        }
    }

    private fun checkFavorites(toilets: ArrayList<ToiletBean>, favorites: List<FavoriteBean>) {
        for (toilet in toilets) {
            toilet.isFavorite = favorites.contains(FavoriteBean(toilet.id))
        }
    }
}