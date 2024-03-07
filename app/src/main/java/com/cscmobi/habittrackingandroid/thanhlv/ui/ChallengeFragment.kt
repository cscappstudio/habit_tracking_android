package com.cscmobi.habittrackingandroid.thanhlv.ui

import android.content.Intent
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.cscmobi.habittrackingandroid.R
import com.cscmobi.habittrackingandroid.base.BaseFragment
import com.cscmobi.habittrackingandroid.databinding.FragmentChallengeBinding
import com.cscmobi.habittrackingandroid.thanhlv.adapter.*
import com.cscmobi.habittrackingandroid.thanhlv.model.Challenge
import com.cscmobi.habittrackingandroid.thanhlv.model.MonthCalendarModel
import com.google.gson.Gson
import com.thanhlv.ads.lib.AdMobUtils
import com.thanhlv.fw.constant.AppConfigs
import com.thanhlv.fw.remoteconfigs.RemoteConfigs
import com.thanhlv.fw.spf.SPF

class ChallengeFragment :
    BaseFragment<FragmentChallengeBinding>(FragmentChallengeBinding::inflate) {

    companion object {
        var myChallenges = MutableLiveData<List<Challenge>>()
        var allChallenges = MutableLiveData<List<Challenge>>()
    }

    private var myChallengeAdapter: MyChallengeAdapter? = null
    private var allChallengeAdapter: AllChallengeAdapter? = null

    override fun initView(view: View) {
        allChallengeAdapter = AllChallengeAdapter(requireContext())
        allChallengeAdapter?.setCallBack(object : AllChallengeAdapter.AllChallengeCallback {
            override fun onClickItem(challenge: Challenge) {
                val intent = Intent(requireContext(), DetailChallengeActivity::class.java)
                intent.putExtra("data", Gson().toJson(challenge))
                startActivity(intent)
            }

        })
        binding.rcAllChallenge.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.rcAllChallenge.adapter = allChallengeAdapter
        binding.rcAllChallenge.overScrollMode = View.OVER_SCROLL_NEVER
        allChallenges.observe(this) { list ->
            if (!list.isNullOrEmpty()) {
                allChallengeAdapter?.setData(list as MutableList<Challenge>?)
            }
        }


        myChallengeAdapter = MyChallengeAdapter(requireContext())
        myChallengeAdapter?.setCallBack(object : MyChallengeAdapter.MyChallengeCallback {
            override fun onClickItem(challenge: Challenge) {
                val intent = Intent(requireContext(), DetailChallengeActivity::class.java)
                intent.putExtra("data", Gson().toJson(challenge))
                startActivity(intent)
            }

        })
        binding.rcMyChallenge.adapter = myChallengeAdapter
        binding.rcMyChallenge.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        myChallenges.observe(this) { list ->
            if (list.isNullOrEmpty()) {
                binding.showMyChallenge.visibility = View.GONE
            } else {
                binding.showMyChallenge.visibility = View.VISIBLE
                myChallengeAdapter?.setData(list as MutableList<Challenge>?)
            }
        }
    }

    override fun setEvent() {
        binding.btnCreateChallenge.setOnClickListener {
            startActivity(Intent(requireContext(), CreateChallengeActivity::class.java))
        }

        binding.bgBannerPro.root.setOnClickListener {
            startActivity(Intent(requireContext(), SubscriptionsActivity::class.java))
        }
    }


    fun toggleAdView() {
        if (!SPF.isProApp(requireContext()) && RemoteConfigs.instance.getConfigValue(
                AppConfigs.KEY_AD_NATIVE_CHALLENGE
            ).asBoolean()
        ) {
            binding.adView.visibility = View.VISIBLE
            AdMobUtils.createNativeAd(
                requireContext(), getString(R.string.native_video_id), binding.adView, null
            )
        } else binding.adView.visibility = View.GONE
    }
}


