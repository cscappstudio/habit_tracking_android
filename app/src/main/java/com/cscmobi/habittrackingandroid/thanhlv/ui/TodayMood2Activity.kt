package com.cscmobi.habittrackingandroid.thanhlv.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.cscmobi.habittrackingandroid.R
import com.cscmobi.habittrackingandroid.databinding.ActivityDetailMoodBinding
import com.cscmobi.habittrackingandroid.databinding.ActivityMoodBinding
import com.cscmobi.habittrackingandroid.databinding.ActivityMoodToday2Binding
import com.cscmobi.habittrackingandroid.databinding.ActivityMoodTodayBinding
import com.cscmobi.habittrackingandroid.thanhlv.adapter.MoodRecordAdapter
import com.cscmobi.habittrackingandroid.thanhlv.adapter.PagerMonthCalendarAdapter
import com.cscmobi.habittrackingandroid.thanhlv.model.MonthCalendarModel
import com.thanhlv.fw.helper.MyUtils
import com.thanhlv.fw.helper.RunUtils
import java.text.SimpleDateFormat
import java.util.*


class TodayMood2Activity : BaseActivity2() {
    private lateinit var binding: ActivityMoodToday2Binding

    override fun setupScreen() {
        binding = ActivityMoodToday2Binding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun loadData() {
    }

    override fun initView() {
        binding.imgMood.startAnimation(AnimationUtils.loadAnimation(this, R.anim.pulse))
    }

    override fun controllerView() {
        binding.btnBackHeader.setOnClickListener {
            onBackPressed()
        }
        binding.btnNext.setOnClickListener {
            startActivity(Intent(this, TodayMood3Activity::class.java))
            finish()
        }
    }

}