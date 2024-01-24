package com.cscmobi.habittrackingandroid.thanhlv.ui

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.cscmobi.habittrackingandroid.databinding.ActivityDetailMoodBinding
import com.cscmobi.habittrackingandroid.thanhlv.adapter.FeelingTag2Adapter
import com.cscmobi.habittrackingandroid.thanhlv.adapter.FeelingTagAdapter
import com.cscmobi.habittrackingandroid.thanhlv.adapter.MoodRecordAdapter
import com.thanhlv.fw.helper.MyUtils.Companion.configKeyboardBelowEditText


class DetailMoodActivity : BaseActivity2() {
    private lateinit var binding: ActivityDetailMoodBinding

    override fun setupScreen() {
        binding = ActivityDetailMoodBinding.inflate(layoutInflater)
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
        binding.rcFeeling.adapter = FeelingTag2Adapter(this)
        binding.rcFeeling.layoutManager = GridLayoutManager(this, 3)

        binding.rcBecause.adapter = FeelingTag2Adapter(this)
        binding.rcBecause.layoutManager = GridLayoutManager(this, 3)
    }

}