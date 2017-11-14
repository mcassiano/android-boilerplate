package me.cassiano.boilerplate.data.local

import io.reactivex.Completable
import io.reactivex.Single
import me.cassiano.boilerplate.data.EntityDataSource
import me.cassiano.boilerplate.data.db.dao.EntityDao
import me.cassiano.boilerplate.data.db.entity.Entity

class LocalEntityDataSource(private val dao: EntityDao) : EntityDataSource {

    override fun saveAll(items: List<Entity>): Completable {
        return Completable.fromCallable { dao.insert(items) }
    }

    override fun getEntities(): Single<List<Entity>> {
        return dao.getAll()
    }
}