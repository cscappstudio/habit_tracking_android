package com.thanhlv.ads.lib

import android.content.Context
import android.graphics.drawable.Drawable
import android.text.TextUtils
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.android.gms.ads.nativead.AdChoicesView
import com.google.android.gms.ads.nativead.MediaView
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdView

class TemplateNativeView : FrameLayout {
    private var styles: NativeTemplateStyle? = null

    @JvmField
    var nativeAd: NativeAd? = null
    private var nativeAdView: NativeAdView? = null
    private var primaryView: TextView? = null
    private var tertiaryView: TextView? = null
    private var iconView: ImageView? = null
    private var mediaView: MediaView? = null
    private var callToActionView: Button? = null
    private var adChoicesView: AdChoicesView? = null
    private var background: ConstraintLayout? = null

    constructor(context: Context?) : super(context!!) {}
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initView(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        initView(context, attrs)
    }

    constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes) {
        initView(context, attrs)
    }

    fun setStyles(styles: NativeTemplateStyle?) {
        this.styles = styles
        applyStyles()
    }

    private fun applyStyles() {
        val mainBackground: Drawable? = styles!!.mainBackgroundColor
        if (mainBackground != null) {
            background!!.background = mainBackground
            if (primaryView != null) primaryView!!.background = mainBackground
            if (tertiaryView != null) tertiaryView!!.background = mainBackground
        }
        val primary = styles!!.primaryTextTypeface
        if (primary != null && primaryView != null) primaryView!!.typeface = primary
        val tertiary = styles!!.tertiaryTextTypeface
        if (tertiary != null && tertiaryView != null) tertiaryView!!.typeface = tertiary
        val ctaTypeface = styles!!.callToActionTextTypeface
        if (ctaTypeface != null && callToActionView != null) callToActionView!!.typeface =
            ctaTypeface
        if (styles!!.primaryTextTypefaceColor != null && primaryView != null) primaryView!!.setTextColor(
            styles!!.primaryTextTypefaceColor!!
        )
        if (styles!!.tertiaryTextTypefaceColor != null && tertiaryView != null) tertiaryView!!.setTextColor(
            styles!!.tertiaryTextTypefaceColor!!
        )
        if (styles!!.callToActionTypefaceColor != null && callToActionView != null) callToActionView!!.setTextColor(
            styles!!.callToActionTypefaceColor!!
        )
        val ctaTextSize = styles!!.callToActionTextSize
        if (ctaTextSize > 0 && callToActionView != null) callToActionView!!.textSize = ctaTextSize
        val primaryTextSize = styles!!.primaryTextSize
        if (primaryTextSize > 0 && primaryView != null) primaryView!!.textSize = primaryTextSize
        val tertiaryTextSize = styles!!.tertiaryTextSize
        if (tertiaryTextSize > 0 && tertiaryView != null) tertiaryView!!.textSize = tertiaryTextSize
        val ctaBackground: Drawable? = styles!!.callToActionBackgroundColor
        if (ctaBackground != null && callToActionView != null) callToActionView!!.background =
            ctaBackground
        val primaryBackground: Drawable? = styles!!.primaryTextBackgroundColor
        if (primaryBackground != null && primaryView != null) primaryView!!.background =
            primaryBackground
        val tertiaryBackground: Drawable? = styles!!.tertiaryTextBackgroundColor
        if (tertiaryBackground != null && tertiaryView != null) tertiaryView!!.background =
            tertiaryBackground
        invalidate()
        requestLayout()
    }

    fun setNativeAd(nativeAd: NativeAd) {
        this.nativeAd = nativeAd
        var headline = nativeAd.headline
        var body = nativeAd.body
        val cta = nativeAd.callToAction
        val icon = nativeAd.icon
        if (headline != null && !TextUtils.isEmpty(headline) && headline?.length!! > 25) {
            var sub = headline?.substring(0, 25)
            val endSpace = sub?.lastIndexOf(" ")
            if (endSpace!! > 22) {
                headline = sub?.substring(0, endSpace)
                val end = headline?.lastIndexOf(" ")
                if (end != null && end >= 0)
                    sub = headline?.substring(0, headline.lastIndexOf(" "))
                headline = "$sub..."
            } else if (endSpace > -1) {
                headline = sub?.substring(0, endSpace)
                headline = "$headline..."
            } else {
                headline = sub?.substring(0, 23) + "..."
            }
        }
        if (body != null && !TextUtils.isEmpty(body) && body.length > 90) {
            var sub = body.substring(0, 90)
            val endSpace = sub.lastIndexOf(" ")
            if (endSpace > 87) {
                body = sub.substring(0, endSpace)
                sub = body.substring(0, body.lastIndexOf(" "))
                body = "$sub..."
            } else if (endSpace > -1) {
                body = sub.substring(0, endSpace)
                body = "$body..."
            } else {
                body = sub.substring(0, 88) + "..."
            }
        }
        nativeAdView!!.callToActionView = callToActionView
        nativeAdView!!.adChoicesView = adChoicesView
        nativeAdView!!.headlineView = primaryView
        nativeAdView!!.mediaView = mediaView
        primaryView!!.text = headline
        callToActionView!!.text = cta
        if (icon != null) {
            iconView!!.visibility = VISIBLE
            iconView!!.setImageDrawable(icon.drawable)
        } else {
            iconView!!.visibility = GONE
        }
        if (tertiaryView != null) {
            tertiaryView!!.text = body
            nativeAdView!!.bodyView = tertiaryView
        }
        nativeAdView!!.setNativeAd(nativeAd)
        shimmer!!.stopShimmer()
        shimmer!!.visibility = GONE
        background!!.visibility = VISIBLE
    }

    /**
     * To prevent memory leaks, make sure to destroy your ad when you don't need it anymore. This
     * method does not destroy the template view.
     * [...](https://developers.google.com/admob/android/native-unified#destroy_ad)
     */
    fun destroyNativeAd() {
        if (nativeAd != null) {
            nativeAd!!.destroy()
            nativeAd = null
        }
    }

    private fun initView(context: Context, attributeSet: AttributeSet?) {
        val attributes =
            context.theme.obtainStyledAttributes(attributeSet, R.styleable.TemplateView, 0, 0)
        val templateType: Int
        templateType = try {
            attributes.getResourceId(
                R.styleable.TemplateView_gnt_template_type,
                R.layout.gnt_medium_template_view
            )
        } finally {
            attributes.recycle()
        }
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        inflater.inflate(templateType, this)
    }

    private var shimmer: ShimmerFrameLayout? = null
    fun preLoadView() {
        background!!.visibility = GONE
        shimmer!!.visibility = VISIBLE
        shimmer!!.startShimmer()
    }

    public override fun onFinishInflate() {
        super.onFinishInflate()
        nativeAdView = findViewById(R.id.native_ad_view)
        primaryView = findViewById(R.id.primary)
        tertiaryView = findViewById(R.id.body)
        callToActionView = findViewById(R.id.cta)
        iconView = findViewById(R.id.icon)
        mediaView = findViewById(R.id.media_view)
        background = findViewById(R.id.background)
        adChoicesView = findViewById(R.id.ad_choice_view)
        shimmer = findViewById(R.id.shimmer_view)
        preLoadView()
    }
}