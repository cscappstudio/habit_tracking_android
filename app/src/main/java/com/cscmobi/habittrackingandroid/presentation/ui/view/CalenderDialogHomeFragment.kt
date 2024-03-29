package com.cscmobi.habittrackingandroid.presentation.ui.view

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cscmobi.habittrackingandroid.R
import com.cscmobi.habittrackingandroid.base.BaseBindingAdapter
import com.cscmobi.habittrackingandroid.databinding.CalenderCustomBinding
import com.cscmobi.habittrackingandroid.databinding.CalenderHomeBinding
import com.cscmobi.habittrackingandroid.presentation.ItemBaseListener
import com.cscmobi.habittrackingandroid.thanhlv.model.DayCalendarModel
import com.cscmobi.habittrackingandroid.utils.Utils.toDate
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.threeten.bp.LocalDate
import org.threeten.bp.YearMonth
import java.text.SimpleDateFormat
import java.util.Calendar

class CalenderDialogHomeFragment : DialogFragment() {
    private var calenderAdapter: BaseBindingAdapter<String>? = null
    private var selectedDate = Calendar.getInstance()
    private lateinit var binding: CalenderHomeBinding
    var actionDateSelect : ((Long) -> Unit)? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)

        // Set the layout parameters for the dialog window
        dialog.window?.apply {
            // Set the width and height to WRAP_CONTENT or your desired size
            attributes.width =  ViewGroup.LayoutParams.WRAP_CONTENT
            attributes.height = ViewGroup.LayoutParams.WRAP_CONTENT
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
        }
        return dialog
    }

    override fun onResume() {
        super.onResume()
        binding.root.requestLayout()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = CalenderHomeBinding.inflate(inflater, container, false)
        binding.vRoot.visibility = View.INVISIBLE

        setMonthView()
        binding.ivPrevios.setOnClickListener {
            previousMonthAction(it)
        }

        binding.ivNext.setOnClickListener {
            nextMonthAction(it)
        }
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        val displayMetrics = DisplayMetrics()
        requireActivity().windowManager.defaultDisplay.getMetrics(displayMetrics)
        val screenWidth = displayMetrics.widthPixels * 0.9

        binding.root.layoutParams.width = screenWidth.toInt()
        binding.vRoot.visibility = View.VISIBLE

    }

    private fun setMonthView(isChange: Boolean = false) {

        val daysInMonth = daysInMonthArray(selectedDate)
        binding.monthYearTV.text = monthYearFromDate(selectedDate);


        if (!isChange) {
            binding.calendarRecyclerView.itemAnimator = null

            val layoutManager: RecyclerView.LayoutManager =
                GridLayoutManager(requireContext(), 7)
            binding.calendarRecyclerView.layoutManager = layoutManager

            calenderAdapter =
                BaseBindingAdapter(R.layout.item_home_calender, layoutInflater, object :
                    DiffUtil.ItemCallback<String>() {
                    override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
                        return oldItem == newItem
                    }

                    override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
                        return oldItem == newItem
                    }

                })

            calenderAdapter?.setListener(object : ItemBaseListener<String> {
                override fun onItemClicked(item: String) {
                    var c = selectedDate
                    c.set(Calendar.DAY_OF_MONTH,item.toInt())

                    actionDateSelect?.invoke(c.time.time.toDate())

                }

            })

            binding.calendarRecyclerView.adapter = calenderAdapter
            calenderAdapter?.submitList(daysInMonth)
            calenderAdapter?.notifyDataSetChanged()

        } else {
            calenderAdapter?.submitList(daysInMonth)
            calenderAdapter?.notifyDataSetChanged()
        }

    }

    private fun monthYearFromDate(calendar: Calendar): String? {
        val formatter = SimpleDateFormat("MMMM yyyy")
        return formatter.format(calendar.time)
    }

    private fun daysInMonthArray(calendar: Calendar): ArrayList<String> {
        val daysInMonthArray = ArrayList<String>()
        calendar[Calendar.DAY_OF_MONTH] = 1
        var preDay = calendar.get(Calendar.DAY_OF_WEEK) - 2
        if (preDay < 0) preDay = 6
        for (i in 1..preDay) daysInMonthArray.add("")
        val numDays = calendar.getActualMaximum(Calendar.DATE)
        for (i in 1..numDays) {
            daysInMonthArray.add(i.toString())
        }

//        for (i in 0 until 35) {
//            val adjustedIndex = (i + dayOfWeek - 1) % 7 // Adjust for Monday start
//            if (i < dayOfWeek - 1 || adjustedIndex >= daysInMonth + dayOfWeek - 1) {
//                daysInMonthArray.add("")
//            } else {
//                val dayOfMonth = i - dayOfWeek + 2
//                if (dayOfMonth in 1..daysInMonth) {
//                    daysInMonthArray.add(dayOfMonth.toString())
//                } else {
//                    daysInMonthArray.add("")
//                }
//            }
//        }
        return daysInMonthArray
    }

    private fun previousMonthAction(view: View?) {
        selectedDate.add(Calendar.MONTH, -1)
        setMonthView(true)
    }

    private fun nextMonthAction(view: View?) {
        selectedDate.add(Calendar.MONTH, 1)
        setMonthView(true)
    }

    companion object {
        val TAG = "CalenderDialogHomeFragment"
    }


}