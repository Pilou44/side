package com.wechantloup.side.domain.usecase

import com.wechantloup.side.dagger.scope.PerApplicationView
import com.wechantloup.side.data.repository.FavoriteRepository
import com.wechantloup.side.domain.bean.FavoriteBean
import io.reactivex.Observable
import io.reactivex.Scheduler
import javax.inject.Inject

@PerApplicationView
class AddFavoriteUseCase

@Inject internal constructor(scheduler: Scheduler, private val mRepository: FavoriteRepository): UseCase<Void, FavoriteBean>(scheduler) {

    override fun buildObservable(favorite: FavoriteBean?): Observable<Void> {
        return mRepository.addFavorite(favorite!!)
    }
}