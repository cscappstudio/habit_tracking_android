package com.thanhlv.fw.helper

import android.content.Context
import android.os.Build
import com.thanhlv.ads.lib.AdjustHelper
import com.thanhlv.ads.lib.AdsConfigs.Companion.ADJUST_RETENTION_DAY_1_TOKEN
import com.thanhlv.ads.lib.AdsConfigs.Companion.ADJUST_RETENTION_DAY_3_TOKEN
import com.thanhlv.ads.lib.AdsConfigs.Companion.ADJUST_RETENTION_DAY_7_TOKEN
import com.thanhlv.ads.lib.AdsConfigs.Companion.ADJUST_RETENTION_DAY_14_TOKEN
import com.thanhlv.ads.lib.AdsConfigs.Companion.ADJUST_RETENTION_DAY_20_TOKEN
import com.thanhlv.ads.lib.AdsConfigs.Companion.ADJUST_RETENTION_DAY_30_TOKEN
import com.thanhlv.fw.constant.AppConfigs
import com.thanhlv.fw.spf.SPF
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.*
import java.util.concurrent.TimeUnit

class AdjustTracking {
    companion object {

    fun logRetention(context: Context) {
        var days =
            TimeUnit.MILLISECONDS.toDays(System.currentTimeMillis() - SPF.getStartOpenTime(context))
                .toInt()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            try {
                val df = SimpleDateFormat("MMM/dd/yyyy", Locale.ENGLISH)
                val startDate: String = df.format(Date(SPF.getStartOpenTime(context)))
                val endDate: String = df.format(Date(System.currentTimeMillis()))
                val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("MMM/dd/yyyy")
                val start: LocalDate = LocalDate.parse(startDate, formatter)
                val end: LocalDate = LocalDate.parse(endDate, formatter)
                days = ChronoUnit.DAYS.between(end, start).toInt()
            } catch (_: Exception) {
            }
        }

        when (days) {
            1, 2 -> {
                if (!SPF.isRetention1D(context)) {
                    AdjustHelper.trackingEvent(ADJUST_RETENTION_DAY_1_TOKEN)
                    FirebaseHelper.getInstance(context)
                        .logEventToFirebase(AppConfigs.EVENT_RETENTION_DAY_1)
                    SPF.setRetention1D(context, true)
                }
            }
            in 3..6 -> {
                if (!SPF.isRetention3D(context)) {
                    AdjustHelper.trackingEvent(ADJUST_RETENTION_DAY_3_TOKEN)
                    FirebaseHelper.getInstance(context)
                        .logEventToFirebase(AppConfigs.EVENT_RETENTION_DAY_3)
                    SPF.setRetention3D(context, true)
                }
            }
            in 7..13 -> {
                if (!SPF.isRetention7D(context)) {
                    AdjustHelper.trackingEvent(ADJUST_RETENTION_DAY_7_TOKEN)
                    FirebaseHelper.getInstance(context)
                        .logEventToFirebase(AppConfigs.EVENT_RETENTION_DAY_7)
                    SPF.setRetention7D(context, true)
                }
            }
            in 14..19 -> {
                if (!SPF.isRetention14D(context)) {
                    AdjustHelper.trackingEvent(ADJUST_RETENTION_DAY_14_TOKEN)
                    FirebaseHelper.getInstance(context)
                        .logEventToFirebase(AppConfigs.EVENT_RETENTION_DAY_14)
                    SPF.setRetention14D(context, true)
                }
            }
            in 20..29 -> {
                if (!SPF.isRetention20D(context)) {
                    AdjustHelper.trackingEvent(ADJUST_RETENTION_DAY_20_TOKEN)
                    FirebaseHelper.getInstance(context)
                        .logEventToFirebase(AppConfigs.EVENT_RETENTION_DAY_20)
                    SPF.setRetention20D(context, true)
                }
            }
            in 30..1000 -> {
                if (!SPF.isRetention30D(context)) {
                    AdjustHelper.trackingEvent(ADJUST_RETENTION_DAY_30_TOKEN)
                    FirebaseHelper.getInstance(context)
                        .logEventToFirebase(AppConfigs.EVENT_RETENTION_DAY_30)
                    SPF.setRetention30D(context, true)
                }
            }
        }
    }

    }
}