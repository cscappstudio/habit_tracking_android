package com.cscmobi.habittrackingandroid.thanhlv.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import com.cscmobi.habittrackingandroid.base.BaseFragment
import com.cscmobi.habittrackingandroid.databinding.FragmentYearCalendarBinding
import com.cscmobi.habittrackingandroid.thanhlv.adapter.YearCalendarAdapter
import com.cscmobi.habittrackingandroid.thanhlv.database.AppDatabase
import com.cscmobi.habittrackingandroid.thanhlv.model.DayCalendarModel
import com.cscmobi.habittrackingandroid.utils.CalendarUtil
import kotlinx.coroutines.runBlocking
import java.util.*

class YearCalendarFragment :
    BaseFragment<FragmentYearCalendarBinding>(FragmentYearCalendarBinding::inflate) {
    private var adapter: YearCalendarAdapter? = null
    private var mYear = 2024

    companion object {
        fun newInstance(year: Int) = YearCalendarFragment().apply {
            arguments = Bundle().apply {
                putInt("YEAR_KEY", year)
            }
        }
    }

    override fun setEvent() {
    }

    override fun initView(view: View) {
        binding.loadingView.visibility = View.VISIBLE
        if (arguments != null) {
            mYear = arguments!!.getInt("YEAR_KEY")
            Handler(Looper.getMainLooper()).postDelayed({
                recyclerView()
                adapter?.updateData(getDataList(mYear))
                binding.loadingView.visibility = View.GONE
            }, 200)

        }
        initListener()
    }

    override fun onResume() {
        super.onResume()
        binding.root.requestLayout()
    }

    private fun recyclerView() {
        if (isDetached || !isAdded) return
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
            list.add(DayCalendarModel(""))
        }

        runBlocking {
            val startYear = Calendar.getInstance()
            startYear[Calendar.YEAR] = year
            startYear[Calendar.MONTH] = 0
            startYear[Calendar.DAY_OF_YEAR] = 1
            startYear[Calendar.HOUR_OF_DAY] = 0
            startYear[Calendar.MINUTE] = 0
            startYear[Calendar.SECOND] = 0
            startYear[Calendar.MILLISECOND] = 0
            val nextYear = CalendarUtil.nextYear(startYear.timeInMillis)
            val dataYear = AppDatabase.getInstance(requireContext()).dao()
                .getHistoryFromAUntilB(startYear.timeInMillis, nextYear)

            if (dataYear.isEmpty()) return@runBlocking

            dataYear.forEach {
                val day = CalendarUtil.dayOfMonth(it.date!!)
                val month = CalendarUtil.getMonth(it.date!!)
                val pos = month * 31 + day - 1
                list[pos].isPauseAllTask = it.allTaskPause
                list[pos].progress = it.progressDay
            }


        }

        return list
    }


}