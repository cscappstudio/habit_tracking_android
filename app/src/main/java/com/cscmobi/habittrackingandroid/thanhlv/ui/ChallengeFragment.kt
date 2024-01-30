package com.cscmobi.habittrackingandroid.thanhlv.ui

import android.content.Intent
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.cscmobi.habittrackingandroid.base.BaseFragment
import com.cscmobi.habittrackingandroid.databinding.FragmentChallengeBinding
import com.cscmobi.habittrackingandroid.thanhlv.adapter.*
import com.cscmobi.habittrackingandroid.thanhlv.model.Challenge
import com.cscmobi.habittrackingandroid.thanhlv.model.MonthCalendarModel
import com.google.gson.Gson

class ChallengeFragment :
    BaseFragment<FragmentChallengeBinding>(FragmentChallengeBinding::inflate) {

    companion object {
        var myChallenges: List<Challenge>? = null
        var allChallenges: List<Challenge>? = null
    }

    private var myChallengeAdapter: MyChallengeAdapter? = null
    private var allChallengeAdapter: AllChallengeAdapter? = null

    override fun initView(view: View) {
        allChallengeAdapter = AllChallengeAdapter(requireContext())
        allChallengeAdapter?.setData(allChallenges as MutableList<Challenge>?)
        allChallengeAdapter?.setCallBack(object : AllChallengeAdapter.MyChallengeCallback {
            override fun onClickItem(challenge: Challenge) {
                val intent = Intent(requireContext(), DetailChallengeActivity::class.java)
                intent.putExtra("data", Gson().toJson(challenge))
                startActivity(intent)
            }

        })
        binding.rcAllChallenge.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.rcAllChallenge.adapter = allChallengeAdapter
        binding.rcAllChallenge.overScrollMode = View.OVER_SCROLL_NEVER

        myChallengeAdapter = MyChallengeAdapter(requireContext())
        myChallengeAdapter?.setData(myChallenges as MutableList<Challenge>?)
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

        if (myChallenges.isNullOrEmpty()) binding.showMyChallenge.visibility = View.GONE
        else binding.showMyChallenge.visibility = View.VISIBLE
    }

    override fun setEvent() {
        binding.btnCreateChallenge.setOnClickListener {
            startActivity(Intent(requireContext(), CreateChallengeActivity::class.java))
        }
    }
}


