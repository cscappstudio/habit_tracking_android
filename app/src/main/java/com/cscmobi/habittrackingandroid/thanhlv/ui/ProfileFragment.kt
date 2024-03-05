package com.cscmobi.habittrackingandroid.thanhlv.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Toast
import com.cscmobi.habittrackingandroid.BuildConfig
import com.cscmobi.habittrackingandroid.R
import com.cscmobi.habittrackingandroid.base.BaseFragment
import com.cscmobi.habittrackingandroid.databinding.FragmentProfileBinding
import com.cscmobi.habittrackingandroid.presentation.ui.activity.MainActivity
import com.cscmobi.habittrackingandroid.thanhlv.adapter.*
import com.thanhlv.fw.constant.AppConfigs.Companion.KEY_PASS_REVIEW_APP
import com.thanhlv.fw.helper.MyClick
import com.thanhlv.fw.helper.MyUtils
import com.thanhlv.fw.helper.MyUtils.Companion.gotoStore
import com.thanhlv.fw.helper.MyUtils.Companion.rippleEffect
import com.thanhlv.fw.remoteconfigs.RemoteConfigs
import com.thanhlv.fw.spf.SPF
import java.util.*

class ProfileFragment :
    BaseFragment<FragmentProfileBinding>(FragmentProfileBinding::inflate) {

    override fun setEvent() {
    }

    private var lastVer = ""
    private var latest = false
    private var avaProfile: String? = ""
    private val popupChoseAvaProfile = PopupChoseAvaProfile()

    @SuppressLint("SetTextI18n")
    override fun initView(view: View) {
        lastVer = RemoteConfigs.instance.appConfigs.lastVersion
        latest = (lastVer == BuildConfig.VERSION_NAME)
        binding.btnLanguage.rippleEffect("#f5f5f5")
        binding.btnNotification.rippleEffect("#f5f5f5")
        binding.btnAbout.rippleEffect("#f5f5f5")
        binding.btnRate.rippleEffect("#f5f5f5")
        binding.btnPrivacy.rippleEffect("#f5f5f5")
        binding.btnShareApp.rippleEffect("#f5f5f5")
        binding.btnSubscription.rippleEffect("#f5f5f5")

        binding.tvVersion.text = "v" + BuildConfig.VERSION_NAME

        if (SPF.getLanguage(requireContext()) != null) {
            when (SPF.getLanguage(requireContext())) {
                "en" -> binding.tvLanguage.text = getString(R.string.united_states)
                "ja" -> binding.tvLanguage.text = getString(R.string.japanese)
                "fr" -> binding.tvLanguage.text = getString(R.string.french)
                "de" -> binding.tvLanguage.text = getString(R.string.german)
                "hi" -> binding.tvLanguage.text = getString(R.string.hindi)
                "es" -> binding.tvLanguage.text = getString(R.string.spanish)
                "ko" -> binding.tvLanguage.text = getString(R.string.korean)
                "ru" -> binding.tvLanguage.text = getString(R.string.rusian)
                "pt" -> binding.tvLanguage.text = getString(R.string.portuguese)
            }
        }

        if (SPF.getAvaProfile(requireContext()) != null) {
            avaProfile = SPF.getAvaProfile(requireContext())
        }
        if (!avaProfile.isNullOrEmpty()) {
            binding.imgProfile.setImageResource(avaProfile!!.toInt())
        } else binding.imgProfile.setImageResource(R.drawable.ava_profile_1)

        binding.tvChangeAva.setOnClickListener(object : MyClick() {
            override fun onMyClick(v: View, count: Long) {
                popupChoseAvaProfile.show(childFragmentManager, "")
            }
        })
        popupChoseAvaProfile.callback = object : PopupChoseAvaProfile.Callback {
            override fun clickChange(ava: Int) {
                binding.imgProfile.setImageResource(ava)
                SPF.setAvaProfile(requireContext(), ava.toString())
            }
        }
        binding.icPlusAva.setOnClickListener(object : MyClick() {
            override fun onMyClick(v: View, count: Long) {
                popupChoseAvaProfile.show(childFragmentManager, "")
            }
        })

        binding.btnSubscription.setOnClickListener(object : MyClick() {
            override fun onMyClick(v: View, count: Long) {
                startActivity(Intent(requireContext(), SubscriptionsActivity::class.java))
            }
        })

        binding.btnLanguage.setOnClickListener(object : MyClick() {
            override fun onMyClick(v: View, count: Long) {
                val intent = Intent(requireContext(), ChangeLanguageActivity::class.java)
                startActivity(intent)
            }
        })

        binding.btnPrivacy.setOnClickListener(object : MyClick() {
            override fun onMyClick(v: View, count: Long) {
                MyUtils.openDeeplink(requireContext(), RemoteConfigs.instance.appConfigs.privacyURL)
            }
        })

        binding.btnShareApp.setOnClickListener(object : MyClick() {
            override fun onMyClick(v: View, count: Long) {
                MyUtils.shareApp(requireContext())
            }
        })

        binding.btnRate.setOnClickListener(object : MyClick() {
            override fun onMyClick(v: View, count: Long) {
                clickRating()
            }
        })

        binding.btnAbout.setOnClickListener {
            clickAbout()
        }


        binding.btnNotification.setOnClickListener {
            if (!(requireActivity() as MainActivity).hasNotificationPermission()) {
                (requireActivity() as MainActivity).requestNotificationPermission()
            }
        }
        if ((requireActivity() as MainActivity).hasNotificationPermission()) {
            binding.grBtnNoti.visibility = View.GONE
        } else  binding.grBtnNoti.visibility = View.VISIBLE
    }

    fun clickRating() {
        val popupRating = PopupRating.newInstance()
        popupRating.setCallback(object : PopupRating.ConfirmCallback {
            override fun clickNegative() {
                MyUtils.hideNavigationBar(requireActivity())
                try {
                    if (!popupRating.isAdded) popupRating.dismissAllowingStateLoss()
                } catch (_: Exception) {
                }
            }

            override fun clickPositive(value: Float) {
                MyUtils.hideNavigationBar(requireActivity())
                if (!RemoteConfigs.instance.getConfigValue(KEY_PASS_REVIEW_APP)
                        .asBoolean() || value > 4
                ) {
                    gotoStore(requireContext())
                }
            }
        })
        try {
            if (!popupRating.isAdded) popupRating.show(
                requireActivity().supportFragmentManager, ""
            )
        } catch (_: Exception) {
        }
    }

    private fun clickAbout() {
        if (latest) {
            Toast.makeText(
                requireContext(),
                getString(R.string.latest_version),
                Toast.LENGTH_LONG
            ).show()
        } else
            Toast.makeText(
                requireContext(),
                getString(R.string.please_update),
                Toast.LENGTH_LONG
            ).show()

    }
}