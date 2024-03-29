package com.cscmobi.habittrackingandroid.presentation.ui.custom

import android.graphics.Color
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.InsetDrawable
import android.graphics.drawable.LayerDrawable


/**
 * A simple class for generating shadows for [ComplexView]
 *
 * @author BluRe
 */
class Shadow(
    private var spread: Int,
    private val opacity: Int,
    color: String,
    private val shape: Int,
    private val radius: FloatArray,
    /**
     * @return The positioning of the shadow
     * @since 1.1
     */
    val shadowPosition: Position
) {
    private val color: String
    private var shadow: LayerDrawable? = null

    /**
     * @param spread   Strength of shadow
     * @param opacity  Opacity of the shadow, from 0 to 255
     * @param color    String representation of the color of the shadow
     * @param shape    Shape of the shadow
     * @param radius   bottomLeftRadius x and y, bottomRightRadius x and y, topLeftRadius x and y, and topRightRadius x and y, all supplied as a float array.
     * @param position @since 1.1 (see [Position]) the position of the shadow
     */
    init {
        this.color = color.replace("#", "")
        init()
    }

    private fun init() {
        var hex = 0
        spread *= 14
        val gradientDrawables = arrayOfNulls<InsetDrawable>(
            spread
        )
        val padding = 1
        val center = shadowPosition == Position.CENTER
        val orientation = getOrientation(shadowPosition)
        var i = 0
        var step = 0
        while (i < spread) {
            val drawable = GradientDrawable()
            drawable.shape = shape
            drawable.gradientType = shape
            var str = Integer.toHexString(hex)
            if (hex < 16) {
                str = "0$str"
            }
            str += color
            val col = Color.parseColor("#$str")
            if (!center) {
                drawable.orientation = orientation
                val colors = intArrayOf(col, Color.parseColor("#00ffffff"))
                drawable.colors = colors
            } else {
                drawable.setColor(col)
            }
            drawable.cornerRadii = radius
            gradientDrawables[i] = InsetDrawable(drawable, padding, padding, padding, padding)
            if (step == spread / 14) {
                ++hex
                step = 0
            }
            ++step
            ++i
        }
        shadow = LayerDrawable(gradientDrawables)
        shadow!!.alpha = opacity
    }


    private fun getOrientation(position: Position): GradientDrawable.Orientation {
        var orientation = GradientDrawable.Orientation.TOP_BOTTOM
        when (position) {

            Position.BOTTOM -> orientation = GradientDrawable.Orientation.BOTTOM_TOP
            Position.LEFT -> orientation = GradientDrawable.Orientation.LEFT_RIGHT
            Position.RIGHT -> orientation = GradientDrawable.Orientation.RIGHT_LEFT
            else -> orientation = GradientDrawable.Orientation.TOP_BOTTOM

        }
        return orientation
    }

    fun getShadow(): Drawable? {
        return shadow
    }

    /**
     * Controls how this shadow is positioned to ComplexViews.
     */
    enum class Position {
        /**
         * Positions the shadow to the center
         * This is the default behaviour of ComplexView shadows.
         */
        CENTER,

        /**
         * Positions the shadow to the right
         */
        RIGHT,

        /**
         * Positions the shadow to the left
         */
        LEFT,

        /**
         * Positions the shadow to the top
         */
        TOP,

        /**
         * Positions the shadow to the bottom
         */
        BOTTOM
    }
}