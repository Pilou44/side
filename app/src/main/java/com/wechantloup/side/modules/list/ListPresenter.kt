package com.wechantloup.side.modules.list

import android.os.Build
import android.util.Log
import com.google.android.gms.maps.model.LatLng
import com.wechantloup.side.domain.bean.FavoriteBean
import com.wechantloup.side.domain.bean.ToiletsBean
import com.wechantloup.side.domain.usecase.AddFavoriteUseCase
import com.wechantloup.side.domain.usecase.GetFavoritesUseCase
import com.wechantloup.side.domain.usecase.GetToiletsUseCase
import com.wechantloup.side.domain.usecase.RemoveFavoriteUseCase
import com.wechantloup.side.events.ModifyFavoriteEvent
import com.wechantloup.side.modules.core.BaseContract
import com.wechantloup.side.modules.core.BasePresenter
import com.wechantloup.side.utils.calculateDistance
import io.reactivex.observers.ResourceObserver
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import java.util.*
import kotlin.collections.ArrayList

class ListPresenter(router: ListContract.Router,
                    private val mGetToiletsUseCase: GetToiletsUseCase,
                    private val mGetFavoritesUseCase: GetFavoritesUseCase,
                    private val mAddFavoriteUseCase: AddFavoriteUseCase,
                    private val mRemoveFavoriteUseCase: RemoveFavoriteUseCase):
    BasePresenter<ListContract.Router, ListContract.View>(router), ListContract.Presenter {

    companion object {
        private var TAG = ListPresenter::class.java.simpleName
        private const val H24 = "24 h / 24"
    }

    private var mToilets: ArrayList<ToiletsBean>? = null
    private var mMyPosition: LatLng? = null
    private var mFavoritesOnly: Boolean = false

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
        val index = mToilets!!.indexOf(event.toilet)
        if (mFavoritesOnly) {
            mToilets!!.removeAt(index)
            mView?.notifyitemRemoved(index)
        } else {
            mToilets!![index].isFavorite = event.toilet.isFavorite
            mView?.notifyItemModified(index)
        }
    }

    override fun getMyPositon(): LatLng? {
        return mMyPosition
    }

    override fun retrieveToiletsList(favorites: Boolean, myPosition: LatLng?) {
        mFavoritesOnly = favorites
        mMyPosition = myPosition
        mGetFavoritesUseCase.execute(GetFavoritesSubscriber())
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

    override fun setFavorite(toilet: ToiletsBean) {
        when (toilet.isFavorite) {
            false -> mAddFavoriteUseCase.execute(FavoriteSubscriber(toilet), FavoriteBean(toilet.id))
            true -> mRemoveFavoriteUseCase.execute(FavoriteSubscriber(toilet), FavoriteBean(toilet.id))
        }
    }

    override fun showDetails(toilet: ToiletsBean) {
        mRouter?.openToilet(mView!!, toilet)
    }

    override fun sortByDistance() {
        mMyPosition?.let {
            val comparator = Comparator<ToiletsBean> { a, b ->
                when {
                    a.distanceToMe < b.distanceToMe -> -1
                    a.distanceToMe > b.distanceToMe -> 1
                    else -> 0
                }
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                mToilets!!.sortWith(comparator)
            } else {
                Collections.sort(mToilets, comparator)
            }
        }
        mView?.notifyToiletsListRetrieved()
    }

    override fun sortByOpens() {
        val list = ArrayList<ToiletsBean>()

        // Get current time in Paris
        val c = Calendar.getInstance()
        c.timeZone = TimeZone.getTimeZone("Europe/Paris")
        val timeInParis = (c.get(Calendar.HOUR_OF_DAY) * 60 + c.get(Calendar.MINUTE))

        for (toilet in mToilets!!) {
            val opening = toilet.getOpening()
            if (opening == H24) {
                list.add(toilet)
            } else {
                try {
                    val opens = opening.substring(0, opening.indexOf(" h"))
                    val closes = opening.substring(opening.indexOf("-") + 2, opening.lastIndexOf(" h"))
                    val opensTime = opens.toLong() * 60
                    val closesTime = closes.toLong() * 60
                    if (timeInParis in opensTime..(closesTime - 1)) {
                        list.add(toilet)
                    }
                } catch (ignored :Exception) {}
            }
        }
        for (toilet in mToilets!!) {
            if (!list.contains(toilet)) {
                list.add(toilet)
            }
        }

        mToilets = list
        mView?.notifyToiletsListRetrieved()
    }

    inner class FavoriteSubscriber(private var toilet: ToiletsBean) : ResourceObserver<Void>() {
        override fun onComplete() {
            toilet.isFavorite = !toilet.isFavorite
            if (!toilet.isFavorite && mFavoritesOnly) {
                val index = mToilets!!.indexOf(toilet)
                mToilets!!.removeAt(index)
                mView?.notifyitemRemoved(index)
            }
        }

        override fun onNext(t: Void) {
            // Nothing to do
        }

        override fun onError(e: Throwable) {
            //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

    }

    inner class GetToiletsSubscriber(private val favorites: List<FavoriteBean>?) : ResourceObserver<ArrayList<ToiletsBean>>() {
        override fun onComplete() {
            // Nothing to do
        }

        override fun onNext(toilets: ArrayList<ToiletsBean>) {
            mToilets = toilets
            favorites?.let {
                checkFavorites(mToilets!!, favorites, mFavoritesOnly)
            }

            mMyPosition?.let {
                for (toilet in mToilets!!) {
                    toilet.distanceToMe = calculateDistance(mMyPosition!!, toilet.getPosition())
                }
            }
            mView?.notifyToiletsListRetrieved()
        }

        override fun onError(e: Throwable) {
            Log.e(TAG, "Error getting toilets list", e)
            //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }
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
}