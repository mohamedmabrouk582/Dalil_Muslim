package com.mabrouk.data.api

import com.mabrouk.domain.models.HadithBookNumber
import com.mabrouk.domain.models.HadithCategory
import com.mabrouk.domain.models.HadithModel
import com.mabrouk.domain.response.HadithResponse
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface HadithApi {

    @GET("collections")
    suspend fun getHadithCategoriesAsync() : Response<HadithResponse<ArrayList<HadithCategory>>>

    @GET("collections/{collectionName}/books?limit=150")
    suspend fun getHadithBooks(@Path("collectionName") collectionName:String) : Response<HadithResponse<ArrayList<HadithBookNumber>>>

    @GET("collections/{collectionName}/books/{bookNumber}/hadiths?limit=100")
    suspend fun getHadith(@Path("collectionName") collectionName:String ,@Path("bookNumber") bookNumber:String,@Query("page") page:Int=1) : Response<HadithResponse<ArrayList<HadithModel>>>

}