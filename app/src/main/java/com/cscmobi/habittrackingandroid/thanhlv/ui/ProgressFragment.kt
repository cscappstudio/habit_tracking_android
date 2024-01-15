package com.cscmobi.habittrackingandroid.thanhlv.ui

import android.view.View
import com.cscmobi.habittrackingandroid.base.BaseFragment
import com.cscmobi.habittrackingandroid.databinding.FragmentProgressBinding
import com.cscmobi.habittrackingandroid.thanhlv.adapter.PagerMonthCalendarAdapter
import com.cscmobi.habittrackingandroid.thanhlv.adapter.PagerYearCalendarAdapter
import com.cscmobi.habittrackingandroid.thanhlv.model.MonthCalendarModel

class ProgressFragment : BaseFragment<FragmentProgressBinding>(FragmentProgressBinding::inflate) {
    private var pagerMonthAdapter: PagerMonthCalendarAdapter? = null
    private var pagerYearAdapter: PagerYearCalendarAdapter? = null

    override fun initView(view: View) {
        viewPager2()
    }

    private fun viewPager2() {
        //for month
        pagerMonthAdapter = PagerMonthCalendarAdapter(requireActivity())
        binding.vpMonth.adapter = pagerMonthAdapter
        pagerMonthAdapter!!.setList(getDataForMonth())


        //for year
        pagerYearAdapter = PagerYearCalendarAdapter(requireActivity())
        binding.vpYear.adapter = pagerYearAdapter
//        binding.vpYear.isSaveEnabled = false
        pagerYearAdapter!!.setList(getDataForYear())

    }

    private fun getDataForMonth(): MutableList<MonthCalendarModel> {
        val list = mutableListOf<MonthCalendarModel>()
        list.add(MonthCalendarModel(10, 2023))
        list.add(MonthCalendarModel(11, 2023))
        list.add(MonthCalendarModel(12, 2023))
        list.add(MonthCalendarModel(1, 2024))
        list.add(MonthCalendarModel(2, 2024))
        list.add(MonthCalendarModel(3, 2024))
        list.add(MonthCalendarModel(4, 2024))
        list.add(MonthCalendarModel(5, 2024))
        return list
    }


    private fun getDataForYear(): MutableList<MonthCalendarModel> {
        val list = mutableListOf<MonthCalendarModel>()
        list.add(MonthCalendarModel(1, 2023))
        list.add(MonthCalendarModel(1, 2024))
        list.add(MonthCalendarModel(1, 2025))
        return list
    }
}