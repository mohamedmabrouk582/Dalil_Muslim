package com.mabrouk.dalilmuslim.viewModels

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.viewModelScope
import com.mabrouk.dalilmuslim.states.RadioStates
import com.mabrouk.dalilmuslim.utils.RADIOS_DOWNLOADS
import com.mabrouk.dalilmuslim.viewModels.base.BaseViewModel
import com.mabrouk.data.repository.RadioDefaultRepository
import com.mabrouk.data.repository.RadioRepository
import com.mabrouk.data.utils.DataStorePreferences
import com.mabrouk.data.utils.toEntity
import com.mabrouk.domain.utils.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * @name Mohamed Mabrouk
 * Copyrights (c) 06/09/2021 created by Just clean
 */
@HiltViewModel
class RadioViewModel @Inject constructor(
    application: Application,
    val repository: RadioDefaultRepository,
    val dataStorePreferences: DataStorePreferences
) : BaseViewModel(application) {

    private val _states = MutableStateFlow<RadioStates>(RadioStates.Idle)
    var states: StateFlow<RadioStates> = _states

    fun requestRadios() {
        viewModelScope.launch {
            if (dataStorePreferences.getBoolean(RADIOS_DOWNLOADS)) {
                _states.value = RadioStates.LoadData(repository.getSavedRadios())
            } else {
                repository.requestRadios().collect {
                    when (it) {
                        is Result.NoInternetConnect -> {
                            _states.value = RadioStates.LoadData(repository.getSavedRadios())
                        }
                        is Result.OnFailure -> Toast.makeText(
                            getApplication(),
                            it.throwable.message,
                            Toast.LENGTH_SHORT
                        ).show()
                        is Result.OnLoading -> {
                        }
                        is Result.OnSuccess -> {
                            _states.value = RadioStates.LoadData(it.data.radios.toEntity())
                            withContext(Dispatchers.IO) {
                                repository.insertRadios(it.data.radios)
                                dataStorePreferences.setBoolean(RADIOS_DOWNLOADS,true)
                            }
                        }
                    }
                }
            }
        }
    }

}