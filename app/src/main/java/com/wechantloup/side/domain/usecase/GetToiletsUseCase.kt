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
            mRepository.saveToilets(it.list)
            Observable.just(it.list)
        }.onErrorReturn{
            val list = mRepository.loadToilets()
            val array = ArrayList<ToiletsBean>()
            array.addAll(list)
            array
        }
    }
}
