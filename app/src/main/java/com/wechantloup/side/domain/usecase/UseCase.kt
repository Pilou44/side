package com.wechantloup.side.domain.usecase

import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.ResourceObserver
import io.reactivex.schedulers.Schedulers

abstract class UseCase<T, Params>(private val mPostExecutionThread: Scheduler) {

    private val mSubscription = CompositeDisposable()

    protected abstract fun buildObservable(params: Params?): Observable<T>

    fun execute(useCaseSubscriber: ResourceObserver<T>, params: Params?) {
        mSubscription.add(buildObservable(params)
            .subscribeOn(Schedulers.io())
            .observeOn(mPostExecutionThread)
            .subscribeWith(useCaseSubscriber))
    }

    fun execute(useCaseSubscriber: ResourceObserver<T>) {
        execute(useCaseSubscriber, null)
    }

    fun unsubscribe() {
        if (!mSubscription.isDisposed) {
            mSubscription.dispose()
        }
    }
}
