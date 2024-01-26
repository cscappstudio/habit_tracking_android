package com.cscmobi.habittrackingandroid.presentation.ui.activity

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
import com.cscmobi.habittrackingandroid.data.model.TaskInDay
import com.cscmobi.habittrackingandroid.databinding.ActivityDetailTaskBinding
import com.cscmobi.habittrackingandroid.presentation.ItemBasePosistionListener
import com.cscmobi.habittrackingandroid.presentation.ui.custom.CircleSeekBar
import com.cscmobi.habittrackingandroid.presentation.ui.intent.DetailTaskIntent
import com.cscmobi.habittrackingandroid.presentation.ui.intent.HomeIntent
import com.cscmobi.habittrackingandroid.presentation.ui.view.BottomSheetPauseTaskFragment
import com.cscmobi.habittrackingandroid.presentation.ui.view.CustomCalenderFragment
import com.cscmobi.habittrackingandroid.presentation.ui.view.CustomDetailTaskCalenderFragment
import com.cscmobi.habittrackingandroid.presentation.ui.view.NewHabitFragment
import com.cscmobi.habittrackingandroid.presentation.ui.viewmodel.DetailTaskViewModel
import com.cscmobi.habittrackingandroid.presentation.ui.viewstate.DetailTaskState
import com.cscmobi.habittrackingandroid.thanhlv.model.History
import com.cscmobi.habittrackingandroid.thanhlv.model.Task
import com.cscmobi.habittrackingandroid.utils.Constant
import com.cscmobi.habittrackingandroid.utils.DialogUtils
import com.cscmobi.habittrackingandroid.utils.ObjectWrapperForBinder
import com.cscmobi.habittrackingandroid.utils.setBackgroundApla
import com.cscmobi.habittrackingandroid.utils.setDrawableString
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.Calendar


class DetailTaskActivity : BaseActivity<ActivityDetailTaskBinding>() {
    private val detailTaskViewModel: DetailTaskViewModel by viewModel()

    val childFragment: CustomDetailTaskCalenderFragment = CustomDetailTaskCalenderFragment()
    private lateinit var checklistAdapter: BaseBindingAdapter<CheckList>

    private var currentTask = Task()
    private var checkList = mutableListOf<CheckList>()
    private val bottomSheetPauseFragment = BottomSheetPauseTaskFragment()

    override fun getLayoutRes(): Int {
        return R.layout.activity_detail_task
    }

    override fun initView() {
        val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        transaction.replace(binding.fragContainer.id, childFragment).commit()

        binding.layoutSteak1.txtInfo.text = "Finished"
        binding.layoutSteak2.txtInfo.text = "Missed"
        binding.layoutSteak3.txtInfo.text = "Long Streak"
        binding.layoutSteak4.txtInfo.text = "Rate"

        binding.layoutSteak1.ivInfo.setImageResource(R.drawable.ic_steak_finish)
        binding.layoutSteak2.ivInfo.setImageResource(R.drawable.ic_steak_miss)
        binding.layoutSteak3.ivInfo.setImageResource(R.drawable.ic_steak_skip)
        binding.layoutSteak4.ivInfo.setImageResource(R.drawable.ic_steak_rate)


        val taskId = intent.getIntExtra(Constant.task_id, -1)
        if (taskId != -1) {
            lifecycleScope.launch {
                detailTaskViewModel.userIntent.send(DetailTaskIntent.FetchTaskbyId(taskId))
            }
        }
         observe()


        test()



    }

    fun test() {
        binding.sbProgress.max = 5
        binding.sbProgress.setProgressDisplayAndInvalidate(0)



    }



    private fun observe() {
        lifecycleScope.launch {
            detailTaskViewModel.state.collect {
                when (it) {
                    DetailTaskState.Idle -> {

                    }

                    is DetailTaskState.TaskById -> {
                        initData(it.task)
                        detailTaskViewModel.userIntent.send(DetailTaskIntent.FetchHistory)
                    }
                }
            }

        }

        lifecycleScope.launch {
            detailTaskViewModel.history.collect {
                Toast.makeText(this@DetailTaskActivity, "fetch history", Toast.LENGTH_SHORT).show()
                if (it.isNotEmpty()) {

                    setUpTaskHistoryData(detailTaskViewModel.getDetailHistory(it, currentTask.id))
                }

            }
        }
    }

    private fun setUpTaskHistoryData(listDataTaskHistory: List<DataTaskHistory>) {
        try {


        var finishDay = 0
        var missDay = 0
        var longStreak = listDataTaskHistory[listDataTaskHistory.size-1].taskInDay.longStreak
        var currentStreak = listDataTaskHistory[listDataTaskHistory.size-1].taskInDay.currentStreak
        listDataTaskHistory.forEach {
            if (it.taskInDay.progress == 100) finishDay++
            if (it.taskInDay.progress == 0) missDay++
        }

        var rate =  ((finishDay.toFloat()  / listDataTaskHistory.size.toFloat())*100).toInt()

        binding.txtStreak.text = "$currentStreak days"
        binding.layoutSteak1.txtDay.text = "$finishDay days"
        binding.layoutSteak2.txtDay.text = "$missDay days"
        binding.layoutSteak3.txtDay.text = "$longStreak days"
        binding.layoutSteak4.txtDay.text = "$rate %"

        Log.d("testtttt", listDataTaskHistory.toString())

        childFragment.setData(listDataTaskHistory) }catch (e: Exception) {
            binding.ctlCurrentStreak.visibility = View.GONE
            binding.llInfoStreak.visibility = View.GONE
        }

    }

    private fun initData(task: Task) {
        currentTask= task
        currentTask.also {
            checkList = it.checklist as MutableList<CheckList>
            it.ava?.let { it1 -> binding.ivTask.setDrawableString(it1) }
            binding.ivTask.imageTintList = ColorStateList.valueOf(Color.parseColor(it.color))
            binding.frIvTask.setBackgroundApla(it.color ?: "#33EBB2BD", 20)
            Log.d("TESTDATA", it.toString())
            binding.txtRepeat.text = detailTaskViewModel.showRepeatString(it.repeate)
            binding.txtRemind.text = detailTaskViewModel.showReminder(it.remind)
            binding.txtNameTask.text = it.name

            binding.txtNoteTask.text = it.note


            if (it.goal != null && it.goal!!.isOn == true) {
                binding.sbProgress.max = it.goal!!.target ?: 5
                binding.sbProgress.setProgressDisplayAndInvalidate(it.goal!!.currentProgress ?: 0)
                binding.ctlProgressGoal.visibility = View.VISIBLE
                binding.txtProgress.text = (it.goal!!.currentProgress ?:0).toString()
                binding.txtGoalTarget.text = (it.goal!!.target ?: 5).toString()
                //TODO need to do

            } else {
                binding.ctlProgressGoal.visibility = View.GONE
            }

            it.checklist?.let {
                checkList = it.toMutableList()
                initCheckList()

            }

        }
    }


    private fun initCheckList() {
        binding.layoutChecklist.edtAdd.visibility = View.GONE
        binding.layoutChecklist.ivAdd.visibility = View.GONE

        //checkList = task.checklist as MutableList<CheckList>

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

        checklistAdapter.submitList(checkList)
        checklistAdapter?.notifyDataSetChanged()

        binding.layoutChecklist.rcvSubtask.adapter = checklistAdapter

    }

    override fun setEvent() {

        binding.ivBack.setOnClickListener {
            finish()
        }

        binding.ivEdit.setOnClickListener {
            Intent(this,NewHabitActivity::class.java).apply {
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

                lifecycleScope.launch {
                    detailTaskViewModel.userIntent.send(DetailTaskIntent.UpdateTask(currentTask))

                }
                Toast.makeText(this, "Update success", Toast.LENGTH_SHORT).show()
            }        }

        binding.ivDelete.setOnClickListener {
            DialogUtils.showDeleteTaskDialog(this, {
                val c = Calendar.getInstance()
                c.add(Calendar.DAY_OF_MONTH, -1)

                if (currentTask.endDate == null) currentTask.endDate = EndDate(true, c.time)
                else {
                    currentTask.endDate?.endDate = c.time
                    currentTask.endDate?.isOpen = true
                }

                Toast.makeText(this, "Delete success", Toast.LENGTH_SHORT).show()

                lifecycleScope.launch {
                    detailTaskViewModel.userIntent.send(DetailTaskIntent.DeleteTask(currentTask,0))
                }
                finish()

                //TODO xoa task id task nay  cua ngay hien tai trong  bang? history


            }, {
                // delete all
                Toast.makeText(this, "Delete success", Toast.LENGTH_SHORT).show()

                lifecycleScope.launch {
                    detailTaskViewModel.userIntent.send(DetailTaskIntent.DeleteTask(currentTask,1))
                }
                //TODO xoa task id nay  trong bang? history
                finish()

            })

        }

        binding.ivPlus.setOnClickListener {
//            this.task.goal?.currentProgress = this.task.goal?.currentProgress?.plus(1)
            binding.sbProgress.setProgressDisplayAndInvalidate(binding.sbProgress.progressDisplay + 1)
            binding.txtProgress.text =  binding.sbProgress.progressDisplay.toString()

        }

        binding.ivMinus.setOnClickListener {
//            binding.sbProgress.progressDisplay =  binding.sbProgress.progressDisplay++
//            this.task.goal?.currentProgress = this.task.goal?.currentProgress?.minus(1)
            binding.sbProgress.setProgressDisplayAndInvalidate( binding.sbProgress.progressDisplay - 1)
            binding.txtProgress.text =  binding.sbProgress.progressDisplay.toString()

        }

        binding.txtFinish.setOnClickListener {
//            this.task.goal?.currentProgress = this.task.goal?.target
            binding.sbProgress.setProgressDisplayAndInvalidate(binding.sbProgress.max)
            binding.txtProgress.text = binding.sbProgress.progressDisplay.toString()


        }

        binding.sbProgress.setSeekBarChangeListener(object :
            CircleSeekBar.OnSeekBarChangedListener {
            override fun onPointsChanged(
                circleSeekBar: CircleSeekBar?,
                points: Int,
                fromUser: Boolean
            ) {

            }

            override fun onStartTrackingTouch(circleSeekBar: CircleSeekBar?) {


            }

            override fun onStopTrackingTouch(circleSeekBar: CircleSeekBar?) {
//                this@DetailTaskActivity.task.goal?.currentProgress = circleSeekBar!!.progressDisplay

            }

            override fun onTrackingChanging(progress: Int) {

                binding.txtProgress.text = progress.toString()
            }

        })


    }

}