package com.thanhlv.fw.constant

class AppConfigs {

    companion object {
        @JvmField
        var accessTokenGSM = ""
        var adFullShowTime = 0L

        //for subs
        var KEY_SUBS_WEEKLY = "com.cscmobi.dogtranslator.weekly"
        var KEY_SUBS_YEARLY = "com.cscmobi.dogtranslator.yearly"
        var KEY_SUBS_MONTHLY = "com.cscmobi.dogtranslator.monthly"
        var KEY_SUBS_LIFETIME = "com.cscmobi.dogtranslator.lifetime"

        //for event log
        const val EVENT_CLICK_PREMIUM = "android_click_premium"
        const val EVENT_CLICK_START_PLAN = "android_click_start_plan"
        const val EVENT_IN_APP_PURCHASE_CUSTOM = "android_in_app_purchase_custom"
        const val EVENT_WATCH_5_ADS_FULL = "android_show_ad_full_5"
        const val EVENT_WATCH_10_ADS_FULL = "android_show_ad_full_10"
        const val EVENT_RETENTION_DAY_1 = "android_retention_day_1"
        const val EVENT_RETENTION_DAY_3 = "android_retention_day_3"
        const val EVENT_RETENTION_DAY_7 = "android_retention_day_7"
        const val EVENT_RETENTION_DAY_14 = "android_retention_day_14"
        const val EVENT_RETENTION_DAY_20 = "android_retention_day_20"
        const val EVENT_RETENTION_DAY_30 = "android_retention_day_30"

        const val EVENT_PURCHASE_WEEKLY = "android_buy_weekly_success"
        const val EVENT_PURCHASE_MONTHLY = "android_buy_monthly_success"
        const val EVENT_PURCHASE_YEARLY = "android_buy_yearly_success"
        const val EVENT_PURCHASE_YEAR_FLASH = "android_buy_year_flash_success"
        const val EVENT_PURCHASE_LIFETIME = "android_buy_lifetime_success"
    }
}