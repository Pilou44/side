package com.wechantloup.side.dagger.modules

import com.facebook.stetho.okhttp3.StethoInterceptor
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
class InternetModule {

    companion object {
        private const val BASE_URL = "https://data.ratp.fr/api/records/1.0/"
    }

    @Provides
    @Singleton
    internal fun provideGson(): Gson {
        return GsonBuilder().create()
    }

    @Provides
    @Singleton
    internal fun provideRetrofit(gson: Gson): Retrofit {
        val client = OkHttpClient.Builder()
        client.addInterceptor { chain ->
            val original = chain.request()
            val requestBuilder = original.newBuilder().addHeader("Accept", "application/json")
            val request = requestBuilder.method(original.method(), original.body()).build()
            chain.proceed(request)
        }
        client.addNetworkInterceptor(StethoInterceptor())

        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .baseUrl(BASE_URL)
            .client(client.build())
            .build()
    }

}