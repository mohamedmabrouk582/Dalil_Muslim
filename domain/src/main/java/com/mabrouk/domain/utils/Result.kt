package com.mabrouk.domain.utils

sealed class Result<out T:Any>{
    data class OnSuccess<out T:Any>(val data:T) : Result<T>()
    data class OnFailure(val throwable:Throwable) : Result<Nothing>()
    data class OnLoading(val loading:Boolean) : Result<Nothing>()
    data class NoInternetConnect(val error:String) : Result<Nothing>()
}