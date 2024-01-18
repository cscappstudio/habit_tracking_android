package com.cscmobi.habittrackingandroid.thanhlv.constant

class Constant {
    class Static {
        companion object {
            @JvmField
            var adsInitialized = false

            @JvmField
            var CURRENT_LANG = "en"

            @JvmField
            var accessTokenGSM = ""
            var KEY_SUBS_YEARLY = "com.cscmobi.rulercamera.yearly"
            var KEY_SUBS_YEARLY_SALE = "com.cscmobi.rulercamera.yearlysale"
            var KEY_SUBS_MONTHLY = "com.cscmobi.rulercamera.monthly"
            var KEY_SUBS_WEEKLY = "com.cscmobi.rulercamera.weekly"
            var KEY_SUBS_LIFETIME = "com.cscmobi.rulercamera.lifetime"

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
        }
    }
}