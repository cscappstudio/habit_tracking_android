package com.cscmobi.habittrackingandroid.presentation.ui.view

import android.graphics.Rect
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.cscmobi.habittrackingandroid.base.BaseFragment
import com.cscmobi.habittrackingandroid.data.model.WeekCalenderItem
import com.cscmobi.habittrackingandroid.databinding.FragmentWeekBinding
import com.cscmobi.habittrackingandroid.presentation.OnItemClickPositionListener
import com.cscmobi.habittrackingandroid.presentation.ui.adapter.WeekAdapter
import com.cscmobi.habittrackingandroid.utils.Helper
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter
import org.threeten.bp.format.TextStyle
import java.util.Locale

class WeekFragment() :
    BaseFragment<FragmentWeekBinding>(FragmentWeekBinding::inflate) {
    private lateinit var adapter: WeekAdapter
    private var data = arrayListOf<WeekCalenderItem>()
    private var date = arrayListOf<LocalDate>()
    private var c = Helper.currentDate
    var actionClickItem: ((date: LocalDate) -> Unit)? = null
    lateinit var startOfWeek: LocalDate

    constructor(startOfWeek: LocalDate) : this() {
        this.startOfWeek = startOfWeek
    }


    fun getDatesofWeek() {
        date.clear()
        data.clear()

        for (day in 0 until 7) {
            val currentDate = startOfWeek.plusDays(day.toLong())
            date.add(currentDate)
            if (currentDate == c)
                data.add(
                    WeekCalenderItem(
                        currentDate.dayOfWeek.getDisplayName(
                            TextStyle.SHORT,
                            Locale.ENGLISH
                        ), currentDate.dayOfMonth, true
                    )
                )
            else data.add(
                WeekCalenderItem(
                    currentDate.dayOfWeek.getDisplayName(
                        TextStyle.SHORT,
                        Locale.ENGLISH
                    ), currentDate.dayOfMonth
                )
            )


        }
    }

    override fun initView(view: View) {
        addDecoration()
        getDatesofWeek()
        adapter = WeekAdapter(object : OnItemClickPositionListener {
            override fun onItemClick(position: Int) {
                data.forEachIndexed { index, item ->
                    item.isSelected = position == index
                }


                actionClickItem?.invoke(date[position])
//                binding.txtTitle.text = if (date[position] == c)  " Today" else  date[position].format(formatter)
//                binding.llToday.visibility = if(date[position] == c) View.INVISIBLE else View.VISIBLE
                adapter.notifyDataSetChanged()
            }

        })
        adapter.submitList(data)
        binding.adapter = adapter

    }

    override fun onResume() {
        super.onResume()
        if (HomeFragment.isSetCurrentDate) {
            data.forEach {
                it.isSelected = it.date == c.dayOfMonth
            }
            adapter.notifyDataSetChanged()
            HomeFragment.isSetCurrentDate = false
        }


    }

    override fun setEvent() {
//        binding.llToday.setOnClickListener {
//            actionClickToday?.invoke()
//            val indexToday = data.indexOfFirst { it.date == c.dayOfMonth }
//            data.forEach {
//                it.isSelected = it.date == c.dayOfMonth
//            }
//            adapter.notifyDataSetChanged()
//           binding.llToday.visibility = View.INVISIBLE
//
//        }
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
}