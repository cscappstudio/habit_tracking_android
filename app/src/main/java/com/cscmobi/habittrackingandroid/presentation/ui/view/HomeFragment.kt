package com.cscmobi.habittrackingandroid.presentation.ui.view

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.Rect
import android.os.Build
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.format.DateFormat
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.Toast
import androidx.core.content.ContextCompat
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
import com.cscmobi.habittrackingandroid.data.model.ChallengeTask
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
import com.cscmobi.habittrackingandroid.thanhlv.database.AppDatabase
import com.cscmobi.habittrackingandroid.thanhlv.model.Challenge
import com.cscmobi.habittrackingandroid.thanhlv.model.History
import com.cscmobi.habittrackingandroid.thanhlv.model.Task
import com.cscmobi.habittrackingandroid.thanhlv.ui.DetailChallengeActivity
import com.cscmobi.habittrackingandroid.thanhlv.ui.MoodActivity
import com.cscmobi.habittrackingandroid.thanhlv.ui.SubscriptionsActivity
import com.cscmobi.habittrackingandroid.utils.CalendarUtil
import com.cscmobi.habittrackingandroid.utils.Constant
import com.cscmobi.habittrackingandroid.utils.Constant.IDLE
import com.cscmobi.habittrackingandroid.utils.DialogUtils
import com.cscmobi.habittrackingandroid.utils.Helper
import com.cscmobi.habittrackingandroid.utils.Helper.calTaskProgress
import com.cscmobi.habittrackingandroid.utils.Helper.freeIAP
import com.cscmobi.habittrackingandroid.utils.Helper.getMySharedPreferences
import com.cscmobi.habittrackingandroid.utils.ObjectWrapperForBinder
import com.cscmobi.habittrackingandroid.utils.Utils
import com.cscmobi.habittrackingandroid.utils.Utils.isListChanged
import com.cscmobi.habittrackingandroid.utils.Utils.isShowAds
import com.cscmobi.habittrackingandroid.utils.Utils.toDate
import com.cscmobi.habittrackingandroid.utils.setSpanTextView
import com.elconfidencial.bubbleshowcase.BubbleShowCase
import com.elconfidencial.bubbleshowcase.BubbleShowCaseBuilder
import com.elconfidencial.bubbleshowcase.BubbleShowCaseListener
import com.google.android.material.chip.Chip
import com.google.gson.Gson
import com.ironsource.fa
import com.thanhlv.ads.lib.AdMobUtils
import com.thanhlv.fw.helper.DisplayUtils
import com.thanhlv.fw.helper.MyUtils
import com.thanhlv.fw.spf.SPF
import gateway.v1.clientInfo
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter
import org.threeten.bp.format.TextStyle
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import kotlin.math.abs
import kotlin.math.roundToInt


class HomeFragment : BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate) {
    private var oldCurrentDate: Long = -1
    private var hasInitChip = false
    private val homeViewModel: HomeViewModel by viewModel()
    private lateinit var taskAdapter: TaskAdapter
    private lateinit var challengeHomeAdpater: BaseBindingAdapter<ChallengeHomeItem>
    private var listTask = mutableListOf<Task>()

    private lateinit var weekAdapter: WeekAdapter
    private var data = arrayListOf<WeekCalenderItem>()
    private var date = arrayListOf<LocalDate>()
    private var c = Helper.currentDate
    lateinit var startOfWeek: LocalDate

    private var currentDate = 0L
    private var currentPosWeek = -1
    private var lastHistory: History? = null
    private var listNormalTask = mutableListOf<Task>()
    private var tasksChallenge = mutableListOf<ChallengeHomeItem>()

    private val bottomSheetPauseFragment = BottomSheetPauseTaskFragment()
    var currentHistory: History? = null
    private var calenderDialogHomeFragment = CalenderDialogHomeFragment()
    private var challengePassHistoryTaskMap: Map<String, List<ChallengeTask>> = mapOf()
    private var challenges = mutableListOf<Challenge>()
    private var resetCurrentTasks = false
    private var isPassHistoryChallengeDone = false

    override fun initView(view: View) {
        binding.isTasksEmpty = true


        homeViewModel.initDateWeek()
        initWeekAdapter()
        binding.txtProgress1.setSpanTextView(R.color.forest_green)
        binding.txtProgress2.setSpanTextView(R.color.forest_green)

        currentDate = Helper.currentDate.toDate()

        lifecycleScope.launch {
            homeViewModel.userIntent.send(HomeIntent.FetchTasksbyDate(currentDate))

            homeViewModel.histories.collect {
                if (it.isNullOrEmpty()) return@collect
                it.forEach {
                    data.forEach { weekData ->
                        if (it.date!!.toDate() == weekData.localDate!!.toDate()) {
                            var tasksFinishNum = it.taskInDay.filter { it.progress == 100 }.size
                            var tasksNum = it.taskInDay.size

                            weekData.progress = calTaskProgress(tasksFinishNum, tasksNum)

                        }

                    }

                }

                lastHistory = if (it.size == 1) it[0] else it.last()
            }
        }

        lifecycleScope.launch {
            homeViewModel.challenges.collect {
                challenges = it
            }
        }

        calenderDialogHomeFragment.actionDateSelect = {
            currentDate = it.toDate()
            Log.d("chaulq_____currentDate", Date(currentDate).toString())
            lifecycleScope.launch {
                homeViewModel.initDateWeek(currentDate)

                homeViewModel.userIntent.send(HomeIntent.FetchTasksbyDate(currentDate))
                getDatesofWeek()
                resolveScrollButtonToday(currentDate)
                weekAdapter.notifyDataSetChanged()

                data.indexOfFirst { it.localDate!!.toDate() == currentDate }.let {
                    if (it != -1) {
                        data.forEachIndexed { index, item ->
                            item.isSelected = it == index
                        }
                        binding.rcvWeek.postDelayed(Runnable {
                            scrollToPositionWithCentering(it)
                        }, 200L)
                    }

                }
                calenderDialogHomeFragment.dismiss()
                MyUtils.hideNavigationBar(requireActivity())
            }

        }


    }


    override fun onResume() {
        super.onResume()
        observeState()
        with(requireActivity().getMySharedPreferences()) {

            if (!this.getBoolean(Constant.FIRSTSHOWUSECASE, false)) {

                try {
                    binding.rcvTasks.postDelayed({
                        val viewHolder = binding.rcvTasks.findViewHolderForAdapterPosition(0)

                        if (viewHolder != null) {
                            // The viewHolder is not null, meaning the item is currently visible
                            val viewItem =
                                viewHolder.itemView.findViewById<FrameLayout>(R.id.fr_root)
                            viewHolder.itemView.visibility = View.INVISIBLE

                            BubbleShowCaseBuilder(requireActivity()) //Activity instance
                                .description(getString(R.string.swipe_left_to_edit_pause_or_delete_your_task))
                                .arrowPosition(BubbleShowCase.ArrowPosition.TOP)
                                .targetView(viewItem) //View to point out
                                .highlightMode(BubbleShowCase.HighlightMode.VIEW_SURFACE)
                                .backgroundColor(
                                    ContextCompat.getColor(
                                        requireContext(),
                                        R.color.jade_black
                                    )
                                ) //Bubble background color
                                .textColor(Color.WHITE) //Bubble Text color
                                .descriptionTextSize(14) //Subtitle text size in SP (default value 14sp)
                                .listener(object :
                                    BubbleShowCaseListener { //Listener for user actions
                                    override fun onTargetClick(bubbleShowCase: BubbleShowCase) {
                                        //Called when the user clicks the target
                                        viewHolder.itemView.visibility = View.VISIBLE
                                        this@with.edit().putBoolean(Constant.FIRSTSHOWUSECASE, true)
                                            .apply()

                                    }

                                    override fun onCloseActionImageClick(bubbleShowCase: BubbleShowCase) {
                                        //Called when the user clicks the close button
                                        viewHolder.itemView.visibility = View.VISIBLE
                                        this@with.edit().putBoolean(Constant.FIRSTSHOWUSECASE, true)
                                            .apply()

                                    }

                                    override fun onBubbleClick(bubbleShowCase: BubbleShowCase) {
                                        //Called when the user clicks on the bubble
                                        viewHolder.itemView.visibility = View.VISIBLE
                                        this@with.edit().putBoolean(Constant.FIRSTSHOWUSECASE, true)
                                            .apply()

                                    }

                                    override fun onBackgroundDimClick(bubbleShowCase: BubbleShowCase) {
                                        //Called when the user clicks on the background dim
                                    }
                                })
                                .showOnce("BUBBLE_SHOW_CASE_TASK_ID")
                                .show() //Display the ShowCase
                            // Do something with yourView
                        } else {
                            // The item is not currently visible or not yet bound
                        }
                    }, 100L)

                } catch (e: Exception) {

                }
            }
        }

        lifecycleScope.launch {
            homeViewModel.challengeTaskMap.collect {
                challengePassHistoryTaskMap = it
            }
        }


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

    @SuppressLint("NotifyDataSetChanged")
    private fun observeState() {
        lifecycleScope.launch {
            homeViewModel.state.collect { state ->
                if (currentDate < Helper.currentDate.toDate()) oldCurrentDate = currentDate

                when (state) {
                    is HomeState.Tasks -> {


                        binding.rcvChallenge.visibility = View.VISIBLE

                        binding.llTask.visibility = View.VISIBLE
                        binding.adView.visibility = View.GONE
//                        listTask.clear()
//                        listTask.addAll(state.tasks.toMutableList())
                        listTask = state.tasks.toMutableList()
                        if (!hasInitChip) {

                            val categories = arrayListOf<String>()
                            categories.add("   All   ")
                            categories.addAll(state.tasks.map { it.tag }.distinct())
                            initChips(categories)
//                            setUpView(listTask)

                            hasInitChip = true
                        }

                        setUpView(listTask)
                        initTaskAdapter()
                        setUpChallenge()

                        if (currentDate == Helper.currentDate.toDate() && oldCurrentDate < currentDate) {
                            currentHistory?.let {
                                it.taskInDay.forEach { taskInDay ->
                                    var index = listTask.indexOfFirst { it.id == taskInDay.taskId }
                                    if (index != -1) {

                                        listTask[index].goal?.currentProgress =
                                            taskInDay.progressGoal
                                        println("done aaaaaaaaaaaaaaaaaaaa ${taskInDay.progressGoal}")
                                    }

                                }
                                taskAdapter?.notifyDataSetChanged()
                                tasksChallenge =
                                    listTask.filter { it.challenge.isNotEmpty() }.map {
                                        ChallengeHomeItem(
                                            idTask = it.id,
                                            note = it.name!!,
                                            name = it.challenge!!,
                                            stateDone = (it.goal?.currentProgress
                                                ?: 0) >= (it.goal?.target ?: 1),
                                            it.imgChallenge
                                        )
                                    }.toMutableList()
                                challengeHomeAdpater.submitList(tasksChallenge)
                            }
                        }

                        lifecycleScope.launch {
                            homeViewModel.getHistorybyDate(currentDate)
                        }

                    }

                    is HomeState.Empty -> {
                        listTask = mutableListOf()
                        binding.rcvChallenge.visibility = View.GONE
                        binding.isTasksEmpty = true
                        setUpView(listTask)

                    }

                    else -> {
                    }
                }

            }


        }

        lifecycleScope.launch {
            homeViewModel.currentHistory.collect {
                println("thanhlv resettttttttttttttttttttttttttttttttttt " + currentDate)
                listTask = listTask.filter { it.id != IDLE }.toMutableList()
                if (it.id != IDLE) {
                    if (it.id != -1L) {
                        currentHistory = it


                        val dataIndex = data.indexOfFirst { it.localDate!!.toDate() == currentDate }
                        if (dataIndex != -1) {
                            val tasksFinishNum = it.taskInDay.filter { it.progress == 100 }.size
                            val tasksNum = it.taskInDay.size

                            data[dataIndex].progress = calTaskProgress(tasksFinishNum, tasksNum)
                        }

                        it.taskInDay.forEach { taskInDay ->
                            val index = listTask.indexOfFirst { it.id == taskInDay.taskId }
                            if (index != -1) {

                                listTask[index].goal?.currentProgress = taskInDay.progressGoal

                            }

                        }

                        taskAdapter.notifyDataSetChanged()
                        tasksChallenge = listTask.filter { !it.challenge.isNullOrEmpty() }.map {
                            ChallengeHomeItem(
                                idTask = it.id,
                                note = it.name!!,
                                name = it.challenge!!,
                                stateDone = (it.goal?.currentProgress
                                    ?: 0) >= (it.goal?.target ?: 1),
                                it.imgChallenge
                            )
                        }.toMutableList()
                        challengeHomeAdpater.submitList(tasksChallenge)

                        if (isListChanged(it.taskInDay.map { it.taskId }, listTask.map { it.id })) {


                            currentHistory!!.taskInDay = getTasksInday(listTask)

                            if (!updateChallenge) {
                                homeViewModel.userIntent.send(
                                    HomeIntent.UpdateHistory(
                                        currentHistory!!
                                    )
                                )
                            } else {
                                updateChallenge = false
                            }
                        }
                    } else {
                        if (currentDate <= Helper.currentDate.toDate()) {
                            var history =
                                History(date = currentDate, taskInDay = getTasksInday(listTask))
                            homeViewModel.insertTaskHistory(history)
                            currentHistory = history

                        }
                    }
                    setUpView(listTask)
                }

            }
        }

    }


    private fun showAds() {
        if (isShowAds(requireContext())) {
//            binding.llTask.visibility = View.GONE
            binding.adView.visibility = View.VISIBLE
            AdMobUtils.createNativeAd(
                binding.root.context,
                binding.root.context.getString(R.string.native_id),
                binding.adView,
                object : AdMobUtils.Companion.LoadAdCallback {
                    override fun onLoaded(ad: Any?) {
                        binding.ivPencil.visibility = View.INVISIBLE
                        binding.txtNotask.visibility = View.INVISIBLE
                    }

                    override fun onLoadFailed() {
                        //  binding.llTask.visibility = View.VISIBLE
                        binding.adView.visibility = View.GONE
                        binding.ivPencil.visibility = View.VISIBLE
                        binding.txtNotask.visibility = View.VISIBLE

                    }
                })
        } else {
            if (listNormalTask.isEmpty()) {
                binding.ivPencil.visibility = View.VISIBLE
                binding.txtNotask.visibility = View.VISIBLE
            }
        }
    }

    private fun getTasksInday(tasks: List<Task>): List<TaskInDay> {
        var tasksInday = mutableListOf<TaskInDay>()
        tasksInday.clear()

        tasks.forEach {
            if (it.id != IDLE)
                tasksInday.add(
                    TaskInDay(
                        it.id,
                        progress = calTaskProgress(it.goal!!.currentProgress, it.goal!!.target),
                        progressGoal = it.goal!!.currentProgress,
                    )
                )
        }
        return tasksInday
    }

    private fun setUpView(data: MutableList<Task>) {
        val task = data.filter { it.id != IDLE }
        if (task.isNullOrEmpty()) {
            setUpProgress2Tasks(0, 0)
            setUpProgress1Tasks(0, 0)
            binding.sbProgress.progressDisplay = 0
            binding.sbProgress.setText("0%")
            binding.txtContent.text = getString(R.string.you_re_almost_done)
            return
        }

        var taskFinishNumber = 0
        val taskNotChallenge = task.filter { it.challenge.isEmpty() }
        var totalTask = 0
        var taskDone = 0
//        totalTask = taskNotChallenge.size
        taskNotChallenge.forEach {
            if (!checkTaskPaused(it)) {
                totalTask++
                it.goal?.let { goal ->
                    if (goal.currentProgress >= goal.target) taskFinishNumber++
                }
            }
        }
        taskDone = taskFinishNumber
        var taskChallenge = task.filter { it.challenge.isNotEmpty() }.groupBy { it.challenge }

        var totalChallenge = taskChallenge.size
        var challengeFinish = taskChallenge.size

        taskChallenge.forEach { t, u ->
            var notDoneTask = u.find { it.goal!!.currentProgress < it.goal!!.target }
            if (notDoneTask != null) challengeFinish--
        }

        setUpProgress1Tasks(challengeFinish, totalChallenge)
        setUpProgress2Tasks(taskDone, totalTask)
        binding.sbProgress.setText(
            ((taskDone + challengeFinish).toFloat() / (totalChallenge + totalTask).toFloat() * 100f).roundToInt()
                .toString() + "%"
        )
        binding.sbProgress.progressDisplay =
            ((taskDone + challengeFinish).toFloat() / (totalChallenge + totalTask).toFloat() * 100f).roundToInt()
        if (binding.sbProgress.progressDisplay == 100) {
            binding.txtContent.text = getString(R.string.awesome)
            binding.txtProgress1.visibility = View.INVISIBLE
            binding.txtProgress2.visibility = View.INVISIBLE
            binding.txtUnit1.visibility = View.INVISIBLE
            binding.txtUnit2.visibility = View.INVISIBLE
            binding.txtDoneTask.visibility = View.VISIBLE


            requireActivity().let { mactivity ->
                with(mactivity.getMySharedPreferences()) {
                    if (!this.getBoolean("isDialogCongraShown1", false)) {
                        DialogUtils.showCongratulationDialog(
                            mactivity,
                            getString(R.string.congratulation),
                            SpannableString(getString(R.string.all_habits_for_today_are_completed)),
                            getString(R.string.only_78_of_users_have_done_this)
                        )
                        this.edit().putBoolean("isDialogCongraShown1", true).apply()
                    }

                    lastHistory?.let {
                        if (it.currentStreakDay + 1 == 2 && it.date != Helper.currentDate.toDate()) {
                            if (!this.getBoolean("isDialogCongraShown2", false)) {
                                DialogUtils.showCongratulationDialog(
                                    mactivity,
                                    getString(R.string.whoa),
                                    SpannableString(getString(R.string.you_just_hit_2_day_habit_streak)).apply {
                                        this.setSpan(
                                            ForegroundColorSpan(
                                                ContextCompat.getColor(
                                                    mactivity,
                                                    R.color.skyblue
                                                )
                                            ), 13, 14, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                                        )
                                    },
                                    getString(R.string.consistency_is_key_and_you_re_on_the_right_track_keep_crushing_those_goals)
                                )
                                this.edit().putBoolean("isDialogCongraShown2", true).apply()
                            }
                        }

                    }
                }
            }

        } else {
            binding.txtContent.text = getString(R.string.you_re_almost_done)
            binding.txtProgress1.visibility = View.VISIBLE
            binding.txtProgress2.visibility = View.VISIBLE
            binding.txtUnit1.visibility = View.VISIBLE
            binding.txtUnit2.visibility = View.VISIBLE
            binding.txtDoneTask.visibility = View.INVISIBLE
        }

    }

    private fun checkTaskPaused(task: Task): Boolean {
        if (task.pause == 0 || task.pauseDate == null) return false
        if (task.pause == -1) return true

        val diff = abs(CalendarUtil.getToDayMs() - CalendarUtil.startDayMs(task.pauseDate!!))
        return diff < task.pause * 24 * 60 * 60 * 1000
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setUpChallenge() {
        tasksChallenge = listTask.filter { !it.challenge.isNullOrEmpty() }.map {
            ChallengeHomeItem(
                idTask = it.id,
                note = it.name!!,
                name = it.challenge!!,
                stateDone = (it.goal?.currentProgress
                    ?: 0) >= (it.goal?.target ?: 1),
                it.imgChallenge
            )
        }.toMutableList()

        challengeHomeAdpater = BaseBindingAdapter<ChallengeHomeItem>(
            R.layout.item_challenge_home,
            layoutInflater,
            object : DiffUtil.ItemCallback<ChallengeHomeItem>() {
                override fun areItemsTheSame(
                    oldItem: ChallengeHomeItem,
                    newItem: ChallengeHomeItem
                ): Boolean {
                    return oldItem.idTask == newItem.idTask
                }

                override fun areContentsTheSame(
                    oldItem: ChallengeHomeItem,
                    newItem: ChallengeHomeItem
                ): Boolean {
                    return oldItem == newItem
                }
            })

        challengeHomeAdpater.setListener(object : ItemChallengeHomeListener<ChallengeHomeItem> {
            override fun onItemClicked(item: ChallengeHomeItem, p: Int) {
                val intent = Intent(requireContext(), DetailChallengeActivity::class.java)
                runBlocking {
                    val challenge = AppDatabase.getInstance(requireContext()).dao()
                        .findChallengeByName(item.name)
                    intent.putExtra("data", Gson().toJson(challenge))
                    startActivity(intent)
                }
            }

            override fun onDone(item: ChallengeHomeItem, p: Int) {

                if (currentDate != Helper.currentDate.toDate()) {
                    return
                }

                tasksChallenge[p].stateDone = !item.stateDone
                challengeHomeAdpater.notifyItemChanged(p)
                challengePassHistoryTaskMap[item.name]?.forEachIndexed { index, it ->

                }


                lifecycleScope.launch {
                    currentHistory?.let { currentHistory ->
                        listTask.find { it.id == item.idTask }?.let {
                            it.goal?.currentProgress =
                                if (tasksChallenge[p].stateDone) it.goal?.target!! else 0
                            currentHistory.taskInDay = getTasksInday(listTask)
                            var tasksFinishNum =
                                currentHistory.taskInDay.filter { it.progress == 100 }.size
                            var tasksNum = currentHistory.taskInDay.size

                            data[currentPosWeek]
                                .progress = calTaskProgress(tasksFinishNum, tasksNum)
                            homeViewModel.userIntent.send(HomeIntent.UpdateHistory(currentHistory))
                            setUpView(listTask)
                        }

                    }
//                    delay(500L)
                }
                // check if current task is finish then show dialog congratulation
                val currentChallenge = challenges.find { it.name == item.name }
                currentChallenge?.let {
                    if (it.finish) {
                        requireActivity().let { mactivity ->
                            with(mactivity.getMySharedPreferences()) {
                                if (!this.getBoolean(
                                        "isDialogCongraChallenge${it.name}Shown",
                                        false
                                    )
                                ) {
                                    DialogUtils.showCongratulationDialog(
                                        mactivity,
                                        getString(R.string.congratulation),
                                        SpannableString(getString(R.string.you_just_finished_challenge)),
                                        getString(R.string.only_78_of_users_have_done_this)
                                    )
                                    this.edit()
                                        .putBoolean("isDialogCongraChallenge${it.name}Shown", true)
                                        .apply()
                                }
                            }
                        }
                    }
                }
            }
        })
        challengeHomeAdpater.submitList(tasksChallenge)
        challengeHomeAdpater.notifyDataSetChanged()
        binding.rcvChallenge.adapter = challengeHomeAdpater
    }

    private fun setUpProgress1Tasks(challengeFinish: Int, totalChallenge: Int) {
        binding.txtProgress1.text = "$challengeFinish/$totalChallenge"
        binding.txtProgress1.setSpanTextView(R.color.forest_green)

        binding.txtContent.text = getString(R.string.you_re_almost_done)
        binding.txtProgress1.visibility = View.VISIBLE
        binding.txtProgress2.visibility = View.VISIBLE
        binding.txtUnit1.visibility = View.VISIBLE
        binding.txtUnit2.visibility = View.VISIBLE
        binding.txtDoneTask.visibility = View.INVISIBLE
    }

    private fun setUpProgress2Tasks(taskDone: Int, totalTask: Int) {
        binding.txtProgress2.text = "$taskDone/$totalTask"
        binding.txtProgress2.setSpanTextView(R.color.forest_green)

        binding.txtContent.text = getString(R.string.you_re_almost_done)
        binding.txtProgress1.visibility = View.VISIBLE
        binding.txtProgress2.visibility = View.VISIBLE
        binding.txtUnit1.visibility = View.VISIBLE
        binding.txtUnit2.visibility = View.VISIBLE
        binding.txtDoneTask.visibility = View.INVISIBLE
    }

    private fun initTaskAdapter() {

        listNormalTask = listTask.filter { it.challenge.isNullOrEmpty() }.toMutableList()
        taskAdapter = TaskAdapter(requireActivity(), object : ItemTaskWithEdit<Task> {
            override fun onItemClicked(item: Task, p: Int) {
                Intent(requireActivity(), DetailTaskActivity::class.java).apply {
                    val bundle = Bundle()
                    bundle.putBinder(Constant.EditTask, ObjectWrapperForBinder(item))
                    currentHistory?.let {
                        bundle.putLong(Constant.IDHISTORY, it.id)
                    }
                    this.putExtras(bundle)
                    startActivity(this)
                }
            }

            override fun skip(item: Task, p: Int) {
                if (!freeIAP.canSkip(requireActivity())) {
                    val intent = Intent(requireActivity(), SubscriptionsActivity::class.java)
                    startActivity(intent)
                    return
                }

                if (!bottomSheetPauseFragment.isAdded)
                    bottomSheetPauseFragment.show(
                        childFragmentManager,
                        bottomSheetPauseFragment.tag
                    )
                bottomSheetPauseFragment.actionPause = {
                    item.pause = it
                    item.pauseDate = currentDate
                    lifecycleScope.launch {
                        homeViewModel.userIntent.send(HomeIntent.UpdateTask(item))
                    }
                    Toast.makeText(requireContext(), "Update success", Toast.LENGTH_SHORT).show()
                    taskAdapter.notifyItemChanged(p)
                    freeIAP.isSkip = true
                    updateAllTaskPause()
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
                    listNormalTask.removeIf { it.id == item.id }
                    //listTask.removeAt(p)
                    // taskAdapter?.notifyItemRemoved(p)
                    val adsIndex = listNormalTask.indexOfFirst { it.id == IDLE }
                    if (adsIndex != -1 && adsIndex != 1) {
                        listNormalTask.removeAt(adsIndex)
                        listNormalTask.add(1, Task(id = IDLE, name = "ads"))
                    }

                    taskAdapter?.notifyDataSetChanged()
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.delete_success),
                        Toast.LENGTH_SHORT
                    ).show()

                    lifecycleScope.launch {
                        homeViewModel.userIntent.send(HomeIntent.DeleteTask(item, 0))
                    }
                    homeViewModel.deleteTaskInHistory(currentDate, item.id)
                }, {
                    // delete all
                    // listTask.removeAt(p)
                    listNormalTask.removeIf { it.id == item.id }

                    val adsIndex = listNormalTask.indexOfFirst { it.id == IDLE }
                    if (adsIndex != -1 && adsIndex != 1) {
                        listNormalTask.removeAt(adsIndex)
                        listNormalTask.add(1, Task(id = IDLE, name = "ads"))
                    }
                    taskAdapter?.notifyItemRemoved(p)
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.delete_success),
                        Toast.LENGTH_SHORT
                    ).show()

                    lifecycleScope.launch {
                        homeViewModel.userIntent.send(HomeIntent.DeleteTask(item, 1))
                    }

                    if (item.startDate != null) {
                        homeViewModel.deleteTaskInHistory(item.startDate!!.toDate(), item.id)
                    }
                    if (freeIAP.rewardTimes > 0) {
                        freeIAP.rewardTimes--
                    }
                })
            }

            override fun onItemChange(p: Int, item: Task, isChange: Boolean) {
                if (isChange) {
                    item.goal?.currentProgress = item.goal?.target!!
                    binding.lottie.playAnimation()
                } else {
                    item.goal?.currentProgress = 0
                }

//                runBlocking {
//                    AppDatabase.getInstance(requireContext()).dao().updateTask(item)
//                }


                taskAdapter.notifyItemChanged(p)
                setUpView(listTask)
                lifecycleScope.launch {
                    currentHistory?.let { currentHistory ->
                        currentHistory.taskInDay = getTasksInday(listTask)
                        var tasksFinishNum =
                            currentHistory.taskInDay.filter { it.progress == 100 }.size
                        var tasksNum = currentHistory.taskInDay.size
                        data[currentPosWeek]
                            .progress = calTaskProgress(tasksFinishNum, tasksNum)
                        weekAdapter.notifyItemChanged(currentPosWeek)
                        homeViewModel.userIntent.send(HomeIntent.UpdateHistory(currentHistory))
                    }
//                    delay(500L)
                }
            }

            override fun onResume(p: Int, item: Task) {
                item.pause = 0
                item.pauseDate = null
                lifecycleScope.launch {
                    homeViewModel.userIntent.send(HomeIntent.UpdateTask(item))
                }
                freeIAP.isSkip = false
                updateAllTaskPause()
            }

        })
        taskAdapter.date = currentDate

        binding.rcvTasks.adapter = taskAdapter

        if (listNormalTask.isNotEmpty()) {
            if (Utils.isShowAds(requireContext()))
                listNormalTask.add(1, Task(id = IDLE, name = "ads"))
            binding.isTasksEmpty = false

        } else {
            binding.isTasksEmpty = true
            showAds()
        }

        taskAdapter.submitList(listNormalTask)
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
                chip.typeface = ResourcesCompat.getFont(requireContext(), R.font.worksans_bold)
                chip.setTextSize(
                    TypedValue.COMPLEX_UNIT_PX,
                    DisplayUtils.spToPx(14f).toFloat()
                )
                chip.layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    DisplayUtils.dpToPx(54f)
                )

                if (it == "   All   ") {
                    chip.isChecked = true
                    changeChipState(true, chip)

                } else {
                    changeChipState(false, chip)

                }


                chip.setOnCheckedChangeListener { compoundButton, b ->

                    if (chip.isChecked) {
                        listNormalTask = if (chip.tag.toString() != "   All   ") {
                            listTask.filter { it.challenge.isNullOrEmpty() && it.tag == chip.tag.toString() }
                                .toMutableList()
                        } else listTask.filter { it.challenge.isNullOrEmpty() }.toMutableList()
                        listNormalTask.add(1, Task(id = IDLE, name = "ads"))
                        taskAdapter.submitList(listNormalTask)
                        //lifecycleScope.launch {
                        // homeViewModel.userIntent.send(HomeIntent.FetchTasksbyCategory(chip.tag.toString()))
                        // }
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

    @SuppressLint("NotifyDataSetChanged")
    override fun setEvent() {
        binding.btnTodayRight.setOnClickListener { it ->
            it.visibility = View.GONE
            if (currentDate.diffDays(Helper.currentDate.toDate()) < 3) {
                binding.rcvWeek.postDelayed(
                    { scrollToPositionWithCentering(homeViewModel.currentWeekPos) },
                    100
                )
            } else scrollToPositionWithCentering(homeViewModel.currentWeekPos)
            data.forEach { day -> day.isSelected = false }
            data[homeViewModel.currentWeekPos].isSelected = true
            binding.txtDate.text = getString(R.string.today)
            weekAdapter.notifyDataSetChanged()
            currentDate = Helper.currentDate.toDate()
            lifecycleScope.launch {
                homeViewModel.userIntent.send(HomeIntent.FetchTasksbyDate(currentDate))
            }

//            setUpView(homeViewModel.tasks)
        }

        binding.btnTodayLeft.setOnClickListener { it ->
            it.visibility = View.GONE
            if (currentDate.diffDays(Helper.currentDate.toDate()) > -3) {
                binding.rcvWeek.postDelayed(
                    { scrollToPositionWithCentering(homeViewModel.currentWeekPos) },
                    100
                )
            } else scrollToPositionWithCentering(homeViewModel.currentWeekPos)
            data.forEach { day -> day.isSelected = false }
            data[homeViewModel.currentWeekPos].isSelected = true
            binding.txtDate.text = getString(R.string.today)
            weekAdapter.notifyDataSetChanged()
            currentDate = Helper.currentDate.toDate()
            lifecycleScope.launch {
                homeViewModel.userIntent.send(HomeIntent.FetchTasksbyDate(currentDate))
            }

//            setUpView(homeViewModel.tasks)
        }
        binding.btnAddMood.setOnClickListener {
            startActivity(Intent(requireContext(), MoodActivity::class.java))
        }

        binding.ivCalender.setOnClickListener {
            calenderDialogHomeFragment.show(
                childFragmentManager.beginTransaction().remove(calenderDialogHomeFragment),
                CalenderDialogHomeFragment.TAG
            )
        }
    }

    private fun initWeekAdapter() {
        addDecoration()
        getDatesofWeek()
        weekAdapter = WeekAdapter(object : OnItemClickPositionListener {
            @SuppressLint("NotifyDataSetChanged")
            override fun onItemClick(position: Int) {
                data.forEachIndexed { index, item ->
                    item.isSelected = position == index
                }
                currentDate = date[position].toDate()
                lifecycleScope.launch {
                    homeViewModel.userIntent.send(HomeIntent.FetchTasksbyDate(currentDate))
//                    delay(200)
//                    setUpView(homeViewModel.tasks)
                }

                resolveScrollButtonToday(currentDate)

                weekAdapter.notifyDataSetChanged()

                binding.rcvWeek.postDelayed(Runnable {
                    scrollToPositionWithCentering(position)
                }, 50L)
            }

        })



        weekAdapter.submitList(data)
        binding.rcvWeek.adapter = weekAdapter
        binding.rcvWeek.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val lmR = recyclerView.layoutManager
                val posF = (lmR as LinearLayoutManager).findFirstCompletelyVisibleItemPosition()
                val countInline =
                    (lmR as LinearLayoutManager).findLastCompletelyVisibleItemPosition() - posF
                if (currentDate.diffDays(Helper.currentDate.toDate()) > 0) {
                    binding.btnTodayLeft.visibility = View.GONE
                    binding.btnTodayRight.visibility = View.VISIBLE
                } else if (currentDate.diffDays(Helper.currentDate.toDate()) < 0) {
                    binding.btnTodayLeft.visibility = View.VISIBLE
                    binding.btnTodayRight.visibility = View.GONE
                } else if (posF < homeViewModel.currentWeekPos - countInline) {
                    binding.btnTodayRight.visibility = View.VISIBLE
                    binding.btnTodayLeft.visibility = View.GONE
                } else if (posF > homeViewModel.currentWeekPos) {
                    binding.btnTodayRight.visibility = View.GONE
                    binding.btnTodayLeft.visibility = View.VISIBLE
                } else {
                    binding.btnTodayRight.visibility = View.GONE
                    binding.btnTodayLeft.visibility = View.GONE
                }
            }
        })

        binding.rcvWeek.post {
            scrollToPositionWithCentering(homeViewModel.currentWeekPos)
        }
    }

    @SuppressLint("SimpleDateFormat")
    private fun resolveScrollButtonToday(time: Long) {
        if (CalendarUtil.sameDay(time, c.toDate())) {
            binding.txtDate.text = getString(R.string.today)
        } else {
            binding.txtDate.text = SimpleDateFormat("MMM dd, yyyy").format(time)
        }
    }

    fun scrollToPositionWithCentering(position: Int) {
        val layoutManager = binding.rcvWeek.layoutManager as LinearLayoutManager
        val smoothScroller = CenterSmoothScroller(binding.rcvWeek.context)
        smoothScroller.targetPosition = position
        layoutManager.startSmoothScroll(smoothScroller)
        currentPosWeek = position

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
            return (boxStart + (boxEnd - boxStart) / 2) - (viewStart + (viewEnd - viewStart) / 2) + 28
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
            val spaceWidth = resources.getDimensionPixelSize(com.intuit.sdp.R.dimen._15sdp)
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

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (!hidden) {
            SPF.getAvaProfile(requireContext())?.let {
                if (it.isNotEmpty())
                    binding.ivAvatar.setImageResource(it.toInt())
            }
        }
    }

    private fun updateAllTaskPause() {
        if (listTask.isNotEmpty())
            currentHistory?.let {
                var isAllTaskPause = true
                listTask.forEach {
                    if (it.pause == 0 || it.pauseDate == null) {
                        isAllTaskPause = false
                        return@forEach
                    }

                }


                if (it.allTaskPause != isAllTaskPause) {
                    it.allTaskPause = isAllTaskPause
                    homeViewModel.updateHistory(it)
                }
            }
    }

    override fun onPause() {
        super.onPause()

        hasInitChip = false
        Helper.chooseDate = currentDate
    }

    companion object {
        var isSetCurrentDate = false
        var updateChallenge = false
    }

}

private fun Long.diffDays(end: Long): Int {
    val startDate = Calendar.getInstance()
    startDate.timeInMillis = this
    val endDate = Calendar.getInstance()
    endDate.timeInMillis = end
    return if (startDate[Calendar.YEAR] == endDate[Calendar.YEAR]) {
        endDate[Calendar.DAY_OF_YEAR] - startDate[Calendar.DAY_OF_YEAR]
    } else if (startDate[Calendar.YEAR] < endDate[Calendar.YEAR]) {
        val daysOfYear = if (startDate[Calendar.YEAR] % 4 == 0) 366 else 365
        endDate[Calendar.DAY_OF_YEAR] + daysOfYear - startDate[Calendar.DAY_OF_YEAR]
    } else {
        val daysOfYear = if (endDate[Calendar.YEAR] % 4 == 0) 366 else 365
        endDate[Calendar.DAY_OF_YEAR] - daysOfYear - startDate[Calendar.DAY_OF_YEAR]
    }
}
