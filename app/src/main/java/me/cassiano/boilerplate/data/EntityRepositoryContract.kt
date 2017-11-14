package me.cassiano.boilerplate.data

import io.reactivex.Single
import me.cassiano.boilerplate.data.db.entity.Entity

interface EntityRepositoryContract {

    fun getEntities(): Single<List<Entity>>

    fun fetchEntities(): Single<List<Entity>>

}