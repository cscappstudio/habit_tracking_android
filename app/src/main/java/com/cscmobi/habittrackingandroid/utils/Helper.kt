package com.cscmobi.habittrackingandroid.utils

import android.util.Log
import com.cscmobi.habittrackingandroid.R
import com.cscmobi.habittrackingandroid.thanhlv.model.Task
import org.threeten.bp.LocalDate
import java.util.Calendar
import java.util.Date
import kotlin.math.abs


object Helper {
    var currentDate = LocalDate.now()
    var colorTask = mutableListOf<Int>(R.color.blue,R.color.pink, R.color.orange, R.color.green, R.color.purple)


    fun validateTask(task: Task, date: Long): Boolean {
        var isValid = true

        task.pauseDate?.let {
            var c = Calendar.getInstance()
            c.time = Date(it)
            c.add(Calendar.DAY_OF_MONTH, task.pause)

            if (c.time.time > date) {
                isValid = false
            }

            Log.d("isValid", "1 $isValid")
        }

        task.repeate?.let {
            if (it.isOn == true) {
                when (it.type) {
                    "daily" -> {

                        isValid =
                            abs(Utils.getDayofYear(date) - Utils.getDayofYear(task.startDate!!)) % it.frequency == 0


                    }

                    "monthly" -> {
                        var checkMonthValid =
                            abs(Utils.getMonth(date) - Utils.getMonth(task.startDate!!)) % it.frequency == 0
                        if (checkMonthValid) {

                            val dayValid = it.days?.find { Utils.getDayofMonth(date) == it }
                            isValid = (dayValid != null && dayValid != -1)
                        } else isValid = false

                    }

                    "weekly" -> {
                        var checkWeekValid =
                            abs(Utils.getWeek(date) - Utils.getWeek(task.startDate!!)) % it.frequency == 0
                        if (checkWeekValid) {
                            var dayValid = it.days?.find { it == Utils.getDayofWeek(date) }
                            isValid = dayValid != null && dayValid != -1


                        } else isValid = false


                    }
                }
                Log.d("isValid", "2 $isValid")

            }

        }

        val _date = getDateWithoutHour(date)
        if (getDateWithoutHour(task.startDate!!) > _date || (task.endDate!!.isOpen == true && task.endDate!!.endDate!! < _date)) {
            isValid = false
            Log.d("isValid", "3 $isValid")

        }

        if (isValid == false) Log.d("isvalid", task.name.toString())
        return isValid
    }

    fun getDateWithoutHour(date: Long): Long {
        val calendar = Calendar.getInstance()
        calendar.time = Date(date)
        // Set hour, minute, second, and millisecond to zero
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        return calendar.time.time
    }

}