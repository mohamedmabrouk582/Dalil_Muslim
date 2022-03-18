package com.mabrouk.data.api

import com.mabrouk.domain.response.*
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*

interface QuranApi {
    @GET("chapters")
    suspend fun getAllSura() : Response<SurasResponse>

    @GET("juzs")
    suspend fun getJuzs() : Response<JuzResponse>

    @GET("chapters/{chapter_id}/verses?recitation=1&translations=21&language=en&text_type=words")
    suspend fun getSuraVerses(@Path("chapter_id") chapter_id:Int, @Query("page") page:Int) : Response<VersesResponse>

    @GET("chapters/{chapter_id}/verses/{verse_id}/tafsirs")
    suspend fun getVerseTafsir(@Path("chapter_id") chapter_id:Int,@Path("verse_id") verse_id:Int) : Response<TafsirResponse>

    @GET
    @Streaming
    suspend fun downloadAudio(@Url url:String) : Response<ResponseBody>

    @GET
    suspend fun getRadios(@Url url : String) : Response<RadioResponse>

    @GET("search?size=20&page=1&language=ar")
    suspend fun search(@Query("q") query:String) : Response<Any>

}