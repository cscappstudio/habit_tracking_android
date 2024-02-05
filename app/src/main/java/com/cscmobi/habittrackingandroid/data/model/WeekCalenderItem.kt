package com.cscmobi.habittrackingandroid.data.model

import org.threeten.bp.LocalDate

data class WeekCalenderItem(
    val day: String,
    val date: Int,
    var isSelected: Boolean = false,
    var progress:Int = 0,
    var localDate: LocalDate? = null
)
