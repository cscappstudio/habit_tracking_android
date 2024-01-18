package com.cscmobi.habittrackingandroid.thanhlv.adapter

import android.annotation.SuppressLint
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.cscmobi.habittrackingandroid.thanhlv.model.MonthCalendarModel
import com.cscmobi.habittrackingandroid.thanhlv.ui.MonthCalendarFragment

class PagerMonthCalendarAdapter(fragmentActivity: FragmentActivity) :
    FragmentStateAdapter(fragmentActivity) {
    private var mList = mutableListOf<MonthCalendarModel>()

    @SuppressLint("NotifyDataSetChanged")
    fun setList(list: MutableList<MonthCalendarModel>) {
        this.mList = list
        notifyDataSetChanged()
    }

    fun getList(): MutableList<MonthCalendarModel> {
        return this.mList
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    override fun createFragment(position: Int): Fragment {
        val monthYearModel = mList[position]
        return MonthCalendarFragment.newInstance(monthYearModel.month, monthYearModel.year)
    }
}