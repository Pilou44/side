package com.wechantloup.side.dagger.modules

import com.wechantloup.side.dagger.scope.PerApplicationView
import com.wechantloup.side.modules.details.DetailsActivity
import com.wechantloup.side.modules.details.DetailsModule
import com.wechantloup.side.modules.home.HomeActivity
import com.wechantloup.side.modules.home.HomeModule
import com.wechantloup.side.modules.list.ListActivity
import com.wechantloup.side.modules.list.ListModule
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBuilder {

    @PerApplicationView
    @ContributesAndroidInjector(modules = [HomeModule::class])
    internal abstract fun bindHomeActivity(): HomeActivity

    @PerApplicationView
    @ContributesAndroidInjector(modules = [ListModule::class])
    internal abstract fun bindListActivity(): ListActivity

    @PerApplicationView
    @ContributesAndroidInjector(modules = [DetailsModule::class])
    internal abstract fun bindDetailsActivity(): DetailsActivity
}