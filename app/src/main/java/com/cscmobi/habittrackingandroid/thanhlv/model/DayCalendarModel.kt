package com.cscmobi.habittrackingandroid.thanhlv.model

import android.annotation.SuppressLint
import android.icu.util.Calendar
import android.os.Build
import java.text.SimpleDateFormat

data class DayCalendarModel(var date: String) {
    var memo: String = ""
    var type: Int = TYPE_DEFAULT

    companion object {
        const val TYPE_DEFAULT = -1
        const val TYPE_NONE = 0
        const val TYPE_MENSTRUATION_INFERTILE = 1
        const val TYPE_FERTILE = 2
        const val TYPE_INFERTILE = 3
    }

    @SuppressLint("SimpleDateFormat")
    fun getDay(): String {
        if (date.isNotEmpty()) {
            val mDate = SimpleDateFormat("dd/MM/yyyy").parse(date)
            val calendar = Calendar.getInstance()
            calendar.time = mDate
            return calendar.get(Calendar.DAY_OF_MONTH).toString()
        }
        return ""
    }

    @SuppressLint("SimpleDateFormat")
    fun getDateFormat(): String {
        if (date.isNotEmpty()) {
            val mDate = SimpleDateFormat("dd/MM/yyyy").parse(date)
            val calendar = Calendar.getInstance()
            calendar.time = mDate
            return SimpleDateFormat("dd/MM/yyyy").format(calendar.time)
        }
        return ""
    }
}