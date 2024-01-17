package com.cscmobi.habittrackingandroid.thanhlv.ui

import android.annotation.SuppressLint
import android.view.View
import android.view.animation.AnimationUtils
import androidx.viewpager2.widget.ViewPager2
import com.cscmobi.habittrackingandroid.R
import com.cscmobi.habittrackingandroid.base.BaseFragment
import com.cscmobi.habittrackingandroid.databinding.FragmentProgressBinding
import com.cscmobi.habittrackingandroid.thanhlv.adapter.PagerMonthCalendarAdapter
import com.cscmobi.habittrackingandroid.thanhlv.adapter.PagerYearCalendarAdapter
import com.cscmobi.habittrackingandroid.thanhlv.model.MonthCalendarModel
import com.github.aachartmodel.aainfographics.aachartcreator.AAChartModel
import com.github.aachartmodel.aainfographics.aachartcreator.AAChartType
import com.github.aachartmodel.aainfographics.aachartcreator.AASeriesElement
import com.thanhlv.fw.helper.RunUtils
import java.text.SimpleDateFormat
import java.util.*

class ProgressFragment : BaseFragment<FragmentProgressBinding>(FragmentProgressBinding::inflate) {
    private var pagerMonthAdapter: PagerMonthCalendarAdapter? = null
    private var pagerYearAdapter: PagerYearCalendarAdapter? = null

    override fun initView(view: View) {

        binding.bbCurrentStreak
            .startAnimation(AnimationUtils.loadAnimation(requireContext(), R.anim.pulse))
        binding.bbCompletionRate
            .startAnimation(AnimationUtils.loadAnimation(requireContext(), R.anim.pulse))

        viewPager2()
        val aaChartModel: AAChartModel = AAChartModel()
            .chartType(AAChartType.Column)
            .backgroundColor("#00000000")
            .series(
                arrayOf(
                    AASeriesElement()
                        .data(
                            arrayOf(
                                15,
                                30,
                                55,
                                120,
                                80,
                                30,
                                60,
                                15,
                                30,
                                55,
                                120,
                                80,
                                30,
                                60,
                                15,
                                30,
                                55,
                                120,
                                80,
                                30,
                                60,
                                15,
                                30,
                                55,
                                120,
                                80
                            )
                        )
                        .enableMouseTracking(false)
                        .step(5)
                        .showInLegend(true)
                )
            )
//            .categories(arrayOf("1", "2", "3", "4", "5", "6", "7","8", "9", "10", "11", "12", "13", "14", "15", "16", "17","18", "19", "20", "21", "22", "23", "24", "25", "26", "27","28", "29", "30"))
            .yAxisMax(120)
            .yAxisMin(0)
            .borderRadius(1)
            .yAxisLabelsEnabled(true)
            .yAxisTitle("")
//            .xAxisTickInterval(7)
            .gradientColorEnable(true)
            .colorsTheme(arrayOf("#54BA8F", "#FB7950", "#54BA8F", "#FB7950"))
            .legendEnabled(false) //show series name

        binding.aaChartView.aa_drawChartWithChartModel(aaChartModel)
    }

    private fun viewPager2() {
        //for month
        pagerMonthAdapter = PagerMonthCalendarAdapter(requireActivity())
        binding.vpMonth.adapter = pagerMonthAdapter
//        binding.vpMonth.isSaveEnabled = false
        pagerMonthAdapter!!.setList(getDataForMonth())
        binding.vpMonth.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                updateTitleMonth(pagerMonthAdapter?.getList()?.get(position))
            }
        })


        //for year
        pagerYearAdapter = PagerYearCalendarAdapter(requireActivity())
        binding.vpYear.adapter = pagerYearAdapter
//        binding.vpYear.isSaveEnabled = false
//        binding.vpYear.offscreenPageLimit = 2
        pagerYearAdapter!!.setList(getDataForYear())
        binding.vpYear.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                updateTitleYear(pagerYearAdapter?.getList()?.get(position))
            }
        })

    }

    @SuppressLint("SimpleDateFormat")
    private fun updateTitleMonth(monthYear: MonthCalendarModel?) {
        if (monthYear == null) return
        val calendar = Calendar.getInstance()
        calendar[Calendar.YEAR] = monthYear.year
        calendar[Calendar.MONTH] = monthYear.month - 1
        val monthYearString = SimpleDateFormat("MMMM yyyy").format(calendar.time)
        if (isAdded && !isDetached)
            RunUtils.runOnUI {
                binding.tvMonth.text = monthYearString.uppercase()
            }
    }


    @SuppressLint("SimpleDateFormat")
    private fun updateTitleYear(monthYear: MonthCalendarModel?) {
        if (monthYear == null) return
        if (isAdded && !isDetached)
            RunUtils.runOnUI {
                binding.tvYear.text = monthYear.year.toString()
            }
    }

    private fun getDataForMonth(): MutableList<MonthCalendarModel> {
        val list = mutableListOf<MonthCalendarModel>()
        list.add(MonthCalendarModel(9, 2023))
        list.add(MonthCalendarModel(10, 2023))
        list.add(MonthCalendarModel(11, 2023))
        list.add(MonthCalendarModel(12, 2023))
        list.add(MonthCalendarModel(1, 2024))
        list.add(MonthCalendarModel(2, 2024))
        list.add(MonthCalendarModel(3, 2024))
        list.add(MonthCalendarModel(4, 2024))
        list.add(MonthCalendarModel(5, 2024))
        return list
    }


    private fun getDataForYear(): MutableList<MonthCalendarModel> {
        val list = mutableListOf<MonthCalendarModel>()
        list.add(MonthCalendarModel(1, 2023))
        list.add(MonthCalendarModel(1, 2024))
        list.add(MonthCalendarModel(1, 2025))
        return list
    }
}