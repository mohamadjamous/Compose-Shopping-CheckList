package com.example.composeshoppingchecklist

import android.app.Application
import com.example.composeshoppingchecklist.di.dataModule
import com.example.composeshoppingchecklist.di.uiDataModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin()
    }

    private fun startKoin() {
        startKoin {
            androidLogger(if (BuildConfig.DEBUG) Level.ERROR else Level.NONE)
            androidContext(this@App)
            modules(listOf(dataModule, uiDataModule))
        }
    }
}