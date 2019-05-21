package com.wechantloup.side.dagger.components

import android.app.Application
import com.wechantloup.side.dagger.modules.ActivityBuilder
import com.wechantloup.side.dagger.modules.ApplicationModule
import com.wechantloup.side.modules.MyApplication
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import dagger.android.support.DaggerApplication
import javax.inject.Singleton

@Singleton
@Component(modules = [AndroidSupportInjectionModule::class, ApplicationModule::class, ActivityBuilder::class])
interface ApplicationComponent : AndroidInjector<DaggerApplication> {

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder

        fun applicationModule(applicationModule: ApplicationModule): Builder

        fun build(): ApplicationComponent
    }

    fun inject(target: MyApplication)

    class Initializer private constructor() {

        companion object {

            fun init(application: Application): ApplicationComponent {
                return DaggerApplicationComponent.builder()
                    .application(application)
                    .applicationModule(ApplicationModule(application))
                    .build()
            }
        }
    }
}