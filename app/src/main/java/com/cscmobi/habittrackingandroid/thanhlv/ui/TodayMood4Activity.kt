package com.cscmobi.habittrackingandroid.thanhlv.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.cscmobi.habittrackingandroid.databinding.ActivityDetailMoodBinding
import com.cscmobi.habittrackingandroid.databinding.ActivityMoodBinding
import com.cscmobi.habittrackingandroid.databinding.ActivityMoodToday3Binding
import com.cscmobi.habittrackingandroid.databinding.ActivityMoodTodayBinding
import com.cscmobi.habittrackingandroid.thanhlv.adapter.AllChallengeAdapter
import com.cscmobi.habittrackingandroid.thanhlv.adapter.FeelingTagAdapter
import com.cscmobi.habittrackingandroid.thanhlv.adapter.MoodRecordAdapter
import com.cscmobi.habittrackingandroid.thanhlv.adapter.PagerMonthCalendarAdapter
import com.cscmobi.habittrackingandroid.thanhlv.model.FeelingTagModel
import com.cscmobi.habittrackingandroid.thanhlv.model.MonthCalendarModel
import com.thanhlv.fw.helper.MyUtils
import com.thanhlv.fw.helper.RunUtils
import java.text.SimpleDateFormat
import java.util.*


class TodayMood4Activity : BaseActivity2() {
    private lateinit var binding: ActivityMoodToday3Binding
    override fun setupScreen() {
        binding = ActivityMoodToday3Binding.inflate(layoutInflater)
        setContentView(binding.root)
        MyUtils.configKeyboardBelowEditText(this)
    }

    override fun loadData() {
    }

    override fun initView() {
        binding.tvQuestion.text = "What’s making you feel great?"
        binding.bgNote.visibility = View.VISIBLE
        recyclerView()
    }

    override fun controllerView() {
        binding.btnBackHeader.setOnClickListener {
            onBackPressed()
        }
        binding.btnNext.setOnClickListener {
            startActivity(Intent(this, MoodActivity::class.java))
            finish()
        }
    }

    private var adapter: FeelingTagAdapter? = null
    private fun recyclerView() {
        mList = getData()
        adapter = FeelingTagAdapter(this)
        adapter?.updateData(mList)
        adapter?.setCallBack(object : FeelingTagAdapter.FeelingTagCallback {
            override fun onClickItem(pos: Int) {

                var sum = 0
                mList.forEach {
                    if (it.selected) sum++
                }
                if (sum == 0) {
                    binding.btnNext.backgroundTintList =
                        ColorStateList.valueOf(Color.parseColor("#B5B5B5"))
                    binding.btnNext.isEnabled = false
                } else {
                    binding.btnNext.backgroundTintList =
                        ColorStateList.valueOf(Color.parseColor("#54BA8F"))
                    binding.btnNext.isEnabled = true
                }

            }

        })
        binding.rcFeeling.layoutManager = GridLayoutManager(this, 3)
        binding.rcFeeling.adapter = adapter

    }

    private var mList = mutableListOf<FeelingTagModel>()
    private fun getData(): MutableList<FeelingTagModel> {
        val list = mutableListOf<FeelingTagModel>()
        list.add(FeelingTagModel("abc", false))
        list.add(FeelingTagModel("abc", false))
        list.add(FeelingTagModel("abc", false))
        list.add(FeelingTagModel("abc", false))
        list.add(FeelingTagModel("abc", false))
        list.add(FeelingTagModel("abc", false))
        list.add(FeelingTagModel("abc", false))
        list.add(FeelingTagModel("abc", false))
        list.add(FeelingTagModel("abc", false))
        list.add(FeelingTagModel("abc", false))
        list.add(FeelingTagModel("abc", false))
        list.add(FeelingTagModel("abc", false))
        list.add(FeelingTagModel("abc", false))
        return list
    }

}