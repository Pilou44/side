package com.wechantloup.side.dagger.modules

import com.wechantloup.side.dagger.scope.PerApplicationView
import com.wechantloup.side.modules.home.HomeActivity
import com.wechantloup.side.modules.home.HomeModule
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBuilder {

    @PerApplicationView
    @ContributesAndroidInjector(modules = [HomeModule::class])
    internal abstract fun bindHomeActivity(): HomeActivity
}