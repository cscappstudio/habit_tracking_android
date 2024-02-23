package com.cscmobi.habittrackingandroid.data.model

import android.app.Activity
import com.cscmobi.habittrackingandroid.utils.Helper.getMySharedPreferences
import com.google.gson.Gson
import com.thanhlv.fw.spf.SPF

data class FreeIAP(
    var rewardTimes:Int = 0, // toi da xem reward ads 2 lan add them task
    var week: Int = -1,
    var isSkip: Boolean = false// 1 skip/tuan -> neu > 1 thi tao them bien skip de check
) {
    companion object {
        private val FREEIAP = "FREEIAP"

        fun fromJson(activity: Activity): FreeIAP {
            val json = activity.getMySharedPreferences().getString(FREEIAP,"")
            if (json.isNullOrEmpty()) return  FreeIAP()

            return Gson().fromJson(json,FreeIAP::class.java)
        }
    }
    fun toJson() = Gson().toJson(this)

    fun canSkip(activity: Activity) :Boolean {
        if (SPF.isProApp(activity)) return  true

        return  !isSkip
    }


    fun saveToPreference(activity: Activity) {
        activity.getMySharedPreferences().edit().putString(FREEIAP,toJson())
    }


}