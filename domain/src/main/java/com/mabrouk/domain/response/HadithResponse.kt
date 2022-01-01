package com.mabrouk.domain.response

data class HadithResponse<T:Any>(
    val data:T,
    val total:Int,
    val limit:Int,
    val previous:Int?,
    val next:Int?
)