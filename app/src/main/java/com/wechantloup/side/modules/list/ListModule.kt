package com.wechantloup.side.modules.list

import com.wechantloup.side.dagger.scope.PerApplicationView
import com.wechantloup.side.domain.usecase.AddFavoriteUseCase
import com.wechantloup.side.domain.usecase.GetFavoritesUseCase
import com.wechantloup.side.domain.usecase.GetToiletsUseCase
import com.wechantloup.side.domain.usecase.RemoveFavoriteUseCase
import dagger.Module
import dagger.Provides

@Module
class ListModule {

    @Provides
    @PerApplicationView
    internal fun provideListPresenter(router: ListContract.Router,
                                      getToiletsUseCase: GetToiletsUseCase,
                                      getFavoritesUseCase: GetFavoritesUseCase,
                                      addFavoriteUseCase: AddFavoriteUseCase,
                                      removeFavoriteUseCase: RemoveFavoriteUseCase): ListContract.Presenter {
        return ListPresenter(router, getToiletsUseCase, getFavoritesUseCase, addFavoriteUseCase, removeFavoriteUseCase)
    }

    @Provides
    @PerApplicationView
    internal fun provideListRouter(): ListContract.Router {
        return ListRouter()
    }
}