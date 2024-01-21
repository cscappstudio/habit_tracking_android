package com.cscmobi.habittrackingandroid.presentation.ui.custom

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.view.View
import android.view.animation.Animation
import android.view.animation.Interpolator
import android.view.animation.ScaleAnimation
import android.widget.RelativeLayout
import com.cscmobi.habittrackingandroid.R
import java.util.Locale


/**
 * A view that can be manipulated to produce different UI effects
 *
 * @author BluRe
 */
class ComplexView : RelativeLayout, View.OnClickListener, Animation.AnimationListener {
    var amplitude = 0f
    private var pClick: OnClickListener? = null
    var frequency = 0f
    private var init = true
    private var animation: Animation? = null
    /**
     * Returns the duration of the animation set.
     */
    /**
     * Sets the duration of the default animation.
     *
     * @param animationDuration duration of animation in millis.
     */
    var animationDuration = 0
    var toXScale = 0f
    private var fromXScale = 0f
    var toYScale = 0f
    private var fromYScale = 0f
    private var pivotX = 0f
    private var pivotY = 0f
    /**
     * Returns color set when ComplexView is clicked
     */
    /**
     * Sets the color shown when a ComplexView is clicked.
     * Works only when an animation is set.
     *
     * @param color onclick color
     */
    var onclickColor = 0
    private var color = 0
    private val gd = GradientDrawable()
    private var rad = 0f
    var topRightRadius = 0f
    var topLeftRadius = 0f
    var bottomRightRadius = 0f

    /**
     * Sets the bottom left radius value of the ComplexView
     */
    var bottomLeftRadius = 0f
    private var shape = 0
    private val view: View? = null
    private var animate = false
    private val colors = IntArray(3)
    /**
     * Returns if ComplexView's parent can detect clicks made to its child.
     */
    /**
     * Sets if ComplexView can send clicks to its parent.
     * Set true if the parent has work to do.
     */
    var isClickTransferable = false
    /**
     * Returns if onclick is set to be performed before or after animation
     */
    /**
     * When set to true, animation completes before onclick is performed
     */
    var isClickAfterAnimation = false
    private val main: RelativeLayout? = null
    private var gradientType: String? = "linear"

    /**
     * @return returns the gradient angle set
     */
    var gradientAngle: String? = "TOP_BOTTOM"
        private set
    private var interpolate = false
    /**
     * Returns a boolean value indicating if ComplexView can be clicked as a parent view.
     * If false, only the child's clicks are detected.
     * please refer to [.setSelfClickable]
     */
    /**
     * @param selfClickable Sets if ComplexView as a parent can detect clicks.
     * This is usually set to true to detect clicks on visible areas of the view as a parent.
     * Visible areas may include padding or margin created by its child or by effects such as shadows.
     */
    var isSelfClickable = false
    private var fromChild = false
    var interpolator: Interpolator? = null
    private var first = true
    private var shadow: Shadow? = null
    private var currentShadow: Shadow? = null

    private var shadowPosition: Shadow.Position = Shadow.Position.CENTER

    constructor(context: Context?) : super(context) {
        background = gd
    }

    constructor(context: Context?, attrs: AttributeSet) : super(context, attrs) {
        init(attrs)
    }

    private fun init(set: AttributeSet) {
        val ta = context.obtainStyledAttributes(set, R.styleable.ComplexView)
        val shadow = ta.getBoolean(R.styleable.ComplexView_shadow, false)
        var shadowColor = ta.getString(R.styleable.ComplexView_shadowColor)
        if (shadowColor == null) {
            shadowColor = "000000"
        }
        isClickTransferable = ta.getBoolean(R.styleable.ComplexView_clickTransferable, false)
        isSelfClickable = ta.getBoolean(R.styleable.ComplexView_selfClickable, true)
        gradientAngle = ta.getString(R.styleable.ComplexView_gradientAngle)
        isClickAfterAnimation = ta.getBoolean(R.styleable.ComplexView_clickAfterAnimation, true)
        if (gradientAngle == null) gradientAngle = "TOP_BOTTOM"
        val gradientCenterColor = ta.getColor(R.styleable.ComplexView_gradientCenterColor, 0)
        val gradientEndColor = ta.getInteger(R.styleable.ComplexView_gradientEndColor, 0)
        val gradientStartColor = ta.getInt(R.styleable.ComplexView_gradientStartColor, 0)
        colors[0] = gradientStartColor
        colors[1] = gradientCenterColor
        colors[2] = gradientEndColor
        gradientType = ta.getString(R.styleable.ComplexView_gradientType)
        if (gradientType == null) {
            gradientType = "linear"
        }
        val spread = ta.getInt(R.styleable.ComplexView_shadowSpread, 1)
        val shadowAlpha = ta.getInt(R.styleable.ComplexView_shadowAlpha, 255)
        animationDuration = ta.getInteger(R.styleable.ComplexView_animationDuration, 2000)
        toXScale = ta.getFloat(R.styleable.ComplexView_toXScale, 1.0f)
        fromXScale = ta.getFloat(R.styleable.ComplexView_fromXScale, .3f)
        toYScale = ta.getFloat(R.styleable.ComplexView_toYScale, 1.0f)
        fromYScale = ta.getFloat(R.styleable.ComplexView_fromYScale, .3f)
        pivotX = ta.getFloat(R.styleable.ComplexView_pX, -1f)
        pivotY = ta.getFloat(R.styleable.ComplexView_pY, -1f)
        animate = ta.getBoolean(R.styleable.ComplexView_animate, false)
        rad = ta.getDimension(R.styleable.ComplexView_radius, 0f)
        topRightRadius = ta.getDimension(R.styleable.ComplexView_topRightRadius, 0f)
        topLeftRadius = ta.getDimension(R.styleable.ComplexView_topLeftRadius, 0f)
        bottomRightRadius = ta.getDimension(R.styleable.ComplexView_bottomRightRadius, 0f)
        bottomLeftRadius = ta.getDimension(R.styleable.ComplexView_bottomLeftRadius, 0f)
        color = ta.getColor(R.styleable.ComplexView_colorComplex, android.R.attr.colorBackground)
        onclickColor = ta.getColor(R.styleable.ComplexView_onclickColor, -1)
        interpolate = ta.getBoolean(R.styleable.ComplexView_interpolate, false)
        amplitude = ta.getFloat(R.styleable.ComplexView_amplitude, 1.0f)
        frequency = ta.getFloat(R.styleable.ComplexView_frequency, .3f)
        val sh = ta.getString(R.styleable.ComplexView_shape)
        viewTreeObserver.addOnGlobalLayoutListener {
            if (first && animate) {
                animation = ScaleAnimation(
                    fromXScale,
                    toXScale,
                    fromYScale,
                    toYScale,
                    if (pivotX == -1f) width.toFloat() / 2 else pivotX,
                    if (pivotY == -1f) height.toFloat() / 2 else pivotY
                )
                animation?.setDuration(animationDuration.toLong())
                animation?.setAnimationListener(this@ComplexView)
                if (interpolate) {
                    val interpolator: Interpolator =
                        DefaultInterpolator()
                    animation?.setInterpolator(interpolator)
                }
                first = false
            }
        }
        if (sh != null) {
            when (sh) {
                "oval" -> shape = GradientDrawable.OVAL
                "ring" -> shape = GradientDrawable.RING
                "line" -> shape = GradientDrawable.LINE
            }
        }
        val sPosition = ta.getString(R.styleable.ComplexView_shadowPosition)
        if (sPosition != null) {
            when (sPosition) {
                "top" -> shadowPosition = Shadow.Position.TOP
                "left" -> shadowPosition = Shadow.Position.LEFT
                "right" -> shadowPosition = Shadow.Position.RIGHT
                "bottom" -> shadowPosition = Shadow.Position.BOTTOM
            }
        }
        setShape(shape)
        if (!isEmpty(colors)) {
            gd.colors = colors
        } else {
            gd.setColor(color)
        }
        setGradientType(gradientType)
        setGradientAngle(gradientAngle!!)
        val initRad = floatArrayOf(rad, rad, rad, rad, rad, rad, rad, rad)
        val radius = floatArrayOf(
            topLeftRadius,
            topLeftRadius,
            topRightRadius,
            topRightRadius,
            bottomRightRadius,
            bottomRightRadius,
            bottomLeftRadius,
            bottomLeftRadius
        )
        val radToSet = if (rad == 0f) radius else initRad
        setCornerRadii(radToSet)
        currentShadow = Shadow(spread, shadowAlpha, shadowColor, shape, radToSet, shadowPosition)
        if (shadow) {
            this.shadow = Shadow(spread, shadowAlpha, shadowColor, shape, radToSet, shadowPosition)
            background = this.shadow?.getShadow()
        } else background = gd
        setOnClickListener(this)
        ta.recycle()
    }

    /**
     * Set the shape of ComplexView.
     *
     * @param shape Can be either GradientDrawable.OVAL, GradientDrawable.RECTANGLE, GradientDrawable.RING or GradientDrawable.LINE
     */
    fun setShape(shape: Int) {
        gd.shape = shape
    }

    fun setCurrentShadow(isSet: Boolean) {
        if (isSet) {
            shadow = currentShadow
            background = shadow?.getShadow()
        }
    }

    var radius: Float
        /**
         * Returns the radius of ComplexView
         */
        get() = rad
        /**
         * Sets the radius ComplexView
         *
         * @param radius Preferred radius
         */
        set(radius) {
            rad = radius
            gd.cornerRadius = radius
        }

    /**
     * @return The shadow Object applied to this ComplexView if any
     * @since 1.1
     */
    fun getShadow(): Shadow? {
        return shadow
    }

    /**
     * Converts ComplexView into a shadow object.
     * This is to be used as a parent to another view.
     *
     * @param shadow An initialized shadow object.
     */
    fun setShadow(shadow: Shadow) {
        background = shadow.getShadow()
    }

    fun setFromXScale(fromXScale: Float) {
        this.fromXScale = fromXScale
    }

    fun setFromYScale(fromYScale: Float) {
        this.fromYScale = fromYScale
    }

    /**
     * Sets gradient color of ComplexView
     */
    fun setGradientColor(colors: IntArray?) {
        gd.colors = colors
    }

    /**
     * Since our underlying technique of achieving curved edges is based on setting the background drawable resource to a gradient drawable,
     * we simply can not set another background during runtime.
     */
    override fun setBackgroundResource(resid: Int) {
        throw RuntimeException("setBackgroundResource not supported in ComplexView")
    }

    private fun isEmpty(ints: IntArray): Boolean {
        for (i in ints) {
            if (i != 0) return false
        }
        return true
    }

    fun setColor(color: Int) {
        gd.setColor(color)
    }

    /**
     * Sets the corner radius of ComplexView
     *
     * @param radii Must be of 4 float values comprising of topLeftRadius, topRightRadius, bottomLeftRadius, bottomRightRadius
     */
    fun setCornerRadii(radii: FloatArray?) {
        gd.cornerRadii = radii
    }

    fun setInterpolate(interpolate: Boolean) {
        this.interpolate = interpolate
    }

    override fun setOnClickListener(l: OnClickListener?) {
        if (init) {
            super.setOnClickListener(l)
            init = false
            return
        }
        pClick = l
    }

    override fun onClick(v: View) {
        if (!isSelfClickable && !fromChild) {
            return
        }
        val parent = parent
        if (parent is ComplexView && isClickTransferable) {
            val complexView = parent
            complexView.fromChild = true
            complexView.onClick(complexView)
        }
        if (onclickColor != -1) {
            gd.setColor(onclickColor)
        }
        if (animate) {
            if (!isClickAfterAnimation) {
                if (pClick != null) pClick!!.onClick(v)
            }
            startAnimation(animation)
            return
        }
        if (pClick != null) pClick!!.onClick(v)
        fromChild = false
    }

    fun startAnimation() {
        startAnimation(animation)
    }

    /**
     * We can not change background color due to our manipulations
     */
    override fun setBackgroundColor(color: Int) {
        throw RuntimeException("setBackgroundColor not supported!")
    }

    override fun onAnimationStart(animation: Animation) {}
    override fun onAnimationEnd(animation: Animation) {
        gd.setColor(color)
        if (isClickAfterAnimation && pClick != null) {
            pClick!!.onClick(view)
        }
    }

    override fun onAnimationRepeat(animation: Animation) {}
    fun getGradientType(): String? {
        return gradientType
    }

    /**
     * @param gradientType The gradient type to used.
     */
    fun setGradientType(gradientType: String?) {
        var gradient = GradientDrawable.LINEAR_GRADIENT
        when (gradientType) {
            "sweep" -> gradient = GradientDrawable.SWEEP_GRADIENT
            "radial" -> gradient = GradientDrawable.RADIAL_GRADIENT
        }
        this.gradientType = gradientType
        gd.gradientType = gradient
    }

    /**
     * @param gradientAngle sets the angle used by the gradient
     */
    private fun setGradientAngle(gradientAngle: String) {
        this.gradientAngle = gradientAngle
        gd.orientation =
            GradientDrawable.Orientation.valueOf(gradientAngle.uppercase(Locale.getDefault()))
    }

    /**
     * Default interpolator used if [.interpolate] is true
     */
    private inner class DefaultInterpolator : Interpolator {
        override fun getInterpolation(time: Float): Float {
            return (-1 * Math.pow(
                Math.E,
                (-time / amplitude).toDouble()
            ) * Math.cos((frequency * time).toDouble()) + 1).toFloat()
        }
    }
}