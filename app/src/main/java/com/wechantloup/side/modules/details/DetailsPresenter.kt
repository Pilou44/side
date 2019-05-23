package com.wechantloup.side.modules.details

import android.util.Log
import com.wechantloup.side.domain.bean.FavoriteBean
import com.wechantloup.side.domain.bean.ToiletBean
import com.wechantloup.side.domain.usecase.AddFavoriteUseCase
import com.wechantloup.side.domain.usecase.RemoveFavoriteUseCase
import com.wechantloup.side.events.ModifyFavoriteEvent
import com.wechantloup.side.modules.core.BasePresenter
import io.reactivex.observers.ResourceObserver
import org.greenrobot.eventbus.EventBus

class DetailsPresenter(router: DetailsContract.Router,
                       private val mAddFavoriteUseCase: AddFavoriteUseCase,
                       private val mRemoveFavoriteUseCase: RemoveFavoriteUseCase) :
    BasePresenter<DetailsContract.Router, DetailsContract.View>(router), DetailsContract.Presenter {

    companion object {
        private val TAG = DetailsPresenter::class.java.simpleName
    }

    private lateinit var mToilet: ToiletBean

    override fun setFavorite(favorite: Boolean) {
        when (favorite) {
            true -> mAddFavoriteUseCase.execute(FavoriteSubscriber(), FavoriteBean(mToilet.id))
            false -> mRemoveFavoriteUseCase.execute(FavoriteSubscriber(), FavoriteBean(mToilet.id))
        }
    }

    override fun setToilet(toilet: ToiletBean) {
        mToilet = toilet
    }

    override fun share() {
        val text = mToilet.getAdministrator() + "\n" + mToilet.getAddress() + "\n" + mToilet.getOpening()
        mRouter?.share(mView!!, text)
    }

    override fun onContextRestored() {
        // Nothing to do
    }

    inner class FavoriteSubscriber : ResourceObserver<Void>() {
        override fun onComplete() {
            mToilet.isFavorite = !mToilet.isFavorite
            EventBus.getDefault().post(ModifyFavoriteEvent(mToilet))
        }

        override fun onNext(t: Void) {
            // Nothing to do
        }

        override fun onError(e: Throwable) {
            Log.e(TAG, "Error modifying favorite", e)
            mView?.notifyErrorModifyingFavorite(mToilet)
            mView?.notifyError()
        }

    }
}