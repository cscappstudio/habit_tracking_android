package com.cscmobi.habittrackingandroid.thanhlv.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.cscmobi.habittrackingandroid.BuildConfig
import com.cscmobi.habittrackingandroid.R
import com.cscmobi.habittrackingandroid.base.BaseFragment
import com.cscmobi.habittrackingandroid.databinding.FragmentChallengeBinding
import com.cscmobi.habittrackingandroid.databinding.FragmentProfileBinding
import com.cscmobi.habittrackingandroid.thanhlv.adapter.*
import com.cscmobi.habittrackingandroid.thanhlv.model.MonthCalendarModel
import com.thanhlv.fw.helper.MyClick
import com.thanhlv.fw.helper.MyUtils
import com.thanhlv.fw.helper.MyUtils.Companion.rippleEffect
import com.thanhlv.fw.remoteconfigs.RemoteConfigs
import com.thanhlv.fw.spf.SPF

class ProfileFragment :
    BaseFragment<FragmentProfileBinding>(FragmentProfileBinding::inflate) {

    override fun setEvent() {
    }

    private var lastVer = ""
    private var latest = false

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
                "fil" -> binding.tvLanguage.text = getString(R.string.filipino)
                "fr" -> binding.tvLanguage.text = getString(R.string.french)
                "de" -> binding.tvLanguage.text = getString(R.string.german)
                "hi" -> binding.tvLanguage.text = getString(R.string.hindi)
                "es" -> binding.tvLanguage.text = getString(R.string.spanish)
                "th" -> binding.tvLanguage.text = getString(R.string.thailand)
                "vi" -> binding.tvLanguage.text = getString(R.string.vietnamese)
                "pt" -> binding.tvLanguage.text = getString(R.string.portuguese)
            }
        }


        binding.tvChangeAva.setOnClickListener(object : MyClick() {
            override fun onMyClick(v: View, count: Long) {
                PopupChoseAvaProfile(object : PopupChoseAvaProfile.Callback{
                    override fun clickChange(ava: Int) {
                        binding.imgProfile.setImageResource(ava)
                    }
                }).show(childFragmentManager, "")
            }
        })
        binding.icPlusAva.setOnClickListener(object : MyClick() {
            override fun onMyClick(v: View, count: Long) {
                PopupChoseAvaProfile(object : PopupChoseAvaProfile.Callback{
                    override fun clickChange(ava: Int) {
                        binding.imgProfile.setImageResource(ava)
                    }
                }).show(childFragmentManager, "")
            }
        })

        binding.btnLanguage.setOnClickListener(object : MyClick() {
            override fun onMyClick(v: View, count: Long) {
                startActivity(Intent(requireContext(), ChangeLanguageActivity::class.java))
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

        binding.btnAbout.setOnClickListener {
            clickAbout()
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