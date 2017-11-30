package me.cassiano.boilerplate.injection

import android.app.Application
import android.arch.persistence.room.Room
import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.android.AndroidInjectionModule
import dagger.android.ContributesAndroidInjector
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import me.cassiano.boilerplate.data.db.AppDatabase
import me.cassiano.boilerplate.features.feature.FeatureActivity
import me.cassiano.boilerplate.features.feature.injection.FeatureActivityModule
import me.cassiano.boilerplate.injection.qualifiers.ComputationScheduler
import me.cassiano.boilerplate.injection.qualifiers.IoScheduler
import me.cassiano.boilerplate.injection.qualifiers.MainThreadScheduler
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.CallAdapter
import retrofit2.Converter
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
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
        fun provideContext(application: Application): Context = application.applicationContext

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

        @Singleton
        @JvmStatic
        @Provides
        @IoScheduler
        fun providesIoScheduler(): Scheduler = Schedulers.io()

        @Singleton
        @JvmStatic
        @Provides
        @ComputationScheduler
        fun providesComputationScheduler(): Scheduler = Schedulers.computation()

        @Singleton
        @JvmStatic
        @Provides
        @MainThreadScheduler
        fun providesMainThreadScheduler(): Scheduler = AndroidSchedulers.mainThread()
    }

}