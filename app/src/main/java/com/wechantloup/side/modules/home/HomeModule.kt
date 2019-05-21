package com.wechantloup.side.modules.home

import com.wechantloup.side.dagger.scope.PerApplicationView
import dagger.Module
import dagger.Provides


@Module
class HomeModule {

    @Provides
    @PerApplicationView
    internal fun provideHomePresenter(router: HomeContract.Router): HomeContract.Presenter {
        return HomePresenter(router)
    }

    @Provides
    @PerApplicationView
    internal fun provideHomeRouter(): HomeContract.Router {
        return HomeRouter()
    }
}