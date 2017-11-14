package me.cassiano.boilerplate.data

import io.reactivex.Completable
import io.reactivex.Single
import me.cassiano.boilerplate.data.db.entity.Entity

interface EntityDataSource {
    fun getEntities(): Single<List<Entity>>
    fun saveAll(items: List<Entity>): Completable
}