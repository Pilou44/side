package com.wechantloup.side.dagger.modules

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.wechantloup.side.data.content.DataBase
import dagger.Module
import dagger.Provides
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import javax.inject.Singleton

@Module
class ApplicationModule(private var application: Application) {

    @Provides
    @Singleton
    internal fun provideApplicationContext(): Context {
        return application
    }

    @Provides
    @Singleton
    internal fun provideScheduler(): Scheduler {
        return AndroidSchedulers.mainThread()
    }

    @Provides
    @Singleton
    internal fun provideDatabase(): DataBase {
        return Room.databaseBuilder(application, DataBase::class.java, "my-database").build()
    }
}