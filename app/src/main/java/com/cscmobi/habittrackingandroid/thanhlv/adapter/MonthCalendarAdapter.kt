package com.cscmobi.habittrackingandroid.thanhlv.adapter

import android.annotation.SuppressLint
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.cscmobi.habittrackingandroid.thanhlv.model.MonthCalendarModel
import com.cscmobi.habittrackingandroid.thanhlv.ui.MonthCalendarFragment

class MonthCalendarAdapter(fragmentActivity: FragmentActivity) :
    FragmentStateAdapter(fragmentActivity) {
    private var mList = mutableListOf<MonthCalendarModel>()

    @SuppressLint("NotifyDataSetChanged")
    fun setList(list: MutableList<MonthCalendarModel>) {
        this.mList = list
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    override fun createFragment(position: Int): Fragment {
        val monthYearModel = mList[position]
        return MonthCalendarFragment(monthYearModel.month, monthYearModel.year)
    }
}