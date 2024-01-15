package com.cscmobi.habittrackingandroid.thanhlv.ui

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import com.cscmobi.habittrackingandroid.BuildConfig
import com.cscmobi.habittrackingandroid.thanhlv.constant.Constant.Static.Companion.CURRENT_LANG
import com.thanhlv.fw.helper.MyUtils
import com.thanhlv.fw.remoteconfigs.RemoteConfigs
import com.thanhlv.fw.spf.SPF
import java.util.Locale


open class BaseActivity2 : AppCompatActivity() {

    override fun onResume() {
        super.onResume()
        MyUtils.hideNavigationBar(this)
    }

    override fun attachBaseContext(base: Context) {
        var currentLanguage: String? = SPF.getLanguage(base)
        if (currentLanguage == null) currentLanguage = CURRENT_LANG
        val resources = base.resources
        val locale = Locale(currentLanguage)
        Locale.setDefault(locale)
        val config = resources.configuration
        config.setLocale(locale)
        val base1 = base.createConfigurationContext(config)
        super.attachBaseContext(base1)
    }

//    fun gotoHomeActivity() {
//        startActivity(Intent(this, HomeActivity::class.java))
//    }
//
//    fun gotoOnBoard() {
//        startActivity(Intent(this, OnboardActivity::class.java))
//    }

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