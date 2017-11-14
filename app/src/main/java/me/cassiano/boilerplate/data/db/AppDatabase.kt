package me.cassiano.boilerplate.data.db

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import me.cassiano.boilerplate.data.db.dao.EntityDao
import me.cassiano.boilerplate.data.db.entity.Entity

@Database(entities = arrayOf(Entity::class), version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun entityDao(): EntityDao
}