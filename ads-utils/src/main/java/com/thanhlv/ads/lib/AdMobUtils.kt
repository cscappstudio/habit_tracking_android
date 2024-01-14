package com.thanhlv.ads.lib

import android.app.Activity
import android.content.Context
import android.content.res.Resources
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.View
import android.view.ViewGroup
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.ads.mediation.admob.AdMobAdapter
import com.google.android.gms.ads.*
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdOptions
import com.google.android.gms.ads.rewarded.RewardItem
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import com.thanhlv.ads.lib.AdjustHelper.Companion.trackingRevenueAdjust
import com.thanhlv.fw.spf.SPF

class AdMobUtils {
    companion object {
        private const val AD_BANNER_ID_DEV = "ca-app-pub-3940256099942544/6300978111"
        private const val AD_NATIVE_ID_DEV = "ca-app-pub-3940256099942544/2247696110"
        private const val AD_INTERSTITIAL_ID_DEV = "ca-app-pub-3940256099942544/1033173712"
        private const val AD_LOADING_TIMEOUT_MS = 15000 //ms

        const val BANNER_NORMAL = 0 // normal

        const val BANNER_INLINE = 1 //

        const val BANNER_COLLAPSIBLE_BOTTOM = 2 //

        const val BANNER_COLLAPSIBLE_TOP = 3 //

        // for BannerAd
        var isBannerAlready = false

        var mInterAd: InterstitialAd? = null
        var isLoadingInter = false
        fun interstitialAdAlready(context: Context?): Boolean {
            return context != null && mInterAd != null
        }

        var mNativeAd: NativeAd? = null

        var rewardedAd: RewardedAd? = null

        fun rewardAdAlready(mContext: Context?): Boolean {
            return mContext != null && rewardedAd != null
        }

        private fun isNetworkAvailable(context: Context): Boolean {
            val connectivityManager =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            return connectivityManager.activeNetworkInfo != null && connectivityManager.activeNetworkInfo!!
                .isConnected
        }

        fun createBanner(
            context: Context?,
            id: String,
            type: Int,
            adView: ViewGroup,
            loadBannerCallback: LoadAdCallback?
        ) {
            if (context == null) return
            println("thanhlv createBanner ----------------- banner ")
            var shimmer: ShimmerFrameLayout? = null
            for (i in 0 until adView.childCount) {
                if (adView.getChildAt(i) is ShimmerFrameLayout) {
                    shimmer = adView.getChildAt(i) as ShimmerFrameLayout
                    break
                }
            }
            if (!isNetworkAvailable(context)) {
                for (i in 0 until adView.childCount)
                    if (adView.getChildAt(i) is AdView) {
                        adView.removeView(adView.getChildAt(i))
                        break
                    }
                adView.visibility = View.GONE
            } else {
                adView.visibility = View.VISIBLE
                if (shimmer != null) {
                    shimmer.visibility = View.VISIBLE
                    shimmer.startShimmer()
                }
                isBannerAlready = false
                for (i in 0 until adView.childCount) {
                    if (adView.getChildAt(i) is AdView) {
                        isBannerAlready = true
                        loadBannerCallback?.onLoaded(adView.getChildAt(i) as AdView)
                        break
                    }
                }
                if (isBannerAlready) {
                    if (shimmer != null) shimmer.visibility = View.GONE
                    return
                }
                val mAdView = AdView(context)
                mAdView.setAdSize(getAdSize(context, type))
                val finalShimmer = shimmer
                mAdView.adListener = object : AdListener() {
                    override fun onAdOpened() {
                        super.onAdOpened()
                        println("thanhlv onAdOpened ----------------- banner " + mAdView + "--- " + mAdView.adSize + "--- " + mAdView.adSize!!.height)
                    }

                    override fun onAdLoaded() {
                        loadBannerCallback?.onLoaded(mAdView)
                        super.onAdLoaded()
                        isBannerAlready = true
                        if (finalShimmer != null) finalShimmer.visibility = View.GONE
                        trackingRevenueAdjust(mAdView)
                        println("thanhlv onAdLoaded ----------------- banner " + mAdView + "--- " + mAdView.adSize + "--- " + mAdView.adSize!!.height)
                    }

                    override fun onAdClosed() {
                        super.onAdClosed()
                        println("thanhlv onAdClosed ----------------- banner $mAdView")
                    }

                    override fun onAdImpression() {
                        super.onAdImpression()
                        println("thanhlv onAdImpression ----------------- banner $mAdView")
                    }

                    override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                        super.onAdFailedToLoad(loadAdError)
                        if (finalShimmer != null) finalShimmer.visibility = View.GONE
                       loadBannerCallback?.onLoadFailed()
                        println("thanhlv onAdFailedToLoad ----------------- banner " + loadAdError.responseInfo)
                    }
                }
                mAdView.adUnitId =
                    id.ifEmpty { AD_BANNER_ID_DEV }
                if (mAdView.parent != null) {
                    (mAdView.parent as ViewGroup).removeView(mAdView)
                }
                if (mAdView.parent == null) {
                    adView.addView(mAdView)
                    println("thanhlv add view show ----------------- banner ")
                }
                //requestAd
                val adRequest: AdRequest =
                    if (type == BANNER_COLLAPSIBLE_BOTTOM || type == BANNER_COLLAPSIBLE_TOP) {
                        val extras = Bundle()
                        extras.putString("collapsible", if (type == BANNER_COLLAPSIBLE_BOTTOM) "bottom" else "top")
                        AdRequest.Builder()
                            .addNetworkExtrasBundle(AdMobAdapter::class.java, extras)
                            .build()
                    } else AdRequest.Builder().build()
                mAdView.loadAd(adRequest)
            }
        }


        interface LoadAdCallback {
            fun onLoaded(ad: Any?)
            fun onLoadFailed()
        }

        private fun getAdSize(context: Context, type: Int): AdSize {
                val display = Resources.getSystem().displayMetrics
                val widthPixels = display.widthPixels.toFloat()
                val density = display.density
                val adWidth = (widthPixels / density).toInt()

            return when (type) {
//                BANNER_INLINE -> AdSize.getInlineAdaptiveBannerAdSize(adWidth, 50)
                BANNER_NORMAL -> AdSize.BANNER
                else -> AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(context, adWidth)
            }
        }


        fun createInterstitialAd(context: Context?, id: String, callback: LoadAdCallback?) {

            println("thanhlv createInterstitialAd " + context)
            if (context == null) {
                mInterAd = null
                return
            }
            if (isLoadingInter) return
            isLoadingInter = true
            val adRequest =
                AdRequest.Builder().setHttpTimeoutMillis(AD_LOADING_TIMEOUT_MS).build()
            InterstitialAd.load(context,
                id.ifEmpty { AD_INTERSTITIAL_ID_DEV },
                adRequest,
                object : InterstitialAdLoadCallback() {
                    override fun onAdLoaded(interstitialAd: InterstitialAd) {
                        isLoadingInter = false
                        mInterAd = interstitialAd
                        callback?.onLoaded(interstitialAd)
                        trackingRevenueAdjust(interstitialAd)

                        println("thanhlv createInterstitialAd  loadedddddd" + interstitialAd)
                    }

                    override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                        isLoadingInter = false
                        mInterAd = null
                        callback?.onLoadFailed()

                        println("thanhlv createInterstitialAd faillllllll" )
                    }
                })
        }

        fun showInterstitialAd(
            context: Context?,
            fullScreenContentCallback: FullScreenContentCallback?
        ) {
            if (context is Activity && mInterAd != null) {
                mInterAd?.show((context as Activity?)!!)
                mInterAd?.fullScreenContentCallback = fullScreenContentCallback
            }
        }


        fun createNativeAd(
            context: Context?,
            id: String,
            templateView: TemplateNativeView?,
            loadNativeAdCallback: LoadAdCallback?
        ) {
            if (context == null || templateView == null) return
            if (SPF.isProApp(context) || !isNetworkAvailable(context)) {
                templateView.preLoadView()
                templateView.visibility = View.GONE
                templateView.destroyNativeAd()
                loadNativeAdCallback?.onLoadFailed()
            } else {
                templateView.visibility = View.VISIBLE
                if (templateView.nativeAd == null) {
                    val adLoader =
                        AdLoader.Builder(context, id.ifEmpty { AD_NATIVE_ID_DEV })
                            .forNativeAd { nativeAd_: NativeAd ->
                                if (context is Activity && context.isDestroyed) {
                                    nativeAd_.destroy()
                                    return@forNativeAd
                                }
                                mNativeAd = nativeAd_
                                templateView.setNativeAd(nativeAd_)
                                trackingRevenueAdjust(nativeAd_)
                                println("thanhlv createNativeAd  loadedddddd --------" + nativeAd_)
                            }
                            .withAdListener(object : AdListener() {
                                override fun onAdImpression() {
                                    super.onAdImpression()
                                    if (loadNativeAdCallback != null && mNativeAd != null) loadNativeAdCallback.onLoaded(
                                        mNativeAd
                                    )
                                    println("thanhlv createNativeAd  onAdImpression --------")
                                }

                                override fun onAdFailedToLoad(adError: LoadAdError) {
                                    templateView.visibility = View.GONE
                                    loadNativeAdCallback?.onLoadFailed()
                                    println("thanhlv createNativeAd  onAdFailedToLoad --------" + adError)
                                }
                            })
                            .withNativeAdOptions(
                                NativeAdOptions.Builder()
                                    .setAdChoicesPlacement(NativeAdOptions.ADCHOICES_TOP_RIGHT)
                                    .build()
                            )
                            .build()
                    adLoader.loadAd(
                        AdRequest.Builder().setHttpTimeoutMillis(AD_LOADING_TIMEOUT_MS).build()
                    )
                }
            }
        }

        fun createRewardAds(mContext: Context, id: String?, loadRewardAdCallback: LoadAdCallback?) {
            RewardedAd.load(mContext, id!!,
                AdRequest.Builder().setHttpTimeoutMillis(AD_LOADING_TIMEOUT_MS).build(),
                object : RewardedAdLoadCallback() {
                    override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                        rewardedAd = null
                        loadRewardAdCallback?.onLoadFailed()
                    }

                    override fun onAdLoaded(ad: RewardedAd) {
                        rewardedAd = ad
                        loadRewardAdCallback?.onLoaded(ad)
                        trackingRevenueAdjust(ad)
                    }
                })
        }

        fun showRewardAds(
            mActivity: Activity?,
            fullScreenContentCallback: FullScreenContentCallback?
        ) {
            if (rewardedAd != null) {
                rewardedAd?.fullScreenContentCallback = fullScreenContentCallback
                rewardedAd?.show(mActivity!!) { rewardItem: RewardItem? -> }
            }
        }
    }

}