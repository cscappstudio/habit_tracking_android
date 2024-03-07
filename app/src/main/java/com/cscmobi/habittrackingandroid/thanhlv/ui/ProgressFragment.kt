package com.cscmobi.habittrackingandroid.thanhlv.ui

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.graphics.Color
import android.view.View
import android.view.animation.AnimationUtils
import androidx.lifecycle.MutableLiveData
import androidx.viewpager2.widget.ViewPager2
import com.cscmobi.habittrackingandroid.BuildConfig
import com.cscmobi.habittrackingandroid.R
import com.cscmobi.habittrackingandroid.base.BaseFragment
import com.cscmobi.habittrackingandroid.databinding.FragmentProgressBinding
import com.cscmobi.habittrackingandroid.thanhlv.adapter.PagerMonthCalendarAdapter
import com.cscmobi.habittrackingandroid.thanhlv.adapter.PagerYearCalendarAdapter
import com.cscmobi.habittrackingandroid.thanhlv.database.AppDatabase
import com.cscmobi.habittrackingandroid.thanhlv.model.History
import com.cscmobi.habittrackingandroid.thanhlv.model.MonthCalendarModel
import com.cscmobi.habittrackingandroid.thanhlv.model.Task
import com.cscmobi.habittrackingandroid.utils.CalendarUtil
import com.cscmobi.habittrackingandroid.utils.ChartUtil.Companion.categoriesMonthLabelAxisX
import com.cscmobi.habittrackingandroid.utils.ChartUtil.Companion.categoriesWeekLabelAxisX
import com.cscmobi.habittrackingandroid.utils.ChartUtil.Companion.categoriesYearLabelAxisX
import com.cscmobi.habittrackingandroid.utils.Helper
import com.cscmobi.habittrackingandroid.utils.Utils
import com.github.aachartmodel.aainfographics.aachartcreator.AAChartModel
import com.github.aachartmodel.aainfographics.aachartcreator.AAChartType
import com.github.aachartmodel.aainfographics.aachartcreator.AASeriesElement
import com.thanhlv.ads.lib.AdMobUtils
import com.thanhlv.fw.constant.AppConfigs.Companion.KEY_AD_NATIVE_PROGRESS
import com.thanhlv.fw.helper.RunUtils
import com.thanhlv.fw.remoteconfigs.RemoteConfigs
import com.thanhlv.fw.spf.SPF
import kotlinx.coroutines.runBlocking
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.abs

class ProgressFragment : BaseFragment<FragmentProgressBinding>(FragmentProgressBinding::inflate) {
    private var pagerMonthAdapter: PagerMonthCalendarAdapter? = null
    private var pagerYearAdapter: PagerYearCalendarAdapter? = null

    private var mCurrentStartPeriod = CalendarUtil.startWeekMs(System.currentTimeMillis())
    private var mCurrentYear = CalendarUtil.startYearMs(System.currentTimeMillis())
    private var mCurrentMonth = CalendarUtil.startMonthMs(System.currentTimeMillis())


    companion object {
        var mCurrentStreak = MutableLiveData<Int>()
        var mPerfectDay = MutableLiveData<Int>()
        var mCompletionRate = MutableLiveData<Int>()
        var mLongestStreak = MutableLiveData<Int>()
    }

    override fun setEvent() {
        observeData()
    }

    @SuppressLint("SetTextI18n")
    private fun observeData() {

        mCurrentStreak.observe(this) { it ->
            binding.tvCurrentStreak.text = it.toString()
        }
        mPerfectDay.observe(this) { it ->
            binding.tvPerfectDay.text = it.toString()
        }
        mCompletionRate.observe(this) { it ->
            binding.tvCompletionRatePercent.text = it.toString() + "%"
        }
        mLongestStreak.observe(this) { it ->
            binding.tvLongestStreak.text = it.toString()
        }

    }

    override fun initView(view: View) {
        viewPager2()
        loadHistoryData()

        binding.bbCurrentStreak.startAnimation(
            AnimationUtils.loadAnimation(
                requireContext(),
                R.anim.pulse
            )
        )
        binding.bbCompletionRate.startAnimation(
            AnimationUtils.loadAnimation(
                requireContext(),
                R.anim.pulse
            )
        )
        binding.bbLongestStreak.startAnimation(
            AnimationUtils.loadAnimation(
                requireContext(),
                R.anim.pulse
            )
        )
        binding.bbPerfectDay.startAnimation(
            AnimationUtils.loadAnimation(
                requireContext(),
                R.anim.pulse
            )
        )

        handleMonthCalendar()
        handleChart()
        handleYearStats()

    }

    private fun viewPager2() {
        mDataVPMonth = getDataForMonth()
        mDataVPYear = getDataForYear()
        pagerMonthAdapter = PagerMonthCalendarAdapter(requireActivity())
        pagerMonthAdapter!!.setList(mDataVPMonth)
        binding.vpMonth.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                if (mDataVPMonth.isNotEmpty() && position < mDataVPMonth.size) updateTitleMonth(
                    mDataVPMonth[position]
                )
                if (mDataVPMonth.size > 1 && position < mDataVPMonth.size - 1) {
                    binding.btnNextMonth.alpha = 1f
                    binding.btnNextMonth.isEnabled = true
                } else {
                    binding.btnNextMonth.alpha = 0.3f
                    binding.btnNextMonth.isEnabled = false
                }

                if (position > 0 && mDataVPMonth.size > 1) {
                    binding.btnPreviousMonth.alpha = 1f
                    binding.btnPreviousMonth.isEnabled = true
                } else {
                    binding.btnPreviousMonth.alpha = 0.3f
                    binding.btnPreviousMonth.isEnabled = false
                }
            }
        })

        pagerYearAdapter = PagerYearCalendarAdapter(requireActivity())
        pagerYearAdapter!!.setList(mDataVPYear)
        binding.vpYear.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                if (mDataVPYear.isNotEmpty() && position < mDataVPYear.size) updateTitleYear(
                    mDataVPYear[position]
                )
                if (mDataVPYear.size > 1 && position < mDataVPYear.size - 1) {
                    binding.btnNextYear.alpha = 1f
                    binding.btnNextYear.isEnabled = true
                } else {
                    binding.btnNextYear.alpha = 0.3f
                    binding.btnNextYear.isEnabled = false
                }

                if (position > 0 && mDataVPYear.size > 1) {
                    binding.btnPreviousYear.alpha = 1f
                    binding.btnPreviousYear.isEnabled = true
                } else {
                    binding.btnPreviousYear.alpha = 0.3f
                    binding.btnPreviousYear.isEnabled = false
                }
            }
        })
        binding.vpMonth.adapter = pagerMonthAdapter
        binding.vpYear.adapter = pagerYearAdapter

        binding.vpMonth.setCurrentItem(mDataVPMonth.size - 1, true)
        binding.vpYear.setCurrentItem(mDataVPYear.size - 1, true)
    }


    fun loadHistoryData() {
        runBlocking {
            val allHistory = AppDatabase.getInstance(requireContext()).dao().getAllHistory2()
            if (allHistory.isEmpty()) {
                reloadViewPager()
                validateBtn(mChartType, -1)
                drawChart(mChartType, -1)
                return@runBlocking
            }
            allHistory.sortedBy { it.date }
            var streak = 0
            var currentStreak = 0
            var perfectDay = 0
            var longStreak = 0
            for (i in allHistory.indices) {
                if (allHistory[i].progressDay >= 100 && !allHistory[i].allTaskPause) {
                    streak += 1
                    perfectDay += 1
                    if (longStreak < streak) longStreak = streak
                } else {
                    if (streak > 0) currentStreak = streak
                    if (!allHistory[i].allTaskPause) streak = 0
                }
            }
            if (streak > 0) currentStreak = streak

            mCurrentStreak.postValue(currentStreak)
            mLongestStreak.postValue(longStreak)
            mCompletionRate.postValue(perfectDay * 100 / allHistory.size)
            mPerfectDay.postValue(perfectDay)
            reloadViewPager()
            validateBtn(mChartType, mCurrentStartPeriod)
            drawChart(mChartType, mCurrentStartPeriod)
        }
    }

    private fun handleMonthCalendar() {
        validateMonthCalendarBtn(mCurrentMonth)
        binding.btnPreviousMonth.setOnClickListener {
            resolvePreviousMonth()
        }
        binding.btnNextMonth.setOnClickListener {
            resolveNextMonth()
        }

    }

    @SuppressLint("SetTextI18n")
    private fun resolveNextMonth() {
        mCurrentMonth = CalendarUtil.nextMonthMs(mCurrentMonth)
        binding.tvMonth.text = CalendarUtil.getTitleMonthYear(mCurrentMonth)
        validateMonthCalendarBtn(mCurrentMonth)
        val currentPageMonth = binding.vpMonth.currentItem
        binding.vpMonth.setCurrentItem(currentPageMonth + 1, true)
    }

    private fun resolvePreviousMonth() {
        mCurrentMonth = CalendarUtil.previousMonth(mCurrentMonth)
        binding.tvMonth.text = CalendarUtil.getTitleMonthYear(mCurrentMonth)
        validateMonthCalendarBtn(mCurrentMonth)
        val currentPageMonth = binding.vpMonth.currentItem
        binding.vpMonth.setCurrentItem(currentPageMonth - 1, true)
        //
    }

    private fun validateMonthCalendarBtn(startPeriod: Long) {
        if (startPeriod < 0) {
            binding.btnNextPeriod.alpha = 0.3f
            binding.btnNextPeriod.isEnabled = false
            if (CalendarUtil.startMonth(System.currentTimeMillis()) > CalendarUtil.startMonth(
                    SPF.getStartOpenTime(
                        requireContext()
                    )
                )
            ) {
                binding.btnPreviousMonth.alpha = 1f
                binding.btnPreviousMonth.isEnabled = true
            } else {
                binding.btnPreviousMonth.alpha = 0.3f
                binding.btnPreviousMonth.isEnabled = false
            }
        } else {
            if (CalendarUtil.startMonth(startPeriod) > CalendarUtil.startMonth(
                    SPF.getStartOpenTime(
                        requireContext()
                    )
                )
            ) {
                binding.btnPreviousMonth.alpha = 1f
                binding.btnPreviousMonth.isEnabled = true
            } else {
                binding.btnPreviousMonth.alpha = 0.3f
                binding.btnPreviousMonth.isEnabled = false
            }

            if (CalendarUtil.startMonth(startPeriod) < CalendarUtil.startMonth(System.currentTimeMillis())) {
                binding.btnNextMonth.alpha = 1f
                binding.btnNextMonth.isEnabled = true
            } else {
                binding.btnNextMonth.alpha = 0.3f
                binding.btnNextMonth.isEnabled = false
            }
        }
    }

    private var mChartType = 1
    private fun handleChart() {
        binding.btnWeek.setOnClickListener {
            mChartType = 1
            mCurrentStartPeriod = System.currentTimeMillis()
            validateBtn(mChartType, -1)
            binding.btnWeek.setTextColor(Color.parseColor("#ffffff"))
            binding.btnMonth.setTextColor(Color.parseColor("#b5b5b5"))
            binding.btnYear.setTextColor(Color.parseColor("#b5b5b5"))
            binding.btnWeek.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#EDCA15"))
            binding.btnMonth.backgroundTintList =
                ColorStateList.valueOf(Color.parseColor("#f5f5f5"))
            binding.btnYear.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#f5f5f5"))

            drawChart(mChartType, -1)
        }
        binding.btnMonth.setOnClickListener {
            mChartType = 2
            mCurrentStartPeriod = System.currentTimeMillis()
            validateBtn(mChartType, -1)
            binding.btnMonth.setTextColor(Color.parseColor("#ffffff"))
            binding.btnWeek.setTextColor(Color.parseColor("#b5b5b5"))
            binding.btnYear.setTextColor(Color.parseColor("#b5b5b5"))
            binding.btnMonth.backgroundTintList =
                ColorStateList.valueOf(Color.parseColor("#EDCA15"))
            binding.btnWeek.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#f5f5f5"))
            binding.btnYear.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#f5f5f5"))

            drawChart(mChartType, -1)
        }
        binding.btnYear.setOnClickListener {
            mChartType = 3
            mCurrentStartPeriod = System.currentTimeMillis()
            validateBtn(mChartType, -1)
            binding.btnYear.setTextColor(Color.parseColor("#ffffff"))
            binding.btnWeek.setTextColor(Color.parseColor("#b5b5b5"))
            binding.btnMonth.setTextColor(Color.parseColor("#b5b5b5"))
            binding.btnYear.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#EDCA15"))
            binding.btnWeek.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#f5f5f5"))
            binding.btnMonth.backgroundTintList =
                ColorStateList.valueOf(Color.parseColor("#f5f5f5"))
            drawChart(mChartType, -1)
        }
        binding.btnNextPeriod.setOnClickListener {
            resolveNextPeriod(mChartType)
        }
        binding.btnPreviousPeriod.setOnClickListener {
            resolvePreviousPeriod(mChartType)
        }
    }

    private fun validateBtn(type: Int, startPeriod: Long) {

        when (type) {
            1 -> {
                binding.btnWeek.isEnabled = false
                binding.btnMonth.isEnabled = true
                binding.btnYear.isEnabled = true
                if (startPeriod < 0) {
                    binding.btnNextPeriod.alpha = 0.3f
                    binding.btnNextPeriod.isEnabled = false

                    if (CalendarUtil.startWeek(System.currentTimeMillis()) > CalendarUtil.startWeek(
                            SPF.getStartOpenTime(requireContext())
                        )
                    ) {
                        binding.btnPreviousPeriod.alpha = 1f
                        binding.btnPreviousPeriod.isEnabled = true
                    } else {
                        binding.btnPreviousPeriod.alpha = 0.3f
                        binding.btnPreviousPeriod.isEnabled = false
                    }
                } else {
                    if (CalendarUtil.startWeek(startPeriod) > CalendarUtil.startWeek(
                            SPF.getStartOpenTime(
                                requireContext()
                            )
                        )
                    ) {
                        binding.btnPreviousPeriod.alpha = 1f
                        binding.btnPreviousPeriod.isEnabled = true
                    } else {
                        binding.btnPreviousPeriod.alpha = 0.3f
                        binding.btnPreviousPeriod.isEnabled = false
                    }

                    if (CalendarUtil.startWeek(startPeriod) < CalendarUtil.startWeek(System.currentTimeMillis())) {
                        binding.btnNextPeriod.alpha = 1f
                        binding.btnNextPeriod.isEnabled = true
                    } else {
                        binding.btnNextPeriod.alpha = 0.3f
                        binding.btnNextPeriod.isEnabled = false
                    }
                }
            }

            2 -> {
                binding.btnWeek.isEnabled = true
                binding.btnMonth.isEnabled = false
                binding.btnYear.isEnabled = true
                if (startPeriod < 0) {
                    binding.btnNextPeriod.alpha = 0.3f
                    binding.btnNextPeriod.isEnabled = false
                    if (CalendarUtil.startMonth(System.currentTimeMillis()) > CalendarUtil.startMonth(
                            SPF.getStartOpenTime(requireContext())
                        )
                    ) {
                        binding.btnPreviousPeriod.alpha = 1f
                        binding.btnPreviousPeriod.isEnabled = true
                    } else {
                        binding.btnPreviousPeriod.alpha = 0.3f
                        binding.btnPreviousPeriod.isEnabled = false
                    }
                } else {
                    if (CalendarUtil.startMonth(startPeriod) > CalendarUtil.startMonth(
                            SPF.getStartOpenTime(
                                requireContext()
                            )
                        )
                    ) {
                        binding.btnPreviousPeriod.alpha = 1f
                        binding.btnPreviousPeriod.isEnabled = true
                    } else {
                        binding.btnPreviousPeriod.alpha = 0.3f
                        binding.btnPreviousPeriod.isEnabled = false
                    }

                    if (CalendarUtil.startMonth(startPeriod) < CalendarUtil.startMonth(System.currentTimeMillis())) {
                        binding.btnNextPeriod.alpha = 1f
                        binding.btnNextPeriod.isEnabled = true
                    } else {
                        binding.btnNextPeriod.alpha = 0.3f
                        binding.btnNextPeriod.isEnabled = false
                    }
                }
            }

            3 -> {
                binding.btnWeek.isEnabled = true
                binding.btnMonth.isEnabled = true
                binding.btnYear.isEnabled = false
                if (startPeriod < 0) {
                    binding.btnNextPeriod.alpha = 0.3f
                    binding.btnNextPeriod.isEnabled = false
                    if (CalendarUtil.startYear(System.currentTimeMillis()) > CalendarUtil.startYear(
                            SPF.getStartOpenTime(requireContext())
                        )
                    ) {
                        binding.btnPreviousPeriod.alpha = 1f
                        binding.btnPreviousPeriod.isEnabled = true
                    } else {
                        binding.btnPreviousPeriod.alpha = 0.3f
                        binding.btnPreviousPeriod.isEnabled = false
                    }
                } else {
                    if (CalendarUtil.startYear(startPeriod) > CalendarUtil.startYear(
                            SPF.getStartOpenTime(
                                requireContext()
                            )
                        )
                    ) {
                        binding.btnPreviousPeriod.alpha = 1f
                        binding.btnPreviousPeriod.isEnabled = true
                    } else {
                        binding.btnPreviousPeriod.alpha = 0.3f
                        binding.btnPreviousPeriod.isEnabled = false
                    }

                    if (CalendarUtil.startYear(startPeriod) < CalendarUtil.startYear(System.currentTimeMillis())) {
                        binding.btnNextPeriod.alpha = 1f
                        binding.btnNextPeriod.isEnabled = true
                    } else {
                        binding.btnNextPeriod.alpha = 0.3f
                        binding.btnNextPeriod.isEnabled = false
                    }
                }
            }
        }
    }

    private fun resolvePreviousPeriod(type: Int) {
        when (type) {
            1 -> {
                mCurrentStartPeriod = CalendarUtil.previousWeek(mCurrentStartPeriod)
                binding.tvPeriod.text = CalendarUtil.getTitleWeek(mCurrentStartPeriod)
                binding.tvPeriodYear.text = CalendarUtil.getTitleYear(mCurrentStartPeriod)
                binding.tvPeriodYear.visibility = View.VISIBLE
            }

            2 -> {
                mCurrentStartPeriod = CalendarUtil.previousMonth(mCurrentStartPeriod)
                binding.tvPeriod.text = CalendarUtil.getTitleMonth(mCurrentStartPeriod)
                binding.tvPeriodYear.text = CalendarUtil.getTitleYear(mCurrentStartPeriod)
                binding.tvPeriodYear.visibility = View.VISIBLE
            }

            3 -> {
                mCurrentStartPeriod = CalendarUtil.previousYear(mCurrentStartPeriod)
                binding.tvPeriod.text = CalendarUtil.getTitleYear(mCurrentStartPeriod)
                binding.tvPeriodYear.visibility = View.GONE
            }
        }
        validateBtn(mChartType, mCurrentStartPeriod)
        drawChart(mChartType, mCurrentStartPeriod)

    }

    @SuppressLint("SetTextI18n", "SimpleDateFormat")
    private fun resolveNextPeriod(type: Int) {
        when (type) {
            1 -> {
                mCurrentStartPeriod = CalendarUtil.nextWeek(mCurrentStartPeriod)
                binding.tvPeriod.text = CalendarUtil.getTitleWeek(mCurrentStartPeriod)
                binding.tvPeriodYear.text = CalendarUtil.getTitleYear(mCurrentStartPeriod)
                binding.tvPeriodYear.visibility = View.VISIBLE
            }

            2 -> {
                mCurrentStartPeriod = CalendarUtil.nextMonthMs(mCurrentStartPeriod)
                binding.tvPeriod.text = CalendarUtil.getTitleMonth(mCurrentStartPeriod)
                binding.tvPeriodYear.text = CalendarUtil.getTitleYear(mCurrentStartPeriod)
                binding.tvPeriodYear.visibility = View.VISIBLE
            }

            3 -> {
                mCurrentStartPeriod = CalendarUtil.nextYear(mCurrentStartPeriod)
                binding.tvPeriod.text = CalendarUtil.getTitleYear(mCurrentStartPeriod)
                binding.tvPeriodYear.visibility = View.GONE
            }
        }
        validateBtn(mChartType, mCurrentStartPeriod)
        drawChart(mChartType, mCurrentStartPeriod)

    }


    private fun handleYearStats() {
        validateStatsBtn(mCurrentYear)
        binding.btnPreviousYear.setOnClickListener {
            resolvePreviousYearStats()
        }
        binding.btnNextYear.setOnClickListener {
            resolveNextYearStats()
        }
    }

    private fun validateStatsBtn(startPeriod: Long) {
        if (startPeriod < 0) {
            binding.btnNextYear.alpha = 0.3f
            binding.btnNextYear.isEnabled = false
            if (CalendarUtil.startYear(System.currentTimeMillis()) > CalendarUtil.startYear(
                    SPF.getStartOpenTime(
                        requireContext()
                    )
                )
            ) {
                binding.btnPreviousYear.alpha = 1f
                binding.btnPreviousYear.isEnabled = true
            } else {
                binding.btnPreviousYear.alpha = 0.3f
                binding.btnPreviousYear.isEnabled = false
            }
        } else {
            if (CalendarUtil.startYear(startPeriod) > CalendarUtil.startYear(
                    SPF.getStartOpenTime(
                        requireContext()
                    )
                )
            ) {
                binding.btnPreviousYear.alpha = 1f
                binding.btnPreviousYear.isEnabled = true
            } else {
                binding.btnPreviousYear.alpha = 0.3f
                binding.btnPreviousYear.isEnabled = false
            }

            if (CalendarUtil.startYear(startPeriod) < CalendarUtil.startYear(System.currentTimeMillis())) {
                binding.btnNextYear.alpha = 1f
                binding.btnNextYear.isEnabled = true
            } else {
                binding.btnNextYear.alpha = 0.3f
                binding.btnNextYear.isEnabled = false
            }
        }
    }

    private fun resolvePreviousYearStats() {
        mCurrentYear = CalendarUtil.previousYear(mCurrentYear)
        binding.tvYear.text = CalendarUtil.getTitleYear(mCurrentYear)
        validateStatsBtn(mCurrentYear)
        //
    }

    private fun resolveNextYearStats() {
        mCurrentYear = CalendarUtil.nextYear(mCurrentYear)
        binding.tvYear.text = CalendarUtil.getTitleYear(mCurrentYear)
        validateStatsBtn(mCurrentYear)
//
    }

    private fun fetchDataForChart(type: Int, startDate: Long) {
        when (type) {
            1 -> {
                if (startDate < 0) { //tuần hiện tại
                    binding.tvPeriod.text = CalendarUtil.getTitleWeek(System.currentTimeMillis())
                    binding.tvPeriodYear.text =
                        CalendarUtil.getTitleYear(System.currentTimeMillis())
                    binding.tvPeriodYear.visibility = View.VISIBLE
                    val temp = arrayListOf<Any>(0, 0, 0, 0, 0, 0, 0)
                    runBlocking {
                        val startWeek = CalendarUtil.startWeekMs(System.currentTimeMillis())
                        val dataWeek = AppDatabase.getInstance(requireContext()).dao()
                            .getHistoryFromAUntilB(startWeek, startWeek + 7 * 24 * 60 * 60 * 1000)
                        for (i in 2..7) for (j in dataWeek.indices) if (CalendarUtil.dayOfWeek(
                                dataWeek[j].date!!
                            ) == i
                        ) {
                            if (!dataWeek[j].allTaskPause) temp[i - 2] = dataWeek[j].progressDay
                        }
                        for (j in dataWeek.indices) if (CalendarUtil.dayOfWeek(dataWeek[j].date!!) == 1) {
                            if (!dataWeek[j].allTaskPause) temp[6] = dataWeek[j].progressDay
                        }

                        mCurrentData = temp.toArray()
                    }
                } else {
                    binding.tvPeriod.text = CalendarUtil.getTitleWeek(startDate)
                    val temp = arrayListOf<Any>(0, 0, 0, 0, 0, 0, 0)
                    runBlocking {
                        val startWeek = CalendarUtil.startWeekMs(startDate)
                        val dataWeek = AppDatabase.getInstance(requireContext()).dao()
                            .getHistoryFromAUntilB(startWeek, startWeek + 7 * 24 * 60 * 60 * 1000)
                        for (i in 2..7) for (j in dataWeek.indices) if (CalendarUtil.dayOfWeek(
                                dataWeek[j].date!!
                            ) == i
                        ) {
                            if (!dataWeek[j].allTaskPause) temp[i - 2] = dataWeek[j].progressDay
                        }
                        for (j in dataWeek.indices) if (CalendarUtil.dayOfWeek(dataWeek[j].date!!) == 1) {
                            if (!dataWeek[j].allTaskPause) temp[6] = dataWeek[j].progressDay
                        }

                        mCurrentData = temp.toArray()
                    }
                }
            }

            2 -> {
                if (startDate < 0) { //tháng hiện tại
                    binding.tvPeriod.text = CalendarUtil.getTitleMonth(System.currentTimeMillis())
                    binding.tvPeriodYear.text =
                        CalendarUtil.getTitleYear(System.currentTimeMillis())
                    binding.tvPeriodYear.visibility = View.VISIBLE
                    runBlocking {
                        val startMonth = CalendarUtil.startMonthMs(System.currentTimeMillis())
                        val nextMonth = CalendarUtil.nextMonthMs(System.currentTimeMillis())
                        val dataMonth = AppDatabase.getInstance(requireContext()).dao()
                            .getHistoryFromAUntilB(startMonth, nextMonth)
                        mCurrentData = mapDataMonth(
                            dataMonth, CalendarUtil.totalDayOfMonth(System.currentTimeMillis())
                        ).toArray()
                    }
                } else {
                    binding.tvPeriod.text = CalendarUtil.getTitleMonth(startDate)
                    runBlocking {
                        val startMonth = CalendarUtil.startMonthMs(startDate)
                        val nextMonth = CalendarUtil.nextMonthMs(startDate)
                        val dataMonth = AppDatabase.getInstance(requireContext()).dao()
                            .getHistoryFromAUntilB(startMonth, nextMonth)
                        mCurrentData = mapDataMonth(
                            dataMonth, CalendarUtil.totalDayOfMonth(startDate)
                        ).toArray()
                    }
                }
            }


            3 -> {
                if (startDate < 0) { //năm hiện tại
                    binding.tvPeriod.text = CalendarUtil.getTitleYear(System.currentTimeMillis())
                    binding.tvPeriodYear.visibility = View.GONE
                    runBlocking {
                        val startYear = CalendarUtil.startYearMs(System.currentTimeMillis())
                        val nextYear = CalendarUtil.nextYear(System.currentTimeMillis())
                        val dataYear = AppDatabase.getInstance(requireContext()).dao()
                            .getHistoryFromAUntilB(startYear, nextYear)
                        mCurrentData = mapDataYear(dataYear, startYear).toArray()
                    }
                } else {
                    binding.tvPeriod.text = CalendarUtil.getTitleYear(startDate)
                    runBlocking {
                        val startYear = CalendarUtil.startYearMs(startDate)
                        val nextYear = CalendarUtil.nextYear(startDate)
                        val dataYear = AppDatabase.getInstance(requireContext()).dao()
                            .getHistoryFromAUntilB(startYear, nextYear)
                        mCurrentData = mapDataYear(dataYear, startYear).toArray()
                    }
                }
            }
        }
    }

    private fun mapDataMonth(dataMonth: List<History>, days: Int): ArrayList<Any> {
        val temp = arrayListOf<Any>()
        for (i in 0 until days) temp.add(0)
        dataMonth.forEach {
            val day = CalendarUtil.dayOfMonth(it.date!!)
            if (!it.allTaskPause) temp[day - 1] = it.progressDay
        }
        return temp
    }

    private fun mapDataYear(dataYear: List<History>, startYear: Long): ArrayList<Any> {
        val temp = arrayListOf<Any>(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)
        val allPerMonth = arrayListOf<Int>(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)
        val donePerMonth = arrayListOf<Int>(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)

        dataYear.forEach {
            val month = CalendarUtil.getMonth(it.date!!)
            allPerMonth[month] += 1
            if (!it.allTaskPause && it.progressDay >= 100) donePerMonth[month] += 1
        }
        val curYear = CalendarUtil.startYearMs(System.currentTimeMillis())
        if (curYear == startYear) {
            val curMonth = CalendarUtil.getMonth(System.currentTimeMillis())
            allPerMonth[curMonth] = calculatePlusCurrentMonth()
        }
        for (i in temp.indices) if (allPerMonth[i] > 0) temp[i] =
            donePerMonth[i] * 100 / allPerMonth[i]
        return temp
    }

    private fun calculatePlusCurrentMonth(): Int {
        var n = 0
        val days = CalendarUtil.totalDayOfMonth(System.currentTimeMillis())
        val temp = arrayListOf<Int>()
        val dateMonth = arrayListOf<Long>()

        var startMonth = CalendarUtil.startMonthMs(System.currentTimeMillis())
        for (i in 0 until days) {
            temp.add(0)
            dateMonth.add(startMonth)
            startMonth = CalendarUtil.nextDayMs(startMonth)
        }

        runBlocking {
            val allTask = AppDatabase.getInstance(requireContext()).dao().getAllTask2()
            allTask.forEach {
                for (i in dateMonth.indices) {
                    if (haveTaskInDay(it, dateMonth[i])) temp[i] = 1
                }
            }

        }

        for (i in temp.indices) n += temp[i]
        return n
    }


    private fun haveTaskInDay(task: Task, date: Long): Boolean {
        var isValid = true

        if (task.pauseDate != null) {
            if (task.pause == -1) return false
            if (CalendarUtil.getDaysBetween(task.pauseDate!!, date) <= task.pause) return false
        }

        task.repeate?.let {
            if (it.isOn == true) {
                when (it.type) {
                    "daily" -> {
                        isValid =
                            abs(Utils.getDayofYear(date) - Utils.getDayofYear(task.startDate!!)) % it.frequency == 0
                    }

                    "monthly" -> {
                        val checkMonthValid =
                            abs(Utils.getMonth(date) - Utils.getMonth(task.startDate!!)) % it.frequency == 0
                        return if (checkMonthValid) {
                            val dayValid = it.days?.find { Utils.getDayofMonth(date) == it }
                            (dayValid != null && dayValid != -1)
                        } else false

                    }

                    "weekly" -> {
                        val checkWeekValid =
                            abs(Utils.getWeek(date) - Utils.getWeek(task.startDate!!)) % it.frequency == 0
                        return if (checkWeekValid) {
                            var dayValid = it.days?.find { it == Utils.getDayofWeek(date) }
                            dayValid != null && dayValid != -1
                        } else false
                    }
                }
            }
        }

        val _date = Helper.getDateWithoutHour(date)

        if (task.startDate != null) {
            if (Helper.getDateWithoutHour(task.startDate!!) > _date) {
                return false
            }
        }

        if (task.endDate.isOpen == true && task.endDate.endDate!! < _date) {
            return false
        }

        return isValid
    }

    private var mCurrentData = arrayOf<Any>(
        5, 30, 55, 100, 80, 30, 60
    )

    private fun drawChart(type: Int, startDate: Long) {

        fetchDataForChart(mChartType, startDate)

        var categories = categoriesWeekLabelAxisX
        var radiusColumn = 10
        var interval = 1
        when (type) {
            2 -> {
                categories = categoriesMonthLabelAxisX
                radiusColumn = 3
                interval = 6
            }

            3 -> {
                categories = categoriesYearLabelAxisX
                radiusColumn = 6
                interval = 1
            }

            else -> {
                categories = categoriesWeekLabelAxisX
                radiusColumn = 10
                interval = 1
            }
        }
        val aaChartModel: AAChartModel =
            AAChartModel().chartType(AAChartType.Column).backgroundColor("#00000000").series(
                arrayOf(
                    AASeriesElement().data(mCurrentData).enableMouseTracking(false)
                        .showInLegend(true)
                )
            ).animationDuration(0).categories(categories).borderRadius(radiusColumn)
                .xAxisTickInterval(interval).yAxisLabelsEnabled(true).yAxisMax(100).yAxisTitle("")
                .gradientColorEnable(true).colorsTheme(arrayOf("#54BA8F", "#FB7950"))
                .legendEnabled(false) //show series name

        binding.aaChartView.aa_drawChartWithChartModel(aaChartModel)
    }

    private var mDataVPMonth = mutableListOf<MonthCalendarModel>()
    private var mDataVPYear = mutableListOf<MonthCalendarModel>()
    private fun reloadViewPager() {
        //for month

        binding.vpMonth.adapter = pagerMonthAdapter
        binding.vpYear.adapter = pagerYearAdapter
        binding.vpMonth.postDelayed({
            binding.vpMonth.setCurrentItem(mDataVPMonth.size - 1, true)
        }, 150)
        binding.vpYear.postDelayed({
            binding.vpYear.setCurrentItem(mDataVPYear.size - 1, true)
        }, 150)

    }

    @SuppressLint("SimpleDateFormat")
    private fun updateTitleMonth(monthYear: MonthCalendarModel?) {
        if (monthYear == null) return
        val calendar = Calendar.getInstance()
        calendar[Calendar.DAY_OF_MONTH] = 1
        calendar[Calendar.YEAR] = monthYear.year
        calendar[Calendar.MONTH] = monthYear.month - 1
        val monthYearString = SimpleDateFormat("MMMM yyyy").format(calendar.time)
        if (isAdded && !isDetached) RunUtils.runOnUI {
            binding.tvMonth.text = monthYearString.uppercase()
        }
    }

    @SuppressLint("SimpleDateFormat")
    private fun updateTitleYear(monthYear: MonthCalendarModel?) {
        if (monthYear == null) return
        if (isAdded && !isDetached) RunUtils.runOnUI {
            binding.tvYear.text = monthYear.year.toString()
        }
    }

    private fun getDataForMonth(): MutableList<MonthCalendarModel> {
        val list = mutableListOf<MonthCalendarModel>()
        var startMonth = CalendarUtil.startMonthMs(SPF.getStartOpenTime(requireContext()))
        val currentMonth = CalendarUtil.startMonthMs(System.currentTimeMillis())
        while (startMonth <= currentMonth) {
            list.add(
                MonthCalendarModel(
                    CalendarUtil.getMonth(startMonth) + 1, CalendarUtil.getYear(startMonth)
                )
            )
            startMonth = CalendarUtil.nextMonthMs(startMonth)
        }

        return list
    }


    private fun getDataForYear(): MutableList<MonthCalendarModel> {
        val list = mutableListOf<MonthCalendarModel>()
        var startYear = CalendarUtil.startYearMs(SPF.getStartOpenTime(requireContext()))
        val currentYear = CalendarUtil.startYearMs(System.currentTimeMillis())
        while (startYear <= currentYear) {
            list.add(
                MonthCalendarModel(1, CalendarUtil.getYear(startYear))
            )
            startYear = CalendarUtil.nextYear(startYear)
        }
        return list
    }

    fun toggleAdView() {
        if (!SPF.isProApp(requireContext()) && RemoteConfigs.instance.getConfigValue(
                KEY_AD_NATIVE_PROGRESS
            ).asBoolean()
        ) {
            binding.adView.visibility = View.VISIBLE
            AdMobUtils.createNativeAd(
                requireContext(), getString(R.string.native_video_id), binding.adView, null
            )
        } else binding.adView.visibility = View.GONE
    }
}