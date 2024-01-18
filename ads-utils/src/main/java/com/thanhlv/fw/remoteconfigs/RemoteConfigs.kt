package com.thanhlv.fw.remoteconfigs

import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigValue
import com.google.gson.Gson
import com.thanhlv.fw.model.ConfigsGeneralModel
import com.thanhlv.fw.model.SubscriptionsModel
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class RemoteConfigs private constructor() {
    var config: FirebaseRemoteConfig? = null

    fun getConfigValue(key: String): FirebaseRemoteConfigValue {
        return this.config!!.getValue(key)
    }

    val appConfigs: ConfigsGeneralModel
        get() {
            val configsGeneralModel: ConfigsGeneralModel
            val gson = Gson()
            try {
                configsGeneralModel = gson.fromJson(
                    config!!.getString("android_KEY_APP_CONFIGS"),
                    ConfigsGeneralModel::class.java
                )
                if (configsGeneralModel != null) return configsGeneralModel
            } catch (e: Exception) {
                return ConfigsGeneralModel()
            }
            return ConfigsGeneralModel()
        }

    // Walk through the Array.
    val listSubs: ArrayList<SubscriptionsModel>
        get() {
            val subs: ArrayList<SubscriptionsModel> = ArrayList()
            var arr: JSONArray? = null
            val gson = Gson()
            try {
                val messages: String = appConfigs.subscription
                if (messages != null) arr = JSONArray(messages)
                if (arr != null) for (i in 0 until arr.length()) { // Walk through the Array.
                    val obj: JSONObject = arr.getJSONObject(i)
                    val sub: SubscriptionsModel =
                        gson.fromJson(obj.toString(), SubscriptionsModel::class.java)
                    subs.add(sub)
                }
            } catch (e: JSONException) {
                e.printStackTrace()
                return ArrayList()
            }
            return subs
        }

    companion object {
        var getInstance: RemoteConfigs? = null
        val instance: RemoteConfigs
            get() {
                if (getInstance == null) {
                    getInstance = RemoteConfigs()
                }
                return getInstance!!
            }
    }
}
