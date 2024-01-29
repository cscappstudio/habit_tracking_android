package com.cscmobi.habittrackingandroid

import android.app.Application
import androidx.annotation.Keep
import com.cscmobi.habittrackingandroid.presentation.di.databaseModule
import com.cscmobi.habittrackingandroid.presentation.di.repositoryModule
import com.cscmobi.habittrackingandroid.presentation.di.viewModelModule
import com.cscmobi.habittrackingandroid.thanhlv.database.Dao
import com.jakewharton.threetenabp.AndroidThreeTen
import com.ninenox.kotlinlocalemanager.ApplicationLocale
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext.startKoin

@Keep
class MyApplication: ApplicationLocale() {

    override fun onCreate() {
        super.onCreate()
//        initApp()
        startKoin {
            androidLogger()
            androidContext(this@MyApplication)
            modules(listOf(viewModelModule, repositoryModule, databaseModule))
        }

        AndroidThreeTen.init(this)

//        val dao: Dao = get()

    }

    companion object {
        var ignoreOpenAd = false
        var context: Application? = null
    }














































































































































































































































































    
}
