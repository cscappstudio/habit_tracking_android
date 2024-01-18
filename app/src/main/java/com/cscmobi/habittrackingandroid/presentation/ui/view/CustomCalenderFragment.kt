package com.cscmobi.habittrackingandroid.presentation.ui.view

import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cscmobi.habittrackingandroid.base.BaseFragment
import com.cscmobi.habittrackingandroid.databinding.CalenderCustomBinding
import com.cscmobi.habittrackingandroid.presentation.ItemWithPostionListener
import com.cscmobi.habittrackingandroid.presentation.ui.adapter.CalendarAdapter
import com.cscmobi.habittrackingandroid.presentation.ui.adapter.CalenderData
import org.threeten.bp.LocalDate
import org.threeten.bp.YearMonth
import org.threeten.bp.format.DateTimeFormatter


class CustomCalenderFragment : BaseFragment<CalenderCustomBinding>(CalenderCustomBinding::inflate) {
    private lateinit var selectedDate: LocalDate
    private var calendarAdapter: CalendarAdapter? = null
    private var calenderData = arrayListOf<CalenderData>()

    override fun initView(view: View) {
        selectedDate = LocalDate.now();
        setMonthView()

    }

    override fun setEvent() {
        binding.ivPrevios.setOnClickListener{
            previousMonthAction(it)
        }

        binding.ivNext.setOnClickListener{
            nextMonthAction(it)
        }
    }

    private fun setMonthView() {
        calenderData.clear()
        val daysInMonth = daysInMonthArray(selectedDate)
        binding.monthYearTV.text = monthYearFromDate(selectedDate);

        calenderData = daysInMonth.map { CalenderData(it) } as ArrayList<CalenderData>

        if (calendarAdapter == null) {
            val layoutManager: RecyclerView.LayoutManager =
                GridLayoutManager(requireContext(), 7)
            binding.calendarRecyclerView.layoutManager = layoutManager

            val calendarAdapter = CalendarAdapter()
            calendarAdapter.setListener(object : ItemWithPostionListener<CalenderData>{
                override fun onItemClicked(item: CalenderData, p: Int) {
                    calenderData.forEach {
                        it.isSelected = false
                    }

                    item.isSelected = true
                    calendarAdapter?.notifyDataSetChanged()
                }

            })

            binding.calendarRecyclerView.adapter = calendarAdapter
            calendarAdapter.submitList(calenderData)

        }

        calendarAdapter?.submitList(calenderData)
        calendarAdapter?.notifyDataSetChanged()

    }

    private fun daysInMonthArray(date: LocalDate): ArrayList<String> {
        val daysInMonthArray = ArrayList<String>()
        val yearMonth: YearMonth = YearMonth.from(date)
        val daysInMonth: Int = yearMonth.lengthOfMonth()
        val firstOfMonth = selectedDate.withDayOfMonth(1)
        val dayOfWeek = firstOfMonth.dayOfWeek.value
        for (i in 1..42) {
            if (i <= dayOfWeek || i > daysInMonth + dayOfWeek) {
                daysInMonthArray.add("")
            } else {
                daysInMonthArray.add((i - dayOfWeek).toString())
            }
        }
        return daysInMonthArray
    }

    private fun monthYearFromDate(date: LocalDate): String? {
        val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("MMMM yyyy")
        return date.format(formatter)
    }

    fun previousMonthAction(view: View?) {
        selectedDate = selectedDate.minusMonths(1)
        setMonthView()
    }

    fun nextMonthAction(view: View?) {
        selectedDate = selectedDate.plusMonths(1)
        setMonthView()
    }
}