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
import javax.inject.Singleton


@Module(includes = arrayOf(AndroidInjectionModule::class,
        NetworkingModule::class, ThreadingModule::class))
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
    }

}