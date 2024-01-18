package com.cscmobi.habittrackingandroid.presentation.ui.view

import android.content.res.ColorStateList
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.FragmentTransaction
import com.cscmobi.habittrackingandroid.R
import com.cscmobi.habittrackingandroid.base.BaseFragment
import com.cscmobi.habittrackingandroid.databinding.FragmentCreateNewhabitBinding
import com.cscmobi.habittrackingandroid.presentation.ui.adapter.Day
import com.cscmobi.habittrackingandroid.presentation.ui.adapter.DayOfMonthCalendarAdapter
import com.cscmobi.habittrackingandroid.presentation.ui.adapter.FrequencyTextAdapter


class NewHabitFragment :
    BaseFragment<FragmentCreateNewhabitBinding>(FragmentCreateNewhabitBinding::inflate) {
    private val unit = listOf("time", "minute", "glass", "km", "page", "hour")
    private val time = listOf("perday", "perweek", "permonth")
    private val days = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")
    private var frequencyTextAdapter: FrequencyTextAdapter? = null
    private val frequency = listOf("111111111111", "2222", "3333")
    private val date = (1..31).toList()
    private var listDay = mutableListOf<Day>()

    companion object {
        val TAG = "NewHabitFragment"
    }


    override fun initView(view: View) {
        setUpUnitPicker()
        setUpTimePicker()
        setUpDayofMonthCalender()
        setUpReminder()
        setUpTag()

        val childFragment: CustomCalenderFragment = CustomCalenderFragment()
        val transaction: FragmentTransaction = childFragmentManager.beginTransaction()
        transaction.replace(binding.layoutEndDate.childFragmentContainer.id, childFragment).commit()


    }

    private fun setUpDayofMonthCalender() {
        listDay.clear()
        listDay = date.map { Day(it) }.toMutableList()


        val adapter = DayOfMonthCalendarAdapter(requireContext(), listDay)

        binding.layoutRepeate.calendarGridview.adapter = adapter
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
        val fontTypetext=  ResourcesCompat.getFont(context!!, R.font.worksans_medium);
        val fontTypetextSelected=   ResourcesCompat.getFont(context!!, R.font.worksans_semibold)

        val days = arrayListOf("AM","PM")
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
            val bottomSheetFragment = BottomsheetNewHabitFragment()

            bottomSheetFragment.show(childFragmentManager, bottomSheetFragment.tag)
        }
    }

    override fun setEvent() {
        binding.ivClose.setOnClickListener {
            Toast.makeText(
                requireContext(), unit[binding.unitPicker.value - 1], Toast.LENGTH_SHORT
            ).show()
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

        setListenerListDay()
        
        binding.layoutReminder.unitHour.setOnClickListener {
            setStateBackgroundUnitReminder(binding.layoutReminder.frHour,binding.layoutReminder.frMinute,binding.layoutReminder.frDay)
        }

        binding.layoutReminder.unitMinute.setOnClickListener {
            setStateBackgroundUnitReminder(binding.layoutReminder.frMinute,binding.layoutReminder.frHour,binding.layoutReminder.frDay)

        }

        binding.layoutReminder.unitDay.setOnClickListener {
            setStateBackgroundUnitReminder(binding.layoutReminder.frDay,binding.layoutReminder.frHour,binding.layoutReminder.frMinute)

        }
    }

    private fun setStateBackgroundUnitReminder(fr: FrameLayout,vararg lstFr: FrameLayout) {
        lstFr.forEach {
            it.setBackgroundResource(R.drawable.bg_white_corner_12)
        }

        fr.setBackgroundResource(R.drawable.bg_white_border_corner_12)

    }

    private fun setListenerListDay() {
        val childCount = binding.layoutRepeate.llWeekly.childCount
        if (childCount != 0) {
            for (i in 0 until childCount) {
                var txt: View? = binding.layoutRepeate.llWeekly.getChildAt(i)
                txt?.let {
                    if (txt is TextView) {
                        txt.setOnClickListener {
                            resetTextInListDay()
                            txt.changeBackgroundText(true)

                        }

                    }
                }
            }
        }
    }

    private fun resetTextInListDay() {
        val childCount = binding.layoutRepeate.llWeekly.childCount
        if (childCount != 0) {
            for (i in 0 until childCount) {
                var txt: View? = binding.layoutRepeate.llWeekly.getChildAt(i)
                txt?.let {
                    if (txt is TextView) {
                        txt.changeBackgroundText(false)
                    }
                }
            }
        }
    }

    private fun setStateTextRepeat(frequencyType: Int) {
        binding.layoutRepeate.frequencyType = frequencyType

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
                ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.blue))
        } else {
            this.setTextColor(ContextCompat.getColor(requireContext(), R.color.gray))
            this.backgroundTintList =
                ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.white))
        }
    }


}