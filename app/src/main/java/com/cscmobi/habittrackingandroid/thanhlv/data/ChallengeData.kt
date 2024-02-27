package com.cscmobi.habittrackingandroid.thanhlv.data

import android.content.Context
import com.cscmobi.habittrackingandroid.data.model.ChallengeDays
import com.google.gson.Gson
import com.thanhlv.fw.helper.MyUtils.Companion.readJsonAsset
import org.json.JSONArray
import org.json.JSONObject


class ChallengeData(var mContext: Context) {

    data class ChallengeDefault(
        var name: String = "",
        var description: String = "",
        var image: String = "",
        var duration: Int = 7,
        var cycle: Int = 0,
        var repeat: List<Int>? = null,
        var days: List<ChallengeDays>? = null
    )

    companion object {
        var mChallengeDefaultList = mutableListOf<ChallengeDefault>()
    }

    init {
        mChallengeDefaultList.clear()
        mChallengeDefaultList.addAll(getDefaultData())
    }


    private fun getDefaultData(): List<ChallengeDefault> {
        val list = mutableListOf<ChallengeDefault>()
        val str = readJsonAsset(mContext, "challenge_default.json")
        val jsonObject = JSONObject(str)
        val challenges: JSONArray = jsonObject.getJSONArray("challenges")
        for (i in 0 until challenges.length()) {
            val item = JSONObject(challenges[i].toString())
            val challenge = Gson().fromJson(
                item.toString(),
                ChallengeDefault::class.java
            )
//            val tempTask = mutableListOf<ChallengeDays>()
//            val days = JSONObject(item.toString()).getJSONArray("days")
//            for (j in 0 until days.length()) {
//                val tasks: JSONArray = JSONObject(days[j].toString()).getJSONArray("tasks")
//                for (k in 0 until tasks.length()) {
//                    tempTask.add(
//                        Gson().fromJson(
//                            tasks[k].toString(),
//                            ChallengeDays::class.java
//                        )
//                    )
//                }
//            }
//            challenge.days = tempTask.toList()
            list.add(challenge)
        }


        return list
    }


}