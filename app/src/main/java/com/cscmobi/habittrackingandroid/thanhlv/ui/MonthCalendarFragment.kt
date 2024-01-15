package com.cscmobi.habittrackingandroid.thanhlv.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import com.cscmobi.habittrackingandroid.base.BaseFragment
import com.cscmobi.habittrackingandroid.databinding.FragmentMonthCalendarBinding
import com.cscmobi.habittrackingandroid.thanhlv.adapter.MonthCalendarAdapter
import com.cscmobi.habittrackingandroid.thanhlv.model.DayCalendarModel
import java.util.Calendar

class MonthCalendarFragment :
    BaseFragment<FragmentMonthCalendarBinding>(FragmentMonthCalendarBinding::inflate),
    MonthCalendarAdapter.Callback {
    private var adapter: MonthCalendarAdapter? = null
    private var mMonth = 1
    private var mYear = 2024

    companion object {
        fun newInstance(month: Int, year: Int) = MonthCalendarFragment().apply {
            arguments = Bundle().apply {
                putInt("MONTH_KEY", month)
                putInt("YEAR_KEY", year)
            }
        }
    }

    override fun initView(view: View) {
        recyclerView()
        if (arguments != null) {
            mMonth = arguments!!.getInt("MONTH_KEY")
            mYear = arguments!!.getInt("YEAR_KEY")
            adapter?.updateData(getDataList(mMonth, mYear))
        }
        initListener()
    }

    override fun onResume() {
        super.onResume()
        binding.root.requestLayout()
    }

    private fun recyclerView() {
        adapter = MonthCalendarAdapter(requireContext(), this)
        binding.recyclerView.layoutManager = GridLayoutManager(requireContext(), 7)
        binding.recyclerView.adapter = adapter
        binding.recyclerView.overScrollMode = View.OVER_SCROLL_NEVER
    }

    private fun initListener() {

    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(m: Int, y: Int) {
        adapter?.updateData(getDataList(m, y))
    }

    @SuppressLint("SimpleDateFormat")
    private fun getDataList(month: Int, year: Int): MutableList<DayCalendarModel> {
        val list = mutableListOf<DayCalendarModel>()

        val calendar = Calendar.getInstance()
        calendar[Calendar.DAY_OF_MONTH] = 1
        calendar[Calendar.YEAR] = year
        calendar[Calendar.MONTH] = month - 1
        calendar[Calendar.DAY_OF_MONTH] = 1

        println(
            "thanhlv getDataList ==== " + calendar.get(Calendar.DAY_OF_WEEK)
        )
        when (calendar.get(Calendar.DAY_OF_WEEK)) {
            Calendar.MONDAY -> {}
            Calendar.TUESDAY -> {
                list.add(DayCalendarModel(""))
            }

            Calendar.WEDNESDAY -> {
                list.add(DayCalendarModel(""))
                list.add(DayCalendarModel(""))
            }

            Calendar.THURSDAY -> {
                list.add(DayCalendarModel(""))
                list.add(DayCalendarModel(""))
                list.add(DayCalendarModel(""))
            }

            Calendar.FRIDAY -> {
                list.add(DayCalendarModel(""))
                list.add(DayCalendarModel(""))
                list.add(DayCalendarModel(""))
                list.add(DayCalendarModel(""))
            }

            Calendar.SATURDAY -> {
                list.add(DayCalendarModel(""))
                list.add(DayCalendarModel(""))
                list.add(DayCalendarModel(""))
                list.add(DayCalendarModel(""))
                list.add(DayCalendarModel(""))
            }

            Calendar.SUNDAY -> {
                list.add(DayCalendarModel(""))
                list.add(DayCalendarModel(""))
                list.add(DayCalendarModel(""))
                list.add(DayCalendarModel(""))
                list.add(DayCalendarModel(""))
                list.add(DayCalendarModel(""))
            }
        }
        val numDays = calendar.getActualMaximum(Calendar.DATE)
        for (i in 1..numDays) {
            list.add(DayCalendarModel("$i/${month}/$year"))
        }

        return list
    }

    override fun onClickDayCalendar(ovulationCalendarModel: DayCalendarModel) {

    }


}