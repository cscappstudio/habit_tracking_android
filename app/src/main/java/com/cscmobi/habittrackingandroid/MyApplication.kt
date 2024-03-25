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
import com.cscmobi.habittrackingandroid.presentation.di.databaseModule
import com.cscmobi.habittrackingandroid.presentation.di.repositoryModule
import com.cscmobi.habittrackingandroid.presentation.di.viewModelModule
import com.cscmobi.habittrackingandroid.thanhlv.database.Dao
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
class MyApplication : ApplicationLocale(), Application.ActivityLifecycleCallbacks,
    LifecycleObserver {
    override fun onCreate() {
        super.onCreate()
        initApp()
        startKoin {
            androidLogger()
            androidContext(this@MyApplication)
            modules(listOf(viewModelModule, repositoryModule, databaseModule))
        }
        context = this
        AndroidThreeTen.init(this)
    }

    companion object {
        var ignoreOpenAd = false
        var context: Application? = null
    }

    private var appOpenAdManager: AppOpenAdManager? = null
    private var currentActivity: Activity? = null
    private fun configs() {
        val config = FirebaseRemoteConfig.getInstance()
        val settings = FirebaseRemoteConfigSettings.Builder() //3600
            .setMinimumFetchIntervalInSeconds(0)
            .build()
        config.setConfigSettingsAsync(settings)
        config.setDefaultsAsync(R.xml.remote_config_defaults)
        config.fetchAndActivate()
            .addOnCompleteListener { task: Task<Boolean?> ->
                if (task.isSuccessful) {
                    RemoteConfigs.instance.config = config
                }
            }
        RemoteConfigs.instance.config = config
    }

    private var defaultLifecycleObserver = object : DefaultLifecycleObserver {

        override fun onStart(owner: LifecycleOwner) {
            super.onStart(owner)
            if (ignoreOpenAd) {
                ignoreOpenAd = false
                return
            }
            currentActivity?.let {
                configs()
                if (it !is SplashActivity) appOpenAdManager?.showAdIfAvailable(it)
            }
        }

        override fun onStop(owner: LifecycleOwner) {
            super.onStop(owner)
            //your code here
        }
    }


    private var activities = ArrayList<Activity>()

    override fun onActivityCreated(p0: Activity, p1: Bundle?) {
        activities.add(p0)
    }

    override fun onActivityStarted(p0: Activity) {
//        if (appOpenAdManager != null && !appOpenAdManager!!.isShowingAd && p0 !is AdActivity) {
//            currentActivity = p0
//        }
    }

    override fun onActivityResumed(p0: Activity) {
        Adjust.onResume()
        AdjustTracking.logRetention(this)
    }


    override fun onActivityPaused(p0: Activity) {
        Adjust.onPause()
        if (p0.isFinishing) {
            activities.remove(p0)
        }
        if (p0 is AdActivity && currentActivity != null && !currentActivity!!.isFinishing)
            MyUtils.hideNavigationBar(currentActivity!!)
    }

    override fun onActivityStopped(p0: Activity) {
        if (p0.isFinishing) {
            activities.remove(p0)
        }
    }

    override fun onActivitySaveInstanceState(p0: Activity, p1: Bundle) {
    }

    override fun onActivityDestroyed(p0: Activity) {
        activities.remove(p0)
        if (activities.isEmpty()) {
            adsInitialized = false
            initApp()
        }
    }

    private fun initApp() {
        configs()
        if (!SPF.isProApp(this)) {
            appOpenAdManager = AppOpenAdManager()
        }
//        val appToken = getString(com.google.android.gms.ads.R.string.token_adjust)
//        var environment = AdjustConfig.ENVIRONMENT_SANDBOX
//        if (!BuildConfig.DEBUG) environment = AdjustConfig.ENVIRONMENT_PRODUCTION
//        val adjustConfig = AdjustConfig(this, appToken, environment)
//        adjustConfig.setLogLevel(LogLevel.VERBOSE)
//        Adjust.onCreate(adjustConfig)
//        println("thanhlv initAdjust ---- " + environment)
    }


    fun loadAd(activity: Activity) {
        // We wrap the loadAd to enforce that other classes only interact with MyApplication
        // class.
        appOpenAdManager?.loadAd(activity)
    }

    /**
     * Shows an app open ad.
     *
     * @param activity                 the activity that shows the app open ad
     * @param onShowAdCompleteListener the listener to be notified when an app open ad is complete
     */
    fun showAdIfAvailable(
        activity: Activity,
        onShowAdCompleteListener: OnShowAdCompleteListener
    ) {
        context?.let {
            if (SPF.isProApp(it)) return
        }
        if (ignoreOpenAd) {
            ignoreOpenAd = false
            return
        }
        appOpenAdManager?.showAdIfAvailable(activity, onShowAdCompleteListener)
    }

    /**
     * Interface definition for a callback to be invoked when an app open ad is complete
     * (i.e. dismissed or fails to show).
     */
    interface OnShowAdCompleteListener {
        fun onShowAdComplete(done: Boolean)
    }

    /**
     * Inner class that loads and shows app open ads.
     */
    private inner class AppOpenAdManager
    /**
     * Constructor.
     */
    {
        private var googleMobileAdsConsentManager: GoogleMobileAdsConsentManager =
            GoogleMobileAdsConsentManager.getInstance(applicationContext)
        var appOpenAd: AppOpenAd? = null
        private var isLoadingAd = false
        var isShowingAd = false

        /**
         * Keep track of the time an app open ad is loaded to ensure you don't show an expired ad.
         */
        private var loadTime: Long = 0

        /**
         * Load an ad.
         *
         * @param context the context of the activity that loads the ad
         */
        @SuppressLint("SuspiciousIndentation")
        fun loadAd(context: Context) {
            // Do not load ad if there is an unused ad or one is already loading.
            if (isLoadingAd || isAdAvailable) {
                return
            }
            isLoadingAd = true
            val request = AdRequest.Builder().build()
            AppOpenAd.load(
                context,
                getString(R.string.admob_app_open_id),
                request,
                object : AppOpenAd.AppOpenAdLoadCallback() {
                    /**
                     * Called when an app open ad has loaded.
                     *
                     * @param ad the loaded app open ad.
                     */
                    override fun onAdLoaded(ad: AppOpenAd) {
                        println("thanhlv override fun onAdLoaded(ad: AppOpenAd)")
                        appOpenAd = ad
                        isLoadingAd = false
                        loadTime = Date().time
                        AdjustHelper.trackingRevenueAdjust(ad)
                    }

                    /**
                     * Called when an app open ad has failed to load.
                     *
                     * @param loadAdError the error.
                     */
                    override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                        appOpenAd = null
                        isLoadingAd = false
                    }
                })

        }

        /**
         * Check if ad was loaded more than n hours ago.
         */
        private fun wasLoadTimeLessThanNHoursAgo(): Boolean {
            val dateDifference = Date().time - loadTime
            return dateDifference < 3600000 * 4 // 4h
        }

        /**
         * Check if ad exists and can be shown.
         */
        private val isAdAvailable: Boolean
            get() = appOpenAd != null && wasLoadTimeLessThanNHoursAgo()

        /**
         * Show the ad if one isn't already showing.
         *
         * @param activity the activity that shows the app open ad
         */
        fun showAdIfAvailable(
            activity: Activity,
            onShowAdCompleteListener: OnShowAdCompleteListener = object :
                OnShowAdCompleteListener {
                override fun onShowAdComplete(done: Boolean) {}
            }
        ) {
            if (SPF.isProApp(activity)
                || !RemoteConfigs.instance.getConfigValue(KEY_AD_OPEN_APP).asBoolean()
                || isShowingAd
            ) {
                onShowAdCompleteListener.onShowAdComplete(true)
                return
            }

            if (!isAdAvailable) {
                onShowAdCompleteListener.onShowAdComplete(true)
                if (googleMobileAdsConsentManager.canRequestAds) {
                    loadAd(activity)
                }
                return
            }
            if (activity is AdActivity /*|| activity is SubscriptionsActivity*/) return
            appOpenAd!!.fullScreenContentCallback = object : FullScreenContentCallback() {
                /** Called when full screen content is dismissed.  */
                override fun onAdDismissedFullScreenContent() {
                    appOpenAd = null
                    isShowingAd = false
                    onShowAdCompleteListener.onShowAdComplete(true)
                    if (googleMobileAdsConsentManager.canRequestAds) {
                        loadAd(activity)
                    }
                }

                /** Called when fullscreen content failed to show.  */
                override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                    appOpenAd = null
                    isShowingAd = false
                    onShowAdCompleteListener.onShowAdComplete(true)
                    if (googleMobileAdsConsentManager.canRequestAds) {
                        loadAd(activity)
                    }
                }

                /** Called when fullscreen content is shown.  */
                override fun onAdShowedFullScreenContent() {
                    onShowAdCompleteListener.onShowAdComplete(false)
                    appOpenAd = null
                    MyUtils.logEventShowAdFull(activity)
                }
            }
            isShowingAd = true
            if (appOpenAd != null && !SPF.isProApp(activity)) appOpenAd!!.show(activity)
        }
    }
}