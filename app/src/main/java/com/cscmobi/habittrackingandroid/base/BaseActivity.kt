package com.cscmobi.habittrackingandroid.base

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.View.SYSTEM_UI_FLAG_FULLSCREEN
import android.view.View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.cscmobi.habittrackingandroid.BuildConfig
import com.cscmobi.habittrackingandroid.MyApplication
import com.cscmobi.habittrackingandroid.R
import com.cscmobi.habittrackingandroid.presentation.ui.activity.MainActivity
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


    val NOTIFICATION_PERMISSION = "android.permission.POST_NOTIFICATIONS"
    val NOTIFICATION_PERMISSION_CODE = 11255
    val CHANNEL_NOTIFICATION_DEFAULT = "tape_measure_notification_channel"
    public fun hasNotificationPermission(): Boolean {
        return (ContextCompat.checkSelfPermission(this, NOTIFICATION_PERMISSION)
                == PackageManager.PERMISSION_GRANTED)
    }

    public fun requestNotificationPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf<String>(NOTIFICATION_PERMISSION),
            NOTIFICATION_PERMISSION_CODE
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (Build.VERSION.SDK_INT >= 33) {
            if (hasNotificationPermission()) {
                Toast.makeText(
                    this,
                    getString(R.string.notification_access_granted),
                    Toast.LENGTH_SHORT
                ).show()
                try {
                    val notificationManager =
                        applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        notificationManager.createNotificationChannel(
                            NotificationChannel(
                                CHANNEL_NOTIFICATION_DEFAULT,
                                "Default Channel",
                                NotificationManager.IMPORTANCE_HIGH
                            )
                                .apply {
                                    setSound(null, null)
                                    enableLights(false)
                                    enableVibration(false)
                                    setShowBadge(false)
                                }
                        )
                    }
                } catch (ignored: Exception) {
                }
            } else Toast.makeText(
                this,
                getString(R.string.notification_access_denied),
                Toast.LENGTH_SHORT
            ).show()
        }
    }


}