package com.cscmobi.habittrackingandroid.presentation.ui.adapter

import android.util.Log
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.cscmobi.habittrackingandroid.presentation.ui.view.WeekFragment
import org.threeten.bp.LocalDate

class WeekPagerAdapter(fragment: Fragment): FragmentStateAdapter(fragment) {
    var listWeekData = arrayListOf<LocalDate>()
    var doActionviewPager : ((date:LocalDate) -> Unit)? = null

    override fun getItemCount(): Int {
        return  listWeekData.size
    }

    override fun createFragment(position: Int): Fragment {
        return  WeekFragment(listWeekData[position]).apply {
            this.actionClickItem = {
                doActionviewPager?.invoke(it)
            }

        }
    }


}

