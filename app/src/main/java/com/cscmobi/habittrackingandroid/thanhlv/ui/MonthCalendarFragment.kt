package com.cscmobi.habittrackingandroid.thanhlv.ui

import android.annotation.SuppressLint
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import com.cscmobi.habittrackingandroid.base.BaseFragment
import com.cscmobi.habittrackingandroid.databinding.FragmentMonthCalendarBinding
import com.cscmobi.habittrackingandroid.thanhlv.adapter.CalendarAdapter
import com.cscmobi.habittrackingandroid.thanhlv.model.DayCalendarModel
import java.text.SimpleDateFormat
import java.util.Calendar

class MonthCalendarFragment(private var month: Int, private var year: Int) :
    BaseFragment<FragmentMonthCalendarBinding>(FragmentMonthCalendarBinding::inflate),
    CalendarAdapter.Callback {
    private var adapter: CalendarAdapter? = null

    override fun initView(view: View) {
        initListener()
        recyclerView()
        adapter?.updateData(getDataList(month, year))
    }
    override fun onResume() {
        super.onResume()
        binding.root.requestLayout()
    }

    private fun recyclerView() {
        adapter = CalendarAdapter(requireContext(), this)
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