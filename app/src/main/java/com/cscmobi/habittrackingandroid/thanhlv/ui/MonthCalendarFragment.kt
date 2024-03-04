package com.cscmobi.habittrackingandroid.thanhlv.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import com.cscmobi.habittrackingandroid.base.BaseFragment
import com.cscmobi.habittrackingandroid.databinding.FragmentMonthCalendarBinding
import com.cscmobi.habittrackingandroid.thanhlv.adapter.MonthCalendarAdapter
import com.cscmobi.habittrackingandroid.thanhlv.database.AppDatabase
import com.cscmobi.habittrackingandroid.thanhlv.model.DayCalendarModel
import com.cscmobi.habittrackingandroid.thanhlv.model.Mood
import com.cscmobi.habittrackingandroid.thanhlv.ui.MoodActivity.Companion.mAllMoods
import com.cscmobi.habittrackingandroid.utils.CalendarUtil
import kotlinx.coroutines.runBlocking
import java.util.Calendar

class MonthCalendarFragment :
    BaseFragment<FragmentMonthCalendarBinding>(FragmentMonthCalendarBinding::inflate),
    MonthCalendarAdapter.Callback {
    private var adapter: MonthCalendarAdapter? = null
    private var mMonth = 3
    private var mYear = 2024

    companion object {
        fun newInstance(month: Int, year: Int) = MonthCalendarFragment().apply {
            arguments = Bundle().apply {
                putInt("MONTH_KEY", month)
                putInt("YEAR_KEY", year)
            }

            println("thanhlv 55555555555 ----444444444----- " + month)
        }
    }

    override fun setEvent() {
    }

    override fun initView(view: View) {
        binding.loadingView.visibility = View.VISIBLE

        if (arguments != null) {
            mMonth = arguments!!.getInt("MONTH_KEY")
            mYear = arguments!!.getInt("YEAR_KEY")
//            Handler(Looper.getMainLooper()).postDelayed({
                recyclerView()
                adapter?.updateData(getDataList(mMonth, mYear))
                binding.loadingView.visibility = View.GONE
//            }, 200)
        }
    }

    override fun onResume() {
        super.onResume()
        binding.root.requestLayout()
    }

    private fun recyclerView() {
        if (adapter == null)
            adapter = MonthCalendarAdapter(requireContext(), this)
        binding.recyclerView.layoutManager = GridLayoutManager(requireContext(), 7)
        binding.recyclerView.adapter = adapter
//        binding.recyclerView.overScrollMode = View.OVER_SCROLL_NEVER
    }


    @SuppressLint("NotifyDataSetChanged")
    fun updateData(m: Int, y: Int) {
        adapter?.updateData(getDataList(m, y))
    }

    @SuppressLint("SimpleDateFormat")
    private fun getDataList(month: Int, year: Int): MutableList<DayCalendarModel> {
        val list = mutableListOf<DayCalendarModel>()

        val calendar = Calendar.getInstance()
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
        val moods = getMoodsInMonth(month, year)
        for (i in 1..numDays) {
            val itemDay = DayCalendarModel("$i/${month}/$year")
            moods.forEach {
                val date = Calendar.getInstance()
                date.timeInMillis = it.date
                if (date[Calendar.DAY_OF_MONTH] == i) {
                    itemDay.mood = it.state
                    return@forEach
                }
            }
            list.add(itemDay)
        }

        runBlocking {

//            val thisMonth = Calendar.getInstance()
//            calendar[Calendar.YEAR] = year
//            calendar[Calendar.MONTH] = month - 1
//            calendar[Calendar.DAY_OF_MONTH] = 1
            val endMonth = CalendarUtil.nextMonthMs(calendar.timeInMillis)
            val dataMonth = AppDatabase.getInstance(requireContext()).dao()
                .getHistoryFromAUntilB(CalendarUtil.startMonthMs(calendar.timeInMillis), endMonth)

            if (dataMonth.isEmpty()) return@runBlocking
            dataMonth.sortedBy { it.date }

            println("thanhlv jjjjjjjjjjj ==== " + dataMonth)

            list.forEach {
                dataMonth.forEach { dataFill ->
                    if (CalendarUtil.sameDay(it.date, dataFill.date!!)) {
                        it.progress = dataFill.progressDay
                        it.isPauseAllTask = dataFill.allTaskPause
                        return@forEach
                    }
                }
            }

        }

        return list
    }

    override fun onClickDayCalendar(ovulationCalendarModel: DayCalendarModel) {

    }

    private fun getMoodsInMonth(month: Int, year: Int): List<Mood> {
        val moods = arrayListOf<Mood>()
        mAllMoods.forEach {
            val date = Calendar.getInstance()
            date.timeInMillis = it.date
            if (date[Calendar.MONTH] == month - 1 && date[Calendar.YEAR] == year) {
                moods.add(it)
            }
        }

        return moods
    }

}