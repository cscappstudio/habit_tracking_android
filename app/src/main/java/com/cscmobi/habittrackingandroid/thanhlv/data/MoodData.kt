package com.cscmobi.habittrackingandroid.thanhlv.data

import android.content.Context
import com.cscmobi.habittrackingandroid.thanhlv.model.FeelingTagModel
import com.thanhlv.fw.helper.MyUtils.Companion.readJsonAsset
import org.json.JSONArray
import org.json.JSONObject


class MoodData(var mContext: Context) {

    companion object {
        var mDescribeList = mutableListOf<FeelingTagModel>()
    }

    init {
        mDescribeList.clear()
        mDescribeList.addAll(getMoodData())
    }

    private fun getMoodData(): List<FeelingTagModel> {
        val list = mutableListOf<FeelingTagModel>()
        val str = readJsonAsset(mContext,"mood_default.json")
        val jsonObject = JSONObject(str)
        val describeList: JSONArray = jsonObject.getJSONArray("describe")
        for (i in 0 until describeList.length()) {
            val moodItem = JSONObject(describeList[i].toString())
            for (key in moodItem.keys()) {
                val subList = moodItem.getJSONArray(key)
                for (j in 0 until subList.length()) {
                    list.add(FeelingTagModel(key, subList[j].toString()))
                }
            }
        }

        val becauseOf: JSONArray = jsonObject.getJSONArray("because_of")
        for (i in 0 until becauseOf.length()) {
            list.add(FeelingTagModel("because_of", becauseOf[i].toString()))
        }

        return list
    }


}