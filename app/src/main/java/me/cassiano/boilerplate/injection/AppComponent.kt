package me.cassiano.boilerplate.injection

import android.app.Application
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import me.cassiano.boilerplate.app.App
import javax.inject.Singleton


@Singleton
@Component(modules = arrayOf(AndroidInjectionModule::class, AppModule::class))
interface AppComponent : AndroidInjector<App> {

    override fun inject(instance: App)

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): AppComponent.Builder

        fun build(): AppComponent
    }

}