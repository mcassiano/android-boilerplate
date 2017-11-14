package me.cassiano.boilerplate.features.feature.injection

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import dagger.Module
import dagger.Provides
import me.cassiano.boilerplate.data.EntityDataSource
import me.cassiano.boilerplate.data.EntityRepository
import me.cassiano.boilerplate.data.EntityRepositoryContract
import me.cassiano.boilerplate.data.db.AppDatabase
import me.cassiano.boilerplate.data.db.dao.EntityDao
import me.cassiano.boilerplate.data.local.LocalEntityDataSource
import me.cassiano.boilerplate.data.remote.EntityApi
import me.cassiano.boilerplate.data.remote.RemoteEntityDataSource
import me.cassiano.boilerplate.injection.PerActivity
import okhttp3.OkHttpClient
import retrofit2.CallAdapter
import retrofit2.Converter
import retrofit2.Retrofit
import javax.inject.Named

@PerActivity
@Module
abstract class FeatureActivityModule {

    @Module
    companion object {

        @PerActivity
        @JvmStatic
        @Provides
        @Named("baseUrl")
        fun providesBaseUrl() = "http://192.168.1.74:8000/"

        @JvmStatic
        @Provides
        fun providesPlaceDao(appDatabase: AppDatabase): EntityDao {
            return appDatabase.entityDao()
        }

        @PerActivity
        @JvmStatic
        @Provides
        fun providesRetrofit(@Named("baseUrl") baseUrl: String, client: OkHttpClient,
                             converter: Converter.Factory, callAdapter: CallAdapter.Factory): Retrofit {
            return Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .client(client)
                    .addConverterFactory(converter)
                    .addCallAdapterFactory(callAdapter)
                    .build()
        }

        @PerActivity
        @JvmStatic
        @Provides
        fun providesApi(retrofit: Retrofit): EntityApi {
            return retrofit.create(EntityApi::class.java)
        }

        @PerActivity
        @JvmStatic
        @Provides
        @Named("localEntityDataSource")
        fun providesLocalDataSource(entityDao: EntityDao): EntityDataSource {
            return LocalEntityDataSource(entityDao)
        }

        @PerActivity
        @JvmStatic
        @Provides
        @Named("remoteEntityDataSource")
        fun providesRemoteDataSource(apiService: EntityApi): EntityDataSource {
            return RemoteEntityDataSource(apiService)
        }

        @PerActivity
        @JvmStatic
        @Provides
        fun providesSharedPrefs(appContext: Context): SharedPreferences {
            return appContext.getSharedPreferences("app_prefs", MODE_PRIVATE)
        }

        @PerActivity
        @JvmStatic
        @Provides
        fun providesRepository(
                @Named("localEntityDataSource") localSource: EntityDataSource,
                @Named("remoteEntityDataSource") remoteSource: EntityDataSource
        ): EntityRepositoryContract {
            return EntityRepository(localSource, remoteSource)
        }
    }

}
