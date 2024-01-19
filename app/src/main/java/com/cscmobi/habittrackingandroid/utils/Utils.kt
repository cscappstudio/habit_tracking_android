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
}


