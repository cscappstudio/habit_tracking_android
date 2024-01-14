package com.cscmobi.habittrackingandroid.thanhlv.ui

import android.view.View
import com.cscmobi.habittrackingandroid.base.BaseFragment
import com.cscmobi.habittrackingandroid.databinding.FragmentProgressBinding
import com.cscmobi.habittrackingandroid.thanhlv.adapter.MonthCalendarAdapter
import com.cscmobi.habittrackingandroid.thanhlv.model.MonthCalendarModel

class ProgressFragment: BaseFragment<FragmentProgressBinding>(FragmentProgressBinding::inflate) {
    private var adapter: MonthCalendarAdapter? = null

    override fun initView(view: View) {
        adapter = MonthCalendarAdapter(requireActivity())
        binding.viewPager2.adapter = adapter
        binding.viewPager2.isSaveEnabled = false
        adapter!!.setList(getData())
    }

    private fun getData(): MutableList<MonthCalendarModel> {
        val list = mutableListOf<MonthCalendarModel>()
        list.add(MonthCalendarModel(11, 2024))
        list.add(MonthCalendarModel(12, 2024))
        return list
    }
}