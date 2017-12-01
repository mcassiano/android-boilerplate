package me.cassiano.boilerplate.injection

import dagger.Module
import dagger.Provides
import me.cassiano.boilerplate.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.CallAdapter
import retrofit2.Converter
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
abstract class NetworkingModule {

    @Module
    companion object {

        @Singleton
        @JvmStatic
        @Provides
        fun providesLogging(): HttpLoggingInterceptor {
            return HttpLoggingInterceptor().apply {
                if (BuildConfig.DEBUG)
                    level = HttpLoggingInterceptor.Level.BODY
                else level = HttpLoggingInterceptor.Level.NONE
            }
        }

        @Singleton
        @JvmStatic
        @Provides
        fun providesHttpClient(logging: HttpLoggingInterceptor): OkHttpClient {
            return OkHttpClient.Builder()
                    .addInterceptor(logging)
                    .cache(null)
                    .connectTimeout(10, TimeUnit.SECONDS)
                    .readTimeout(10, TimeUnit.SECONDS)
                    .writeTimeout(10, TimeUnit.SECONDS)
                    .build()
        }

        @Singleton
        @JvmStatic
        @Provides
        fun providesJsonConverter(): Converter.Factory = MoshiConverterFactory.create()

        @Singleton
        @JvmStatic
        @Provides
        fun providesCallAdapter(): CallAdapter.Factory = RxJava2CallAdapterFactory.create()

    }

}