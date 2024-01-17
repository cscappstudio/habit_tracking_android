package com.cscmobi.habittrackingandroid.thanhlv.ui

import android.os.Build
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.cscmobi.habittrackingandroid.databinding.ActivityDetailMoodBinding
import com.cscmobi.habittrackingandroid.thanhlv.adapter.MoodRecordAdapter
import com.cscmobi.habittrackingandroid.thanhlv.model.MonthCalendarModel
import com.thanhlv.fw.helper.MyUtils
import com.thanhlv.fw.helper.MyUtils.Companion.configKeyboardBelowEditText
import java.util.*


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
        viewPager2()
    }

    override fun controllerView() {
        binding.btnBackHeader.setOnClickListener {
            onBackPressed()
        }
    }

    private fun viewPager2() {
        binding.rcFeeling.adapter = MoodRecordAdapter(this)
        binding.rcFeeling.layoutManager = LinearLayoutManager(this)
    }

}