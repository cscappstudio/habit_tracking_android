package com.cscmobi.habittrackingandroid.presentation.ui.view

import android.content.res.ColorStateList
import android.graphics.Color
import android.util.TypedValue
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import com.cscmobi.habittrackingandroid.base.BaseFragment
import com.cscmobi.habittrackingandroid.databinding.FragmentHomeBinding
import com.cscmobi.habittrackingandroid.presentation.ui.adapter.WeekPagerAdapter
import com.cscmobi.habittrackingandroid.presentation.ui.viewmodel.HomeViewModel
import com.google.android.material.chip.Chip
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

//    private fun initChips() {
//        val chip = Chip(requireContext())
//        chip.isCheckable = true
//        chip.text = it
//        chip.tag = it
//        chip.typeface = ResourcesCompat.getFont(requireContext(), R.font.allura_regular);
//        chip.setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimension(com.intuit.ssp.R.dimen._12ssp))
//
//        changeChipState(false, chip)
//
//        chip.setOnCheckedChangeListener { compoundButton, b ->
//
//            if (chip.isChecked) {
//                viewModel.initTemplateImage(chip.tag.toString())
//                changeChipState(true, chip)
//
//            } else {
//                changeChipState(false, chip)
//
//            }
//        }
//
//        binding.chipgroupCategory.addView(chip)
//    }
//
//    fun changeChipState(isChanged: Boolean, chip: Chip) {
//        chip.typeface = ResourcesCompat.getFont(requireContext(), R.font.nunito_fontweight500);
//
//        if (isChanged) {
//            chip.chipBackgroundColor = ColorStateList.valueOf(resources.getColor(R.color.darkpurple,null))
//            chip.setTextColor(ColorStateList.valueOf(Color.WHITE));
//            chip.chipStrokeColor = ColorStateList.valueOf(Color.TRANSPARENT)
//
//
//        } else {
//            chip.chipBackgroundColor = ColorStateList.valueOf(Color.WHITE)
//            chip.setTextColor(ColorStateList.valueOf(resources.getColor(R.color.darkpurple,null)));
//            chip.chipStrokeColor = ColorStateList.valueOf(
//                ContextCompat.getColor(
//                    requireContext(),
//                    R.color.lightpurple
//                )
//            )
//
//        }
//    }

    override fun setEvent() {

    }

}