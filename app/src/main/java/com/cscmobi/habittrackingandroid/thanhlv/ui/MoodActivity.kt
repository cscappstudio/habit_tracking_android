package com.cscmobi.habittrackingandroid.thanhlv.ui

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.cscmobi.habittrackingandroid.databinding.ActivityMoodBinding
import com.cscmobi.habittrackingandroid.thanhlv.adapter.MoodRecordAdapter
import com.cscmobi.habittrackingandroid.thanhlv.adapter.PagerMonthCalendarAdapter
import com.cscmobi.habittrackingandroid.thanhlv.model.MonthCalendarModel
import com.thanhlv.fw.helper.MyUtils


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
    }

    private fun viewPager2() {
        //for month
        pagerMonthAdapter = PagerMonthCalendarAdapter(this)
        binding.vpMonth.adapter = pagerMonthAdapter
        pagerMonthAdapter!!.setList(getDataForMonth())


        binding.rcMoodRecord.adapter = MoodRecordAdapter(this)
        binding.rcMoodRecord.layoutManager = LinearLayoutManager(this)
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