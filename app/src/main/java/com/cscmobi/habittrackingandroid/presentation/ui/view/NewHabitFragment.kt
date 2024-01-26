package com.cscmobi.habittrackingandroid.presentation.ui.view

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.util.Log
import android.view.View
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DiffUtil
import com.cscmobi.habittrackingandroid.R
import com.cscmobi.habittrackingandroid.base.BaseBindingAdapter
import com.cscmobi.habittrackingandroid.base.BaseFragment
import com.cscmobi.habittrackingandroid.data.model.CheckList
import com.cscmobi.habittrackingandroid.data.model.ColorTask
import com.cscmobi.habittrackingandroid.data.model.DayWeekRepeat
import com.cscmobi.habittrackingandroid.data.model.EndDate
import com.cscmobi.habittrackingandroid.data.model.Goal
import com.cscmobi.habittrackingandroid.data.model.RemindTask
import com.cscmobi.habittrackingandroid.data.model.TaskRepeat
import com.cscmobi.habittrackingandroid.databinding.FragmentCreateNewhabitBinding
import com.cscmobi.habittrackingandroid.presentation.ItemBasePosistionListener
import com.cscmobi.habittrackingandroid.presentation.ui.adapter.Day
import com.cscmobi.habittrackingandroid.presentation.ui.adapter.DayOfMonthCalendarAdapter
import com.cscmobi.habittrackingandroid.presentation.ui.adapter.FrequencyTextAdapter
import com.cscmobi.habittrackingandroid.presentation.ui.intent.CollectionIntent
import com.cscmobi.habittrackingandroid.presentation.ui.viewmodel.CollectionViewModel
import com.cscmobi.habittrackingandroid.presentation.ui.viewstate.CollectionState
import com.cscmobi.habittrackingandroid.thanhlv.model.Task
import com.cscmobi.habittrackingandroid.utils.Utils
import com.cscmobi.habittrackingandroid.utils.hideKeyboardFrom
import com.cscmobi.habittrackingandroid.utils.onDone
import com.cscmobi.habittrackingandroid.utils.setBackgroundApla
import com.cscmobi.habittrackingandroid.utils.setDrawableString
import kotlinx.coroutines.launch
import java.util.Calendar


class NewHabitFragment :
    BaseFragment<FragmentCreateNewhabitBinding>(FragmentCreateNewhabitBinding::inflate) {
    private val collectionViewModel by activityViewModels<CollectionViewModel>()


    private var selectRepeatUnit = ""
    private var textRepeatSelect = ""
    private val unit = listOf("time", "minute", "glass", "km", "page", "hour")
    private val time = listOf("perday", "perweek", "permonth")
    private val days = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")
    private var frequencyTextAdapter: FrequencyTextAdapter? = null
    private val date = (1..31).toList()
    private var listDay = mutableListOf<Day>()
    private val bottomSheetFragment = BottomsheetNewHabitFragment()
    private var numberRepeat = 1
    private var subTasks = mutableListOf<String>()
    private var colorSelect = -1
    val childFragment: CustomCalenderFragment = CustomCalenderFragment()
    var newHabitFragmentState: NewHabitFragmentState = NewHabitFragmentState.ADDTOROUTINE
    private val bottomSheetAvaFragment = BottomSheetAvaFragment()

    private var listener: INewHabitListener? = null
    private var currentTask = Task()
    private var daysRepeat = mutableListOf<Int>()
    private var daysWeekRepeat = mutableListOf<DayWeekRepeat>()
    private var subTaskAdapter: BaseBindingAdapter<String>? = null

    companion object {
        val TAG = "NewHabitFragment"
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is INewHabitListener) {
            listener = context
        } else {
            throw ClassCastException("$context must implement DataPassListener")
        }
    }

    override fun initView(view: View) {
        currentTask.ava = requireContext().resources.getResourceEntryName(R.drawable.ic_item_collection2)
        when (newHabitFragmentState) {
            NewHabitFragmentState.EDITTASKCOLLECTION -> {

            }

            NewHabitFragmentState.ADDTOROUTINE -> {
                binding.layoutAddRoutine.vRoot.visibility = View.VISIBLE
            }

            NewHabitFragmentState.ADDTOCOLLECTION -> {

                binding.layoutBtnSave.vRoot.visibility = View.VISIBLE
            }

            else -> {

            }
        }

        colorSelect = ContextCompat.getColor(requireContext(), R.color.blue)
        daysWeekRepeat = mutableListOf(
            DayWeekRepeat(2),
            DayWeekRepeat(3),
            DayWeekRepeat(4),
            DayWeekRepeat(5),
            DayWeekRepeat(6),
            DayWeekRepeat(7),
            DayWeekRepeat(1),
        )

        setUpUnitPicker()
        setUpTimePicker()
        setUpDayofMonthCalender()
        setUpReminder()
        setUpTag()
        setUpCheckList()
        setUpColor()



        bottomSheetFragment.setListener(object : BottomsheetNewHabitFragment.BottomListener {
            override fun saveUnitNumberRepeat(number: Int) {
                numberRepeat = number
                binding.layoutRepeate.txtCategory.text = "Every $numberRepeat $selectRepeatUnit"
            }

            override fun createTag(name: String) {
                binding.layoutTag.addIvVisible = name == "No tag"
                binding.layoutTag.txtTag.text = name
            }

        })

        val transaction: FragmentTransaction = childFragmentManager.beginTransaction()
        transaction.replace(binding.layoutEndDate.childFragmentContainer.id, childFragment).commit()

        Log.d("Task Create", currentTask.toString())


        bottomSheetAvaFragment.actionGetIcon = {
            binding.ivHabit.setDrawableString(it)
            currentTask.ava = it
        }
    }


    private fun observe() {
        lifecycleScope.launch {
            collectionViewModel.state.collect {
                when (it) {
                    is CollectionState.CreateTaskRoutineSuccess -> {
                        if (it.isSuccess) {
                            requireActivity().finish()

                        } else {
                            Toast.makeText(
                                requireContext(),
                                "Error Create Task",
                                Toast.LENGTH_SHORT
                            )
                                .show()
                        }
                    }

                    is CollectionState.GetTask -> {
                        setUpDataTask(it.task)
                    }

                    is CollectionState.EditTask -> {
                        editTask(it.task)
                    }

                    else -> {}
                }


            }
        }

    }

    fun editTask(task: Task) {
        binding.layoutBtnSave.vRoot.visibility = View.VISIBLE
        newHabitFragmentState = NewHabitFragmentState.EDITTASK
        setUpDataTask(task)
    }

    fun setUpDataTask(task: Task) {

        currentTask = task
        Log.d("CAYVL", task.toString())


        task.ava?.let { binding.ivHabit.setDrawableString(it) }
        binding.edtName.setText(task.name.toString())

        task.color?.let {
            colorSelect = Color.parseColor(it)
            binding.ivHabit.setBackgroundApla(it, 20)
        }
        binding.edtNote.setText(task.note)
        task.goal?.let {
            binding.isGoalEdit = it.isOn
            binding.edtTargetGoal.setText(it.target.toString())

            binding.unitPicker.value = unit.indexOf(it.unit)
            if (!it.period.isNullOrEmpty())
                binding.timePicker.value = time.indexOf(it.period)
        }

        task.repeate?.let {
            var type = ""
            when (it.type) {
                "daily" -> {
                    type = "Day"
                    binding.layoutRepeate.txtDaily.performClick()
                }

                "monthly" -> {
                    type = "Month"
                    binding.layoutRepeate.txtMonthly.performClick()
                    it.days?.let {
                        it.forEach { day ->
                            listDay.forEach {

                                if (day == it.date)
                                    it.isSelected = true
                            }
                        }
                        dayOfMonthCalendarAdapter?.notifyDataSetChanged()
                    }
                }

                "weekly" -> {
                    type = "Week"
                    binding.layoutRepeate.txtWeekly.performClick()

                    val commonElements = it.days?.intersect(daysWeekRepeat)

                    it.days?.let {
                        it.forEach { day ->
                            daysWeekRepeat.forEach {

                            if (day == it.day)
                                it.isSelect = true
                            }
                        }
                    }

                }

                else -> {}
            }

            numberRepeat = it.frequency ?: 1

//                binding.layoutRepeate.txtCategory.text = "Every ${${it.frequency}} $type"
            binding.layoutRepeate.isRepeatEdit = it.isOn
        }

        task.endDate?.let {
            binding.layoutEndDate.isEndDateEdit = it.isOpen
            it.endDate?.let { it1 -> childFragment.setSelectDate(it1) }
        }

        task.remind?.let {
            var dayIndex = if (it.unit == "PM") 1 else 0

            binding.layoutReminder.isRenindEdit = it.isOpen
            binding.layoutReminder.unitHour.value = it.hour ?: 10
            binding.layoutReminder.unitMinute.value = it.minute ?: 2
            binding.layoutReminder.unitDay.value = dayIndex
        }



        if (task.tag.isNullOrEmpty()) {
            binding.layoutTag.addIvVisible = true
        } else {
            binding.layoutTag.addIvVisible = false
            binding.layoutTag.txtTag.text = task.tag
        }

    
        task.checklist?.let { checkList ->
            subTasks = checkList.map { it.title }.toMutableList()
            subTaskAdapter?.submitList(subTasks)
            subTaskAdapter?.notifyDataSetChanged()
        }

        resetColorTask()

        binding.swGoal.isChecked = binding.isGoalEdit ?: false
        binding.layoutRepeate.swRepeat.isChecked =  binding.layoutRepeate.isRepeatEdit ?: false
        binding.layoutEndDate.swEndDate.isChecked = binding.layoutEndDate.isEndDateEdit ?: false
        binding.layoutReminder.swRemind.isChecked = binding.layoutReminder.isRenindEdit ?: false
    }
    var colorsTask = mutableListOf<ColorTask>()
    var colorAdapter: BaseBindingAdapter<ColorTask>? = null
    private fun setUpColor() {
        colorsTask.clear()
        val idColors = mutableListOf<Int>(
            R.color.blue,
            R.color.pink,
            R.color.orange,
            R.color.green,
            R.color.purple
        )
        colorsTask = idColors.map { ColorTask(it) } as MutableList
        colorsTask[0].isSelected = true
         colorAdapter = BaseBindingAdapter<ColorTask>(
            R.layout.item_color_task,
            layoutInflater,
            object : DiffUtil.ItemCallback<ColorTask>() {
                override fun areItemsTheSame(oldItem: ColorTask, newItem: ColorTask): Boolean {
                    return newItem.resId == oldItem.resId
                }

                override fun areContentsTheSame(oldItem: ColorTask, newItem: ColorTask): Boolean {
                    return newItem == oldItem
                }
            })

        colorAdapter?.submitList(colorsTask)


        colorAdapter?.setListener(object : ItemBasePosistionListener {
            override fun onItemClicked(p: Int) {
                colorsTask.forEach {
                    it.isSelected = false
                }
                colorsTask[p].isSelected = true
                colorAdapter?.notifyDataSetChanged()

                colorSelect = ContextCompat.getColor(requireContext(), colorsTask[p].resId)
                resetColorTask()
            }

        })

        binding.rcvColor.adapter = colorAdapter

    }

    private fun resetColorTask() {



        dayOfMonthCalendarAdapter?.colorSelect = colorSelect
        dayOfMonthCalendarAdapter?.notifyDataSetChanged()

        childFragment.resetColorTask(colorSelect)
        setStrokeBackgroundUnitRemind(
            binding.layoutReminder.frDay,
            binding.layoutReminder.frMinute,
            binding.layoutReminder.frHour
        )
        binding.edtTargetGoal.background = Utils.createCustomDrawable(colorSelect)
        setStateTextRepeat(binding.layoutRepeate.frequencyType ?: 0)
        childFragment.resetColorTask(colorSelect)
        resetTextInListDay()

        val indexColor =  colorsTask.indexOfFirst { ContextCompat.getColor(requireContext(),it.resId) == colorSelect }
        if (indexColor != -1) {
            colorsTask[indexColor].isSelected = true
            colorAdapter?.submitList(colorsTask)
        }

    }

    var dayOfMonthCalendarAdapter: DayOfMonthCalendarAdapter? = null
    private fun setUpDayofMonthCalender() {
        listDay.clear()
        listDay = date.map { Day(it) }.toMutableList()


        dayOfMonthCalendarAdapter = DayOfMonthCalendarAdapter(requireContext(), listDay)

        binding.layoutRepeate.calendarGridview.adapter = dayOfMonthCalendarAdapter
    }


    private fun setUpUnitPicker() {
        binding.unitPicker.minValue = 1;
        binding.unitPicker.maxValue = unit.size;
        binding.unitPicker.displayedValues = unit.toTypedArray()
        binding.unitPicker.isFadingEdgeEnabled = true
        binding.unitPicker.typeface = ResourcesCompat.getFont(context!!, R.font.worksans_medium);
        binding.unitPicker.setSelectedTypeface(
            ResourcesCompat.getFont(
                context!!,
                R.font.worksans_semibold
            )
        )

    }

    private fun setUpTimePicker() {
        binding.timePicker.minValue = 1;
        binding.timePicker.maxValue = time.size;
        binding.timePicker.displayedValues = time.toTypedArray()
        binding.timePicker.isFadingEdgeEnabled = true
        binding.timePicker.typeface = ResourcesCompat.getFont(context!!, R.font.worksans_medium);
        binding.timePicker.setSelectedTypeface(
            ResourcesCompat.getFont(
                context!!,
                R.font.worksans_semibold
            )
        )
    }

    private fun setUpReminder() {
        val fontTypetext = ResourcesCompat.getFont(context!!, R.font.worksans_medium);
        val fontTypetextSelected = ResourcesCompat.getFont(context!!, R.font.worksans_semibold)

        val days = arrayListOf("AM", "PM")
        binding.layoutReminder.unitDay.maxValue = days.size;
        binding.layoutReminder.unitDay.displayedValues = days.toTypedArray()
        binding.layoutReminder.unitDay.isFadingEdgeEnabled = true

        binding.layoutReminder.unitDay.typeface = fontTypetext
        binding.layoutReminder.unitDay.setSelectedTypeface(fontTypetextSelected)

        binding.layoutReminder.unitHour.typeface = fontTypetext
        binding.layoutReminder.unitHour.setSelectedTypeface(fontTypetextSelected)

        binding.layoutReminder.unitMinute.typeface = fontTypetext
        binding.layoutReminder.unitMinute.setSelectedTypeface(fontTypetextSelected)


    }

    private fun setUpTag() {
        binding.layoutTag.addIvVisible = binding.layoutTag.txtTag.text.isNullOrEmpty()
        binding.layoutTag.ivAdd.setOnClickListener {
            bottomSheetFragment.caseType = 0
            bottomSheetFragment.tagSelect = ""
            bottomSheetFragment.show(childFragmentManager, bottomSheetFragment.tag)
        }

        binding.layoutTag.txtTag.setOnClickListener {
            bottomSheetFragment.caseType = 0
            bottomSheetFragment.tagSelect = binding.layoutTag.txtTag.text.toString()
            bottomSheetFragment.show(childFragmentManager, bottomSheetFragment.tag)

        }
    }

    private fun setUpCheckList() {
        subTaskAdapter = BaseBindingAdapter<String>(
            R.layout.item_subtask,
            layoutInflater,
            object : DiffUtil.ItemCallback<String>() {
                override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
                    return oldItem == newItem
                }

                override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
                    return oldItem == newItem
                }

            })
        subTaskAdapter?.submitList(subTasks)

        subTaskAdapter?.setListener(object : ItemBasePosistionListener {
            override fun onItemClicked(p: Int) {
                subTasks.removeAt(p)
                subTaskAdapter?.notifyItemRemoved(p)

            }

        })


        binding.layoutChecklist.rcvSubtask.adapter = subTaskAdapter

        binding.layoutChecklist.edtAdd.onDone {
            binding.layoutChecklist.edtAdd.visibility = View.GONE
            if (!binding.layoutChecklist.edtAdd.text.isNullOrEmpty()) {
                subTasks.add(binding.layoutChecklist.edtAdd.text.toString())
                subTaskAdapter?.notifyItemInserted(subTasks.size - 1)
            }
            hideKeyboardFrom(requireContext(), binding.layoutChecklist.edtAdd)

        }

        binding.layoutChecklist.ivAdd.setOnClickListener {
            binding.layoutChecklist.edtAdd.visibility = View.VISIBLE
            showKeyboardOnView(binding.layoutChecklist.edtAdd)
            binding.root.viewTreeObserver.addOnGlobalLayoutListener(OnGlobalLayoutListener {
                binding.nestScroll.post {
                    binding.nestScroll.smoothScrollTo(0, binding.layoutChecklist.root.bottom)
                }
            })
        }


    }

    private fun setUpCreateTask() {

        if (!binding.edtName.text.isNullOrEmpty())
            currentTask.name = binding.edtName.text.toString()

        if (!binding.edtNote.text.isNullOrEmpty())
            currentTask.note = binding.edtNote.text.toString()

        currentTask.goal = Goal(
            isOn = binding.isGoalEdit,
            unit = binding.unitPicker.displayedValues[binding.unitPicker.value - 1],
            target = if (binding.edtTargetGoal.text.isNullOrEmpty()) 1 else binding.edtTargetGoal.text.toString()
                .toInt(),
            period = binding.timePicker.displayedValues[binding.timePicker.value - 1]
        )


        val hexColor = String.format("#%06X", 0xFFFFFF and colorSelect)

        currentTask.color = hexColor

        currentTask.tag = binding.layoutTag.txtTag.text.toString()

        var daysRepeat = when (textRepeatSelect) {
            "weekly" -> daysWeekRepeat.filter { it.isSelect }.map { it.day }
            "monthly" -> listDay.filter { it.isSelected }.map { it.date }
            else -> emptyList()
        }


        currentTask.repeate = TaskRepeat(
            isOn = binding.layoutRepeate.isRepeatEdit,
            type = textRepeatSelect,
            frequency = numberRepeat,
            days = daysRepeat
        )

        currentTask.startDate = Calendar.getInstance().time
        currentTask.endDate =
            EndDate(binding.layoutEndDate.isEndDateEdit, childFragment.getDateSelected())

        currentTask.remind = RemindTask(
            isOpen = binding.layoutReminder.isRenindEdit,
            hour = binding.layoutReminder.unitHour.value,
            minute = binding.layoutReminder.unitMinute.value,
            unit = binding.layoutReminder.unitDay.displayedValues[binding.layoutReminder.unitDay.value - 1]
        )


        currentTask.checklist = subTasks.map { CheckList(title = it) }


        Log.d("DEBUGTASK", currentTask.toString())


    }


    override fun setEvent() {
        binding.ivHabit.setOnClickListener {
            bottomSheetAvaFragment.show(childFragmentManager, bottomSheetAvaFragment.tag)
        }

        binding.edtName.onDone {
            hideKeyboardFrom(requireContext(), binding.edtName)

        }

        binding.edtNote.onDone {
            hideKeyboardFrom(requireContext(), binding.edtNote)
        }



        binding.ivClose.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        binding.swGoal.setOnCheckedChangeListener { buttonView, isChecked ->
            binding.isGoalEdit = isChecked
        }

        binding.layoutRepeate.swRepeat.setOnCheckedChangeListener { buttonView, isChecked ->
            binding.layoutRepeate.isRepeatEdit = isChecked
        }

        binding.layoutReminder.swRemind.setOnCheckedChangeListener { buttonView, isChecked ->
            binding.layoutReminder.isRenindEdit =
                isChecked
        }

        binding.layoutEndDate.swEndDate.setOnCheckedChangeListener { buttonView, isChecked ->

            binding.layoutEndDate.isEndDateEdit = isChecked
        }

        binding.layoutRepeate.txtDaily.setOnClickListener {
            setStateTextRepeat(0)
        }

        binding.layoutRepeate.txtWeekly.setOnClickListener {
            setStateTextRepeat(1)
        }

        binding.layoutRepeate.txtMonthly.setOnClickListener {
            setStateTextRepeat(2)
        }

        binding.layoutRepeate.ctlFrequency.setOnClickListener {


            binding.layoutChecklist.edtAdd.visibility = View.VISIBLE
            bottomSheetFragment.caseType = 1
            bottomSheetFragment.unitSelect = selectRepeatUnit
            bottomSheetFragment.show(childFragmentManager, bottomSheetFragment.tag)
        }

        setListenerListDay()

        binding.layoutReminder.unitHour.setOnClickListener {
            setStateBackgroundUnitReminder(
                binding.layoutReminder.frHour,
                binding.layoutReminder.frMinute,
                binding.layoutReminder.frDay
            )
        }

        binding.layoutReminder.unitMinute.setOnClickListener {
            setStateBackgroundUnitReminder(
                binding.layoutReminder.frMinute,
                binding.layoutReminder.frHour,
                binding.layoutReminder.frDay
            )

        }

        binding.layoutReminder.unitDay.setOnClickListener {
            setStateBackgroundUnitReminder(
                binding.layoutReminder.frDay,
                binding.layoutReminder.frHour,
                binding.layoutReminder.frMinute
            )

        }

        binding.layoutBtnSave.btnSave.setOnClickListener {
            if (newHabitFragmentState == NewHabitFragmentState.ADDTOCOLLECTION) {
                listener?.addTask(
                    Task(
                        name = "my task",
                        ava = resources.getResourceEntryName(R.drawable.ic_item_collection2)
                    )
                )
                parentFragmentManager.popBackStack()
            }

            else if (newHabitFragmentState == NewHabitFragmentState.EDITTASK) {
                lifecycleScope.launch {
                    setUpCreateTask()
                    collectionViewModel.userIntent.send(CollectionIntent.UpdateTask(currentTask))
                }
                requireActivity().finish()
            }
        }

        binding.layoutAddRoutine.vRoot.setOnClickListener {
            lifecycleScope.launch {
                setUpCreateTask()

                collectionViewModel.userIntent.send(CollectionIntent.CreateTaskToRoutine(currentTask))
            }
        }


    }

    override fun onResume() {
        super.onResume()
        observe()
//
//        NewHabitActivity.dataTask?.let {
//            setUpDataTask(it)
//        }
    }

    private var gradientBgUnitReminder: GradientDrawable? = null
    private fun setStateBackgroundUnitReminder(fr: FrameLayout, vararg lstFr: FrameLayout) {
        lstFr.forEach {
            it.setBackgroundResource(R.drawable.bg_white_corner_12)
        }

        if (gradientBgUnitReminder != null) fr.background = gradientBgUnitReminder
        else
            fr.setBackgroundResource(R.drawable.bg_white_border_corner_12)

    }


    private fun setStrokeBackgroundUnitRemind(vararg lstFr: FrameLayout) {
        val w = requireContext().resources.getDimension(
            com.intuit.sdp.R.dimen._1sdp
        )
        val corner = requireContext().resources.getDimension(
            com.intuit.sdp.R.dimen._10sdp
        )
        val bg = Utils.createRoundedRectDrawable(colorSelect, w, Color.WHITE, corner)
        gradientBgUnitReminder = bg

        lstFr.forEach {
            if (it.background is GradientDrawable) {
                it.background = gradientBgUnitReminder
                it.invalidate()
            }
        }

    }

    private fun setListenerListDay() {
        val childCount = binding.layoutRepeate.llWeekly.childCount
        if (childCount != 0) {
            for (i in 0 until childCount) {
                var txt: View? = binding.layoutRepeate.llWeekly.getChildAt(i)
                txt?.let {
                    if (txt is TextView) {
                        txt.setOnClickListener {
                            daysWeekRepeat[i].isSelect = !daysWeekRepeat[i].isSelect
                            txt.changeBackgroundText(daysWeekRepeat[i].isSelect)
                        }

                    }
                }
            }
        }
    }

    private fun resetTextInListDay() {
        daysWeekRepeat.forEachIndexed { index, dayWeekRepeat ->
            if (dayWeekRepeat.isSelect) {
                (binding.layoutRepeate.llWeekly.getChildAt(index) as TextView).changeBackgroundText(
                    true
                )
            }
        }

    }

    private fun setStateTextRepeat(frequencyType: Int) {
        binding.layoutRepeate.frequencyType = frequencyType
        selectRepeatUnit = when (binding.layoutRepeate.frequencyType) {
            0 -> "Day"
            1 -> "Week"
            2 -> "Month"
            else -> "Week"
        }
        when (binding.layoutRepeate.frequencyType) {
            0 -> {
                selectRepeatUnit = "Day"
                textRepeatSelect = "daily"

            }

            1 -> {
                selectRepeatUnit = "Week"
                textRepeatSelect = "weekly"

            }

            2 -> {
                selectRepeatUnit = "Month"
                textRepeatSelect = "monthly"


            }
        }

        binding.layoutRepeate.txtCategory.text = "Every $numberRepeat $selectRepeatUnit"

        when (frequencyType) {
            0 -> {
                binding.layoutRepeate.txtDaily.changeBackgroundText(true)
                binding.layoutRepeate.txtWeekly.changeBackgroundText(false)
                binding.layoutRepeate.txtMonthly.changeBackgroundText(false)
            }

            1 -> {
                binding.layoutRepeate.txtDaily.changeBackgroundText(false)
                binding.layoutRepeate.txtWeekly.changeBackgroundText(true)
                binding.layoutRepeate.txtMonthly.changeBackgroundText(false)
            }

            2 -> {
                binding.layoutRepeate.txtDaily.changeBackgroundText(false)
                binding.layoutRepeate.txtWeekly.changeBackgroundText(false)
                binding.layoutRepeate.txtMonthly.changeBackgroundText(true)
            }
        }
    }

    private fun TextView.changeBackgroundText(isChanged: Boolean) {
        if (isChanged) {
            this.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
            this.backgroundTintList =
                ColorStateList.valueOf(colorSelect)
        } else {
            this.setTextColor(ContextCompat.getColor(requireContext(), R.color.gray))
            this.backgroundTintList =
                ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.white))
        }
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (!hidden) {

            Toast.makeText(requireContext(), "show ff", Toast.LENGTH_SHORT).show()
            when (newHabitFragmentState) {
                NewHabitFragmentState.EDITTASKCOLLECTION -> {

                }

                NewHabitFragmentState.ADDTOROUTINE -> {
                    binding.layoutAddRoutine.vRoot.visibility = View.VISIBLE
                }

                NewHabitFragmentState.ADDTOCOLLECTION -> {

                    binding.layoutBtnSave.vRoot.visibility = View.VISIBLE
                }

                NewHabitFragmentState.EDITTASK -> {
                }

                else -> {

                }
            }
        }
    }

    interface INewHabitListener {
        fun addTask(task: Task)

    }

    enum class NewHabitFragmentState {
        ADDTOCOLLECTION,
        EDITTASK,
        EDITTASKCOLLECTION,
        ADDTOROUTINE
    }
}
