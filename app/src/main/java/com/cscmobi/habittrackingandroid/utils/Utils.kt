package com.cscmobi.habittrackingandroid.utils

import android.content.Context
import android.content.Intent
import android.content.res.AssetManager
import android.content.res.ColorStateList
import android.content.res.Resources
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.StateListDrawable
import android.os.Build
import android.os.Bundle
import android.util.TypedValue
import androidx.annotation.ColorInt
import org.threeten.bp.LocalDate
import java.io.IOException
import java.io.InputStream
import java.io.Serializable
import java.util.Calendar


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
        focusedDrawable.setStroke(dpToPx(1), color) // Orange color
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

    fun loadImageFromAssets(context: Context, fileName: String): Drawable? {
        val assetManager: AssetManager = context.assets
        val inputStream: InputStream = assetManager.open(fileName)
        return Drawable.createFromStream(inputStream, null)
    }

    inline fun <reified T : Serializable> Bundle.serializable(key: String): T? = when {
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> getSerializable(key, T::class.java)
        else -> @Suppress("DEPRECATION") getSerializable(key) as? T
    }

    inline fun <reified T : Serializable> Intent.serializable(key: String): T? = when {
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> getSerializableExtra(
            key,
            T::class.java
        )

        else -> @Suppress("DEPRECATION") getSerializableExtra(key) as? T
    }


    fun isAssetImage(context: Context, imagePath: String?): Boolean {
        val assetManager = context.assets
        try {
            // Attempt to open the InputStream for the given asset
            val inputStream = assetManager.open(imagePath!!)

            // If successful, close the InputStream and return true
            if (inputStream != null) {
                inputStream.close()
                return true
            }
        } catch (e: IOException) {
            // If an exception occurs, the asset does not exist, return false
        }
        return false
    }

    fun LocalDate.toDate(): Long {
        val c = Calendar.getInstance()
        c.set(this.year, this.monthValue - 1, this.dayOfMonth)
        c.set(Calendar.HOUR_OF_DAY, 0)
        c.set(Calendar.MINUTE, 0)
        c.set(Calendar.SECOND, 0)
        c.set(Calendar.MILLISECOND, 0)

        return  c.time.time

    }
}


