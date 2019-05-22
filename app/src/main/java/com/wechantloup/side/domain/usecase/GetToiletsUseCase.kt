package com.wechantloup.side.domain.usecase

import com.wechantloup.side.dagger.scope.PerApplicationView
import com.wechantloup.side.data.repository.ToiletsRepository
import com.wechantloup.side.domain.bean.ToiletsBean
import io.reactivex.Observable
import io.reactivex.Scheduler
import javax.inject.Inject

@PerApplicationView
class GetToiletsUseCase

@Inject internal constructor(scheduler: Scheduler, private val mRepository: ToiletsRepository): UseCase<ArrayList<ToiletsBean>, Void>(scheduler) {

    override fun buildObservable(params: Void?): Observable<ArrayList<ToiletsBean>> {
        return mRepository.getToilets().flatMap {
            Observable.just(it.list)
        }
    }
}