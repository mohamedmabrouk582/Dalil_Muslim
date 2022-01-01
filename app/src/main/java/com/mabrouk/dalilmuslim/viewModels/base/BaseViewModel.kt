package com.mabrouk.dalilmuslim.viewModels.base

import android.app.Application
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LifecycleObserver
import com.mabrouk.loaderlib.RetryCallBack

open class BaseViewModel(application: Application) : AndroidViewModel(application) {
    //lateinit var view:V
    val loader: ObservableBoolean = ObservableBoolean()
    val error: ObservableField<String> = ObservableField()
    val retryCallBack = object : RetryCallBack {
        override fun onRetry() {
            retry()
        }

    }
    open fun retry(){}
//    override fun attachView(view: V) {
//        this.view=view
//    }

}