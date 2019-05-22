package com.wechantloup.side.modules.details

import com.wechantloup.side.domain.bean.FavoriteBean
import com.wechantloup.side.domain.bean.ToiletsBean
import com.wechantloup.side.domain.usecase.AddFavoriteUseCase
import com.wechantloup.side.domain.usecase.RemoveFavoriteUseCase
import com.wechantloup.side.events.ModifyFavoriteEvent
import com.wechantloup.side.modules.core.BaseContract
import com.wechantloup.side.modules.core.BasePresenter
import io.reactivex.observers.ResourceObserver
import org.greenrobot.eventbus.EventBus

class DetailsPresenter(private var mAddFavoriteUseCase: AddFavoriteUseCase,
                       private var mRemoveFavoriteUseCase: RemoveFavoriteUseCase) :
    BasePresenter<BaseContract.Router, DetailsContract.View>(null), DetailsContract.Presenter {

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

    inner class FavoriteSubscriber() : ResourceObserver<Void>() {
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