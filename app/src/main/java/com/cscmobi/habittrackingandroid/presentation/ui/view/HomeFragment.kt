package com.cscmobi.habittrackingandroid.presentation.ui.view

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
import com.cscmobi.habittrackingandroid.data.model.EndDate
import com.cscmobi.habittrackingandroid.data.model.TaskInDay
import com.cscmobi.habittrackingandroid.data.model.WeekCalenderItem
import com.cscmobi.habittrackingandroid.databinding.FragmentHomeBinding
import com.cscmobi.habittrackingandroid.presentation.ItemChallengeHomeListener
import com.cscmobi.habittrackingandroid.presentation.ItemTaskWithEdit
import com.cscmobi.habittrackingandroid.presentation.OnItemClickPositionListener
import com.cscmobi.habittrackingandroid.presentation.ui.activity.DetailTaskActivity
import com.cscmobi.habittrackingandroid.presentation.ui.activity.MainActivity
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
import com.cscmobi.habittrackingandroid.utils.Constant.IDLE
import com.cscmobi.habittrackingandroid.utils.DialogUtils
import com.cscmobi.habittrackingandroid.utils.Helper
import com.cscmobi.habittrackingandroid.utils.Helper.calTaskProgress
import com.cscmobi.habittrackingandroid.utils.Helper.getMySharedPreferences
import com.cscmobi.habittrackingandroid.utils.ObjectWrapperForBinder
import com.cscmobi.habittrackingandroid.utils.Utils
import com.cscmobi.habittrackingandroid.utils.Utils.isListChanged
import com.cscmobi.habittrackingandroid.utils.Utils.isShowAds
import com.cscmobi.habittrackingandroid.utils.Utils.removeSelf
import com.cscmobi.habittrackingandroid.utils.Utils.toDate
import com.cscmobi.habittrackingandroid.utils.setSpanTextView
import com.elconfidencial.bubbleshowcase.BubbleShowCase
import com.elconfidencial.bubbleshowcase.BubbleShowCaseBuilder
import com.elconfidencial.bubbleshowcase.BubbleShowCaseListener
import com.google.android.material.chip.Chip
import com.thanhlv.ads.lib.AdMobUtils
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter
import org.threeten.bp.format.TextStyle
import java.util.Calendar
import java.util.Date
import java.util.Locale
import kotlin.math.roundToInt


class HomeFragment : BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate) {
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
    override fun initView(view: View) {
        binding.isTasksEmpty = true

        homeViewModel.initDateWeek()
        initWeekApdater()
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
        calenderDialogHomeFragment.actionDateSelect = {
            currentDate = it.toDate()
            Log.d("chaulq_____currentDate", Date(currentDate).toString())
            lifecycleScope.launch {
                homeViewModel.initDateWeek(currentDate)

                homeViewModel.userIntent.send(HomeIntent.FetchTasksbyDate(currentDate))
                getDatesofWeek()
                if (currentDate == Helper.currentDate.toDate()) {
                    binding.llToday.visibility = View.GONE
                    binding.txtDate.text = "Today"
                } else {
                    binding.llToday.visibility = View.VISIBLE

                    val dateFormat = DateFormat.format("yyyy-MM-dd", currentDate)
                    binding.txtDate.text = dateFormat

                }

                binding.llToday.visibility =
                    if (currentDate == Helper.currentDate.toDate()) View.INVISIBLE else View.VISIBLE
                if (currentDate < Helper.currentDate.toDate()) {
                    binding.ivArrow.setImageResource(R.drawable.nav_arrow_right)

                } else {
                    binding.ivArrow.setImageResource(R.drawable.nav_arrow_left)
                }
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
            }

        }

    }

    fun getSizeTasksNormal() = listNormalTask.filter { it.id != IDLE }.size

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
                                .description("Swipe left to edit, pause or delete your task")
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
                        binding.isTasksEmpty = false
                        binding.llTask.visibility = View.VISIBLE
                        binding.adView.visibility = View.GONE


                        if (!hasInitChip) {
                            val categories = arrayListOf<String>()
                            categories.add("All")
                            categories.addAll(state.tasks.map { it.tag }.distinct())
                            initChips(categories)

                            hasInitChip = true
                        }

                        binding.isTasksEmpty = false

                        listTask.clear()
                        listTask = state.tasks.toMutableList()
                        initTaskAdapter()
                        setUpChallenge()

                        setUpView(listTask)
                        binding.isTasksEmpty = false


                        lifecycleScope.launch {

                            homeViewModel.getHistorybyDate(currentDate)
                            // homeViewModel.userIntent.send(HomeIntent.InsertTaskHistory(history))
                        }

                    }

                    is HomeState.Empty -> {
                        binding.isTasksEmpty = true
                        showAds()

                    }

                    else -> {
                    }
                }
            }


        }

        lifecycleScope.launch {
            homeViewModel.currentHistory.collect {
                val listTask = this@HomeFragment.listTask.filter { it.id != IDLE }.toMutableList()
                if (it.id != IDLE) {
                    if (it.id != -1) {
                        currentHistory = it

                        if (isListChanged(it.taskInDay.map { it.taskId }, listTask.map { it.id })) {


                            currentHistory!!.taskInDay = getTasksInday(listTask)

                            homeViewModel.userIntent.send(HomeIntent.UpdateHistory(currentHistory!!))
                        }

                        data.indexOfFirst { it.localDate!!.toDate() == currentDate }

                        it.taskInDay.forEach { taskInDay ->
                            var index = listTask.indexOfFirst { it.id == taskInDay.taskId }
                            if (index != -1) {
                                listTask[index].goal?.currentProgress = taskInDay.progressGoal
                            }

                        }
                        taskAdapter.notifyDataSetChanged()
                        tasksChallenge = listTask.filter { !it.challenge.isNullOrEmpty() }.map {
                            ChallengeHomeItem(idTask = it.id,note = it.name!!, name = it.challenge!!, stateDone = (it.goal?.currentProgress
                                ?: 0) >= (it.goal?.target ?: 1),   requireContext().resources.getResourceEntryName(R.drawable.bg_home_challenge))
                        }.toMutableList()
                        challengeHomeAdpater.submitList(tasksChallenge)
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
            binding.llTask.visibility = View.GONE
            binding.adView.visibility = View.VISIBLE
            AdMobUtils.createNativeAd(
                binding.root.context,
                binding.root.context.getString(R.string.native_id),
                binding.adView,
                object : AdMobUtils.Companion.LoadAdCallback {
                    override fun onLoaded(ad: Any?) {
                    }

                    override fun onLoadFailed() {
                        binding.llTask.visibility = View.VISIBLE
                        binding.adView.visibility = View.GONE
                    }
                })
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
        if (task.isNullOrEmpty())  {
            setUpProgress2Tasks(0,0)
            setUpProgress1Tasks(0,0)
            binding.sbProgress.progressDisplay = 0
            binding.sbProgress.setText("0%")
            return
        }

        var taskFinishNumber = 0
        val taskNotChallenge = task.filter { it.challenge.isNullOrEmpty() }
         var totalTask = 0
         var taskDone = 0
        totalTask = taskNotChallenge.size
        taskNotChallenge.forEach {
            it.goal?.let { goal ->
                if (goal.currentProgress >= goal.target) taskFinishNumber++
            }
        }
        taskDone = taskFinishNumber
        var taskChallenge = task.filter { !it.challenge.isNullOrEmpty() }.groupBy { it.challenge }

        var totalChallenge = taskChallenge.size
        var challengeFinish = taskChallenge.size

        taskChallenge.forEach { t, u ->
            var notDoneTask = u.find { it.goal!!.currentProgress < it.goal!!.target }
            if (notDoneTask != null) challengeFinish--
        }

        setUpProgress1Tasks(challengeFinish, totalChallenge)
        setUpProgress2Tasks(taskDone,totalTask)
        binding.sbProgress.setText(
            ((taskDone + challengeFinish).toFloat() / (totalChallenge + totalTask).toFloat() * 100f).roundToInt()
                .toString() + "%"
        )
        binding.sbProgress.progressDisplay =
            ((taskDone + challengeFinish).toFloat() / (totalChallenge + totalTask).toFloat() * 100f).roundToInt()
        if (binding.sbProgress.progressDisplay == 100) {
            requireActivity().let { mactivity ->
                with(mactivity.getMySharedPreferences()) {
                    if (!this.getBoolean("isDialogCongraShown1", false)) {
                        DialogUtils.showCongratulationDialog(
                            mactivity,
                            "Congratulation!",
                            SpannableString("All habits for today are completed!"),
                            "Only 78% of users have done this."
                        )
                        this.edit().putBoolean("isDialogCongraShown1", true).apply()
                    }

                    lastHistory?.let {
                        if (it.currentStreakDay + 1 == 2 && it.date != Helper.currentDate.toDate()) {
                            if (!this.getBoolean("isDialogCongraShown2", false)) {
                                DialogUtils.showCongratulationDialog(
                                    mactivity,
                                    "Whoa!",
                                    SpannableString("You just hit 2-day habit streak!").apply {
                                        this.setSpan(
                                            ForegroundColorSpan(
                                                ContextCompat.getColor(
                                                    mactivity,
                                                    R.color.skyblue
                                                )
                                            ), 13, 14, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                                        )
                                    },
                                    "Consistency is key, and you're on the right track. Keep crushing those goals! \uD83D\uDCAA\uD83C\uDF1F"
                                )
                                this.edit().putBoolean("isDialogCongraShown2", true).apply()
                            }
                        }

                    }
                }
            }

        }

    }

    fun setUpTask() {

    }

    fun setUpChallenge() {

        tasksChallenge = listTask.filter { !it.challenge.isNullOrEmpty() }.map {
            ChallengeHomeItem(idTask = it.id,note = it.name!!, name = it.challenge!!, stateDone = (it.goal?.currentProgress
                ?: 0) >= (it.goal?.target ?: 1),   requireContext().resources.getResourceEntryName(R.drawable.bg_home_challenge))
        }.toMutableList()

         challengeHomeAdpater = BaseBindingAdapter<ChallengeHomeItem>(
            R.layout.item_challenge_home,
            layoutInflater,
            object : DiffUtil.ItemCallback<ChallengeHomeItem>() {
                override fun areItemsTheSame(
                    oldItem: ChallengeHomeItem,
                    newItem: ChallengeHomeItem
                ): Boolean {
                    return oldItem.idTask == oldItem.idTask
                }

                override fun areContentsTheSame(
                    oldItem: ChallengeHomeItem,
                    newItem: ChallengeHomeItem
                ): Boolean {
                    return oldItem == newItem
                }
            })

//        challengeHomeAdpater.submitList(
//            listOf(
//                ChallengeHomeItem(
//                    "Drink water when you wake up",
//                    "Morning glow",
//                    false,
//                    requireContext().resources.getResourceEntryName(R.drawable.bg_home_challenge)
//                ),
//                ChallengeHomeItem(
//                    "Drink water when you wake up",
//                    "Morning glow",
//                    true,
//                    requireContext().resources.getResourceEntryName(R.drawable.bg_home_challenge)
//                ),
//                ChallengeHomeItem(
//                    "Drink water when you wake up",
//                    "BBBBBB",
//                    false,
//                    requireContext().resources.getResourceEntryName(R.drawable.bg_home_challenge)
//                ),
//                ChallengeHomeItem(
//                    "Drink water when you wake up",
//                    "CCCCCC",
//                    false,
//                    requireContext().resources.getResourceEntryName(R.drawable.bg_home_challenge)
//                ),
//                ChallengeHomeItem(
//                    "Drink water when you wake up",
//                    "AAAAAA",
//                    false,
//                    requireContext().resources.getResourceEntryName(R.drawable.bg_home_challenge)
//                ),
//            )
//        )
        challengeHomeAdpater.setListener(object : ItemChallengeHomeListener<ChallengeHomeItem> {
            override fun onItemClicked(item: ChallengeHomeItem, p: Int) {

            }

            override fun onDone(item: ChallengeHomeItem, p: Int) {
                item.stateDone = !item.stateDone
                lifecycleScope.launch {
                    currentHistory?.let { currentHistory ->
                         listTask.find { it.id == item.idTask }?.let {
                             it.goal?.currentProgress = it.goal?.target!!
                             currentHistory.taskInDay = getTasksInday(listTask)
                             var tasksFinishNum =
                                 currentHistory.taskInDay.filter { it.progress == 100 }.size
                             var tasksNum = currentHistory.taskInDay.size

                             data[currentPosWeek]
                                 .progress = calTaskProgress(tasksFinishNum, tasksNum)

                             homeViewModel.userIntent.send(HomeIntent.UpdateHistory(currentHistory))
                             setUpView(listTask)

                         }
                        weekAdapter.notifyItemChanged(currentPosWeek)


                    }
                    delay(500L)
                }
                challengeHomeAdpater.notifyItemChanged(p)
            }
        })
        challengeHomeAdpater.submitList(tasksChallenge)
        challengeHomeAdpater.notifyDataSetChanged()
        binding.rcvChallenge.adapter = challengeHomeAdpater

    }

    private fun setUpProgress1Tasks(challengeFinish: Int, totalChallenge: Int) {
        binding.txtProgress1.text = "$challengeFinish/$totalChallenge"
        binding.txtProgress1.setSpanTextView(R.color.forest_green)
    }

    private fun setUpProgress2Tasks(taskDone:Int, totalTask:Int) {
        binding.txtProgress2.text = "$taskDone/$totalTask"
        binding.txtProgress2.setSpanTextView(R.color.forest_green)

    }


    private fun initTaskAdapter() {
//         val goalTargets = homeViewModel.tasks.map { it.goal?.target }
//         val goalProgress = homeViewModel.tasks.map { it.goal?.currentProgress }
        listNormalTask = listTask.filter { it.challenge.isNullOrEmpty() }.toMutableList()
        taskAdapter = TaskAdapter(requireActivity(), object : ItemTaskWithEdit<Task> {
            override fun onItemClicked(item: Task, p: Int) {
//                Intent(requireActivity(), DetailTaskActivity::class.java).apply {
//                    putExtra(Constant.task_id, item.id)
//                    startActivity(this)
//                }
                Intent(requireActivity(), DetailTaskActivity::class.java).apply {
                    val bundle = Bundle()
                    bundle.putBinder(Constant.EditTask, ObjectWrapperForBinder(item))
                    currentHistory?.let {
                        bundle.putInt(Constant.IDHISTORY, it.id)

                    }
                    this.putExtras(bundle)

                    startActivity(this)
                }
            }

            override fun skip(item: Task, p: Int) {
                bottomSheetPauseFragment.show(childFragmentManager, bottomSheetPauseFragment.tag)
                bottomSheetPauseFragment.actionPause = {
                    item.pause = it
                    item.pauseDate = currentDate
                    lifecycleScope.launch {
                        homeViewModel.userIntent.send(HomeIntent.UpdateTask(item))

                    }
                    Toast.makeText(requireContext(), "Update success", Toast.LENGTH_SHORT).show()
                    taskAdapter.notifyItemChanged(p)
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
                    listTask.removeIf { it.id == item.id }
                    //listTask.removeAt(p)
                    // taskAdapter?.notifyItemRemoved(p)
                    val adsIndex = listNormalTask.indexOfFirst { it.id == IDLE }
                    if (adsIndex != -1 && adsIndex != 1) {
                        listNormalTask.removeAt(adsIndex)
                        listNormalTask.add(1, Task(id = IDLE, name = "ads"))
                    }

                    taskAdapter?.notifyDataSetChanged()
                    Toast.makeText(requireContext(), "Delete success", Toast.LENGTH_SHORT).show()

                    lifecycleScope.launch {
                        homeViewModel.userIntent.send(HomeIntent.DeleteTask(item, 0))
                    }
                    homeViewModel.deleteTaskInHistory(currentDate, item.id)


                }, {
                    // delete all
                   // listTask.removeAt(p)
                    listTask.removeIf { it.id == item.id }

                    val adsIndex = listNormalTask.indexOfFirst { it.id == IDLE }
                    if (adsIndex != -1 && adsIndex != 1) {
                        listNormalTask.removeAt(adsIndex)
                        listNormalTask.add(1, Task(id = IDLE, name = "ads"))
                        taskAdapter.notifyDataSetChanged()
                    } else
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
                    delay(500L)
                }


            }

            override fun onResume(p: Int, item: Task) {
                item.pause = 0
                item.pauseDate = null
                lifecycleScope.launch {
                    homeViewModel.userIntent.send(HomeIntent.UpdateTask(item))
                }

            }

        })
        taskAdapter.date = currentDate

        binding.rcvTasks.adapter = taskAdapter

        if (listNormalTask.isNotEmpty()) {
            if (Utils.isShowAds(requireContext()) )
                listNormalTask.add(1, Task(id = IDLE, name = "ads"))
        }else{
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
            if (homeViewModel.currentWeekPos == -1) {
                homeViewModel.initDateWeek()
                currentDate = Helper.currentDate.toDate()
                getDatesofWeek()

                weekAdapter.notifyDataSetChanged()
                if (currentDate == Helper.currentDate.toDate()) {
                    binding.llToday.visibility = View.GONE
                    binding.txtDate.text = "Today"
                } else {
                    binding.llToday.visibility = View.VISIBLE

                    val dateFormat = DateFormat.format("yyyy-MM-dd", currentDate)
                    binding.txtDate.text = dateFormat
                }
                binding.rcvWeek.postDelayed(Runnable {
                    scrollToPositionWithCentering(homeViewModel.currentWeekPos)
                }, 200L)
                lifecycleScope.launch {
                    homeViewModel.userIntent.send(HomeIntent.FetchTasksbyDate(currentDate))
                }
                return@setOnClickListener
            }


            binding.llToday.visibility = View.GONE
            scrollToPositionWithCentering(homeViewModel.currentWeekPos)

            for (i in (homeViewModel.currentWeekPos - 3)..(homeViewModel.currentWeekPos + 3)) {
                data[i].isSelected = false
            }
            data[homeViewModel.currentWeekPos].isSelected = true
            weekAdapter.notifyItemChanged(homeViewModel.currentWeekPos)

            currentDate = Helper.currentDate.toDate()
            if (currentDate == Helper.currentDate.toDate()) {
                binding.llToday.visibility = View.GONE
                binding.txtDate.text = "Today"
            } else {
                binding.llToday.visibility = View.VISIBLE

                val dateFormat = DateFormat.format("yyyy-MM-dd", currentDate)
                binding.txtDate.text = dateFormat
            }
            lifecycleScope.launch {
                homeViewModel.userIntent.send(HomeIntent.FetchTasksbyDate(currentDate))
            }
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
        Helper.chooseDate = currentDate

    }

    companion object {
        var isSetCurrentDate = false
    }

}