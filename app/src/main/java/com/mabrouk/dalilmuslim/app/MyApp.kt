package com.mabrouk.dalilmuslim.app

import android.app.Application
import android.content.Context
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import com.akexorcist.localizationactivity.ui.LocalizationApplication
import dagger.hilt.android.HiltAndroidApp
import java.util.*
import javax.inject.Inject

@HiltAndroidApp
class MyApp : LocalizationApplication() ,Configuration.Provider{
    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    override fun getWorkManagerConfiguration() =
        Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()

    override fun getDefaultLanguage(base: Context): Locale {
        return Locale("ar")
    }

    override fun onCreate() {
        super.onCreate()
    }
}

