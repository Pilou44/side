package com.wechantloup.side.modules.details

import com.wechantloup.side.dagger.scope.PerApplicationView
import com.wechantloup.side.domain.usecase.AddFavoriteUseCase
import com.wechantloup.side.domain.usecase.RemoveFavoriteUseCase
import dagger.Module
import dagger.Provides

@Module
class DetailsModule {

    @Provides
    @PerApplicationView
    internal fun provideDetailsPresenter(router: DetailsContract.Router, addFavoriteUseCase: AddFavoriteUseCase,
                                         removeFavoriteUseCase: RemoveFavoriteUseCase): DetailsContract.Presenter {
        return DetailsPresenter(router, addFavoriteUseCase, removeFavoriteUseCase)
    }

    @Provides
    @PerApplicationView
    internal fun provideDetailsRouter(): DetailsContract.Router {
        return DetailsRouter()
    }
}