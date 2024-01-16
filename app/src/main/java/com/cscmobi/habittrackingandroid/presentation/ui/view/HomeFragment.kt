package com.cscmobi.habittrackingandroid.presentation.ui.view

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Build
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.util.TypedValue
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.lifecycleScope
import com.cscmobi.habittrackingandroid.R
import com.cscmobi.habittrackingandroid.base.BaseFragment
import com.cscmobi.habittrackingandroid.data.model.Task
import com.cscmobi.habittrackingandroid.databinding.FragmentHomeBinding
import com.cscmobi.habittrackingandroid.presentation.ItemTaskWithEdit
import com.cscmobi.habittrackingandroid.presentation.ItemWithPostionListener
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

        binding.isTasksEmpty = true
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

        binding.txtProgress1.setSpanTextView(R.color.forest_green)
        binding.txtProgress2.setSpanTextView(R.color.forest_green)


    }


    private fun observeState() {
        lifecycleScope.launch {
            homeViewModel.state.collect { state ->
                when (state) {

                    is HomeState.Tasks -> {
                        val categories = arrayListOf<String>()
                        categories.add("All")
                        categories.addAll(state.tasks.map { it.tag }.distinct())
                        initChips(categories)

                        if (state.tasks.isEmpty()) {
                            binding.isTasksEmpty = true
                        } else {
                            initTaskAdapter(state.tasks)
                            binding.isTasksEmpty = false

                        }
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
        taskAdapter = TaskAdapter(object : ItemTaskWithEdit<Task> {
            override fun onItemClicked(item: Task, p: Int) {

            }

            override fun skip(item: Task, p: Int) {

            }

            override fun edit(item: Task, p: Int) {
            }

            override fun delete(item: Task, p: Int) {
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
            chip.checkedIcon = null
            chip.tag = it
            chip.typeface = ResourcesCompat.getFont(requireContext(), R.font.worksans_bold);
            chip.setTextSize(
                TypedValue.COMPLEX_UNIT_PX,
                resources.getDimension(com.intuit.ssp.R.dimen._12ssp)
            )

            changeChipState(false, chip)

            chip.setOnCheckedChangeListener { compoundButton, b ->

                if (chip.isChecked) {
                    lifecycleScope.launch {
                        homeViewModel.userIntent.send(HomeIntent.FetchTasksbyCategory(chip.tag.toString()))
                    }
                    changeChipState(true, chip)

                } else {
                    changeChipState(false, chip)

                }
            }

            binding.chipgroupCategory.addView(chip)
        }
    }


    private fun TextView.setSpanTextView(colorSpan: Int) {
        val firstText = this.text.split("/")

        val spannableString = SpannableString(this.text)
        val foregroundColorSpan =
            ForegroundColorSpan(ContextCompat.getColor(this.context, colorSpan))
        spannableString.setSpan(
            foregroundColorSpan,
            0,
            firstText[0].length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        this.text = spannableString

    }

    fun changeChipState(isChanged: Boolean, chip: Chip) {
        chip.typeface = ResourcesCompat.getFont(requireContext(), R.font.worksans_regular);

        if (isChanged) {
            chip.chipBackgroundColor =
                ColorStateList.valueOf(resources.getColor(R.color.tangerine, null))
            chip.setTextColor(ColorStateList.valueOf(Color.WHITE));
            chip.typeface = ResourcesCompat.getFont(requireContext(), R.font.worksans_semibold)
            chip.elevation = 15f
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                chip.outlineSpotShadowColor = resources.getColor(R.color.tangerine, null)
            }


        } else {
            chip.chipBackgroundColor =
                ColorStateList.valueOf(resources.getColor(R.color.white_smoke, null))
            chip.setTextColor(ColorStateList.valueOf(resources.getColor(R.color.gray, null)));
            chip.typeface = ResourcesCompat.getFont(requireContext(), R.font.worksans_regular);
            chip.elevation = 0f

        }
    }

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