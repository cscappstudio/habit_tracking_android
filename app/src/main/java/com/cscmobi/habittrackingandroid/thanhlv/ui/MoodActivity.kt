package com.cscmobi.habittrackingandroid.thanhlv.ui

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.cscmobi.habittrackingandroid.R
import com.cscmobi.habittrackingandroid.databinding.ActivityMoodBinding
import com.cscmobi.habittrackingandroid.thanhlv.adapter.MoodRecordAdapter
import com.cscmobi.habittrackingandroid.thanhlv.adapter.PagerMonthCalendarAdapter
import com.cscmobi.habittrackingandroid.thanhlv.database.AppDatabase
import com.cscmobi.habittrackingandroid.thanhlv.model.MonthCalendarModel
import com.cscmobi.habittrackingandroid.thanhlv.model.Mood
import com.google.gson.Gson
import com.thanhlv.ads.lib.AdMobUtils
import com.thanhlv.fw.constant.AppConfigs
import com.thanhlv.fw.helper.NetworkHelper
import com.thanhlv.fw.helper.RunUtils
import com.thanhlv.fw.remoteconfigs.RemoteConfigs
import com.thanhlv.fw.spf.SPF
import kotlinx.coroutines.runBlocking
import java.text.SimpleDateFormat
import java.util.*


class MoodActivity : BaseActivity2() {
    private lateinit var binding: ActivityMoodBinding
    private var pagerMonthAdapter: PagerMonthCalendarAdapter? = null

    companion object {
        var mAllMoods = listOf<Mood>()
    }

    override fun setupScreen() {
        binding = ActivityMoodBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun loadData() {
        mDataMonth = getDataForMonth()
    }

    override fun onResume() {
        super.onResume()
        runBlocking {
            mAllMoods = AppDatabase.getInstance(applicationContext).dao().getMood()
        }
        viewPager2()
    }

    override fun initView() {
        loadBanner()
    }

    override fun controllerView() {
        binding.btnBackHeader.setOnClickListener {
            onBackPressed()
        }

        binding.btnAddMood.setOnClickListener {
            startActivity(Intent(this, TodayMoodActivity::class.java))
        }

        binding.btnNextMonth.setOnClickListener {
            if (binding.vpMonth.currentItem < mDataMonth.size - 2) {
                binding.vpMonth.currentItem += 1
            }
        }
        if (mDataMonth.size == 1) {
            binding.btnNextMonth.alpha = 0.5f
            binding.btnPreviousMonth.alpha = 0.5f
        }
        binding.btnPreviousMonth.setOnClickListener {
            if (binding.vpMonth.currentItem > 0) {
                binding.vpMonth.currentItem -= 1
            }
        }
    }


    private fun viewPager2() {
        pagerMonthAdapter = PagerMonthCalendarAdapter(this)
        binding.vpMonth.adapter = pagerMonthAdapter
        pagerMonthAdapter!!.setList(mDataMonth)
        binding.vpMonth.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                updateTitleMonth(mDataMonth[position])
                showRecyclerView(mDataMonth[position])
                if (mDataMonth.size > 1 && position < mDataMonth.size - 1) {
                    binding.btnNextMonth.alpha = 1f
                    binding.btnNextMonth.isEnabled = true
                } else {
                    binding.btnNextMonth.alpha = 0.5f
                    binding.btnNextMonth.isEnabled = false
                }

                if (position > 0 && mDataMonth.size > 1) {
                    binding.btnPreviousMonth.alpha = 1f
                    binding.btnPreviousMonth.isEnabled = true
                } else {
                    binding.btnPreviousMonth.alpha = 0.5f
                    binding.btnPreviousMonth.isEnabled = false
                }
            }
        })
        binding.vpMonth.currentItem = mDataMonth.size - 1

    }

    private var moodRecordAdapter: MoodRecordAdapter? = null

    fun showRecyclerView(month: MonthCalendarModel) {
        val listMood = getMoodInMonth(month)

        if (listMood.isNullOrEmpty()) {
            binding.showListRecordMood.visibility = View.GONE
            return
        }
        binding.showListRecordMood.visibility = View.VISIBLE
        if (moodRecordAdapter == null) {
            moodRecordAdapter = MoodRecordAdapter(this)
            moodRecordAdapter?.setCallBack(object : MoodRecordAdapter.MoodRecordCallback {
                override fun onClickItem(mood: Mood) {
                    showDetailMood(mood)
                }

            })
            binding.rcMoodRecord.adapter = moodRecordAdapter
            binding.rcMoodRecord.layoutManager = LinearLayoutManager(this)
        }
        Handler().postDelayed({
            moodRecordAdapter?.setData(listMood)
        }, 200)

    }

    private fun showDetailMood(mood: Mood) {
        PopupDetailMood().newInstance(mood).show(supportFragmentManager, "")
    }

    private fun getMoodInMonth(month: MonthCalendarModel): ArrayList<Mood>? {
        val list = arrayListOf<Mood>()
        mAllMoods.forEach {
            val date = Calendar.getInstance()
            date.timeInMillis = it.date
            if (date[Calendar.MONTH] == month.month - 1 && date[Calendar.YEAR] == month.year) {
                list.add(it)
            }
        }
        list.sortByDescending { it.date }
        return list
    }

    @SuppressLint("SimpleDateFormat")
    private fun updateTitleMonth(monthYear: MonthCalendarModel?) {
        if (monthYear == null) return
        val calendar = Calendar.getInstance()
        calendar[Calendar.YEAR] = monthYear.year
        calendar[Calendar.MONTH] = monthYear.month - 1
        val monthYearString = SimpleDateFormat("MMMM yyyy").format(calendar.time)
        if (!isFinishing)
            RunUtils.runOnUI {
                binding.tvMonth.text = monthYearString.uppercase()
            }
    }

    private var mDataMonth = mutableListOf<MonthCalendarModel>()
    private fun getDataForMonth(): MutableList<MonthCalendarModel> {
        val list = mutableListOf<MonthCalendarModel>()

        val startTimeMs = SPF.getStartOpenTime(this)
        val currentTime = Calendar.getInstance()
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = startTimeMs

        while (calendar[Calendar.YEAR] <= currentTime[Calendar.YEAR]) {
            while (calendar[Calendar.MONTH] <= currentTime[Calendar.MONTH]) {
                list.add(
                    MonthCalendarModel(
                        calendar[Calendar.MONTH] + 1,
                        calendar[Calendar.YEAR],
                        1
                    )
                )
                calendar[Calendar.MONTH] += 1
                if (calendar[Calendar.MONTH] > 11) {
                    calendar[Calendar.MONTH] = 0
                    calendar[Calendar.YEAR] += 1
                }
            }
            calendar[Calendar.YEAR] += 1
        }

        return list
    }

    private fun loadBanner() {
        if (NetworkHelper.isNetworkAvailable(this@MoodActivity)
            && !SPF.isProApp(this@MoodActivity)
            && RemoteConfigs.instance.getConfigValue(AppConfigs.KEY_AD_BANNER_MOOD).asBoolean()
        ) {
            binding.bannerView.visibility = View.VISIBLE
            AdMobUtils.createBanner(
                this@MoodActivity,
                getString(R.string.admob_banner_collapse_id),
                AdMobUtils.BANNER_COLLAPSIBLE_BOTTOM,
                binding.bannerView,
                null
            )
        } else {
            binding.bannerView.visibility = View.GONE
        }
    }


}