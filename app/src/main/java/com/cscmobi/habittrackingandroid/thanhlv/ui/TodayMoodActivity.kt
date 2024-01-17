package com.cscmobi.habittrackingandroid.thanhlv.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.cscmobi.habittrackingandroid.databinding.ActivityDetailMoodBinding
import com.cscmobi.habittrackingandroid.databinding.ActivityMoodBinding
import com.cscmobi.habittrackingandroid.databinding.ActivityMoodTodayBinding
import com.cscmobi.habittrackingandroid.thanhlv.adapter.MoodRecordAdapter
import com.cscmobi.habittrackingandroid.thanhlv.adapter.PagerMonthCalendarAdapter
import com.cscmobi.habittrackingandroid.thanhlv.model.MonthCalendarModel
import com.thanhlv.fw.helper.MyUtils
import com.thanhlv.fw.helper.RunUtils
import java.text.SimpleDateFormat
import java.util.*


class TodayMoodActivity : BaseActivity2() {
    private lateinit var binding: ActivityMoodTodayBinding

    override fun setupScreen() {
        binding = ActivityMoodTodayBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun loadData() {
    }

    override fun initView() {
    }

    override fun controllerView() {
        binding.btnBackHeader.setOnClickListener {
            onBackPressed()
        }
        binding.btnMoodGreat.setOnClickListener {
            gotoMoodStep2(1)
        }
        binding.btnMoodGood.setOnClickListener {
            gotoMoodStep2(2)
        }
        binding.btnMoodNeutral.setOnClickListener {
            gotoMoodStep2(3)
        }
        binding.btnMoodNotGreat.setOnClickListener {
            gotoMoodStep2(4)
        }
        binding.btnMoodBad.setOnClickListener {
            gotoMoodStep2(5)
        }

    }

    private fun gotoMoodStep2(mood: Int) {
        startActivity(Intent(this, TodayMood2Activity::class.java))
    }

}