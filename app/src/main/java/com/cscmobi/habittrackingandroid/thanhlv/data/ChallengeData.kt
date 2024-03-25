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
        var repeat: List<Int> = listOf(),
        var days: List<ChallengeDays> = listOf(),
        var tryCount: Int = 0
    )

    companion object {
        var mChallengeDefaultList = mutableListOf<ChallengeDefault>()
    }

    init {
        mChallengeDefaultList.clear()
        mChallengeDefaultList.addAll(getDefaultData())
        println("thanhlv ggggggggggggg " + mChallengeDefaultList[0])
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
            list.add(challenge)
        }


        return list
    }


}