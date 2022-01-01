package com.mabrouk.dalilmuslim.viewModels

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.viewModelScope
import com.mabrouk.dalilmuslim.states.HadithStates
import com.mabrouk.dalilmuslim.utils.HADITH_CATEGORIES_DOWNLOADED
import com.mabrouk.dalilmuslim.viewModels.base.BaseViewModel
import com.mabrouk.data.repository.HadithMyDafaultRepository
import com.mabrouk.data.utils.DataStorePreferences
import com.mabrouk.data.utils.toEntity
import com.mabrouk.domain.models.HadithBookNumber
import com.mabrouk.domain.repository.HadithDefaultRepository
import com.mabrouk.domain.utils.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class HadithCategoryViewModel @Inject constructor(application: Application,val repository: HadithMyDafaultRepository, val dataStore : DataStorePreferences) : BaseViewModel(application)   {
    private val _states = MutableStateFlow<HadithStates>(HadithStates.Idle)
    val states : StateFlow<HadithStates> = _states
    private var requestType = RequestType.HADITH_CATEGORY
    private lateinit var collectionName:String
    private lateinit var bookNumber:String
    fun getHadithCategories(){
        requestType=RequestType.HADITH_CATEGORY
        viewModelScope.launch {
            if (dataStore.getBoolean(HADITH_CATEGORIES_DOWNLOADED)) {
              repository.getSavedHadithCategories().collect {
                  _states.value = HadithStates.LoadCategories(ArrayList(it))
              }
            } else {
                _states.value = HadithStates.Idle
                repository.getHadithCategories().collect {
                    when (it) {
                        is Result.NoInternetConnect -> {
                            loader.set(false)
                            error.set(it.error)
                            restStates()
                        }
                        is Result.OnFailure -> {
                            loader.set(false)
                            error.set(it.throwable.message ?: "")
                            restStates()
                        }
                        is Result.OnLoading -> loader.set(true)
                        is Result.OnSuccess -> {
                            loader.set(false)
                            _states.value = HadithStates.LoadCategories(it.data.data.toEntity())
                            withContext(Dispatchers.IO) {
                                repository.savedHadithCategories(it.data.data)
                                dataStore.setBoolean(HADITH_CATEGORIES_DOWNLOADED, true)
                            }
                            restStates()
                        }
                    }
                }
            }
        }
    }

    fun loadHadithBook(name:String){
        collectionName=name
        requestType=RequestType.HADITH_BOOK
        viewModelScope.launch {
            val savedHadithBooks = repository.getSavedHadithBooks(name).first()
            if (savedHadithBooks.isNotEmpty()) {
                _states.value = HadithStates.LoadHadithBooks(ArrayList(savedHadithBooks))
            } else {
                repository.getHadithBookNumber(name).collect {
                    when (it) {
                        is Result.NoInternetConnect -> {
                            loader.set(false)
                            error.set(it.error)
                            restStates()
                        }
                        is Result.OnFailure -> {
                            loader.set(false)
                            error.set(it.throwable.message ?: "")
                            restStates()
                        }
                        is Result.OnLoading -> loader.set(true)
                        is Result.OnSuccess -> {
                            loader.set(false)
                            val books = ArrayList(it.data.data.map {
                                it.collectionName = name
                                it
                            })
                            _states.value = HadithStates.LoadHadithBooks(books.toEntity())
                            withContext(Dispatchers.IO) {
                                repository.saveHadithBook(books)
                            }
                        }
                    }
                }
            }
        }
    }

    fun loadHadiths(name:String,bookNumber:String,page:Int?=null){
        collectionName=name
        this.bookNumber=bookNumber
        requestType=RequestType.HADITH
        viewModelScope.launch {
            val hadith = repository.getSavedHadith(name, bookNumber).first()
            if (hadith.isNotEmpty() && page==null) {
                _states.value = HadithStates.LoadHadith(ArrayList(hadith))
            } else {
                repository.getHadith(name, bookNumber,page?:1).collect {
                    when (it) {
                        is Result.NoInternetConnect -> {
                            loader.set(false)
                            error.set(it.error)
                            restStates()
                        }
                        is Result.OnFailure -> {
                            loader.set(false)
                            error.set(it.throwable.message ?: "")
                            restStates()
                        }
                        is Result.OnLoading -> loader.set(true)
                        is Result.OnSuccess -> {
                            loader.set(false)
                            _states.value = HadithStates.LoadHadith(it.data.data.toEntity())
                            withContext(Dispatchers.IO) {
                                repository.saveHadith(it.data.data)
                            }
                            if (it.data.next != null) {
                                loadHadiths(name, bookNumber, it.data.next)
                            }
                        }
                    }
                }
            }
        }
    }

    override fun retry() {
        when(requestType){
            RequestType.HADITH -> loadHadiths(collectionName,bookNumber)
            RequestType.HADITH_BOOK -> loadHadithBook(collectionName)
            RequestType.HADITH_CATEGORY -> getHadithCategories()
        }
    }

    private enum class RequestType{
        HADITH,HADITH_BOOK,HADITH_CATEGORY
    }

    private fun restStates(){
        _states.value=HadithStates.Idle
    }


}