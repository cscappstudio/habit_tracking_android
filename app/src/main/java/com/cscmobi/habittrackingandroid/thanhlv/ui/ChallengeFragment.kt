package com.cscmobi.habittrackingandroid.thanhlv.ui

import android.content.Intent
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.cscmobi.habittrackingandroid.base.BaseFragment
import com.cscmobi.habittrackingandroid.databinding.FragmentChallengeBinding
import com.cscmobi.habittrackingandroid.thanhlv.adapter.*
import com.cscmobi.habittrackingandroid.thanhlv.model.MonthCalendarModel

class ChallengeFragment :
    BaseFragment<FragmentChallengeBinding>(FragmentChallengeBinding::inflate) {

    private var myChallengeAdapter: MyChallengeAdapter? = null
    private var allChallengeAdapter: AllChallengeAdapter? = null

    override fun initView(view: View) {
        allChallengeAdapter = AllChallengeAdapter(requireContext())
        allChallengeAdapter?.setCallBack(object : AllChallengeAdapter.MyChallengeCallback{
            override fun onClickItem(pos: Int) {
                startActivity(Intent(requireContext(), DetailChallengeActivity::class.java))
            }

        })
        binding.rcAllChallenge.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.rcAllChallenge.adapter = allChallengeAdapter
        binding.rcAllChallenge.overScrollMode = View.OVER_SCROLL_NEVER

        myChallengeAdapter = MyChallengeAdapter(requireContext())
        myChallengeAdapter?.setCallBack(object : MyChallengeAdapter.MyChallengeCallback{
            override fun onClickItem(pos: Int) {
                startActivity(Intent(requireContext(), DetailChallengeActivity::class.java))
            }

        })
        binding.rcMyChallenge.adapter = myChallengeAdapter
        binding.rcMyChallenge.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
    }

    override fun setEvent() {
    }
}