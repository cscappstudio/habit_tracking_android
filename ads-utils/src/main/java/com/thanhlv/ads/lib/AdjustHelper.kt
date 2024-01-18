package com.thanhlv.ads.lib

import android.content.Context
import android.os.Bundle
import com.adjust.sdk.*
import com.android.billingclient.api.ProductDetails
import com.android.billingclient.api.Purchase
import com.google.android.gms.ads.AdValue
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.OnPaidEventListener
import com.google.android.gms.ads.appopen.AppOpenAd
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.firebase.analytics.FirebaseAnalytics
import com.thanhlv.ads.lib.AdsConfigs.Companion.ADJUST_BUY_LIFETIME_PACK
import com.thanhlv.ads.lib.AdsConfigs.Companion.ADJUST_BUY_MONTHLY_SUCCESS
import com.thanhlv.ads.lib.AdsConfigs.Companion.ADJUST_BUY_WEEKLY_SUCCESS
import com.thanhlv.ads.lib.AdsConfigs.Companion.ADJUST_BUY_YEARLY_PACK
import com.thanhlv.ads.lib.AdsConfigs.Companion.ADJUST_IN_APP_PURCHASE_TOKEN
import com.thanhlv.fw.helper.FirebaseHelper
import java.util.*

class AdjustHelper {
    companion object {
        fun trackingRevenueAdjust(ad: AppOpenAd?) {
            if (ad == null) return
            ad.onPaidEventListener = OnPaidEventListener { adValue: AdValue ->
                val loadedAdapterResponseInfo =
                    ad.responseInfo.loadedAdapterResponseInfo
                val adRevenue = AdjustAdRevenue(AdjustConfig.AD_REVENUE_ADMOB)
                adRevenue.setRevenue(adValue.valueMicros / 1000000.0, adValue.currencyCode)
                if (loadedAdapterResponseInfo != null) adRevenue.setAdRevenueNetwork(
                    loadedAdapterResponseInfo.adSourceName
                )
                Adjust.trackAdRevenue(adRevenue)
            }
        }

        fun trackingRevenueAdjust(ad: NativeAd?) {
            if (ad == null) return
            ad.setOnPaidEventListener(OnPaidEventListener { adValue: AdValue ->
                val loadedAdapterResponseInfo =ad.responseInfo?.loadedAdapterResponseInfo
                val adRevenue = AdjustAdRevenue(AdjustConfig.AD_REVENUE_ADMOB)
                adRevenue.setRevenue(adValue.valueMicros / 1000000.0, "USD")
                if (loadedAdapterResponseInfo != null) adRevenue.setAdRevenueNetwork(
                    loadedAdapterResponseInfo.adSourceName
                )
                Adjust.trackAdRevenue(adRevenue)
            })
        }

        fun trackingRevenueAdjust(ad: RewardedAd?) {
            if (ad == null) return
            ad.onPaidEventListener = OnPaidEventListener { adValue: AdValue ->
                val loadedAdapterResponseInfo =
                    Objects.requireNonNull(ad.responseInfo)
                        .loadedAdapterResponseInfo
                val adRevenue = AdjustAdRevenue(AdjustConfig.AD_REVENUE_ADMOB)
                adRevenue.setRevenue(adValue.valueMicros / 1000000.0, adValue.currencyCode)
                if (loadedAdapterResponseInfo != null) adRevenue.setAdRevenueNetwork(
                    loadedAdapterResponseInfo.adSourceName
                )
                Adjust.trackAdRevenue(adRevenue)
            }
        }

        fun trackingRevenueAdjust(ad: InterstitialAd?) {
            if (ad == null) return
            ad.onPaidEventListener = OnPaidEventListener { adValue: AdValue ->
                val loadedAdapterResponseInfo =
                    Objects.requireNonNull(ad.responseInfo).loadedAdapterResponseInfo
                val adRevenue = AdjustAdRevenue(AdjustConfig.AD_REVENUE_ADMOB)
                adRevenue.setRevenue(adValue.valueMicros / 1000000.0, adValue.currencyCode)
                if (loadedAdapterResponseInfo != null) adRevenue.setAdRevenueNetwork(
                    loadedAdapterResponseInfo.adSourceName
                )
                Adjust.trackAdRevenue(adRevenue)
            }
        }

        fun trackingRevenueAdjust(ad: AdView?) {
            if (ad == null) return
            ad.onPaidEventListener = OnPaidEventListener { adValue: AdValue ->
                val loadedAdapterResponseInfo =
                    Objects.requireNonNull(ad.responseInfo)?.loadedAdapterResponseInfo
                val adRevenue = AdjustAdRevenue(AdjustConfig.AD_REVENUE_ADMOB)
                adRevenue.setRevenue(adValue.valueMicros / 1000000.0, adValue.currencyCode)
                if (loadedAdapterResponseInfo != null) adRevenue.setAdRevenueNetwork(
                    loadedAdapterResponseInfo.adSourceName
                )
                Adjust.trackAdRevenue(adRevenue)
            }
        }

        fun trackingEvent(tokenEvent: String?, orderID: String?) {
            if (tokenEvent == null) return
            val adjustEvent = AdjustEvent(tokenEvent)
            adjustEvent.setOrderId(orderID)
            Adjust.trackEvent(adjustEvent)
        }

        fun trackingSubscription(purchase: Purchase?, productDetails: ProductDetails?) {
            if (productDetails == null || purchase == null) return
            val type = productDetails.productType
            var value: Long = 0
            var currencyCode = "USD"
            if (type == "inapp") {
                if (productDetails.oneTimePurchaseOfferDetails != null) {
                    value = productDetails.oneTimePurchaseOfferDetails!!.priceAmountMicros / 1000000
                    currencyCode = productDetails.oneTimePurchaseOfferDetails!!.priceCurrencyCode
                }
            }
            if (type == "subs") {
                if (productDetails.subscriptionOfferDetails != null) {
                    value =
                        productDetails.subscriptionOfferDetails!![0].pricingPhases.pricingPhaseList[0].priceAmountMicros / 1000000
                    currencyCode =
                        productDetails.subscriptionOfferDetails!![0].pricingPhases.pricingPhaseList[0].priceCurrencyCode
                }
            }
            val subscription = AdjustPlayStoreSubscription(
                value,
                currencyCode,
                purchase.products[0],
                purchase.orderId,
                purchase.signature,
                purchase.purchaseToken
            )
            subscription.purchaseTime = purchase.purchaseTime
            Adjust.trackPlayStoreSubscription(subscription)
            trackingEvent(ADJUST_IN_APP_PURCHASE_TOKEN)
            if (productDetails.productId.lowercase(Locale.getDefault()).contains("week"))
                trackingEvent(ADJUST_BUY_WEEKLY_SUCCESS, value, currencyCode)
            if (productDetails.productId.lowercase(Locale.getDefault()).contains("lifetime"))
                trackingEvent(ADJUST_BUY_LIFETIME_PACK, value, currencyCode)
            if (productDetails.productId.lowercase(Locale.getDefault()).contains("month"))
                trackingEvent(ADJUST_BUY_MONTHLY_SUCCESS, value, currencyCode)
            if (productDetails.productId.lowercase(Locale.getDefault()).contains("year"))
                trackingEvent(ADJUST_BUY_YEARLY_PACK, value, currencyCode)
            println("thanhlv trackingSubscription Adjust ----- " + productDetails.productId + " -- " + value)
        }

        fun trackingEvent(tokenEvent: String?, value: Long, currencyCode: String?) {
            if (tokenEvent == null) return
            val adjustEvent = AdjustEvent(tokenEvent)
            adjustEvent.setRevenue(value.toDouble(), currencyCode)
            Adjust.trackEvent(adjustEvent)
        }

        fun trackingEvent(tokenEvent: String?) {
            if (tokenEvent == null) return
            val adjustEvent = AdjustEvent(tokenEvent)
            Adjust.trackEvent(adjustEvent)
        }
    }


}