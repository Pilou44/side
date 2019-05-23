package com.wechantloup.side.data.repository

import com.wechantloup.side.data.content.DataBase
import com.wechantloup.side.domain.bean.ToiletBean
import com.wechantloup.side.domain.bean.ToiletsListBean
import io.reactivex.Observable
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Query
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ToiletsRepository

@Inject
constructor(retrofit: Retrofit, dataBase: DataBase) {

    private var mService: ToiletsService = retrofit.create(ToiletsService::class.java)
    private var mToiletsDao: DataBase.ToiletsDao = dataBase.toiletsDao()

    fun getToilets(): Observable<ToiletsListBean> {
        return mService.getToilets("sanisettesparis2011", 1000)
    }

    fun saveToilets(list: List<ToiletBean>) {
        mToiletsDao.deleteAll()
        mToiletsDao.insertAll(list)
    }

    fun loadToilets(): List<ToiletBean> {
        return mToiletsDao.getAll()
    }

    private interface ToiletsService {
        @GET("search/")
        fun getToilets(@Query(value = "dataset") dataset: String, @Query(value = "rows") rows: Int ): Observable<ToiletsListBean>
    }
}