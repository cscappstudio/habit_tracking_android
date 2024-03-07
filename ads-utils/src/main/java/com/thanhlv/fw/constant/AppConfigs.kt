package com.thanhlv.fw.constant

class AppConfigs {

    companion object {
        @JvmField
        var adFullShowTime = 0L
        @JvmField
        var adsInitialized = false

        @JvmField
        var CURRENT_LANG = "en"

        @JvmField
        var accessTokenGSM = ""

        @JvmField
        var FLASH_SALE_TIME = 60 * 60 * 1000L

        @JvmField
        var FLASH_SALE_NOTIFY_TIME = 30 * 60 * 1000L
        const val SERVICE_ACTION_START = "action_start"

        const val HOME_ACTION_RESTART = "show_from_restart_app"
        const val HOME_ACTION_FROM_NOTI_PUSH = "show_from_noti_push"
        const val HOME_ACTION_FROM_NOTI_FCM = "show_from_noti_push_fcm"
        const val HOME_ACTION_UPGRADE_PREMIUM = "upgrade_premium"
        const val HOME_ACTION_UPDATEUI_BLACK_FRIDAY = "update_ui_black_friday"

        const val CHANGE_LANGUAGE = 1122

        //for Ads
        const val KEY_PASS_REVIEW_APP = "android_pass_review_app"
        const val KEY_AD_BANNER_PROTRACTOR = "android_banner_protractor"
        const val KEY_AD_NATIVE_ONBOARD = "android_native_onboard"
        const val KEY_AD_NATIVE_LANGUAGE = "android_native_language"
        const val KEY_AD_NATIVE_LIST_IMAGE = "android_native_list_image"
        const val KEY_AD_INTER_AR_RULER = "android_inter_ar_ruler"
        const val KEY_AD_INTER_PHYSICAL_RULER = "android_inter_physical_ruler"
        const val KEY_AD_INTER_SHOW_CAPTURE = "android_inter_show_capture"
        const val KEY_AD_OPEN_APP = "android_ad_open_app"
        const val KEY_AD_NATIVE_HOME = "android_native_home"
        const val KEY_AD_NATIVE_SETTINGS = "android_native_settings"
        const val KEY_AD_BANNER_HOME = "android_banner_home"
        const val KEY_AD_BANNER_DETAIL_CHALLENGE = "android_banner_detail_challenge"
        const val KEY_AD_BANNER_LANGUAGE = "android_banner_language"
        const val KEY_AD_BANNER_LOG_MOOD = "android_banner_log_mood"
        const val KEY_AD_BANNER_MOOD = "android_banner_mood"
        const val KEY_AD_BANNER_SELECT_TOOLS = "android_banner_select_tools"
        const val KEY_AD_BANNER_SELECT_UNIT = "android_banner_select_unit"
        const val KEY_AD_NATIVE_GRANT_PERMISSION = "android_native_grant_permission"

        //for subs
        var KEY_SUBS_WEEKLY = "com.cscmobi.habitgoals.weekly"
        var KEY_SUBS_YEARLY = "com.cscmobi.habitgoals.yearly"
        var KEY_SUBS_MONTHLY = "com.cscmobi.habitgoals.monthly"
        var KEY_SUBS_LIFETIME = "com.cscmobi.habitgoals.lifetime"

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

        const val EVENT_CLICK_ADD_PROJECT_FAB = "android_click_add_project_fab"
        const val EVENT_CLICK_QUICK_LINE = "android_click_quick_line"
        const val EVENT_CLICK_QUICK_RECTANGLE = "android_click_quick_rectangle"
        const val EVENT_CLICK_QUICK_CYLINDER = "android_click_quick_cylinder"
        const val EVENT_CLICK_QUICK_MORE = "android_click_quick_more"
        const val EVENT_CLICK_CANCEL_SUBS = "android_click_cancel_subs"

        const val EVENT_ACCEPT_CONSENT = "android_accept_consent"
        const val EVENT_DO_NOT_CONSENT = "android_do_not_consent"
    }

}