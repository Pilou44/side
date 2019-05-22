package com.wechantloup.side.modules.list

import com.wechantloup.side.dagger.scope.PerApplicationView
import com.wechantloup.side.domain.usecase.GetToiletsUseCase
import dagger.Module
import dagger.Provides

@Module
class ListModule {

    @Provides
    @PerApplicationView
    internal fun provideListPresenter(getToiletsUseCase: GetToiletsUseCase): ListContract.Presenter {
        return ListPresenter(getToiletsUseCase)
    }
}