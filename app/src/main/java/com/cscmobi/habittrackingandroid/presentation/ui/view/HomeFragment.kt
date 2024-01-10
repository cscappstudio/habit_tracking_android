package com.cscmobi.habittrackingandroid.presentation.ui.view

import android.view.View
import com.cscmobi.habittrackingandroid.base.BaseFragment
import com.cscmobi.habittrackingandroid.databinding.FragmentHomeBinding
import com.cscmobi.habittrackingandroid.presentation.ui.adapter.WeekPagerAdapter
import com.cscmobi.habittrackingandroid.presentation.ui.viewmodel.HomeViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.threeten.bp.LocalDate


class HomeFragment : BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate) {
    private val homeViewModel: HomeViewModel by viewModel()

    override fun initView(view: View) {
        homeViewModel.initDateWeek()
        binding.vpWeek.adapter = WeekPagerAdapter(this).apply {
            this.listWeekData = homeViewModel.listWeekData
            this.doActionviewPager = {
                setCurrentWeekinViewPager()

            }
        }

        setCurrentWeekinViewPager()


    }

    private fun setCurrentWeekinViewPager() {
        if (homeViewModel.currentWeekPos != -1)
            binding.vpWeek.currentItem = homeViewModel.currentWeekPos
    }

    fun initChips() {

    }

    override fun setEvent() {

    }

}