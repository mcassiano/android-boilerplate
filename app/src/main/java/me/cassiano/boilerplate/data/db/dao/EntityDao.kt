package me.cassiano.boilerplate.data.db.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import io.reactivex.Single
import me.cassiano.boilerplate.data.db.entity.Entity

@Dao
interface EntityDao {

    @Query("SELECT * FROM Entity")
    fun getAll(): Single<List<Entity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(places: List<Entity>)
}