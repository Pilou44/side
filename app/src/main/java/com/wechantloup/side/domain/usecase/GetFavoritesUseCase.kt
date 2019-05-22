package com.wechantloup.side.domain.usecase

import com.wechantloup.side.dagger.scope.PerApplicationView
import com.wechantloup.side.data.repository.FavoriteRepository
import com.wechantloup.side.domain.bean.FavoriteBean
import io.reactivex.Observable
import io.reactivex.Scheduler
import javax.inject.Inject

@PerApplicationView
class GetFavoritesUseCase

@Inject internal constructor(scheduler: Scheduler, private val mRepository: FavoriteRepository): UseCase<List<FavoriteBean>, Void>(scheduler) {

    override fun buildObservable(params: Void?): Observable<List<FavoriteBean>> {
        return mRepository.getFavorites()
    }
}