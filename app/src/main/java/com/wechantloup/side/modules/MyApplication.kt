package com.wechantloup.side.modules

import android.app.Activity
import androidx.annotation.VisibleForTesting
import com.facebook.stetho.Stetho
import com.wechantloup.side.dagger.components.ApplicationComponent
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.DaggerApplication
import javax.inject.Inject

class MyApplication: DaggerApplication() {

    @Inject
    @VisibleForTesting(otherwise = VisibleForTesting.PACKAGE_PRIVATE)
    lateinit var mActivityDispatchingAndroidInjector: DispatchingAndroidInjector<Activity>

    private var mApplicationComponent: ApplicationComponent

    init {
        mApplicationComponent = ApplicationComponent.Initializer.init(this)
        mApplicationComponent.inject(this)
    }

    override fun onCreate() {
        super.onCreate()

        Stetho.initialize(
            Stetho.newInitializerBuilder(this)
                .enableDumpapp(Stetho.defaultDumperPluginsProvider(this))
                .enableWebKitInspector(Stetho.defaultInspectorModulesProvider(this))
                .build())
    }

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return mApplicationComponent
    }
}