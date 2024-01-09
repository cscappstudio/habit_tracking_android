package com.cscmobi.habittrackingandroid

import androidx.annotation.Keep
import com.cscmobi.habittrackingandroid.presentation.di.repositoryModule
import com.cscmobi.habittrackingandroid.presentation.di.viewModelModule
import com.ninenox.kotlinlocalemanager.ApplicationLocale
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext.startKoin

@Keep
class MyApplication: ApplicationLocale() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@MyApplication)
            modules(listOf( viewModelModule,repositoryModule))
        }
    }
}