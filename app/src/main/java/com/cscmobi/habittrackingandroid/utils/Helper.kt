package com.cscmobi.habittrackingandroid.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import com.cscmobi.habittrackingandroid.R
import com.cscmobi.habittrackingandroid.data.model.FreeIAP
import com.cscmobi.habittrackingandroid.thanhlv.model.Task
import com.cscmobi.habittrackingandroid.utils.Utils.toDate
import com.elconfidencial.bubbleshowcase.BubbleShowCase
import com.elconfidencial.bubbleshowcase.BubbleShowCaseBuilder
import com.elconfidencial.bubbleshowcase.BubbleShowCaseListener
import org.threeten.bp.LocalDate
import java.util.Calendar
import java.util.Date
import kotlin.math.abs
import kotlin.math.roundToInt


object Helper {
    var isNewDay = false
    var chooseDate = 0L
    var isFirstLoadRewardAds = false

    var currentDate = LocalDate.now()
    var freeIAP = FreeIAP()


    var colorTask = mutableListOf<Int>(
        R.color.blue,
        R.color.pink,
        R.color.orange,
        R.color.green,
        R.color.purple
    )

    fun Activity.getMySharedPreferences(): SharedPreferences {
        return getSharedPreferences(
            "cf", Context.MODE_PRIVATE
        )
    }


    fun validateTask(task: Task, date: Long, isPauseValidate: Boolean = true): Boolean {
        var isValid = true

        if (isPauseValidate) {
            task.pauseDate?.let {
                if (task.pause != -1) {
                    val c = Calendar.getInstance()
                    c.time = Date(it)
                    c.add(Calendar.DAY_OF_MONTH, task.pause - 1)

                    if (date in it.toDate()..c.time.time) {
                        isValid = false
                    }
                } else isValid = false


                Log.d("isValid", "1 $isValid")
            }
        }


        task.repeate.let {
            if (it.isOn) {
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
            } else {
                isValid = CalendarUtil.getToDayMs() == CalendarUtil.startDayMs(date)
            }

        }

        val _date = getDateWithoutHour(date)

        if (task.startDate != null) {
            if (getDateWithoutHour(task.startDate!!) > _date) {
                isValid = false
                Log.d("isValid", "3 $isValid")

            } else isValid = true
        }

        if (task.endDate.isOpen == true && task.endDate.endDate != null && task.endDate.endDate!! < _date) {
            isValid = false
            Log.d("isValid", "3 $isValid")
        }


        return isValid
    }

    fun getDateWithoutHour(date: Long): Long {
        if (date == -1L) return -1
        val calendar = Calendar.getInstance()
        calendar.time = Date(date)
        // Set hour, minute, second, and millisecond to zero
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        return calendar.time.time
    }


    fun calTaskProgress(goal: Int, target: Int): Int {
        if (goal == 0 && target == 0) return 0
        var progressGoal = if (goal > target)
            target else goal

        return (progressGoal * 100f / target).roundToInt()
    }


    @SuppressLint("UseCompatLoadingForDrawables")
    fun Activity.createBubbleShowCaseBuilder(
        v: View,
        des: String,
        id: String
    ): BubbleShowCaseBuilder {
        return BubbleShowCaseBuilder(this) //Activity instance
            .description(des)
            .arrowPosition(BubbleShowCase.ArrowPosition.TOP)
            .targetView(v) //View to point out
            .highlightMode(BubbleShowCase.HighlightMode.VIEW_SURFACE)
            .backgroundColorResourceId(R.color.jade_black)
            .textColor(Color.WHITE) //Bubble Text color
            .descriptionTextSize(14) //Subtitle text size in SP (default value 14sp)
            .showOnce(id)
    }

    fun getCurrentWeek(): Int {
        var c = Calendar.getInstance()
        return c.get(Calendar.WEEK_OF_MONTH)
    }


}