package com.thanhlv.fw.spf

import android.content.Context
import android.content.SharedPreferences

class SPF {
    companion object {

        private const val APP_SETTINGS = "CSCMOBI_APP_DOG_TRANSLATOR_SETTINGS"
        private const val SETTINGS_IS_PRO = "SETTINGS_IS_PRO"
        private const val SETTINGS_RATING_APP = "SETTINGS_RATING_APP"
        private const val SETTINGS_IS_FIRST_OPEN_APP = "SETTINGS_IS_FIRST_OPEN_APP"
        private const val LANGUAGE_APP = "LANGUAGE_APP"
        private const val CURRENT_SUB = "CURRENT_SUB"
        private const val SETTINGS_START_OPEN_TIME = "SETTINGS_START_OPEN_TIME"
        private const val SETTINGS_SHOW_AD_FULL = "SETTINGS_SHOW_AD_FULL"
        private const val SETTINGS_RETENTION_1_D = "SETTINGS_RETENTION_1_D"
        private const val SETTINGS_RETENTION_3_D = "SETTINGS_RETENTION_3_D"
        private const val SETTINGS_RETENTION_7_D = "SETTINGS_RETENTION_7_D"
        private const val SETTINGS_RETENTION_14_D = "SETTINGS_RETENTION_14_D"
        private const val SETTINGS_RETENTION_20_D = "SETTINGS_RETENTION_20_D"
        private const val SETTINGS_RETENTION_30_D = "SETTINGS_RETENTION_30_D"
        private fun getSharedPreferences(context: Context): SharedPreferences {
            return context.getSharedPreferences(APP_SETTINGS, Context.MODE_PRIVATE)
        }

        fun getCountShowAdFull(context: Context): Int {
            return getSharedPreferences(context).getInt(SETTINGS_SHOW_AD_FULL, 0)
        }

        fun setCountShowAdFull(context: Context, value: Int) {
            val editor = getSharedPreferences(context).edit()
            editor.putInt(SETTINGS_SHOW_AD_FULL, value)
            editor.apply()
        }

        fun isRetention1D(context: Context): Boolean {
            return getSharedPreferences(context).getBoolean(SETTINGS_RETENTION_1_D, false)
        }

        fun setRetention1D(context: Context, value: Boolean) {
            val editor = getSharedPreferences(context).edit()
            editor.putBoolean(SETTINGS_RETENTION_1_D, value)
            editor.apply()
        }

        fun isRetention3D(context: Context): Boolean {
            return getSharedPreferences(context).getBoolean(SETTINGS_RETENTION_3_D, false)
        }

        fun setRetention3D(context: Context, value: Boolean) {
            val editor = getSharedPreferences(context).edit()
            editor.putBoolean(SETTINGS_RETENTION_3_D, value)
            editor.apply()
        }

        fun isRetention7D(context: Context): Boolean {
            return getSharedPreferences(context).getBoolean(SETTINGS_RETENTION_7_D, false)
        }

        fun setRetention7D(context: Context, value: Boolean) {
            val editor = getSharedPreferences(context).edit()
            editor.putBoolean(SETTINGS_RETENTION_7_D, value)
            editor.apply()
        }

        fun isRetention14D(context: Context): Boolean {
            return getSharedPreferences(context).getBoolean(SETTINGS_RETENTION_14_D, false)
        }

        fun setRetention14D(context: Context, value: Boolean) {
            val editor = getSharedPreferences(context).edit()
            editor.putBoolean(SETTINGS_RETENTION_14_D, value)
            editor.apply()
        }

        fun isRetention20D(context: Context): Boolean {
            return getSharedPreferences(context).getBoolean(SETTINGS_RETENTION_20_D, false)
        }

        fun setRetention20D(context: Context, value: Boolean) {
            val editor = getSharedPreferences(context).edit()
            editor.putBoolean(SETTINGS_RETENTION_20_D, value)
            editor.apply()
        }

        fun isRetention30D(context: Context): Boolean {
            return getSharedPreferences(context).getBoolean(SETTINGS_RETENTION_30_D, false)
        }

        fun setRetention30D(context: Context, value: Boolean) {
            val editor = getSharedPreferences(context).edit()
            editor.putBoolean(SETTINGS_RETENTION_30_D, value)
            editor.apply()
        }

        fun getStartOpenTime(context: Context): Long {
            return getSharedPreferences(context).getLong(SETTINGS_START_OPEN_TIME, 0)
        }

        fun setStartOpenTime(context: Context, value: Long) {
            val editor = getSharedPreferences(context).edit()
            editor.putLong(SETTINGS_START_OPEN_TIME, value)
            editor.apply()
        }

        fun setRatingApp(context: Context, value: Boolean) {
            val editor = getSharedPreferences(context).edit()
            editor.putBoolean(SETTINGS_RATING_APP, value)
            editor.apply()
        }

        fun isRatingApp(context: Context): Boolean {
            return getSharedPreferences(context).getBoolean(SETTINGS_RATING_APP, false)
        }

        fun setProApp(context: Context, value: Boolean) {
            val editor = getSharedPreferences(context).edit()
            editor.putBoolean(SETTINGS_IS_PRO, value)
            editor.apply()
        }

        fun isProApp(context: Context): Boolean {
            return getSharedPreferences(context).getBoolean(SETTINGS_IS_PRO, false)
        }

        fun setFirstOpenApp(context: Context, value: Boolean) {
            val editor = getSharedPreferences(context).edit()
            editor.putBoolean(SETTINGS_IS_FIRST_OPEN_APP, value)
            editor.apply()
        }

        fun isFirstOpenApp(context: Context): Boolean {
            return getSharedPreferences(context).getBoolean(SETTINGS_IS_FIRST_OPEN_APP, true)
        }


        fun getLanguage(context: Context): String? {
            return getSharedPreferences(context).getString(LANGUAGE_APP, "en")
        }

        fun setLanguage(context: Context, value: String?) {
            val editor = getSharedPreferences(context).edit()
            editor.putString(LANGUAGE_APP, value)
            editor.apply()
        }

        fun getCurrentSub(context: Context): String? {
            return getSharedPreferences(context).getString(CURRENT_SUB, "")
        }

        fun setCurrentSub(context: Context, value: String) {
            val editor = getSharedPreferences(context).edit()
            editor.putString(CURRENT_SUB, value)
            editor.apply()
        }
    }

}
