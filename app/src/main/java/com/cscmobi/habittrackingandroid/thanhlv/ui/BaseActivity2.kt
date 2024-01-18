package com.cscmobi.habittrackingandroid.thanhlv.ui

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.cscmobi.habittrackingandroid.BuildConfig
import com.cscmobi.habittrackingandroid.MyApplication
import com.cscmobi.habittrackingandroid.thanhlv.constant.Constant.Static.Companion.CURRENT_LANG
import com.thanhlv.fw.helper.MyUtils
import com.thanhlv.fw.remoteconfigs.RemoteConfigs
import com.thanhlv.fw.spf.SPF
import java.util.Locale


abstract class BaseActivity2 : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupScreen()
        MyUtils.hideStatusBar(this)
        loadData()
        initView()
        controllerView()
    }

    abstract fun setupScreen()
    abstract fun loadData()
    abstract fun initView()
    abstract fun controllerView()

    override fun onResume() {
        super.onResume()
        MyUtils.hideNavigationBar(this)
    }

    override fun attachBaseContext(base: Context) {
        var base = base
        var currentLanguage: String? = SPF.getLanguage(base)
        if (currentLanguage == null) currentLanguage = "en"
        val resources = base.resources
        val locale = Locale(currentLanguage)
        Locale.setDefault(locale)
        val config = resources.configuration
        config.setLocale(locale)
        base = base.createConfigurationContext(config)
        super.attachBaseContext(base)
    }

//    fun gotoHomeActivity() {
//        startActivity(Intent(this, HomeActivity::class.java))
//    }
//
//    fun gotoOnBoard() {
//        startActivity(Intent(this, OnboardActivity::class.java))
//    }

    open fun shareApp() {
        MyApplication.ignoreOpenAd = true
        val sendIntent = Intent(Intent.ACTION_SEND)
        sendIntent.putExtra(
            Intent.EXTRA_TEXT,
            RemoteConfigs.instance.appConfigs.shareText + "\n"
                    + String.format("https://play.google.com/store/apps/details?id=%s", packageName)
        )
        sendIntent.type = "text/plain"
        startActivity(Intent.createChooser(sendIntent, "Share to"))
    }

    fun showMoreApp() {
        MyApplication.ignoreOpenAd = true
        try {
            startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse(RemoteConfigs.instance.appConfigs.moreAppURL)
                )
            )
        } catch (e: ActivityNotFoundException) {
            startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse(RemoteConfigs.instance.appConfigs.moreAppURL)
                )
            )
        }
    }

    fun openDeeplink(url: String?) {
        if (url.isNullOrEmpty()) return
        MyApplication.ignoreOpenAd = true
        try {
            val i = Intent(Intent.ACTION_VIEW)
            if (url.startsWith("http://") || url.startsWith("https://")) {
                i.data = Uri.parse(url)
            } else i.data = Uri.parse("http://$url")
            startActivity(i)
        } catch (ignored: java.lang.Exception) {
        }
    }


    fun gotoSubsOnStore() {
        MyApplication.ignoreOpenAd = true
        try {
            startActivity(rateIntentForUrl("https://play.google.com/store/account/subscriptions/"))
        } catch (ignored: ActivityNotFoundException) {
        }
    }

    fun rateAppOnStore() {
        MyApplication.ignoreOpenAd = true
        try {
            startActivity(rateIntentForUrl("market://details"))
        } catch (e: ActivityNotFoundException) {
            startActivity(rateIntentForUrl("https://play.google.com/store/apps/details"))
        }
    }

    private fun rateIntentForUrl(url: String?): Intent {
        val intent = Intent(
            Intent.ACTION_VIEW,
            Uri.parse(String.format("%s?id=%s", url, packageName))
        )
        var flags = Intent.FLAG_ACTIVITY_NO_HISTORY or Intent.FLAG_ACTIVITY_MULTIPLE_TASK
        flags = flags or Intent.FLAG_ACTIVITY_NEW_DOCUMENT
        intent.addFlags(flags)
        return intent
    }


    fun contactUs() {
//        DogApp.ignoreOpenAd = true
//        val intent = Intent(Intent.ACTION_SEND)
//        intent.type = "message/rfc822"
//        intent.putExtra(Intent.EXTRA_EMAIL, arrayOf(RemoteConfigs.instance.appConfigs.emailSupport))
//        intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.subject))
//        intent.putExtra(Intent.EXTRA_TEXT, getString(R.string.give_us_your_feedback))
//        try {
//            startActivity(Intent.createChooser(intent, getString(R.string.send_feed_back)))
//        } catch (_: ActivityNotFoundException) {
//        }
    }

    private fun gotoStore() {
        MyApplication.ignoreOpenAd = false
        try {
            startActivity(rateIntentForUrl("market://details"))
        } catch (e: ActivityNotFoundException) {
            startActivity(rateIntentForUrl("https://play.google.com/store/apps/details"))
        }
    }

    interface UpdatePopupCallback {
        fun clickRemind()
        fun clickUpdate()
    }

    open fun checkShowUpdate(context: Context, callback: UpdatePopupCallback?): Boolean {
        try {
            val lastVer: String = RemoteConfigs.instance.appConfigs.lastVersion
            println("thanhlv checkShowUpdate ---- " + lastVer)
            if (lastVer == BuildConfig.VERSION_NAME) return false
            val forceUpdate: Boolean =
                RemoteConfigs.instance.appConfigs.isForceUpdate
            val showUpdate: Boolean = RemoteConfigs.instance.appConfigs.isShowUpdate

            println("thanhlv checkShowUpdate showUpdate---- " + showUpdate)
//            val content: String = RemoteConfigs.instance.appConfigs.updateMessage
//            val alertDialogBuilder = AlertDialog.Builder(context)
//            alertDialogBuilder.setTitle(getString(R.string.new_update_available))
//            if (!forceUpdate) {
//                if (showUpdate) {
//                    alertDialogBuilder
//                        .setMessage(getString(R.string.update_for_new_features_and_best_performance))
//                        .setCancelable(true)
//                        .setPositiveButton(getString(R.string.update)) { dialog: DialogInterface, _: Int ->
//                            callback?.clickUpdate()
//                            gotoStore(context, false)
//                            dialog.dismiss()
//                        }
//                        .setNegativeButton(
//                            getString(R.string.remind)
//                        ) { dialog: DialogInterface, _: Int ->
//                            callback?.clickRemind()
//                            dialog.dismiss() }
//                        .setOnCancelListener {
//                            callback?.clickRemind()
//                        }
//                    val alertDialog = alertDialogBuilder.create()
//                    if (!isFinishing) {
//                        alertDialog.show()
//                        return true
//                    }
//                }
//            } else {
//                alertDialogBuilder
//                    .setMessage(getString(R.string.update_for_new_features_and_best_performance))
//                    .setCancelable(false)
//                    .setPositiveButton(getString(R.string.update)) { dialog: DialogInterface, _: Int ->
//                        callback?.clickUpdate()
//                        gotoStore(context, false)
//                        dialog.dismiss()
//                    }
//                val alertDialog = alertDialogBuilder.create()
//                if (!isFinishing) {
//                    alertDialog.show()
//                    return true
//                }
//            }
        } catch (ignored: Exception) {
            return false
        }
        return false
    }
}