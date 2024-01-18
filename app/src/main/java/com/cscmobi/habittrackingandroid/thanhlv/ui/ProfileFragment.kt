package com.cscmobi.habittrackingandroid.thanhlv.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.cscmobi.habittrackingandroid.BuildConfig
import com.cscmobi.habittrackingandroid.base.BaseFragment
import com.cscmobi.habittrackingandroid.databinding.FragmentChallengeBinding
import com.cscmobi.habittrackingandroid.databinding.FragmentProfileBinding
import com.cscmobi.habittrackingandroid.thanhlv.adapter.*
import com.cscmobi.habittrackingandroid.thanhlv.model.MonthCalendarModel
import com.thanhlv.fw.helper.MyUtils
import com.thanhlv.fw.helper.MyUtils.Companion.rippleEffect
import com.thanhlv.fw.remoteconfigs.RemoteConfigs

class ProfileFragment :
    BaseFragment<FragmentProfileBinding>(FragmentProfileBinding::inflate) {

    override fun setEvent() {
    }
    @SuppressLint("SetTextI18n")
    override fun initView(view: View) {

        binding.btnLanguage.rippleEffect("#f5f5f5")
        binding.btnNotification.rippleEffect("#f5f5f5")
        binding.btnAbout.rippleEffect("#f5f5f5")
        binding.btnRate.rippleEffect("#f5f5f5")
        binding.btnPrivacy.rippleEffect("#f5f5f5")
        binding.btnShareApp.rippleEffect("#f5f5f5")
        binding.btnSubscription.rippleEffect("#f5f5f5")

        binding.tvVersion.text = "v" + BuildConfig.VERSION_NAME

        binding.btnLanguage.setOnClickListener {
            startActivity(Intent(requireContext(), ChangeLanguageActivity::class.java))
        }

        binding.btnPrivacy.setOnClickListener {
            MyUtils.openDeeplink(requireContext(), RemoteConfigs.instance.appConfigs.privacyURL)
        }

        binding.btnShareApp.setOnClickListener {
            MyUtils.shareApp(requireContext())
        }

        binding.btnAbout.setOnClickListener {

        }

    }
}