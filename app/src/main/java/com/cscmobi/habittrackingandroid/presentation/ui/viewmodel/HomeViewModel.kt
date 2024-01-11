package com.cscmobi.habittrackingandroid.presentation.ui.viewmodel

import com.cscmobi.habittrackingandroid.base.BaseViewModel
import com.cscmobi.habittrackingandroid.data.repository.HomeRepository
import org.threeten.bp.DateTimeUtils
import org.threeten.bp.DayOfWeek
import org.threeten.bp.LocalDate;
import org.threeten.bp.format.DateTimeFormatter
import org.threeten.bp.temporal.WeekFields
import java.util.Date


class HomeViewModel(private val repository: HomeRepository): BaseViewModel() {
    var  listWeekData = arrayListOf<LocalDate>()
    var currentWeekPos = -1


    fun initDateWeek() {
        listWeekData.clear()

        val c = LocalDate.now()
        val weekRange = 12L
        for (i in -weekRange until weekRange) {
            val startOfWeek = c.plusWeeks(i).with(DayOfWeek.MONDAY) // Set to the first day of the week (Monday)
            listWeekData.add(startOfWeek)
        }

         currentWeekPos = listWeekData.indexOfFirst { it == c.with(DayOfWeek.MONDAY) }
        println("chaulq____________${currentWeekPos}")

    }



}