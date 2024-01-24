package com.cscmobi.habittrackingandroid.thanhlv.ui

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.View
import com.cscmobi.habittrackingandroid.databinding.ActivityLanguageBinding
import com.cscmobi.habittrackingandroid.presentation.ui.activity.MainActivity
import com.thanhlv.fw.helper.MyUtils.Companion.rippleEffect
import com.thanhlv.fw.spf.SPF

class ChangeLanguageActivity : BaseActivity2() {

    private lateinit var binding: ActivityLanguageBinding
    override fun setupScreen() {
        binding = ActivityLanguageBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    @SuppressLint("MissingSuperCall")
    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        if (!SPF.isFirstOpenApp(this)) {
            finish()
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun initView() {
        if (SPF.isFirstOpenApp(this)) {
            binding.btnBackHeader.visibility = View.GONE
        }
        listLanguage.add(binding.checkEnglish)
        listLanguage.add(binding.checkFilipino)
        listLanguage.add(binding.checkFrench)
        listLanguage.add(binding.checkGermany)
        listLanguage.add(binding.checkHindi)
        listLanguage.add(binding.checkSpanish)
        listLanguage.add(binding.checkThailand)
        listLanguage.add(binding.checkVietnam)
        listLanguage.add(binding.checkPortuguese)

        binding.tvUsa.rippleEffect()
        binding.tvGermany.rippleEffect()
        binding.tvPortuguese.rippleEffect()
        binding.tvFilipino.rippleEffect()
        binding.tvFrench.rippleEffect()
        binding.tvHindi.rippleEffect()
        binding.tvThailand.rippleEffect()
        binding.tvVietnam.rippleEffect()
        binding.tvSpanish.rippleEffect()

        if (SPF.getLanguage(this) != null) currentLanguage = SPF.getLanguage(this)!!
        toggleItemLanguage()
    }

    override fun loadData() {
    }


    private var currentLanguage = "en"
    override fun controllerView() {

        binding.btnDone.setOnClickListener {
            if (SPF.isFirstOpenApp(this)) {
                SPF.setLanguage(this, currentLanguage)
//                gotoOnBoard()
            } else {
                if (currentLanguage != SPF.getLanguage(this)) {
                    SPF.setLanguage(this, currentLanguage)
//                    setResult(CHANGE_LANGUAGE_CODE, null)
                    val i = Intent(this, MainActivity::class.java)
                    i.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                    startActivity(i)
                    finish()
                }
            }
            finish()
        }

        binding.btnBackHeader.setOnClickListener {
            finish()
        }

        binding.tvUsa.setOnClickListener {
            currentLanguage = "en"
            toggleItemLanguage()
        }

        binding.tvGermany.setOnClickListener {
            currentLanguage = "de"
            toggleItemLanguage()
        }

        binding.tvPortuguese.setOnClickListener {
            currentLanguage = "pt"
            toggleItemLanguage()
        }

        binding.tvHindi.setOnClickListener {
            currentLanguage = "hi"
            toggleItemLanguage()
        }

        binding.tvVietnam.setOnClickListener {
            currentLanguage = "vi"
            toggleItemLanguage()
        }

        binding.tvThailand.setOnClickListener {
            currentLanguage = "th"
            toggleItemLanguage()
        }
        binding.tvSpanish.setOnClickListener {
            currentLanguage = "es"
            toggleItemLanguage()
        }

        binding.tvFilipino.setOnClickListener {
            currentLanguage = "fil"
            toggleItemLanguage()
        }

        binding.tvFrench.setOnClickListener {
            currentLanguage = "fr"
            toggleItemLanguage()
        }

    }

    private val listLanguage = ArrayList<View>()

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun toggleItemLanguage() {
        listLanguage.forEach { it.visibility = View.GONE }
        when (currentLanguage) {
            "en" -> listLanguage[0].visibility = View.VISIBLE
            "fil" -> listLanguage[1].visibility = View.VISIBLE
            "fr" -> listLanguage[2].visibility = View.VISIBLE
            "de" -> listLanguage[3].visibility = View.VISIBLE
            "hi" -> listLanguage[4].visibility = View.VISIBLE
            "es" -> listLanguage[5].visibility = View.VISIBLE
            "th" -> listLanguage[6].visibility = View.VISIBLE
            "vi" -> listLanguage[7].visibility = View.VISIBLE
            "pt" -> listLanguage[8].visibility = View.VISIBLE
        }
    }


    ////////////////////////////////////////////////////////
    private val mIntentFilter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
    private val mReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
//            if (!SPF.isProApp(context)
//                && RemoteConfigs.instance.getConfigValue(AdsConfigs.KEY_AD_NATIVE_CHANGE_LANGUAGE)
//                    .asBoolean()
//            ) {
//                binding.adView.visibility = View.VISIBLE
//                createNativeAd(context, BuildConfig.ad_native_full, binding.adView, null)
//            } else binding.adView.visibility = View.GONE
        }
    }

    override fun onStart() {
        super.onStart()
        registerReceiver(mReceiver, mIntentFilter)
    }

    override fun onStop() {
        super.onStop()
        unregisterReceiver(mReceiver)
    }
}