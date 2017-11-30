package me.cassiano.boilerplate.features.feature.viewmodel

import me.cassiano.boilerplate.data.db.entity.Entity

data class EntityViewState(
        val data: List<Entity>? = null,
        val loading: Boolean = false,
        val error: Throwable? = null) {

    companion object {

        @JvmStatic
        fun loading(): EntityViewState = EntityViewState(null, true, null)

        @JvmStatic
        fun success(data: List<Entity>): EntityViewState = EntityViewState(data, false, null)

        @JvmStatic
        fun error(error: Throwable): EntityViewState = EntityViewState(null, false, error)
    }
}