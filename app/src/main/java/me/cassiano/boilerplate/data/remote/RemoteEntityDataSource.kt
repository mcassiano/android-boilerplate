package me.cassiano.boilerplate.data.remote

import io.reactivex.Completable
import io.reactivex.Single
import me.cassiano.boilerplate.data.EntityDataSource
import me.cassiano.boilerplate.data.db.entity.Entity
import me.cassiano.boilerplate.data.mapper.mapToEntity

class RemoteEntityDataSource(private val api: EntityApi) : EntityDataSource {

    override fun saveAll(items: List<Entity>): Completable {
        throw IllegalStateException("Not supported")
    }

    override fun getEntities(): Single<List<Entity>> {
        return api.getEntities()
                .toFlowable()
                .flatMapIterable { it.results }
                .map { it.mapToEntity() }
                .toList()
    }

}