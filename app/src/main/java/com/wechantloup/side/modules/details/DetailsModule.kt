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
    internal fun provideListPresenter(addFavoriteUseCase: AddFavoriteUseCase,
                                      removeFavoriteUseCase: RemoveFavoriteUseCase): DetailsContract.Presenter {
        return DetailsPresenter(addFavoriteUseCase, removeFavoriteUseCase)
    }
}