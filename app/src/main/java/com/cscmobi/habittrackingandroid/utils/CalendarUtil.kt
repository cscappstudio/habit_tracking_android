package com.cscmobi.habittrackingandroid.utils

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.Calendar

class CalendarUtil {
    companion object {
        fun dayOfYear(ms: Long): Int {
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = ms
            return calendar[Calendar.DAY_OF_YEAR]
        }

        fun dayOfWeek(ms: Long): Int {
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = ms
            return calendar[Calendar.DAY_OF_WEEK]
        }

        fun dayOfMonth(ms: Long): Int {
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = ms
            return calendar[Calendar.DAY_OF_MONTH]
        }

        fun totalDayOfMonth(ms: Long): Int {
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = ms
            return calendar.getActualMaximum(Calendar.DATE)
        }

        fun startWeek(ms: Long): Int {// start from monday
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = ms
            calendar[Calendar.DAY_OF_WEEK] = Calendar.MONDAY
            if (calendar.timeInMillis > ms) calendar[Calendar.DAY_OF_YEAR] -= 7
            return calendar[Calendar.DAY_OF_YEAR]
        }

        fun startWeekMs(ms: Long): Long { // start from monday
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = ms
            calendar[Calendar.DAY_OF_WEEK] = Calendar.MONDAY
            if (calendar.timeInMillis > ms) calendar[Calendar.DAY_OF_YEAR] -= 7
            return calendar.timeInMillis
        }

        fun nextDay(ms: Long): Long {// start from monday
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = ms
            calendar[Calendar.DAY_OF_YEAR] += 1
            return calendar.timeInMillis
        }


        fun startMonthMs(ms: Long): Long {
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = ms
            calendar[Calendar.DAY_OF_MONTH] = 1
            return calendar.timeInMillis
        }

        fun startYearMs(ms: Long): Long {
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = ms
            calendar[Calendar.DAY_OF_YEAR] = 1
            return calendar.timeInMillis
        }

        fun startMonth(ms: Long): Int {
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = ms
            calendar[Calendar.DAY_OF_MONTH] = 1
            return calendar[Calendar.DAY_OF_YEAR]
        }

        fun startYear(ms: Long): Int {
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = ms
            calendar[Calendar.DAY_OF_YEAR] = 1
            return calendar[Calendar.DAY_OF_YEAR]
        }

        fun nextWeek(ms: Long): Long {
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = ms
            val rest =
                if (calendar[Calendar.DAY_OF_WEEK] == 1) 1 else 9 - calendar[Calendar.DAY_OF_WEEK]
            calendar[Calendar.DAY_OF_YEAR] += rest
            return calendar.timeInMillis
        }

        fun previousWeek(ms: Long): Long {
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = ms
            val rest =
                if (calendar[Calendar.DAY_OF_WEEK] == 1) 1 else 9 - calendar[Calendar.DAY_OF_WEEK]
            calendar[Calendar.DAY_OF_YEAR] += (rest - 14)
            return calendar.timeInMillis
        }

        fun nextMonth(ms: Long): Long {
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = ms
            calendar[Calendar.MONTH] += 1
            calendar[Calendar.DAY_OF_MONTH] = 1
            return calendar.timeInMillis
        }

        fun previousMonth(ms: Long): Long {
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = ms
            calendar[Calendar.MONTH] -= 1
            calendar[Calendar.DAY_OF_MONTH] = 1
            return calendar.timeInMillis
        }


        fun nextYear(ms: Long): Long {
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = ms
            calendar[Calendar.YEAR] += 1
            calendar[Calendar.MONTH] = 0
            calendar[Calendar.DAY_OF_MONTH] = 1
            return calendar.timeInMillis
        }

        fun previousYear(ms: Long): Long {
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = ms
            calendar[Calendar.YEAR] -= 1
            calendar[Calendar.MONTH] = 0
            calendar[Calendar.DAY_OF_MONTH] = 1
            return calendar.timeInMillis
        }

        @SuppressLint("SimpleDateFormat")
        fun getTitleDayMonth(ms: Long): String {
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = ms
            return SimpleDateFormat("MMM d").format(calendar.time)
        }

        @SuppressLint("SimpleDateFormat")
        fun getTitleWeek(ms: Long): String {
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = startWeekMs(ms)
            val start = SimpleDateFormat("MMM d").format(calendar.time)
            calendar[Calendar.DAY_OF_YEAR] += 6
            val end = SimpleDateFormat("MMM d").format(calendar.time)
            return "$start - $end"
        }

        @SuppressLint("SimpleDateFormat")
        fun getTitleMonth(ms: Long): String {
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = ms
            return SimpleDateFormat("MMMM").format(calendar.time)
        }

        @SuppressLint("SimpleDateFormat")
        fun getTitleYear(ms: Long): String {
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = ms
            return SimpleDateFormat("yyyy").format(calendar.time)
        }


        fun getMonth(ms: Long): Int {
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = ms
            return calendar[Calendar.MONTH]
        }

        fun getYear(ms: Long): Int {
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = ms
            return calendar[Calendar.YEAR]
        }

        fun getDaysBetween(start: Long, end: Long): Int {

            if (start > end) return -1
            val startDay = Calendar.getInstance()
            startDay.timeInMillis = start

            val endDay = Calendar.getInstance()
            endDay.timeInMillis = end

            if (endDay[Calendar.YEAR] > startDay[Calendar.YEAR]) {
                val nhuan = if (startDay[Calendar.YEAR] % 4 == 0) 366 else 365
                return endDay[Calendar.DAY_OF_YEAR] + nhuan - startDay[Calendar.DAY_OF_YEAR]
            } else
                return endDay[Calendar.DAY_OF_YEAR] - startDay[Calendar.DAY_OF_YEAR]
        }

    }
}