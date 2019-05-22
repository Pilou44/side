package com.wechantloup.side.data.repository

import com.wechantloup.side.data.content.DataBase
import com.wechantloup.side.domain.bean.FavoriteBean
import io.reactivex.Observable
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FavoriteRepository

@Inject
constructor(dataBase: DataBase) {

    private var mFavoriteDao: DataBase.FavoriteDao = dataBase.favoritesDao()

    fun getFavorites(): Observable<List<FavoriteBean>> {
        return Observable.defer { Observable.just(mFavoriteDao.getAll()) }
    }

    fun addFavorite(favorite: FavoriteBean): Observable<Void> {
        return Observable.defer {
            mFavoriteDao.insert(favorite)
            Observable.empty<Void>()
        }
    }

    fun removeFavorite(favorite: FavoriteBean): Observable<Void> {
        return Observable.defer {
            mFavoriteDao.delete(favorite)
            Observable.empty<Void>()
        }
    }
}