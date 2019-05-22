package com.wechantloup.side.modules.home

import com.wechantloup.side.dagger.scope.PerApplicationView
import com.wechantloup.side.domain.usecase.GetToiletsUseCase
import dagger.Module
import dagger.Provides

@Module
class HomeModule {

    @Provides
    @PerApplicationView
    internal fun provideHomePresenter(router: HomeContract.Router, getToiletsUseCase: GetToiletsUseCase): HomeContract.Presenter {
        return HomePresenter(router, getToiletsUseCase)
    }

    @Provides
    @PerApplicationView
    internal fun provideHomeRouter(): HomeContract.Router {
        return HomeRouter()
    }
}