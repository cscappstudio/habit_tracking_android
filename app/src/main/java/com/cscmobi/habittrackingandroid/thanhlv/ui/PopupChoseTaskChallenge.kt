package com.cscmobi.habittrackingandroid.thanhlv.ui

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.cscmobi.habittrackingandroid.R
import com.cscmobi.habittrackingandroid.databinding.PopupChoseAvaProfileLayoutBinding
import com.cscmobi.habittrackingandroid.databinding.PopupChoseTaskChallengeLayoutBinding
import com.cscmobi.habittrackingandroid.thanhlv.adapter.AddTaskChallengeAdapter
import com.cscmobi.habittrackingandroid.thanhlv.adapter.MyChallengeAdapter
import com.cscmobi.habittrackingandroid.thanhlv.adapter.OldTaskInCreateChallengeAdapter
import com.cscmobi.habittrackingandroid.thanhlv.model.Challenge
import com.cscmobi.habittrackingandroid.thanhlv.model.CreateTaskChallenge
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.gson.Gson
import com.thanhlv.fw.spf.SPF
import kotlinx.android.parcel.Parcelize

class PopupChoseTaskChallenge :
    BottomSheetDialogFragment() {
    var callback: Callback? = null


    private lateinit var binding: PopupChoseTaskChallengeLayoutBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = PopupChoseTaskChallengeLayoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var oldTasks = mutableListOf<CreateTaskChallenge>()
        CreateChallengeActivity.mDataTaskCreateChallenge.forEach {
            if (it.type == 2 || it.type == 3)
                oldTasks.add(it)
        }
        val oldTasks2 = filterDiff(oldTasks)
        val adapter = OldTaskInCreateChallengeAdapter(requireContext())
        adapter.setData(oldTasks2)
        adapter.setCallBack(object :
            OldTaskInCreateChallengeAdapter.OldTaskInCreateChallengeAdapter {
            override fun onClickAdd(item: CreateTaskChallenge) {
                callback?.onclickItemTask(item)
                dismissAllowingStateLoss()
            }

        })
        binding.rcTask.adapter = adapter
        binding.rcTask.layoutManager = LinearLayoutManager(requireContext())

        (dialog as? BottomSheetDialog)?.behavior?.apply {
            isFitToContents = true
            state = BottomSheetBehavior.STATE_EXPANDED
        }
        controllerView()
    }

    private fun filterDiff(oldList: MutableList<CreateTaskChallenge>): MutableList<CreateTaskChallenge> {
        val list = mutableListOf<CreateTaskChallenge>()
        val sortList = oldList.sortedBy { it.name }
        val sortList2 = sortList.toMutableList()
        sortList2.add(0, CreateTaskChallenge(0, 0, "thanhlv"))

        for (i in 0 until sortList2.size - 1)
            if (sortList2[i].diff(sortList2[i + 1])) list.add(sortList2[i + 1])
        return list
    }

    private fun controllerView() {
        binding.btnCreate.setOnClickListener {
            callback?.onclickCreateNew()
            dismissAllowingStateLoss()
        }
        binding.btnBackHeader.setOnClickListener {
            dismissAllowingStateLoss()
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState).apply {
            setOnShowListener {
                val bottomSheet =
                    findViewById<View>(com.google.android.material.R.id.design_bottom_sheet) as FrameLayout
                bottomSheet.setBackgroundResource(android.R.color.transparent)
            }
        }


    }

    interface Callback {
        fun onclickBack()
        fun onclickCreateNew()
        fun onclickItemTask(task: CreateTaskChallenge)
    }

}