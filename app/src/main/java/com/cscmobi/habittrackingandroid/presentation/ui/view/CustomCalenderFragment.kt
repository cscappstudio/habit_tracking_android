package com.cscmobi.habittrackingandroid.presentation.ui.view

import android.os.Build
import android.view.View
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
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

    fun resetColorTask( color: Int?) {
        color?.let {
            calendarAdapter?.colorSelect = it

        }
    }

//    fun convertDateToLocalDate(date: Date): LocalDate? {
//    }

    fun setSelectDate(date: Date) {

        val calendar = Calendar.getInstance()
        calendar.time = date
        selectedDate =  LocalDate.of(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH)+1,calendar.get(Calendar.DAY_OF_MONTH))
        setMonthView()

    }

    fun getDateSelected(): Date? {

        if (dayDate != null) {
            val calendar = Calendar.getInstance()
            calendar.set(dayDate!!.year, dayDate!!.monthValue - 1, dayDate!!.dayOfMonth)
            return calendar.time
        }

        return  null

    }

    override fun initView(view: View) {
//        binding.vRoot.elevation = 0f
//        binding.vRoot.setBackgroundResource(R.drawable.bg_calender1)



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

            val calendarAdapter = CalendarAdapter(selectedDate)
            calendarAdapter.setListener(object : ItemWithPostionListener<CalenderData>{
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
            calendarAdapter.submitList(calenderData)
        }
        else {
            calendarAdapter?._currentDate = selectedDate
            calendarAdapter?.submitList(calenderData)
            calendarAdapter?.notifyDataSetChanged()
        }


    }

    private fun daysInMonthArray(date: LocalDate): ArrayList<String> {
        val daysInMonthArray = ArrayList<String>()
        val yearMonth: YearMonth = YearMonth.from(date)
        val daysInMonth: Int = yearMonth.lengthOfMonth()
        val firstOfMonth = selectedDate.withDayOfMonth(1)
        val dayOfWeek = firstOfMonth.dayOfWeek.value
        for (i in 1..35) {
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