package me.cassiano.boilerplate.injection

import android.app.Application
import android.arch.persistence.room.Room
import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.android.AndroidInjectionModule
import dagger.android.ContributesAndroidInjector
import me.cassiano.boilerplate.data.db.AppDatabase
import me.cassiano.boilerplate.features.feature.FeatureActivity
import me.cassiano.boilerplate.features.feature.injection.FeatureActivityModule
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.CallAdapter
import retrofit2.Converter
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton


@Module(includes = arrayOf(AndroidInjectionModule::class))
abstract class AppModule {

    @PerActivity
    @ContributesAndroidInjector(modules = arrayOf(FeatureActivityModule::class))
    abstract fun featureActivity(): FeatureActivity

    @Module
    companion object {

        @Singleton
        @JvmStatic
        @Provides
        fun provideContext(application: Application): Context {
            return application.applicationContext
        }

        @Singleton
        @JvmStatic
        @Provides
        fun providesDatabase(appContext: Context): AppDatabase {
            return Room.databaseBuilder(appContext,
                    AppDatabase::class.java, "app-database").build()
        }

        @Singleton
        @JvmStatic
        @Provides
        fun providesLogging(): HttpLoggingInterceptor {
            return HttpLoggingInterceptor().apply {
                //                if (BuildConfig.DEBUG)
                level = HttpLoggingInterceptor.Level.BODY
//                else level = HttpLoggingInterceptor.Level.NONE
            }
        }

        @Singleton
        @JvmStatic
        @Provides
        fun providesHttpClient(logging: HttpLoggingInterceptor): OkHttpClient {
            return OkHttpClient.Builder()
                    .addInterceptor(logging)
                    .cache(null)
                    .build()
        }

        @Singleton
        @JvmStatic
        @Provides
        fun providesJsonConverter(): Converter.Factory {
            return MoshiConverterFactory.create()
        }

        @Singleton
        @JvmStatic
        @Provides
        fun providesCallAdapter(): CallAdapter.Factory {
            return RxJava2CallAdapterFactory.create()
        }
    }

}