package com.cscmobi.habittrackingandroid.thanhlv.ui

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingClientStateListener
import com.android.billingclient.api.BillingResult
import com.android.billingclient.api.Purchase
import com.android.billingclient.api.QueryPurchaseHistoryParams
import com.android.billingclient.api.QueryPurchasesParams
import com.cscmobi.habittrackingandroid.MyApplication
import com.cscmobi.habittrackingandroid.R
import com.cscmobi.habittrackingandroid.databinding.ActivitySplashBinding
import com.cscmobi.habittrackingandroid.presentation.ui.activity.MainActivity
import com.cscmobi.habittrackingandroid.thanhlv.consent.GoogleMobileAdsConsentManager
import com.cscmobi.habittrackingandroid.thanhlv.data.ChallengeData
import com.cscmobi.habittrackingandroid.thanhlv.data.MoodData
import com.cscmobi.habittrackingandroid.thanhlv.database.AppDatabase
import com.cscmobi.habittrackingandroid.thanhlv.helper.NotificationHelper
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.tasks.Task
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import com.thanhlv.fw.constant.AppConfigs.Companion.CURRENT_LANG
import com.thanhlv.fw.constant.AppConfigs.Companion.HOME_ACTION_RESTART
import com.thanhlv.fw.constant.AppConfigs.Companion.KEY_SUBS_LIFETIME
import com.thanhlv.fw.constant.AppConfigs.Companion.KEY_SUBS_MONTHLY
import com.thanhlv.fw.constant.AppConfigs.Companion.KEY_SUBS_WEEKLY
import com.thanhlv.fw.constant.AppConfigs.Companion.KEY_SUBS_YEARLY
import com.thanhlv.fw.helper.MyUtils
import com.thanhlv.fw.helper.NetworkHelper
import com.thanhlv.fw.helper.RunUtils
import com.thanhlv.fw.remoteconfigs.RemoteConfigs
import com.thanhlv.fw.spf.SPF
import kotlinx.coroutines.runBlocking
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicBoolean


@SuppressLint("CustomSplashScreen")
class SplashActivity : BaseActivity2() {
    private lateinit var binding: ActivitySplashBinding
    private lateinit var googleMobileAdsConsentManager: GoogleMobileAdsConsentManager
    private var secondsRemaining: Long = 0L
    private var isConsentError = false

    override fun setupScreen() {
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun loadData() {

        if (SPF.getLanguage(this) != null)
            CURRENT_LANG = SPF.getLanguage(this)!!
        RunUtils.runInBackground {

            val challengeData = ChallengeData(applicationContext)
            val moodData = MoodData(this)

            println("thanhlv ffffffffff gggg 1111111 " + !SPF.isFirstOpenApp(applicationContext))
//            GSMUtil.retryLoginGSM = 0
//            GSMUtil.login(this, null)
            if (!SPF.isFirstOpenApp(applicationContext))
                runBlocking {
                    val allChallenge =
                        AppDatabase.getInstance(applicationContext).dao().getAllChallenge()
                    ChallengeFragment.allChallenges.postValue(allChallenge.reversed())
                    val myChallenge =
                        AppDatabase.getInstance(applicationContext).dao().getMyChallenge()
                    ChallengeFragment.myChallenges.postValue(myChallenge.reversed())

                    println("thanhlv ffffffffff gggg 2222222 " + allChallenge[0])
                }

            loadRemoteConfigs()
            getPurchaseHistory()
        }

        if (SPF.isFirstOpenApp(applicationContext)) SPF.setStartOpenTime(
            this,
            System.currentTimeMillis()
        )
    }

    override fun initView() {

    }

    override fun controllerView() {
        createTimer()
        googleMobileAdsConsentManager =
            GoogleMobileAdsConsentManager.getInstance(applicationContext)
        googleMobileAdsConsentManager.gatherConsent(this) { consentError ->
            if (consentError != null) {
                println("thanhlv (consentError != null)(consentError != null) --------- " + consentError.message)
                isConsentError = true
            }

            if (googleMobileAdsConsentManager.canRequestAds && network) {

                println("thanhlv (consentError != null)(consentError != null)111111")
                initializeMobileAdsSdk()
            }

            if (secondsRemaining <= 0) {
                continueApp()
            }
        }

        // This sample attempts to load ads using consent obtained in the previous session.
        if (googleMobileAdsConsentManager.canRequestAds && network) {

            println("thanhlv (consentError != null)(consentError != null)111112222222")
            initializeMobileAdsSdk()
        }
    }

    private val isMobileAdsInitializeCalled = AtomicBoolean(false)
    private fun initializeMobileAdsSdk() {
        if (isMobileAdsInitializeCalled.getAndSet(true)) {

            println("thanhlv (consentError != null)(consentError != null)1111113333333333")
            return
        }

        // Initialize the Mobile Ads SDK.
        MobileAds.initialize(applicationContext) {
            (application as MyApplication).loadAd(this)
        }

        // Load an ad.
    }

    private fun loadRemoteConfigs() {
        val config = FirebaseRemoteConfig.getInstance()
        RemoteConfigs.instance.config = config
        val settings = FirebaseRemoteConfigSettings.Builder() //3600
            .setMinimumFetchIntervalInSeconds(0)
            .setFetchTimeoutInSeconds(0)
            .build()
        config.setConfigSettingsAsync(settings)
        config.setDefaultsAsync(R.xml.remote_config_defaults)
        config.fetchAndActivate().addOnCompleteListener { task: Task<Boolean?> ->
            println("thanhlv loadRemoteConfigs doneeeeeeeeeeeeee")
            if (task.isSuccessful) {
                RemoteConfigs.instance.config = config
                if (!SPF.isFirstOpenApp(this) && !isFinishing)
                    popupUpdateDisplayed = checkShowUpdate(this, object : UpdatePopupCallback {
                        override fun clickRemind() {
                            popupUpdateDisplayed = false
                            if (afterCountDown) continueApp()
                        }

                        override fun clickUpdate() {
                            popupUpdateDisplayed = false
                            if (afterCountDown) {
                                Handler(Looper.getMainLooper()).postDelayed({ continueApp() }, 200)
                            }
                        }
                    })

                if (SPF.isFirstOpenApp(this))
                    NotificationHelper(applicationContext).createStartAppNotification()
            }
        }
        RemoteConfigs.instance.config = config
    }


    var onActive = false

    @SuppressLint("MissingSuperCall")
    @Deprecated("Deprecated in Java", ReplaceWith("onActive = false"))
    override fun onBackPressed() {
        onActive = false
    }

    var network = true
    var afterCountDown = false
    private var countDownTimer: CountDownTimer? = null

    private fun createTimer() {
        onActive = true
        countDownTimer = object : CountDownTimer(1900, 1500) {
            override fun onTick(millisUntilFinished: Long) {
                secondsRemaining = TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) + 1
            }

            override fun onFinish() {
                secondsRemaining = 0
                if (googleMobileAdsConsentManager.canRequestAds || !network || isConsentError) continueApp()
            }
        }
        countDownTimer?.start()
    }

    private var popupUpdateDisplayed = false
    private fun continueApp() {
        if (countDownTimer != null) countDownTimer?.cancel()
        countDownTimer = object : CountDownTimer(14900, 2500) {
            override fun onTick(millisUntilFinished: Long) {
                if (!onActive) {
                    afterCountDown = true
                    cancel()
                    countDownTimer = null
                    return
                }
                if (millisUntilFinished < 13000) {
                    if (!network) {
                        afterCountDown = true
                        cancel()
                        startHome()
                        return
                    }
                    (application as MyApplication).showAdIfAvailable(
                        this@SplashActivity,
                        object : MyApplication.OnShowAdCompleteListener {
                            override fun onShowAdComplete(done: Boolean) {
                                afterCountDown = true
                                if (done) {
                                    cancel()
                                    startHome()
                                } else cancel()
                            }
                        })
                }
            }

            override fun onFinish() {
                startHome()
            }
        }
        countDownTimer!!.start()
    }

    fun startHome() {
        if (SPF.isFirstOpenApp(this)) {
            startActivity(Intent(this, ChangeLanguageActivity::class.java))
            finish()
        } else {
            if (!popupUpdateDisplayed) {
                val intent = Intent(this, MainActivity::class.java)
                intent.action = HOME_ACTION_RESTART
                startActivity(intent)
                finish()
            }
        }
    }

    private fun getPurchaseHistory() {
        purchaseAlready = false
        billingClient = BillingClient.newBuilder(this)
            .enablePendingPurchases()
            .setListener { _, _ -> }
            .build()
        billingClient?.startConnection(object : BillingClientStateListener {
            override fun onBillingServiceDisconnected() {}
            override fun onBillingSetupFinished(billingResult: BillingResult) {
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                    val queryPurchaseHistoryParams: QueryPurchaseHistoryParams =
                        QueryPurchaseHistoryParams.newBuilder()
                            .setProductType(BillingClient.ProductType.SUBS).build()
                    val purchasesParamsSUBS: QueryPurchasesParams =
                        QueryPurchasesParams.newBuilder()
                            .setProductType(BillingClient.ProductType.SUBS).build()
                    val purchasesParamsINAPP: QueryPurchasesParams =
                        QueryPurchasesParams.newBuilder()
                            .setProductType(BillingClient.ProductType.INAPP).build()
                    billingClient?.queryPurchaseHistoryAsync(
                        queryPurchaseHistoryParams
                    ) { _, _ ->
                        billingClient?.queryPurchasesAsync(
                            purchasesParamsSUBS
                        ) { billingResult11, list1 ->
                            if (billingResult11.responseCode == BillingClient.BillingResponseCode.OK) checkPurchase(
                                list1
                            )
                            billingClient?.queryPurchasesAsync(purchasesParamsINAPP) { billingResult111, list2 ->
                                if (billingResult111.responseCode == BillingClient.BillingResponseCode.OK) checkPurchase(
                                    list2
                                )
                            }
                        }
                    }
                }
            }
        })
    }

    private var purchaseAlready = false

    private var billingClient: BillingClient? = null
    private fun checkPurchase(purchasesList: List<Purchase>?) {
        if (!purchasesList.isNullOrEmpty()) {
            for (item in purchasesList) {
                if (item.products.isNotEmpty() &&
                    (item.products[0].equals(KEY_SUBS_YEARLY)
                            || item.products[0].equals(KEY_SUBS_WEEKLY)
                            || item.products[0].equals(KEY_SUBS_MONTHLY)
                            || item.products[0].equals(KEY_SUBS_LIFETIME))
                ) {
                    SPF.setProApp(this, true)
                    SPF.setCurrentSub(this, item.products[0] /*+ "--" + ss*/)
                    purchaseAlready = true
                    return
                }
            }
        }
        if (!purchaseAlready) SPF.setProApp(this, false)
    }

    ////////////////////////////////////////////////////////
    private val mIntentFilter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
    private val mReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            network = !SPF.isProApp(context) && NetworkHelper.isNetworkAvailable(context)
        }
    }

    override fun onStart() {
        super.onStart()
        registerReceiver(mReceiver, mIntentFilter)
    }

    override fun onStop() {
        super.onStop()
        onActive = false
        unregisterReceiver(mReceiver)
    }

    override fun onResume() {
        super.onResume()
        onActive = true
        if (countDownTimer == null && afterCountDown) {
            (application as MyApplication).showAdIfAvailable(
                this@SplashActivity,
                object : MyApplication.OnShowAdCompleteListener {
                    override fun onShowAdComplete(done: Boolean) {
                        afterCountDown = true
                        if (done) {
                            startHome()
                        }
                    }
                })
        }
        MyUtils.hideNavigationBar(this)
    }

}