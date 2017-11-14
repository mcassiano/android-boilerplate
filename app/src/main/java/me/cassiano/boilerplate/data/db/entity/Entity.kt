package me.cassiano.boilerplate.data.db.entity

import android.arch.persistence.room.Entity

@Entity(primaryKeys = arrayOf("id"))
data class Entity(val id: String, val name: String)
