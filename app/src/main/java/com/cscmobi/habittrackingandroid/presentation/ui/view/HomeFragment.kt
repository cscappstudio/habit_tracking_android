package com.cscmobi.habittrackingandroid.presentation.ui.view

import android.util.TypedValue
import android.view.View
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.lifecycleScope
import com.cscmobi.habittrackingandroid.R
import com.cscmobi.habittrackingandroid.base.BaseFragment
import com.cscmobi.habittrackingandroid.data.model.Task
import com.cscmobi.habittrackingandroid.databinding.FragmentHomeBinding
import com.cscmobi.habittrackingandroid.presentation.ItemWithPostionListener
import com.cscmobi.habittrackingandroid.presentation.OnItemClickPositionListener
import com.cscmobi.habittrackingandroid.presentation.ui.adapter.TaskAdapter
import com.cscmobi.habittrackingandroid.presentation.ui.adapter.WeekPagerAdapter
import com.cscmobi.habittrackingandroid.presentation.ui.intent.HomeIntent
import com.cscmobi.habittrackingandroid.presentation.ui.viewmodel.HomeViewModel
import com.cscmobi.habittrackingandroid.presentation.ui.viewstate.HomeState
import com.cscmobi.habittrackingandroid.utils.Helper
import com.google.android.material.chip.Chip
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.threeten.bp.format.DateTimeFormatter
import java.util.Locale


class HomeFragment : BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate) {
    private val homeViewModel: HomeViewModel by viewModel()
    private lateinit var weekPagerAdapter: WeekPagerAdapter
    private lateinit var taskAdapter: TaskAdapter

    override fun initView(view: View) {
        lifecycleScope.launch {
            homeViewModel.userIntent.send(HomeIntent.FetchTasks)
        }

        observeState()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.ENGLISH)

        homeViewModel.initDateWeek()
        weekPagerAdapter = WeekPagerAdapter(this).apply {
            this.listWeekData = homeViewModel.listWeekData
            this.doActionviewPager = {

//                binding.txtDate.text =  if (it == Helper.currentDate)  " Today" else  it.format(formatter)

                if (it.isBefore(Helper.currentDate)) {
                    binding.ivArrow.setImageResource(R.drawable.nav_arrow_right)

                } else {
                    binding.ivArrow.setImageResource(R.drawable.nav_arrow_left)
                }
            }

        }


        binding.vpWeek.adapter = weekPagerAdapter

        setCurrentWeekinViewPager()


    }


    private fun observeState() {
        lifecycleScope.launch {
            homeViewModel.state.collect{ state ->
                when(state) {

                    is HomeState.Tasks -> {
                        initChips(state.tasks.map { it.tag }.distinct())
                        initTaskAdapter(state.tasks)
                    }
                    else -> {

                    }
                }
            }
        }

    }
    private fun setCurrentWeekinViewPager() {
        if (homeViewModel.currentWeekPos != -1)
            binding.vpWeek.currentItem = homeViewModel.currentWeekPos

        isSetCurrentDate = true
    }

    private fun initTaskAdapter(list: List<Task>) {
        taskAdapter = TaskAdapter(object : ItemWithPostionListener<Task> {
            override fun onItemClicked(item: Task, p: Int) {

            }

        })
        binding.rcvTasks.adapter = taskAdapter
        taskAdapter.submitList(list)
        taskAdapter.notifyDataSetChanged()

    }



    private fun initChips(tags: List<String>) {
        tags.forEach {
        homeViewModel.listWeekData
        val chip = Chip(requireContext())
        chip.isCheckable = true
        chip.text = it
        chip.tag = it
        chip.typeface = ResourcesCompat.getFont(requireContext(), R.font.worksans_bold);
        chip.setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimension(com.intuit.ssp.R.dimen._12ssp))

      //  changeChipState(false, chip)

        chip.setOnCheckedChangeListener { compoundButton, b ->
//
//            if (chip.isChecked) {
////                viewModel.initTemplateImage(chip.tag.toString())
//                changeChipState(true, chip)
//
//            } else {
//                changeChipState(false, chip)
//
//            }
        }

        binding.chipgroupCategory.addView(chip)
        }
    }

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
            binding.txtDate.setOnClickListener {
                setCurrentWeekinViewPager()
            }
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    companion object {
        var isSetCurrentDate = false
    }

}