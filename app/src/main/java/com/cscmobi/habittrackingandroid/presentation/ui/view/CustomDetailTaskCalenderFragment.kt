package com.cscmobi.habittrackingandroid.presentation.ui.view

import android.content.res.ColorStateList
import android.graphics.Color
import android.util.Log
import android.view.View
import android.view.ViewTreeObserver
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cscmobi.habittrackingandroid.base.BaseFragment
import com.cscmobi.habittrackingandroid.data.model.DataTaskHistory
import com.cscmobi.habittrackingandroid.databinding.CalenderCustomBinding
import com.cscmobi.habittrackingandroid.presentation.ui.adapter.DetailData
import com.cscmobi.habittrackingandroid.presentation.ui.adapter.DetailTaskCalenderAdapter
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.threeten.bp.LocalDate
import org.threeten.bp.YearMonth
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import kotlin.math.max

class CustomDetailTaskCalenderFragment :
    BaseFragment<CalenderCustomBinding>(CalenderCustomBinding::inflate) {

    private lateinit var selectedDate: Calendar
    private var calenderData = arrayListOf<DetailData>()
    private var calendarAdapter: DetailTaskCalenderAdapter? = null
    private var dataTaskHistorys = mutableListOf<DataTaskHistory>()
    val dateMapByYear = mutableMapOf<Int, MutableList<DataDate>>()
    var minYear = 0
    var maxYear = 0
    private var height = 0
    private var mColor = "#B6D6DD"

    fun setData(listDataTaskHistory: List<DataTaskHistory>) {
        dataTaskHistorys = listDataTaskHistory.toMutableList()
        setRangeYear()
        getYear()
        setMonthView()
    }

    fun setColor(color: String) {
        mColor = color
    }

    override fun initView(view: View) {
        binding.calendarRecyclerView.viewTreeObserver.addOnGlobalLayoutListener(object :
            ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {

                height = binding.calendarRecyclerView.height
                binding.calendarRecyclerView.viewTreeObserver.removeOnGlobalLayoutListener(this)
                binding.calendarRecyclerView.layoutParams.height = height

            }
        })
        selectedDate = Calendar.getInstance()
        setMonthView()

        binding.monthYearTV.setTextColor(Color.parseColor(mColor))
        binding.ivPrevios.imageTintList = ColorStateList.valueOf(Color.parseColor(mColor))
        binding.ivNext.imageTintList = ColorStateList.valueOf(Color.parseColor(mColor))
    }

    override fun onStart() {
        super.onStart()


    }

    override fun onResume() {
        super.onResume()

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

        calenderData = daysInMonth.map { DetailData(it) } as ArrayList<DetailData>

        val currentYear = selectedDate.get(Calendar.YEAR)

        if (currentYear < minYear) {
            calenderData.forEach {
                it.progress = -2
                it.textDateState = false
            }
        } else if (currentYear > maxYear) {
            calenderData.forEach {
                it.progress = -2
                it.textDateState = true
            }
        } else {
            dateMapByYear[currentYear]?.forEach { data ->
                val checkMonth = selectedDate.get(Calendar.MONTH) == data.month
                if (checkMonth) {

                    calenderData.forEach {
//                        it.progress = -1
                        if (it.day.isNotEmpty() && data.day == it.day.toInt()) {
                            it.progress = data.progress
                        }

                    }
                }

            }

        }


        if (calendarAdapter == null) {
            val layoutManager: RecyclerView.LayoutManager =
                GridLayoutManager(requireContext(), 7)
            binding.calendarRecyclerView.layoutManager = layoutManager

            calendarAdapter = DetailTaskCalenderAdapter()

            binding.calendarRecyclerView.adapter = calendarAdapter
            calendarAdapter?.submitList(calenderData)
        } else {
            calendarAdapter?.submitList(calenderData)
            calendarAdapter?.notifyDataSetChanged()
        }

    }

//    private fun daysInMonthArray(calendar: Calendar): ArrayList<String> {
//        val daysInMonthArray = ArrayList<String>()
//        val year = calendar.get(Calendar.YEAR)
//        val month = calendar.get(Calendar.MONTH) + 1 // Calendar months are zero-based
//        val daysInMonth: Int = YearMonth.of(year, month).lengthOfMonth()
//        val firstOfMonth = calendar.apply { set(Calendar.DAY_OF_MONTH, 1) }
//        val dayOfWeek = firstOfMonth.get(Calendar.DAY_OF_WEEK)
//
//        for (i in 1..35) {
//            if (i <= dayOfWeek || i > daysInMonth + dayOfWeek) {
//                daysInMonthArray.add("")
//            } else {
//                daysInMonthArray.add((i - dayOfWeek).toString())
//            }
//        }
//
//        return daysInMonthArray
//    }

    private fun daysInMonthArray(calendar: Calendar): ArrayList<String> {
        val daysInMonthArray = ArrayList<String>()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH) + 1 // Calendar months are zero-based
        val daysInMonth: Int = YearMonth.of(year, month).lengthOfMonth()
        val firstOfMonth = LocalDate.of(year, month, 1)
        val dayOfWeek = firstOfMonth.dayOfWeek.value

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


    private fun monthYearFromDate(calendar: Calendar): String? {
        val formatter = SimpleDateFormat("MMMM yyyy")
        return formatter.format(calendar.time)
    }


    fun previousMonthAction(view: View?) {
        selectedDate.add(Calendar.MONTH, -1)
        setMonthView()
    }

    fun nextMonthAction(view: View?) {
        selectedDate.add(Calendar.MONTH, 1)
        setMonthView()
    }

    fun setRangeYear() {
        var c = Calendar.getInstance()
        c.time = Date(dataTaskHistorys[0].date)
        minYear = c.get(Calendar.YEAR)

        c.time = Date(dataTaskHistorys.last().date)
        maxYear = c.get(Calendar.YEAR)
    }


    fun getYear() {


        dateMapByYear.clear()

        dataTaskHistorys.forEach {
            val calendar = Calendar.getInstance()
            calendar.time = Date(it.date)
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)
            if (!dateMapByYear.containsKey(year)) {
                dateMapByYear[year] = mutableListOf()
            }
            dateMapByYear[year]?.add(DataDate(year, month, day, it.taskInDay.progress))
        }
    }

    fun areDatesEqual(date1: Calendar, date2: Calendar): Boolean {
        return date1.get(Calendar.YEAR) == date2.get(Calendar.YEAR) &&
                date1.get(Calendar.MONTH) == date2.get(Calendar.MONTH) &&
                date1.get(Calendar.DAY_OF_MONTH) == date2.get(Calendar.DAY_OF_MONTH)
    }

    data class DataDate(
        val year: Int,
        val month: Int,
        val day: Int,
        val progress: Int
    )
}