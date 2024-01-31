package com.cscmobi.habittrackingandroid.utils

import com.cscmobi.habittrackingandroid.R
import org.threeten.bp.LocalDate


object Helper {
    var currentDate = LocalDate.now()
    var colorTask = mutableListOf<Int>(R.color.blue,R.color.pink, R.color.orange, R.color.green, R.color.purple)
}