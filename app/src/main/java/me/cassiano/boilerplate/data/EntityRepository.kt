package me.cassiano.boilerplate.data

import io.reactivex.Single
import me.cassiano.boilerplate.data.db.entity.Entity

class EntityRepository(private val localSource: EntityDataSource,
                       private val remoteSource: EntityDataSource) : EntityRepositoryContract {

    override fun getEntities(): Single<List<Entity>> {

        val remote = fetchEntities()

        val local = localSource
                .getEntities()
                .filter { it.isNotEmpty() } // use something to identify stale data (i.e. a manager)
                .switchIfEmpty(Single.never())

        return Single.ambArray(local, remote)
    }

    override fun fetchEntities(): Single<List<Entity>> {
        return remoteSource
                .getEntities()
                .flatMap {
                    localSource
                            .saveAll(it)
                            .andThen(Single.just(it))
                }
    }

}

