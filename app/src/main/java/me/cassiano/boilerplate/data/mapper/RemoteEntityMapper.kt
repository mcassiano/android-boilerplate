package me.cassiano.boilerplate.data.mapper

import me.cassiano.boilerplate.data.db.entity.Entity
import me.cassiano.boilerplate.data.remote.entity.RemoteEntity

fun RemoteEntity.mapToEntity(): Entity {
    return Entity(id, name)
}