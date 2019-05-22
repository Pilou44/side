package com.wechantloup.side.data.repository

import com.wechantloup.side.data.content.DataBase
import com.wechantloup.side.domain.bean.ToiletsBean
import com.wechantloup.side.domain.bean.ToiletsListBean
import io.reactivex.Observable
import retrofit2.Retrofit
import retrofit2.http.GET
import javax.inject.Inject

class ToiletsRepository

@Inject
constructor(retrofit: Retrofit, dataBase: DataBase) {

    private var mService: ToiletsService
    private var mToiletsDao: DataBase.ToiletsDao

    init {
        mService = retrofit.create(ToiletsService::class.java)
        mToiletsDao = dataBase.toiletsDao()
    }

    fun getToilets(): Observable<ToiletsListBean> {
        return mService.getToilets()
    }

    fun saveToilets(list: List<ToiletsBean>) {
        mToiletsDao.deleteAll()
        mToiletsDao.insertAll(list)
    }

    fun loadToilets(): List<ToiletsBean> {
        return mToiletsDao.getAll()
    }

    private interface ToiletsService {
        @GET("search/?dataset=sanisettesparis2011&rows=1000")
        fun getToilets(): Observable<ToiletsListBean>
    }
}