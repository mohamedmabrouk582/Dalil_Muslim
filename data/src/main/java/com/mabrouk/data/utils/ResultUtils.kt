package com.mabrouk.data.utils

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import com.mabrouk.data.R
import com.mabrouk.domain.utils.Result
import com.google.gson.JsonIOException
import com.google.gson.JsonSyntaxException
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import org.json.JSONObject
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException
import java.net.SocketTimeoutException
import javax.net.ssl.SSLHandshakeException

typealias ApiResult<T> = suspend () -> Response<T>
typealias ApiResult2<T> = suspend () -> Deferred<T>

suspend fun <T : Any>executeCall(
    context: Context,
    messageInCaseOfError: String = "Network error",
    allowRetries: Boolean = true,
    numberOfRetries: Int = 2,
    apiCall:ApiResult<T>
): Flow<Result<T>> {
    var delayDuration = 1000L
    val delayFactor = 2
    return flow {
        emit(Result.OnLoading(true))
        val response = apiCall()
        if (response.isSuccessful){
            response.body()?.let {
                emit(Result.OnSuccess(it))
            }?:emit(Result.OnFailure(IOException("API call successful but empty response body")))
            return@flow
        }

        emit(
            Result.OnFailure(IOException( "API call failed with error - ${response.errorBody()?.string() ?: messageInCaseOfError}"))
        )
        return@flow
    }.catch{ e ->
        if (CheckNetwork.isOnline(context)){
            emit(Result.OnFailure(IOException(errorMsg(e,context))))
        }else{
            emit(Result.NoInternetConnect(context.getString(R.string.no_internet_connection)))
        }
        return@catch
    }.retryWhen{  cause, attempt ->
        if (!allowRetries || attempt > numberOfRetries || cause !is IOException) return@retryWhen false
        delay(delayDuration)
        delayDuration *= delayFactor
        return@retryWhen true
    }.flowOn(Dispatchers.IO)
}


suspend fun <T : Any>executeCall2(
    context: Context,
    messageInCaseOfError: String = "Network error",
    allowRetries: Boolean = true,
    numberOfRetries: Int = 2,
    apiCall:ApiResult2<T>
): Flow<Result<T>> {
    var delayDuration = 1000L
    val delayFactor = 2
    return flow {
        emit(Result.OnLoading(true))
        val response = apiCall()
          emit(Result.OnSuccess(response.await()))
        return@flow
    }.catch{ e ->
        if (CheckNetwork.isOnline(context)){
            emit(Result.OnFailure(IOException(errorMsg(e,context))))
        }else{
            emit(Result.NoInternetConnect(context.getString(R.string.no_internet_connection)))
        }
        return@catch
    }.retryWhen{  cause, attempt ->
        if (!allowRetries || attempt > numberOfRetries || cause !is IOException) return@retryWhen false
        delay(delayDuration)
        delayDuration *= delayFactor
        return@retryWhen true
    }.flowOn(Dispatchers.IO)
}


interface OnCheckConnection {
    fun ConnectionTrue()
    fun ConnectionError()
}

class CheckNetwork {
    companion object {

        fun isConnected(context: Context, OnCheckConnection: OnCheckConnection) {
            if (isOnline(context)) {
                OnCheckConnection.ConnectionTrue()
            } else {
                OnCheckConnection.ConnectionError()
            }
        }

        @SuppressLint("NewApi")
        fun isOnline(context: Context): Boolean {
            val connectivityManager =
                    context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val capabilities =
                    connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            if (capabilities != null) {
                when {
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
                        Log.i("Internet", "NetworkCapabilities.TRANSPORT_CELLULAR")
                        return true
                    }
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {
                        Log.i("Internet", "NetworkCapabilities.TRANSPORT_WIFI")
                        return true
                    }
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> {
                        Log.i("Internet", "NetworkCapabilities.TRANSPORT_ETHERNET")
                        return true
                    }
                }
            }
            return false
        }
    }
}

fun errorMsg(e: Throwable,context: Context) : String =
     when (e) {
        is HttpException -> {
            val error: String = analysisError(e)
            when (e.code()) {
                401, 403 -> error
                else -> error
            }
        }
        is SocketTimeoutException -> context.getString(R.string.socketTimeout)
        is JsonSyntaxException -> context.getString(R.string.Jsonpars)
        is SSLHandshakeException -> e.message.toString()
        else -> "Api Error"
    }


private fun analysisError(e: HttpException): String {
    return try {
        val responseStrings: String = e.response()!!.errorBody().toString()
        val jsonObject = JSONObject(responseStrings)
        when {
            jsonObject.has("msg") -> jsonObject.get("msg").toString()
            jsonObject.has("error") -> jsonObject.get("error").toString()
            else -> e.message()
        }
    } catch (ex: Exception) {
        if (e.message().isEmpty()) e.localizedMessage else e.message()
    }
}
