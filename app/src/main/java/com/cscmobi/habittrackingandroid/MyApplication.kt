package com.cscmobi.habittrackingandroid

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle
import androidx.annotation.Keep
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.adjust.sdk.Adjust
import com.adjust.sdk.AdjustConfig
import com.adjust.sdk.LogLevel
import com.cscmobi.habittrackingandroid.presentation.di.repositoryModule
import com.cscmobi.habittrackingandroid.presentation.di.viewModelModule
import com.cscmobi.habittrackingandroid.thanhlv.consent.GoogleMobileAdsConsentManager
import com.cscmobi.habittrackingandroid.thanhlv.ui.SplashActivity
import com.google.android.gms.ads.*
import com.google.android.gms.ads.appopen.AppOpenAd
import com.google.android.gms.tasks.Task
import com.google.firebase.FirebaseApp
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import com.jakewharton.threetenabp.AndroidThreeTen
import com.ninenox.kotlinlocalemanager.ApplicationLocale
import com.thanhlv.ads.lib.AdjustHelper
import com.thanhlv.fw.constant.AppConfigs.Companion.KEY_AD_OPEN_APP
import com.thanhlv.fw.constant.AppConfigs.Companion.adsInitialized
import com.thanhlv.fw.helper.AdjustTracking
import com.thanhlv.fw.helper.MyUtils
import com.thanhlv.fw.remoteconfigs.RemoteConfigs
import com.thanhlv.fw.spf.SPF
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext.startKoin
import java.util.*

@Keep
class MyApplication: ApplicationLocale() {

    override fun onCreate() {
        super.onCreate()
        initApp()
        startKoin {
            androidLogger()
            androidContext(this@MyApplication)
            modules(listOf(viewModelModule, repositoryModule))
        }

        AndroidThreeTen.init(this)
    }

    companion object {
        var ignoreOpenAd = false
        var context: Application? = null
    }














































































































































































































































































    
}
