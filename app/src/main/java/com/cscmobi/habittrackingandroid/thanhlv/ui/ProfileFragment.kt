package com.cscmobi.habittrackingandroid.thanhlv.ui

import android.content.Intent
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.cscmobi.habittrackingandroid.base.BaseFragment
import com.cscmobi.habittrackingandroid.databinding.FragmentChallengeBinding
import com.cscmobi.habittrackingandroid.databinding.FragmentProfileBinding
import com.cscmobi.habittrackingandroid.thanhlv.adapter.*
import com.cscmobi.habittrackingandroid.thanhlv.model.MonthCalendarModel

class ProfileFragment :
    BaseFragment<FragmentProfileBinding>(FragmentProfileBinding::inflate) {

    override fun initView(view: View) {

        binding.btnLanguage.setOnClickListener {
            startActivity(Intent(requireContext(), ChangeLanguageActivity::class.java))
        }
    }
}