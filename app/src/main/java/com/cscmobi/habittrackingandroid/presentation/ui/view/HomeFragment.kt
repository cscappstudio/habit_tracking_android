package com.cscmobi.habittrackingandroid.presentation.ui.view

import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.Rect
import android.os.Build
import android.util.TypedValue
import android.view.View
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import com.cscmobi.habittrackingandroid.R
import com.cscmobi.habittrackingandroid.base.BaseFragment
import com.cscmobi.habittrackingandroid.data.model.WeekCalenderItem
import com.cscmobi.habittrackingandroid.databinding.FragmentHomeBinding
import com.cscmobi.habittrackingandroid.presentation.ItemTaskWithEdit
import com.cscmobi.habittrackingandroid.presentation.OnItemClickPositionListener
import com.cscmobi.habittrackingandroid.presentation.ui.activity.DetailTaskActivity
import com.cscmobi.habittrackingandroid.presentation.ui.adapter.TaskAdapter
import com.cscmobi.habittrackingandroid.presentation.ui.adapter.WeekAdapter
import com.cscmobi.habittrackingandroid.presentation.ui.intent.HomeIntent
import com.cscmobi.habittrackingandroid.presentation.ui.viewmodel.HomeViewModel
import com.cscmobi.habittrackingandroid.presentation.ui.viewstate.HomeState
import com.cscmobi.habittrackingandroid.thanhlv.model.Task
import com.cscmobi.habittrackingandroid.thanhlv.ui.MoodActivity
import com.cscmobi.habittrackingandroid.utils.Constant
import com.cscmobi.habittrackingandroid.utils.Helper
import com.cscmobi.habittrackingandroid.utils.setSpanTextView
import com.google.android.material.chip.Chip
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter
import org.threeten.bp.format.TextStyle
import java.util.Locale


class HomeFragment : BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate) {
    private var hasInitChip = false
    private val homeViewModel: HomeViewModel by viewModel()
    private lateinit var taskAdapter: TaskAdapter


    private lateinit var weekAdapter: WeekAdapter
    private var data = arrayListOf<WeekCalenderItem>()
    private var date = arrayListOf<LocalDate>()
    private var c = Helper.currentDate
    lateinit var startOfWeek: LocalDate

    override fun initView(view: View) {


        binding.isTasksEmpty = true
        observeState()

        homeViewModel.initDateWeek()
//        weekPagerAdapter = WeekPagerAdapter(this).apply {
//            this.listWeekData = homeViewModel.listWeekData
//            this.doActionviewPager = {
//
//                if (it.isBefore(Helper.currentDate)) {
//                    binding.ivArrow.setImageResource(R.drawable.nav_arrow_right)
//
//                } else {
//                    binding.ivArrow.setImageResource(R.drawable.nav_arrow_left)
//                }
//            }
//
//        }

        initWeekApdater()

        binding.txtProgress1.setSpanTextView(R.color.forest_green)
        binding.txtProgress2.setSpanTextView(R.color.forest_green)


    }

    override fun onResume() {
        super.onResume()

        lifecycleScope.launch {
            homeViewModel.userIntent.send(HomeIntent.FetchTasks)
        }
    }

    fun getDatesofWeek() {


        date.clear()
        data.clear()

        for (day in  homeViewModel.listWeekData) {
            date.add(day)
            if (day == c)
                data.add(
                    WeekCalenderItem(
                        day.dayOfWeek.getDisplayName(
                            TextStyle.SHORT,
                            Locale.ENGLISH
                        ), day.dayOfMonth, true, localDate = day
                    )
                )
            else data.add(
                WeekCalenderItem(
                    day.dayOfWeek.getDisplayName(
                        TextStyle.SHORT,
                        Locale.ENGLISH
                    ), day.dayOfMonth, localDate = day
                )
            )


        }
    }

    private fun observeState() {
        lifecycleScope.launch {
            homeViewModel.state.collect { state ->
                when (state) {
                    is HomeState.Tasks -> {
                        if (!hasInitChip) {

                            val categories = arrayListOf<String>()
                            categories.add("All")
                            categories.addAll(state.tasks.map { it.tag }.distinct())
                            initChips(categories)

                            hasInitChip = true
                        }

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


    private fun initTaskAdapter(list: List<Task>) {
//         val goalTargets = homeViewModel.tasks.map { it.goal?.target }
//         val goalProgress = homeViewModel.tasks.map { it.goal?.currentProgress }
        taskAdapter = TaskAdapter(object : ItemTaskWithEdit<Task> {
            override fun onItemClicked(item: Task, p: Int) {
                Intent(requireActivity(), DetailTaskActivity::class.java).apply {
                    putExtra(Constant.task_id, item.id)
                    startActivity(this)
                }

            }

            override fun skip(item: Task, p: Int) {

            }

            override fun edit(item: Task, p: Int) {
            }

            override fun delete(item: Task, p: Int) {

            }

            override fun onItemChange(p: Int, item: Task, isChange: Boolean) {
                if (isChange) {
                   item.goal?.currentProgress = item.goal?.target

                }
                else {
                    item.goal?.currentProgress =  0
                }
         taskAdapter.notifyDataSetChanged()
                lifecycleScope.launch {
                    homeViewModel.userIntent.send(HomeIntent.UpdateTask(item))
                    delay(500L)
                }

            }

        })
        binding.rcvTasks.adapter = taskAdapter
        taskAdapter.submitList(list)
        taskAdapter.notifyDataSetChanged()

    }


    private fun initChips(tags: List<String>) {

        tags.forEach {
            if (it.isNotEmpty()) {


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
        binding.llToday.setOnClickListener {
            binding.llToday.visibility = View.GONE
            scrollToPositionWithCentering(homeViewModel.currentWeekPos)
            for ( i in (homeViewModel.currentWeekPos -3)..(homeViewModel.currentWeekPos +3) ) {
                data[i].isSelected = false
            }
            data[homeViewModel.currentWeekPos].isSelected = true
            weekAdapter.notifyItemChanged(homeViewModel.currentWeekPos)
        }


        binding.btnAddMood.setOnClickListener {
            startActivity(Intent(requireContext(), MoodActivity::class.java))
        }

    }

   private fun initWeekApdater() {
       val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.ENGLISH)


       addDecoration()
       getDatesofWeek()
       weekAdapter = WeekAdapter(object : OnItemClickPositionListener {
           override fun onItemClick(position: Int) {
               data.forEachIndexed { index, item ->
                   item.isSelected = position == index
               }

               if (date[position] == c) {
                   binding.llToday.visibility = View.GONE
                   binding.txtDate.text = "Today"
               }
               else {
                   binding.llToday.visibility = View.VISIBLE
                   binding.txtDate.text = date[position].format(formatter)

               }

                binding.llToday.visibility = if(date[position] == c) View.INVISIBLE else View.VISIBLE
                if (date[position].isBefore(Helper.currentDate)) {
                    binding.ivArrow.setImageResource(R.drawable.nav_arrow_right)

                } else {
                    binding.ivArrow.setImageResource(R.drawable.nav_arrow_left)
                }

               weekAdapter.notifyDataSetChanged()

               binding.rcvWeek.postDelayed(Runnable {
                   scrollToPositionWithCentering(position)
               }, 200L)
           }

       })
       weekAdapter.submitList(data)
       binding.rcvWeek.adapter = weekAdapter


       binding.rcvWeek.postDelayed(Runnable {

           scrollToPositionWithCentering(homeViewModel.currentWeekPos)
               // give a delay of one second
           }, 1_000)


   }

    fun scrollToPositionWithCentering(position: Int) {
        val layoutManager = binding.rcvWeek.layoutManager as LinearLayoutManager
        val smoothScroller = CenterSmoothScroller(binding.rcvWeek.context)
        smoothScroller.targetPosition = position
        layoutManager.startSmoothScroll(smoothScroller)

    }

    // Custom SmoothScroller to scroll to the center of the target item
    class CenterSmoothScroller(context: Context) : LinearSmoothScroller(context) {
        override fun calculateDtToFit(viewStart: Int, viewEnd: Int, boxStart: Int, boxEnd: Int, snapPreference: Int): Int {
            return (boxStart + (boxEnd - boxStart) / 2) - (viewStart + (viewEnd - viewStart) / 2)
        }

        override fun getVerticalSnapPreference(): Int {
            return SNAP_TO_START
        }

        override fun getHorizontalSnapPreference(): Int {
            return SNAP_TO_START
        }
    }
    private fun addDecoration() {
        if (!hasDecoration(binding.rcvWeek, HorizontalItemDecoration::class.java)) {
            val spaceWidth = resources.getDimensionPixelSize(com.intuit.sdp.R.dimen._3sdp)
            binding.rcvWeek.addItemDecoration(HorizontalItemDecoration(spaceWidth))
        }
    }

    fun hasDecoration(
        recyclerView: RecyclerView,
        decorationClass: Class<out RecyclerView.ItemDecoration>
    ): Boolean {
        for (i in 0 until recyclerView.itemDecorationCount) {
            val itemDecoration = recyclerView.getItemDecorationAt(i)
            if (decorationClass.isInstance(itemDecoration)) {
                return true
            }
        }
        return false
    }

    inner class HorizontalItemDecoration(private val spaceWidth: Int) :
        RecyclerView.ItemDecoration() {

        override fun getItemOffsets(
            outRect: Rect,
            view: View,
            parent: RecyclerView,
            state: RecyclerView.State
        ) {
            val position = parent.getChildAdapterPosition(view)
            outRect.left = -55

            if (data[position].isSelected && position == 0) {
                outRect.left = -30

            }
        }
    }

    companion object {
        var isSetCurrentDate = false
    }

}