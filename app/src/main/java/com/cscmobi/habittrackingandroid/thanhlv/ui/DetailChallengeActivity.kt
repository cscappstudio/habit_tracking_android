package com.cscmobi.habittrackingandroid.thanhlv.ui

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.cscmobi.habittrackingandroid.databinding.ActivityDetailChallengeBinding
import com.cscmobi.habittrackingandroid.databinding.ActivityDetailMoodBinding
import com.cscmobi.habittrackingandroid.thanhlv.adapter.DetailChallengeAdapter
import com.cscmobi.habittrackingandroid.thanhlv.adapter.FeelingTag2Adapter
import com.cscmobi.habittrackingandroid.thanhlv.adapter.FeelingTagAdapter
import com.cscmobi.habittrackingandroid.thanhlv.adapter.MoodRecordAdapter
import com.thanhlv.fw.helper.MyUtils.Companion.configKeyboardBelowEditText


class DetailChallengeActivity : BaseActivity2() {
    private lateinit var binding: ActivityDetailChallengeBinding

    override fun setupScreen() {
        binding = ActivityDetailChallengeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        configKeyboardBelowEditText(this)
    }

    override fun loadData() {
    }

    override fun initView() {
        recyclerView()
    }

    override fun controllerView() {
        binding.btnBackHeader.setOnClickListener {
            onBackPressed()
        }
    }

    private fun recyclerView() {
        binding.rcTasks.adapter = DetailChallengeAdapter(this)
        binding.rcTasks.layoutManager = LinearLayoutManager(this)

    }

}