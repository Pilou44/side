package com.wechantloup.side.modules.details

import com.wechantloup.side.domain.bean.FavoriteBean
import com.wechantloup.side.domain.bean.ToiletsBean
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

    private lateinit var mToilet: ToiletsBean

    override fun setFavorite(favorite: Boolean) {
        when (favorite) {
            true -> mAddFavoriteUseCase.execute(FavoriteSubscriber(), FavoriteBean(mToilet.id))
            false -> mRemoveFavoriteUseCase.execute(FavoriteSubscriber(), FavoriteBean(mToilet.id))
        }
    }

    override fun setToilet(toilet: ToiletsBean) {
        mToilet = toilet
    }

    override fun share() {
        val text = mToilet.getAdministrator() + "\n" + mToilet.getAddress() + "\n" + mToilet.getOpening()
        mRouter?.share(mView!!, text)
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
            //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

    }
}