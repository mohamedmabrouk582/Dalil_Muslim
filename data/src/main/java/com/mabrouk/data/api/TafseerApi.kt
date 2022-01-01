package com.mabrouk.data.api

import com.mabrouk.domain.models.TafsirAya
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface TafseerApi {
    @GET("tafseer/{tafseer_id}/{sura_number}/{ayah_number}")
    fun getAyaTafseer(@Path("tafseer_id") tafseer_id:Int,@Path("sura_number") sura_number:Int,
    @Path("ayah_number") ayah_number:Int) : Deferred<TafsirAya>
}