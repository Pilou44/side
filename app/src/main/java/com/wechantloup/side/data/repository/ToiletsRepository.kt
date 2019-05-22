package com.wechantloup.side.data.repository

import com.wechantloup.side.domain.bean.ToiletsListBean
import io.reactivex.Observable
import retrofit2.Retrofit
import retrofit2.http.GET
import javax.inject.Inject

class ToiletsRepository

@Inject
constructor(retrofit: Retrofit) {

    private var mService: ToiletsService

    init {
        mService = retrofit.create(ToiletsService::class.java)
    }

    fun getToilets(): Observable<ToiletsListBean> {
        return mService.getToilets()
    }

    private interface ToiletsService {
        @GET("search/?dataset=sanisettesparis2011&rows=1000")
        fun getToilets(): Observable<ToiletsListBean>
    }
}