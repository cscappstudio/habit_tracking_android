package com.cscmobi.habittrackingandroid.thanhlv.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.datastore.dataStoreFile
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.cscmobi.habittrackingandroid.databinding.ActivityMoodBinding
import com.cscmobi.habittrackingandroid.thanhlv.adapter.MoodRecordAdapter
import com.cscmobi.habittrackingandroid.thanhlv.adapter.PagerMonthCalendarAdapter
import com.cscmobi.habittrackingandroid.thanhlv.model.MonthCalendarModel
import com.thanhlv.fw.helper.MyUtils
import com.thanhlv.fw.helper.RunUtils
import java.text.SimpleDateFormat
import java.util.*


class MoodActivity : BaseActivity2() {
    private lateinit var binding: ActivityMoodBinding

    private var pagerMonthAdapter: PagerMonthCalendarAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMoodBinding.inflate(layoutInflater)
        setContentView(binding.root)
        MyUtils.hideStatusBar(this)


        viewPager2()
        controller()
    }

    private fun controller() {
        binding.btnBackHeader.setOnClickListener {
            onBackPressed()
        }


        binding.btnAddMood.setOnClickListener {
            startActivity(Intent(this, TodayMoodActivity::class.java))
        }
    }

    private fun viewPager2() {
        //for month
        pagerMonthAdapter = PagerMonthCalendarAdapter(this)
        binding.vpMonth.adapter = pagerMonthAdapter
        pagerMonthAdapter!!.setList(getDataForMonth())
        binding.vpMonth.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                updateTitleMonth(pagerMonthAdapter?.getList()?.get(position))
            }
        })

        val moodRecordAdapter = MoodRecordAdapter(this)
        moodRecordAdapter.setCallBack(object : MoodRecordAdapter.MoodRecordCallback {
            override fun onClickItem(pos: Int) {
                println("thanhlv onClickItem ----- " + pos)
                startActivity(Intent(this@MoodActivity, DetailMoodActivity::class.java))
            }

        })

        binding.rcMoodRecord.adapter = moodRecordAdapter

        binding.rcMoodRecord.layoutManager = LinearLayoutManager(this)
    }

    @SuppressLint("SimpleDateFormat")
    private fun updateTitleMonth(monthYear: MonthCalendarModel?) {
        if (monthYear == null) return
        val calendar = Calendar.getInstance()
        calendar[Calendar.YEAR] = monthYear.year
        calendar[Calendar.MONTH] = monthYear.month - 1
        val monthYearString = SimpleDateFormat("MMMM yyyy").format(calendar.time)
        if (!isFinishing)
            RunUtils.runOnUI {
                binding.tvMonth.text = monthYearString.uppercase()
            }
    }

    private fun getDataForMonth(): MutableList<MonthCalendarModel> {
        val list = mutableListOf<MonthCalendarModel>()
        list.add(MonthCalendarModel(10, 2023))
        list.add(MonthCalendarModel(11, 2023))
        list.add(MonthCalendarModel(12, 2023))
        list.add(MonthCalendarModel(1, 2024))
        list.add(MonthCalendarModel(2, 2024))
        list.add(MonthCalendarModel(3, 2024))
        return list
    }
}