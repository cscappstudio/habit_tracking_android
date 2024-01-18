package com.thanhlv.fw.helper

import android.content.res.Resources
import android.util.DisplayMetrics
import android.util.TypedValue


class DisplayUtils {

    companion object {
        private val displayMetrics = Resources.getSystem().displayMetrics
        private fun getDisplayMetrics(): DisplayMetrics {
            return displayMetrics
        }

        fun getDeviceWidthDpi(): Float {
            return displayMetrics.widthPixels / (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT)
        }

        fun getDeviceHeightDpi(): Float {
            return displayMetrics.heightPixels / (displayMetrics.ydpi / DisplayMetrics.DENSITY_DEFAULT)
        }

        fun getDeviceWidthPx(): Int {
            return displayMetrics.widthPixels
        }

        fun getDeviceHeightPx(): Int {
            return displayMetrics.heightPixels
        }


        fun dpToPx(dp: Float): Int {
            return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, displayMetrics)
                .toInt()
        }

        fun dpToPx2(dp: Float): Float {
            return dp * (displayMetrics.ydpi / DisplayMetrics.DENSITY_DEFAULT)
        }

        fun dpToPx3(dp: Float): Float {
            return dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT)
        }


        fun pxToDp(px: Float): Float {
            return px / (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT)
        }

        fun pxToSp(px: Float): Float {
            return px / displayMetrics.scaledDensity
        }


        fun spToPx(sp: Float): Int {
            return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, displayMetrics).toInt()
        }

    }
}