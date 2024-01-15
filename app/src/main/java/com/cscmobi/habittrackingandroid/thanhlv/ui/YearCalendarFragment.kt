package com.cscmobi.habittrackingandroid.thanhlv.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import com.cscmobi.habittrackingandroid.base.BaseFragment
import com.cscmobi.habittrackingandroid.databinding.FragmentMonthCalendarBinding
import com.cscmobi.habittrackingandroid.thanhlv.adapter.MonthCalendarAdapter
import com.cscmobi.habittrackingandroid.thanhlv.adapter.YearCalendarAdapter
import com.cscmobi.habittrackingandroid.thanhlv.model.DayCalendarModel
import java.util.Calendar

class YearCalendarFragment :
    BaseFragment<FragmentMonthCalendarBinding>(FragmentMonthCalendarBinding::inflate) {
    private var adapter: YearCalendarAdapter? = null
    private var mYear = 2024

    companion object {
        fun newInstance(year: Int) = YearCalendarFragment().apply {
            arguments = Bundle().apply {
                putInt("YEAR_KEY", year)
            }
        }
    }

    override fun initView(view: View) {
        recyclerView()
        if (arguments != null) {
            mYear = arguments!!.getInt("YEAR_KEY")
            adapter?.updateData(getDataList(mYear))
        }
        initListener()
    }

    override fun onResume() {
        super.onResume()
        binding.root.requestLayout()
    }

    private fun recyclerView() {
        adapter = YearCalendarAdapter(requireContext())
        binding.recyclerView.layoutManager = GridLayoutManager(requireContext(), 31)
        binding.recyclerView.adapter = adapter
        binding.recyclerView.overScrollMode = View.OVER_SCROLL_NEVER
    }

    private fun initListener() {

    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(y: Int) {
        adapter?.updateData(getDataList(y))
    }

    @SuppressLint("SimpleDateFormat")
    private fun getDataList(year: Int): MutableList<DayCalendarModel> {
        val list = mutableListOf<DayCalendarModel>()


        for (i in 1..372) {
            list.add(DayCalendarModel("$i/1/$year"))
        }

        return list
    }


}