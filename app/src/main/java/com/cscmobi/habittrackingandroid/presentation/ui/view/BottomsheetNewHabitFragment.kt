package com.cscmobi.habittrackingandroid.presentation.ui.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cscmobi.habittrackingandroid.databinding.BottomsheetFragmentNewHabitBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.cscmobi.habittrackingandroid.R

class BottomsheetNewHabitFragment :
    BottomSheetDialogFragment() {

    companion object {
        var tag = "BottomsheetNewHabitFragment"
//        fun newInstance(case: Int) = BottomsheetNewHabitFragment().apply {
//            arguments = Bundle().apply {
//                putInt("case",case)
//            }
//        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = BottomsheetFragmentNewHabitBinding.inflate(inflater,container,false)



        return view.root

//        inflater.inflate(R.layout.fragment_bottom_sheet_dialog, container, false)
    }
}