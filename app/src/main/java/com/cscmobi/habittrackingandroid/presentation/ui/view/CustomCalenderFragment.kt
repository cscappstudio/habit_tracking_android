package com.cscmobi.habittrackingandroid.presentation.ui.view

import android.content.res.ColorStateList
import android.os.Build
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cscmobi.habittrackingandroid.R
import com.cscmobi.habittrackingandroid.base.BaseFragment
import com.cscmobi.habittrackingandroid.databinding.CalenderCustomBinding
import com.cscmobi.habittrackingandroid.presentation.ItemWithPostionListener
import com.cscmobi.habittrackingandroid.presentation.ui.adapter.CalendarAdapter
import com.cscmobi.habittrackingandroid.presentation.ui.adapter.CalenderData
import com.google.api.client.util.DateTime
import org.threeten.bp.Instant
import org.threeten.bp.LocalDate
import org.threeten.bp.YearMonth
import org.threeten.bp.ZoneId
import org.threeten.bp.format.DateTimeFormatter
import java.text.DateFormat
import java.time.temporal.TemporalAccessor
import java.util.Calendar
import java.util.Date


class CustomCalenderFragment : BaseFragment<CalenderCustomBinding>(CalenderCustomBinding::inflate) {
    private lateinit var selectedDate: LocalDate
    private var calendarAdapter: CalendarAdapter? = null
    private var calenderData = arrayListOf<CalenderData>()
    private var dayDate: LocalDate? = null

    fun resetColorTask(color: Int?) {
        color?.let {
            binding.ivPrevios.imageTintList = ColorStateList.valueOf(it)
            binding.ivNext.imageTintList = ColorStateList.valueOf(it)
            binding.monthYearTV.setTextColor(ColorStateList.valueOf(it))

            calendarAdapter?.colorSelect = it
            calendarAdapter?.notifyDataSetChanged()
        }
    }

    fun setSelectDate(date: Long) {
        val calendar = Calendar.getInstance()
        calendar.time = Date(date)
        selectedDate = LocalDate.of(
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH) + 1,
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        setMonthView()

        val dayIndex = calenderData.indexOfFirst { it.day == selectedDate.dayOfMonth.toString() }
        if (dayIndex != -1) {
            calenderData[dayIndex].isSelected = true
            calendarAdapter?.notifyDataSetChanged()
        }
    }

    fun getDateSelected(): Long? {
        if (dayDate != null) {
            val calendar = Calendar.getInstance()
            calendar.set(dayDate!!.year, dayDate!!.monthValue - 1, dayDate!!.dayOfMonth)
            return calendar.time.time
        }
        return null
    }

    override fun initView(view: View) {
        selectedDate = LocalDate.now();
        setMonthView()
    }

    override fun setEvent() {
        binding.ivPrevios.setOnClickListener {
            previousMonthAction(it)
        }

        binding.ivNext.setOnClickListener {
            nextMonthAction(it)
        }
    }

    private fun setMonthView() {
        calenderData.clear()
        val daysInMonth = daysInMonthArray(selectedDate)
        binding.monthYearTV.text = monthYearFromDate(selectedDate);
        calenderData = daysInMonth.map { CalenderData(it) } as ArrayList<CalenderData>

        val layoutManager: RecyclerView.LayoutManager =
            GridLayoutManager(requireContext(), 7)
        binding.calendarRecyclerView.layoutManager = layoutManager

        calendarAdapter = CalendarAdapter(selectedDate)
        calendarAdapter?.setListener(object : ItemWithPostionListener<CalenderData> {
            override fun onItemClicked(item: CalenderData, p: Int) {
                calenderData.forEach {
                    it.isSelected = false
                }

                calenderData[p].isSelected = true

                val selectedDayInt = item.day.toIntOrNull()

                if (selectedDayInt != null)
                    dayDate = selectedDate.withDayOfMonth(selectedDayInt)

                calendarAdapter?.notifyDataSetChanged()

            }

        })


        binding.calendarRecyclerView.adapter = calendarAdapter
        calendarAdapter?.submitList(calenderData)
        calendarAdapter?._currentDate = selectedDate
        calendarAdapter?.notifyDataSetChanged()

    }

    private fun daysInMonthArray(date: LocalDate): ArrayList<String> {
        val daysInMonthArray = ArrayList<String>()
        val yearMonth: YearMonth = YearMonth.from(date)
        val daysInMonth: Int = yearMonth.lengthOfMonth()
        val firstOfMonth = date.withDayOfMonth(1)
        val dayOfWeek = firstOfMonth.dayOfWeek.value

        // Adjust the loop to start from 0 (Monday) and end at 6 (Sunday)
        for (i in 0 until 35) {
            val adjustedIndex = (i + dayOfWeek - 1) % 7 // Adjust for Monday start
            if (i < dayOfWeek - 1 || adjustedIndex >= daysInMonth + dayOfWeek - 1) {
                daysInMonthArray.add("")
            } else {
                val dayOfMonth = i - dayOfWeek + 2
                if (dayOfMonth in 1..daysInMonth) {
                    daysInMonthArray.add(dayOfMonth.toString())
                } else {
                    daysInMonthArray.add("")
                }
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