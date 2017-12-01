package me.cassiano.boilerplate.injection

import dagger.Module
import dagger.Provides
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import me.cassiano.boilerplate.injection.qualifiers.ComputationScheduler
import me.cassiano.boilerplate.injection.qualifiers.IoScheduler
import me.cassiano.boilerplate.injection.qualifiers.MainThreadScheduler
import javax.inject.Singleton

@Module
abstract class ThreadingModule {

    @Module
    companion object {

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