package com.mabrouk.dalilmuslim.workManger

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.WorkerParameters
import com.mabrouk.dalilmuslim.utils.DOWNLOAD_VERSES_IDS
import com.mabrouk.dalilmuslim.utils.EventBus
import com.mabrouk.dalilmuslim.utils.VERSES_ID
import com.mabrouk.dalilmuslim.utils.VERSES_IDS
import com.mabrouk.data.repository.QuranRepository
import com.mabrouk.data.utils.errorMsg
import com.mabrouk.domain.utils.Result
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltWorker
class SuraDownload @AssistedInject constructor(@Assisted val context: Context,@Assisted params: WorkerParameters
                                                   , val repository: QuranRepository, val event:EventBus ) : CoroutineWorker(context,params) {

    override suspend fun doWork(): Result {
        var result:String?=null
        var id:Int=0
        val data = Data.Builder()
        inputData.getIntArray(VERSES_IDS)?.toList()?.forEach {
            repository.getSurah(it).first().name_arabic?.let { it1 -> event.sendType(it1) }
            result=downloadSura(it)
        }
        data.putString(DOWNLOAD_VERSES_IDS, result)
        data.putInt(VERSES_ID,id)
        if (result==null)return Result.success(data.build())
        return Result.failure(data.build())
    }

    suspend fun downloadSura(id:Int,page:Int=1) : String? {
        var result:String? =null
        repository.requestVerses(id,page).collect {
          when(it){
              is com.mabrouk.domain.utils.Result.OnSuccess -> {
                  withContext(Dispatchers.IO) {
                      repository.saveVerses(it.data.verses)
                  }
                  if (it.data.meta.current_page <= it.data.meta.total_pages!!) {
                      downloadSura(id, it.data.meta.current_page+1)
                  }

              }
                is com.mabrouk.domain.utils.Result.OnFailure -> result= it.throwable.message!!
                is com.mabrouk.domain.utils.Result.NoInternetConnect -> result = it.error
            }
        }
        return result
    }
}