package com.cscmobi.habittrackingandroid.presentation.ui.view

import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.Rect
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import com.cscmobi.habittrackingandroid.R
import com.cscmobi.habittrackingandroid.base.BaseBindingAdapter
import com.cscmobi.habittrackingandroid.base.BaseFragment
import com.cscmobi.habittrackingandroid.data.model.ChallengeHomeItem
import com.cscmobi.habittrackingandroid.data.model.EndDate
import com.cscmobi.habittrackingandroid.data.model.TaskInDay
import com.cscmobi.habittrackingandroid.data.model.WeekCalenderItem
import com.cscmobi.habittrackingandroid.databinding.FragmentHomeBinding
import com.cscmobi.habittrackingandroid.presentation.ItemChallengeHomeListener
import com.cscmobi.habittrackingandroid.presentation.ItemTaskWithEdit
import com.cscmobi.habittrackingandroid.presentation.OnItemClickPositionListener
import com.cscmobi.habittrackingandroid.presentation.ui.activity.DetailTaskActivity
import com.cscmobi.habittrackingandroid.presentation.ui.activity.NewHabitActivity
import com.cscmobi.habittrackingandroid.presentation.ui.adapter.TaskAdapter
import com.cscmobi.habittrackingandroid.presentation.ui.adapter.WeekAdapter
import com.cscmobi.habittrackingandroid.presentation.ui.intent.HomeIntent
import com.cscmobi.habittrackingandroid.presentation.ui.viewmodel.HomeViewModel
import com.cscmobi.habittrackingandroid.presentation.ui.viewstate.HomeState
import com.cscmobi.habittrackingandroid.thanhlv.model.History
import com.cscmobi.habittrackingandroid.thanhlv.model.Task
import com.cscmobi.habittrackingandroid.thanhlv.ui.MoodActivity
import com.cscmobi.habittrackingandroid.utils.Constant
import com.cscmobi.habittrackingandroid.utils.DialogUtils
import com.cscmobi.habittrackingandroid.utils.Helper
import com.cscmobi.habittrackingandroid.utils.ObjectWrapperForBinder
import com.cscmobi.habittrackingandroid.utils.Utils.isListChanged
import com.cscmobi.habittrackingandroid.utils.Utils.toDate
import com.cscmobi.habittrackingandroid.utils.setSpanTextView
import com.google.android.material.chip.Chip
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter
import org.threeten.bp.format.TextStyle
import java.util.Calendar
import java.util.Locale
import kotlin.math.roundToInt


class HomeFragment : BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate) {
    private var hasInitChip = false
    private val homeViewModel: HomeViewModel by viewModel()
    private lateinit var taskAdapter: TaskAdapter
    private var listTask = mutableListOf<Task>()

    private lateinit var weekAdapter: WeekAdapter
    private var data = arrayListOf<WeekCalenderItem>()
    private var date = arrayListOf<LocalDate>()
    private var c = Helper.currentDate
    lateinit var startOfWeek: LocalDate
    private var totalTask = 0
    private var taskDone = 0
    private var currentDate = 0L

    private val bottomSheetPauseFragment = BottomSheetPauseTaskFragment()
    var currentHistory: History? = null

    override fun initView(view: View) {
        binding.isTasksEmpty = true

        homeViewModel.initDateWeek()
        initWeekApdater()
        setUpChallenge()
        binding.txtProgress1.setSpanTextView(R.color.forest_green)
        binding.txtProgress2.setSpanTextView(R.color.forest_green)

        currentDate = Helper.currentDate.toDate()
    }

    override fun onResume() {
        super.onResume()
        lifecycleScope.launch {
            homeViewModel.userIntent.send(HomeIntent.FetchTasksbyDate(currentDate))
        }
        observeState()

    }

    fun getDatesofWeek() {


        date.clear()
        data.clear()

        for (day in homeViewModel.listWeekData) {
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
                            listTask.clear()
                            listTask = state.tasks.toMutableList()
                            initTaskAdapter()
                            setUpView(listTask)
                            binding.isTasksEmpty = false

                        }
                        if (Helper.currentDate.toDate() == currentDate) {
                            lifecycleScope.launch {

                               homeViewModel.getHistorybyDate(currentDate)
                               // homeViewModel.userIntent.send(HomeIntent.InsertTaskHistory(history))
                            }
                        }
                    }

                    else -> {

                    }
                }
            }


        }

        lifecycleScope.launch {
            homeViewModel.currentHistory.collect {
//                if (it.taskInDay.isNullOrEmpty()) return@collect

                if (it.id != -1) {
                    currentHistory = it

                    if (isListChanged(it.taskInDay.map { it.taskId }, listTask.map { it.id })) {
                        var updateHistory = it.copy(taskInDay = getTasksInday(listTask))
                        homeViewModel.updateHistory(it)
                        currentHistory = updateHistory
                    }

                        it.taskInDay.forEach { taskInDay ->
                            var index = listTask.indexOfFirst { it.id == taskInDay.taskId }
                            if (index != -1) {
                                listTask[index].goal?.currentProgress = taskInDay.progressGoal
                                taskAdapter.notifyDataSetChanged()
                            }

                        }

                } else {
                    if (currentDate <= Helper.currentDate.toDate()) {
                        var history = History(taskInDay = getTasksInday(listTask))
                        homeViewModel.insertTaskHistory(history)
                        currentHistory = history

                    }
                }
            }
        }

    }

    private fun getTasksInday(tasks: List<Task>) : List<TaskInDay> {
        var tasksInday = mutableListOf<TaskInDay>()
        tasksInday.clear()

        tasks.forEach {
            tasksInday.add(
                TaskInDay(
                    it.id,
                    progress = (it.goal!!.currentProgress.toFloat() * 100f / it.goal!!.target.toFloat()).roundToInt(),
                    progressGoal = it.goal!!.currentProgress
                )
            )
        }
        return  tasksInday
    }
    private fun setUpView(task: MutableList<Task>) {
        var taskFinishNumber = 0
        val taskNotChallenge = task.filter { it.challenge.isNullOrEmpty() }
        totalTask = taskNotChallenge.size
        task.forEach {
            it.goal?.let { goal ->
                if (goal.currentProgress == goal.target) taskFinishNumber++
            }
        }
        taskDone = taskFinishNumber
        setUpProgress2Tasks()
    }

    fun setUpTask() {

    }

    fun setUpChallenge() {

        var challengeHomeAdpater = BaseBindingAdapter<ChallengeHomeItem>(
            R.layout.item_challenge_home,
            layoutInflater,
            object : DiffUtil.ItemCallback<ChallengeHomeItem>() {
                override fun areItemsTheSame(
                    oldItem: ChallengeHomeItem,
                    newItem: ChallengeHomeItem
                ): Boolean {
                    return oldItem.name == oldItem.name
                }

                override fun areContentsTheSame(
                    oldItem: ChallengeHomeItem,
                    newItem: ChallengeHomeItem
                ): Boolean {
                    return oldItem == newItem
                }
            })

        challengeHomeAdpater.submitList(
            listOf(
                ChallengeHomeItem(
                    "Drink water when you wake up",
                    "Morning glow",
                    false,
                    requireContext().resources.getResourceEntryName(R.drawable.bg_home_challenge)
                ),
                ChallengeHomeItem(
                    "Drink water when you wake up",
                    "Morning glow",
                    true,
                    requireContext().resources.getResourceEntryName(R.drawable.bg_home_challenge)
                ),
                ChallengeHomeItem(
                    "Drink water when you wake up",
                    "BBBBBB",
                    false,
                    requireContext().resources.getResourceEntryName(R.drawable.bg_home_challenge)
                ),
                ChallengeHomeItem(
                    "Drink water when you wake up",
                    "CCCCCC",
                    false,
                    requireContext().resources.getResourceEntryName(R.drawable.bg_home_challenge)
                ),
                ChallengeHomeItem(
                    "Drink water when you wake up",
                    "AAAAAA",
                    false,
                    requireContext().resources.getResourceEntryName(R.drawable.bg_home_challenge)
                ),
            )
        )
        challengeHomeAdpater.setListener(object : ItemChallengeHomeListener<ChallengeHomeItem> {
            override fun onItemClicked(item: ChallengeHomeItem, p: Int) {

            }

            override fun onDone(item: ChallengeHomeItem, p: Int) {
                item.stateDone = !item.stateDone
                challengeHomeAdpater.notifyItemChanged(p)
            }
        })

        binding.rcvChallenge.adapter = challengeHomeAdpater

    }

    private fun setUpProgress2Tasks() {
        binding.txtProgress2.text = "$taskDone/$totalTask"
    }


    private fun initTaskAdapter() {
//         val goalTargets = homeViewModel.tasks.map { it.goal?.target }
//         val goalProgress = homeViewModel.tasks.map { it.goal?.currentProgress }
        taskAdapter = TaskAdapter(object : ItemTaskWithEdit<Task> {
            override fun onItemClicked(item: Task, p: Int) {
//                Intent(requireActivity(), DetailTaskActivity::class.java).apply {
//                    putExtra(Constant.task_id, item.id)
//                    startActivity(this)
//                }
                Intent(requireActivity(), DetailTaskActivity::class.java).apply {
                    val bundle = Bundle()
                    bundle.putBinder(Constant.EditTask, ObjectWrapperForBinder(item))
                    currentHistory?.let {
                        bundle.putInt(Constant.IDHISTORY,it.id)

                    }
                    this.putExtras(bundle)

                    startActivity(this)
                }
            }

            override fun skip(item: Task, p: Int) {
                bottomSheetPauseFragment.show(childFragmentManager, bottomSheetPauseFragment.tag)
                bottomSheetPauseFragment.actionPause = {
                    item.pause = it

                    lifecycleScope.launch {
                        homeViewModel.userIntent.send(HomeIntent.UpdateTask(item))

                    }
                    Toast.makeText(requireContext(), "Update success", Toast.LENGTH_SHORT).show()
                }

            }

            override fun edit(item: Task, p: Int) {

                Intent(requireActivity(), NewHabitActivity::class.java).apply {
                    val bundle = Bundle()
                    bundle.putBinder(Constant.EditTask, ObjectWrapperForBinder(item))

                    this.putExtras(bundle)
                    startActivity(this)
                }
            }

            override fun delete(item: Task, p: Int) {
                DialogUtils.showDeleteTaskDialog(requireContext(), {
                    val c = Calendar.getInstance()
                    c.add(Calendar.DAY_OF_MONTH, -1)

                    if (item.endDate == null) item.endDate = EndDate(true, c.time.time)
                    else {
                        item.endDate?.endDate = c.time.time
                        item.endDate?.isOpen = true
                    }
                    listTask.removeAt(p)
                    taskAdapter?.notifyItemRemoved(p)
                    Toast.makeText(requireContext(), "Delete success", Toast.LENGTH_SHORT).show()

                    lifecycleScope.launch {
                        homeViewModel.userIntent.send(HomeIntent.DeleteTask(item, 0))

                    }
                    homeViewModel.deleteTaskInHistory(currentDate, item.id)


                }, {
                    // delete all
                    listTask.removeAt(p)
                    taskAdapter?.notifyItemRemoved(p)
                    Toast.makeText(requireContext(), "Delete success", Toast.LENGTH_SHORT).show()

                    lifecycleScope.launch {
                        homeViewModel.userIntent.send(HomeIntent.DeleteTask(item, 1))
                    }

                    if (item.startDate != null)
                        homeViewModel.deleteTaskInHistory(item.startDate!!.toDate(), item.id)

                })

            }

            override fun onItemChange(p: Int, item: Task, isChange: Boolean) {

                if (isChange) {
                    item.goal?.currentProgress = item.goal?.target!!
                } else {
                    item.goal?.currentProgress = 0
                }
                taskAdapter.notifyDataSetChanged()

                setUpView(listTask)
                lifecycleScope.launch {
                   // homeViewModel.userIntent.send(HomeIntent.UpdateTask(item))
                    currentHistory?.let { currentHistory ->
                        val index = currentHistory.taskInDay?.indexOfFirst { it.taskId == item.id }
                        if (index != null && index != -1) {
                            currentHistory.taskInDay[index].progressGoal = item.goal!!.currentProgress

                            homeViewModel.userIntent.send(HomeIntent.UpdateHistory(currentHistory))

                        }
                    }
                    delay(500L)
                }

            }

        })
        binding.rcvTasks.adapter = taskAdapter
        taskAdapter.submitList(listTask)
        taskAdapter.notifyDataSetChanged()

    }


    private fun initChips(tags: List<String>) {
        binding.chipgroupCategory.removeAllViews()

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

                if (it == "All") {
                    changeChipState(true, chip)

                } else {
                    changeChipState(false, chip)

                }


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
            for (i in (homeViewModel.currentWeekPos - 3)..(homeViewModel.currentWeekPos + 3)) {
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

                currentDate = date[position].toDate()
                lifecycleScope.launch {
                    homeViewModel.userIntent.send(HomeIntent.FetchTasksbyDate(currentDate))

//                    if (currentDate <= Helper.currentDate.toDate())
//                    {
//                        var history = History(taskInDay = getTasksInday(listTask))
//                        homeViewModel.userIntent.send(HomeIntent.InsertTaskHistory(history,currentDate))
//                    }
                }
                if (date[position] == c) {
                    binding.llToday.visibility = View.GONE
                    binding.txtDate.text = "Today"
                } else {
                    binding.llToday.visibility = View.VISIBLE
                    binding.txtDate.text = date[position].format(formatter)

                }

                binding.llToday.visibility =
                    if (date[position] == c) View.INVISIBLE else View.VISIBLE
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
        override fun calculateDtToFit(
            viewStart: Int,
            viewEnd: Int,
            boxStart: Int,
            boxEnd: Int,
            snapPreference: Int
        ): Int {
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


    override fun onPause() {
        super.onPause()
        hasInitChip = false


    }

    companion object {
        var isSetCurrentDate = false
    }

}