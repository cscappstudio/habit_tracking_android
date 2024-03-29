package com.cscmobi.habittrackingandroid.presentation.ui.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DiffUtil
import com.cscmobi.habittrackingandroid.R
import com.cscmobi.habittrackingandroid.base.BaseActivity
import com.cscmobi.habittrackingandroid.base.BaseBindingAdapter
import com.cscmobi.habittrackingandroid.data.model.CheckList
import com.cscmobi.habittrackingandroid.data.model.DataTaskHistory
import com.cscmobi.habittrackingandroid.data.model.EndDate
import com.cscmobi.habittrackingandroid.databinding.ActivityDetailTaskBinding
import com.cscmobi.habittrackingandroid.presentation.ItemBasePosistionListener
import com.cscmobi.habittrackingandroid.presentation.ui.custom.CircularSeekBar
import com.cscmobi.habittrackingandroid.presentation.ui.custom.OnCircularSeekBarChangeListener
import com.cscmobi.habittrackingandroid.presentation.ui.intent.CollectionIntent
import com.cscmobi.habittrackingandroid.presentation.ui.intent.DetailTaskIntent
import com.cscmobi.habittrackingandroid.presentation.ui.view.BottomSheetPauseTaskFragment
import com.cscmobi.habittrackingandroid.presentation.ui.view.CustomDetailTaskCalenderFragment
import com.cscmobi.habittrackingandroid.presentation.ui.view.NewHabitFragment
import com.cscmobi.habittrackingandroid.presentation.ui.viewmodel.DetailTaskViewModel
import com.cscmobi.habittrackingandroid.presentation.ui.viewstate.DetailTaskState
import com.cscmobi.habittrackingandroid.thanhlv.model.History
import com.cscmobi.habittrackingandroid.thanhlv.model.Task
import com.cscmobi.habittrackingandroid.utils.Constant
import com.cscmobi.habittrackingandroid.utils.DialogUtils
import com.cscmobi.habittrackingandroid.utils.Helper
import com.cscmobi.habittrackingandroid.utils.Helper.getMySharedPreferences
import com.cscmobi.habittrackingandroid.utils.ObjectWrapperForBinder
import com.cscmobi.habittrackingandroid.utils.Utils
import com.cscmobi.habittrackingandroid.utils.Utils.toDate
import com.cscmobi.habittrackingandroid.utils.setBackgroundApla
import com.cscmobi.habittrackingandroid.utils.setDrawableString
import com.thanhlv.ads.lib.AdMobUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.Calendar
import java.util.Date
import kotlin.math.roundToInt

class DetailTaskActivity : BaseActivity<ActivityDetailTaskBinding>() {
    private val detailTaskViewModel: DetailTaskViewModel by viewModel()

    private val childFragment: CustomDetailTaskCalenderFragment = CustomDetailTaskCalenderFragment()
    private lateinit var checklistAdapter: BaseBindingAdapter<CheckList>

    private var currentTask = Task()
    private var checkList = mutableListOf<CheckList>()
    private val bottomSheetPauseFragment = BottomSheetPauseTaskFragment()
    private var currentProgress = 0
    private var currentGoal = 0
    private var currentTaskHistory: History? = null
    private var historyId = -1L
    private var progressStep = 1f

    override fun getLayoutRes(): Int {
        return R.layout.activity_detail_task
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun initView() {
        val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        transaction.replace(binding.fragContainer.id, childFragment).commit()

        binding.layoutSteak1.txtInfo.text = getString(R.string.finished)
        binding.layoutSteak2.txtInfo.text = getString(R.string.missed)
        binding.layoutSteak3.txtInfo.text = getString(R.string.long_streak)
        binding.layoutSteak4.txtInfo.text = getString(R.string.completion)

        binding.layoutSteak1.ivInfo.setImageResource(R.drawable.ic_steak_finish)
        binding.layoutSteak2.ivInfo.setImageResource(R.drawable.ic_steak_miss)
        binding.layoutSteak3.ivInfo.setImageResource(R.drawable.ic_steak_skip)
        binding.layoutSteak4.ivInfo.setImageResource(R.drawable.ic_steak_rate)

        intent.extras?.let {
            val dataTask = it.getBinder(Constant.EditTask) as? ObjectWrapperForBinder
            dataTask?.let { task ->
                lifecycleScope.launch {
                    initData(task.data as Task)
                    detailTaskViewModel.userIntent.send(DetailTaskIntent.fetchHistoryByTask(task.data as Task))
                }
                setupColor()
            }
            historyId = it.getLong(Constant.IDHISTORY, -1L)

            if (Helper.chooseDate > Helper.currentDate.toDate()) {
                binding.sbProgress.setOnTouchListener { _, _ -> true }
                binding.ivPlus.setOnTouchListener { _, _ -> true }
                binding.ivMinus.setOnTouchListener { _, _ -> true }
                binding.txtFinish.setOnTouchListener { _, _ -> true }
            }

        }
        observe()

        if (Utils.isShowAds(this)) {
            binding.adView.visibility = View.VISIBLE
            AdMobUtils.createBanner(
                this,
                binding.root.context.getString(R.string.admob_banner_id),
                AdMobUtils.BANNER_COLLAPSIBLE_BOTTOM,
                binding.adView,
                object : AdMobUtils.Companion.LoadAdCallback {
                    override fun onLoaded(ad: Any?) {
                    }

                    override fun onLoadFailed() {
                        binding.adView.visibility = View.GONE
                    }
                })
        }
    }

    private fun setupColor() {
        val colorAlpha = "#33" + currentTask.color.substring(1, 7)
        binding.layoutSteak1.ivInfo.imageTintList =
            ColorStateList.valueOf(Color.parseColor(currentTask.color))
        binding.layoutSteak2.ivInfo.imageTintList =
            ColorStateList.valueOf(Color.parseColor(currentTask.color))
        binding.layoutSteak3.ivInfo.imageTintList =
            ColorStateList.valueOf(Color.parseColor(currentTask.color))
        binding.layoutSteak4.ivInfo.imageTintList =
            ColorStateList.valueOf(Color.parseColor(currentTask.color))

        binding.layoutSteak1.rootView.backgroundTintList =
            ColorStateList.valueOf(Color.parseColor(colorAlpha))
        binding.layoutSteak2.rootView.backgroundTintList =
            ColorStateList.valueOf(Color.parseColor(colorAlpha))
        binding.layoutSteak3.rootView.backgroundTintList =
            ColorStateList.valueOf(Color.parseColor(colorAlpha))
        binding.layoutSteak4.rootView.backgroundTintList =
            ColorStateList.valueOf(Color.parseColor(colorAlpha))

        binding.bgBoxCurrentStreak.backgroundTintList =
            ColorStateList.valueOf(Color.parseColor(currentTask.color))


        childFragment.setColor(currentTask.color)
    }

    private fun observe() {
        lifecycleScope.launch {
            detailTaskViewModel.history.collect {
                if (it.isNotEmpty()) {
                    Log.d("DEBUGHISTORY", it.toString())
                    if (historyId != -1L)
                        currentTaskHistory = it.find { it.id == historyId }
                    setUpTaskHistoryData(detailTaskViewModel.getDetailHistory(it, currentTask.id))
                }

            }
        }
    }

    private fun updateTaskHistory(task: Task, currentProgress: Int) {
        currentTaskHistory?.let {
            val index = it.taskInDay.indexOfFirst { it.taskId == task.id }
            if (index != -1) {
                it.taskInDay[index].progressGoal = currentProgress
                it.taskInDay[index].progress =
                    Helper.calTaskProgress(currentProgress, task.goal!!.target)

                detailTaskViewModel.updateHistory(it)
            }

        }
    }

    private fun setUpTaskHistoryData(listDataTaskHistory: List<DataTaskHistory>) {
        try {
            var pauseDate = -1L
            var endPauseDate = -1L
            val c = Calendar.getInstance()

            if (currentTask.pauseDate != null) {
                pauseDate = currentTask.pauseDate!!

                c.time = Date(pauseDate)
                if (currentTask.pause != -1)
                    c.add(Calendar.DAY_OF_MONTH, currentTask.pause)
                endPauseDate = c.time.time
            }

            var finishDay = 0
            var missDay = 0
            var longStreak = 0
            var currentStreak = 0
            Log.d("listDataTaskHistory", listDataTaskHistory.size.toString())
            listDataTaskHistory.forEach {
                if (pauseDate != -1L) {
                    if (currentTask.pause == -1) {
                        if (pauseDate <= it.date) {
                            it.isPause = true
                        }

                    } else {
                        if (it.date in pauseDate..endPauseDate) {
                            it.isPause = true

                        }
                    }

                }
                var progressGoal = if (it.taskInDay.progressGoal > (currentTask.goal?.target
                        ?: 1)
                ) currentTask.goal?.target else it.taskInDay.progressGoal
                val progress = (progressGoal!! * 100f / currentTask.goal!!.target).roundToInt()
                it.taskInDay.progress = progress

                if (progress == 100) {
                    if (!it.isPause) {
                        finishDay++
                        currentStreak++
                    }
                } else {
                    if (!it.isPause) {
                        currentStreak = 0
                        missDay++
                    }
                }

                if (longStreak < currentStreak) {
                    longStreak = currentStreak
                }
            }

            var rate = ((finishDay.toFloat() / listDataTaskHistory.size.toFloat()) * 100).toInt()

            if (missDay > 0) missDay--

            binding.txtStreak.text = "$currentStreak ${getString(R.string.days)}"
            binding.layoutSteak1.txtDay.text = "$finishDay ${getString(R.string.days)}"
            binding.layoutSteak2.txtDay.text = "$missDay ${getString(R.string.days)}"
            binding.layoutSteak3.txtDay.text = "$longStreak ${getString(R.string.days)}"
            binding.layoutSteak4.txtDay.text = "$rate %"

            childFragment.setData(listDataTaskHistory)
        } catch (e: Exception) {
            binding.ctlCurrentStreak.visibility = View.GONE
            binding.llInfoStreak.visibility = View.GONE
        }
    }

    @SuppressLint("SetTextI18n")
    private fun initData(task: Task) {

        currentTask = task
        currentTask.also {
            checkList = it.checklist as MutableList<CheckList>
            it.ava?.let { it1 ->
                binding.ivTask.setDrawableString(it1)
                binding.ivTask.imageTintList = ColorStateList.valueOf(Color.parseColor(it.color))
            }
            val bgColor = "#33" + it.color.substring(1, it.color.length)
            binding.frIvTask.backgroundTintList = ColorStateList.valueOf(Color.parseColor(bgColor))
            binding.txtRepeat.text = detailTaskViewModel.showRepeatString(it.repeate, this)
            binding.txtRemind.text = detailTaskViewModel.showReminder(it.remind, this)
            binding.txtNameTask.text = it.name
            if (it.note.isEmpty()) {
                binding.txtNoteTask.visibility = View.GONE
            } else binding.txtNoteTask.text = it.note

            if (it.goal != null) {
                binding.ctlProgressGoal.visibility = if (it.goal!!.isOn) View.VISIBLE else View.GONE

                progressStep = (100 / (it.goal!!.target ?: 1)).toFloat()

                if (it.goal!!.target > 2)
                    binding.sbProgress.step = progressStep
                binding.sbProgress.progress =
                    ((it.goal!!.currentProgress ?: 0).toFloat()) * progressStep

                currentProgress = (it.goal!!.currentProgress ?: 0)
                currentGoal = (it.goal!!.target ?: 1)


                binding.txtProgress.text = (it.goal!!.currentProgress ?: 0).toString()
                binding.txtGoalTarget.text =
                    "${getString(R.string.goal)}: ${(it.goal!!.target ?: 1)} ${it.goal!!.unit}"
            }

            it.checklist?.let {
                checkList = it.toMutableList()
                Helper.isNewDay = getMySharedPreferences().getLong(
                    "currentDDay",
                    -1L
                ) != Helper.currentDate.toDate()

                if (Helper.isNewDay) {
                    checkList.forEach { cl ->
                        cl.status = false
                    }
                }
                initCheckList()
            }

        }
    }


    @SuppressLint("NotifyDataSetChanged")
    private fun initCheckList() {
        binding.layoutChecklist.showNewSubtask.visibility = View.GONE
        binding.layoutChecklist.ivAdd.visibility = View.GONE
        checklistAdapter = BaseBindingAdapter(
            R.layout.item_checklist,
            layoutInflater,
            object : DiffUtil.ItemCallback<CheckList>() {
                override fun areItemsTheSame(oldItem: CheckList, newItem: CheckList): Boolean {
                    return oldItem.title == newItem.title
                }

                override fun areContentsTheSame(oldItem: CheckList, newItem: CheckList): Boolean {
                    return oldItem == newItem
                }
            })

        checklistAdapter.setListener(object : ItemBasePosistionListener {
            override fun onItemClicked(p: Int) {
                checkList[p].status = !checkList[p].status
            }
        }
        )

        if (checkList.isEmpty()) {
            binding.layoutChecklist.root.visibility = View.GONE
            return
        }
        checklistAdapter.submitList(checkList)
        checklistAdapter?.notifyDataSetChanged()

        binding.layoutChecklist.rcvSubtask.adapter = checklistAdapter


    }

    override fun setEvent() {

        binding.ivBack.setOnClickListener {
            finish()
        }

        binding.ivEdit.setOnClickListener {
            Intent(this, NewHabitActivity::class.java).apply {
                val bundle = Bundle()
                bundle.putBinder(Constant.EditTask, ObjectWrapperForBinder(currentTask))
                this.putExtras(bundle)
                startActivity(this)
                finish()
            }
        }

        binding.ivSkip.setOnClickListener {
            bottomSheetPauseFragment.show(supportFragmentManager, bottomSheetPauseFragment.tag)
            bottomSheetPauseFragment.actionPause = {
                currentTask.pause = it
                currentTaskHistory?.let { h ->
                    currentTask.pauseDate = h.date

                }

                lifecycleScope.launch {
                    detailTaskViewModel.userIntent.send(DetailTaskIntent.UpdateTask(currentTask))
                }
                Toast.makeText(this, "Update success", Toast.LENGTH_SHORT).show()
            }
        }

        binding.ivDelete.setOnClickListener {
            DialogUtils.showDeleteTaskDialog(this, {
                val c = Calendar.getInstance()
                c.add(Calendar.DAY_OF_MONTH, -1)

                if (currentTask.endDate == null) currentTask.endDate = EndDate(true, c.time.time)
                else {
                    currentTask.endDate?.endDate = c.time.time
                    currentTask.endDate?.isOpen = true
                }

                Toast.makeText(this, getString(R.string.delete_success), Toast.LENGTH_SHORT).show()

                lifecycleScope.launch {
                    detailTaskViewModel.userIntent.send(DetailTaskIntent.DeleteTask(currentTask, 0))
                }
                currentTaskHistory?.let {
                    detailTaskViewModel.deleteTaskInHistory(it.date!!, currentTask.id)
                }

                finish()


            }, {
                // delete all
                Toast.makeText(this, "Delete success", Toast.LENGTH_SHORT).show()

                lifecycleScope.launch {
                    detailTaskViewModel.userIntent.send(DetailTaskIntent.DeleteTask(currentTask, 1))
                }

                currentTaskHistory?.let {
                    if (currentTask.startDate != null)
                        detailTaskViewModel.deleteTaskInHistory(
                            currentTask.startDate!!,
                            currentTask.id
                        )
                }
                finish()

            })

        }

        binding.ivPlus.setOnClickListener {
            currentProgress += ((1 / progressStep) + 1).roundToInt()
            if (currentProgress > currentGoal) {
                binding.sbProgress.progress = 100f
            } else binding.sbProgress.progress = currentProgress.toFloat() * progressStep


            binding.txtProgress.text = currentProgress.toString()

        }

        binding.ivMinus.setOnClickListener {
            currentProgress -= ((1 / progressStep) + 1).roundToInt()

            if (currentProgress < 0) {
                currentProgress = 0
            }


            if (currentProgress * progressStep <= 100) {
                binding.sbProgress.progress = currentProgress * progressStep
            } else binding.sbProgress.progress = 100f
            binding.txtProgress.text = currentProgress.toString()

        }

        binding.txtFinish.setOnClickListener {
            binding.sbProgress.progress = 100f
            currentProgress = (100f / progressStep).roundToInt()

            binding.txtProgress.text = currentProgress.toString()

        }

        binding.sbProgress.setOnRoundedSeekChangeListener(object : OnCircularSeekBarChangeListener {

            override fun onProgressChange(CircularSeekBar: CircularSeekBar, progress: Float) {
                currentProgress = ((currentGoal.toFloat() / 100f) * progress).roundToInt()
                binding.txtProgress.text = currentProgress.toString()

            }


            override fun onStartTrackingTouch(CircularSeekBar: CircularSeekBar) {}


            override fun onStopTrackingTouch(CircularSeekBar: CircularSeekBar) {}
        });
    }


    override fun onStop() {
        super.onStop()

        lifecycleScope.launch {
            updateTaskHistory(currentTask, currentProgress)
            withContext(Dispatchers.IO) {
                currentTask.checklist = checkList
                detailTaskViewModel.userIntent.send(DetailTaskIntent.UpdateTask(currentTask))

            }
            delay(1000L)
        }
    }

}