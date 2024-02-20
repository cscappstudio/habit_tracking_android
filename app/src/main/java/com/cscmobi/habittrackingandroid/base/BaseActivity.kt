package com.cscmobi.habittrackingandroid.base

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.View.SYSTEM_UI_FLAG_FULLSCREEN
import android.view.View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
import androidx.activity.OnBackPressedCallback
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.cscmobi.habittrackingandroid.BuildConfig
import com.cscmobi.habittrackingandroid.MyApplication
import com.cscmobi.habittrackingandroid.utils.hideStatusBar
import com.thanhlv.fw.helper.MyUtils
import com.thanhlv.fw.remoteconfigs.RemoteConfigs
import com.thanhlv.fw.spf.SPF
import java.util.*
import javax.inject.Inject


abstract class BaseActivity<VB: ViewDataBinding>: AppCompatActivity() {
    @Inject
    protected lateinit var binding: VB

    @LayoutRes
    abstract fun getLayoutRes(): Int
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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, getLayoutRes()) as VB
        MyUtils.hideStatusBar(this)
        initView()
        setEvent()
//        onBackPressedDispatcher.addCallback(this, object: OnBackPressedCallback(true) {
//            override fun handleOnBackPressed() {
//
//            }
//
//        })
    }

    abstract fun initView()
    abstract fun setEvent()

    protected fun addFragment(@IdRes containerViewId: Int, @NonNull fragment: Fragment, @NonNull fragmentTag: String) {
        supportFragmentManager.beginTransaction()
            .add(containerViewId,fragment,fragmentTag)
            .commit()
    }
    protected fun addFragmentNotHide(@IdRes containerViewId: Int, @NonNull fragment: Fragment, @NonNull fragmentTag: String) {
        supportFragmentManager.beginTransaction()
            .add(containerViewId,fragment,fragmentTag)
             .addToBackStack(fragmentTag)
            .commit()
    }
    protected fun replaceFragment(@IdRes containerViewId: Int, @NonNull fragment: Fragment, @NonNull fragmentTag: String) {
        supportFragmentManager.beginTransaction()
            .replace(containerViewId,fragment,fragmentTag)
           .addToBackStack(fragmentTag)
            .commit()
    }

//    protected fun showFragment(fr: Fragment) {
//        supportFragmentManager.beginTransaction()
//            .show(fr)
//            .commit()
//    }
    protected fun replaceFragmentNotToBackStack(@IdRes containerViewId: Int, @NonNull fragment: Fragment, @NonNull fragmentTag: String) {
        supportFragmentManager.beginTransaction()
            .replace(containerViewId,fragment,fragmentTag)
            .commit()
    }

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