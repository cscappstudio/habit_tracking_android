package com.cscmobi.habittrackingandroid.presentation.ui.view

import android.os.Bundle
import android.view.View
import com.cscmobi.habittrackingandroid.base.BaseFragment
import com.cscmobi.habittrackingandroid.databinding.BottomsheetFragmentNewHabitBinding

class BottomsheetNewHabitFragment :
    BaseFragment<BottomsheetFragmentNewHabitBinding>(BottomsheetFragmentNewHabitBinding::inflate) {

    var case = 0
    companion object {
        var tag = "BottomsheetNewHabitFragment"
        fun newInstance(case: Int) = BottomsheetNewHabitFragment().apply {
            arguments = Bundle().apply {
                putInt("case",case)
            }
        }
    }

    override fun initView(view: View) {
    }

    override fun setEvent() {
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (!hidden)  binding.case = case
    }
}