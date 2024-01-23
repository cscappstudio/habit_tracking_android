package com.cscmobi.habittrackingandroid.utils

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.drawable.GradientDrawable
import android.util.TypedValue
import androidx.annotation.ColorInt

object Utils {

    fun dpToPx(dip: Float, context: Context): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, dip,
            context.resources.displayMetrics
        ).toInt()
    }

    fun pxTodip(px: Int, context: Context): Float {
        return px / context.resources.displayMetrics.density
    }


    fun createRoundedRectDrawable(
        @ColorInt strokeColor: Int,
        strokeWidth: Float,
        @ColorInt solidColor: Int,
        cornerRadius: Float
    ): GradientDrawable {
        val gradientDrawable = GradientDrawable()
        gradientDrawable.shape = GradientDrawable.RECTANGLE
        gradientDrawable.setStroke(strokeWidth.toInt(), strokeColor)
        gradientDrawable.color = ColorStateList.valueOf(solidColor)
        gradientDrawable.cornerRadius = cornerRadius
        return gradientDrawable
    }


    fun mapNumbersToDayNames(numbers: IntArray): Array<String> {

        val dayNames = arrayOf("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat")

        return numbers.map { dayNames[it - 1] }.toTypedArray()
    }


    fun addOrdinalSuffixToDays(numbers: IntArray): Array<String> {
        return numbers.map { day -> "$day${getOrdinalSuffix(day)}" }.toTypedArray()
    }

    fun getOrdinalSuffix(day: Int): String {
        return when {
            day in 11..13 -> "th" // Special case for 11th, 12th, 13th
            day % 10 == 1 -> "st"
            day % 10 == 2 -> "nd"
            day % 10 == 3 -> "rd"
            else -> "th"
        }
    }
}


