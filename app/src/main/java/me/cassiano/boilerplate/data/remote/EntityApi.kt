package me.cassiano.boilerplate.data.remote

import io.reactivex.Single
import me.cassiano.boilerplate.data.remote.entity.EntitiesResponse
import retrofit2.http.GET

interface EntityApi {

    @GET("entities.json")
    fun getEntities(): Single<EntitiesResponse>

}