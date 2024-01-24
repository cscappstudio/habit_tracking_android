package com.cscmobi.habittrackingandroid.presentation.ui.activity

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableString
import android.text.style.BackgroundColorSpan
import android.text.style.RelativeSizeSpan
import android.text.style.TextAppearanceSpan
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
import com.cscmobi.habittrackingandroid.databinding.ActivityDetailTaskBinding
import com.cscmobi.habittrackingandroid.presentation.ItemBasePosistionListener
import com.cscmobi.habittrackingandroid.presentation.ui.custom.CircleSeekBar
import com.cscmobi.habittrackingandroid.presentation.ui.intent.DetailTaskIntent
import com.cscmobi.habittrackingandroid.presentation.ui.view.CustomCalenderFragment
import com.cscmobi.habittrackingandroid.presentation.ui.view.NewHabitFragment
import com.cscmobi.habittrackingandroid.presentation.ui.viewmodel.DetailTaskViewModel
import com.cscmobi.habittrackingandroid.presentation.ui.viewstate.DetailTaskState
import com.cscmobi.habittrackingandroid.thanhlv.model.Task
import com.cscmobi.habittrackingandroid.utils.Constant
import com.cscmobi.habittrackingandroid.utils.setBackgroundApla
import com.cscmobi.habittrackingandroid.utils.setDrawableString
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.Calendar
import java.util.Date
import kotlin.random.Random
import kotlin.random.nextInt


class DetailTaskActivity : BaseActivity<ActivityDetailTaskBinding>() {
    private val detailTaskViewModel: DetailTaskViewModel by viewModel()

    val childFragment: CustomCalenderFragment = CustomCalenderFragment()
    private lateinit var checklistAdapter: BaseBindingAdapter<CheckList>

    private var task = Task()
    private var checkList = mutableListOf<CheckList>()

    override fun getLayoutRes(): Int {
        return R.layout.activity_detail_task
    }

    override fun initView() {
        val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        transaction.replace(binding.fragContainer.id, childFragment).commit()

        val taskId = intent.getIntExtra(Constant.task_id, -1)
        if (taskId != -1) {
            lifecycleScope.launch {
                detailTaskViewModel.userIntent.send(DetailTaskIntent.FetchTaskbyId(taskId))
            }
        }
         observe()
        initCheckList()


        test()


    }

    fun test() {
        binding.sbProgress.max = 5
        binding.sbProgress.setProgressDisplayAndInvalidate(0)

        val calendar = Calendar.getInstance()
        calendar.time = Date() // Set the calendar's time to the current date

// Add 3 months to the current date

// Get the updated date


    }



    private fun observe() {
        lifecycleScope.launch {
            detailTaskViewModel.state.collect {
                when (it) {
                    DetailTaskState.Idle -> {

                    }

                    is DetailTaskState.TaskById -> {
                        initData(it.task)
                    }
                }
            }
        }
    }

    private fun initData(task: Task) {
        this.task = task
        this.task.also {
            checkList = it.checklist as MutableList<CheckList>
            it.ava?.let { it1 -> binding.ivTask.setDrawableString(it1) }
            binding.ivTask.imageTintList = ColorStateList.valueOf(Color.parseColor(it.color))
            binding.frIvTask.setBackgroundApla(it.color ?: "#33EBB2BD", 20)
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
            childFragment.resetColorTask(Color.parseColor(it.color))

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

        binding.layoutChecklist.rcvSubtask.adapter = checklistAdapter
    }

    override fun setEvent() {

        binding.ivBack.setOnClickListener {
            finish()
        }

        binding.ivEdit.setOnClickListener {
            Intent(this, NewHabitActivity::class.java).run {
                this.putExtra(NewHabitFragment.TAG, NewHabitFragment.NewHabitFragmentState.EDITTASK)
                startActivity(this)
            }
        }

        binding.ivSkip.setOnClickListener {
            Toast.makeText(this, "skip", Toast.LENGTH_SHORT).show()
        }

        binding.ivDelete.setOnClickListener {
            Toast.makeText(this, "delete", Toast.LENGTH_SHORT).show()
            finish()

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