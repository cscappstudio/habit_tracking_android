package com.cscmobi.habittrackingandroid.utils

import android.content.Context
import android.content.res.AssetManager
import android.content.res.ColorStateList
import android.content.res.Resources
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.StateListDrawable
import android.util.TypedValue
import androidx.annotation.ColorInt
import java.io.InputStream

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


    fun createCustomDrawable(color: Int): StateListDrawable {
        val drawable = StateListDrawable()

        // Focused state
        val focusedDrawable = GradientDrawable()
        focusedDrawable.shape = GradientDrawable.RECTANGLE
        focusedDrawable.setStroke(dpToPx(1),color) // Orange color
        focusedDrawable.color = ColorStateList.valueOf(Color.WHITE)
        focusedDrawable.cornerRadius = dpToPx(10).toFloat()

        // Unfocused state
        val unfocusedDrawable = GradientDrawable()
        unfocusedDrawable.shape = GradientDrawable.RECTANGLE
        unfocusedDrawable.color = ColorStateList.valueOf(Color.WHITE)
        unfocusedDrawable.cornerRadius = dpToPx(10).toFloat()

        // Add states to the StateListDrawable
        drawable.addState(intArrayOf(android.R.attr.state_focused), focusedDrawable)
        drawable.addState(intArrayOf(), unfocusedDrawable)

        return drawable
    }

    private fun dpToPx(dp: Int): Int {
        val density = Resources.getSystem().displayMetrics.density
        return (dp * density).toInt()
    }

    fun loadImageFromAssets(context: Context,fileName: String): Drawable? {
        val assetManager: AssetManager = context.assets
        val inputStream: InputStream = assetManager.open(fileName)
        return Drawable.createFromStream(inputStream, null)
    }
}


