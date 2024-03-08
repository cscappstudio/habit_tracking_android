package com.cscmobi.habittrackingandroid.presentation.ui.view

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.recyclerview.widget.DiffUtil
import com.cscmobi.habittrackingandroid.R
import com.cscmobi.habittrackingandroid.base.BaseBindingAdapter
import com.cscmobi.habittrackingandroid.data.model.Tag
import com.cscmobi.habittrackingandroid.databinding.BottomsheetPauseTaskBinding
import com.cscmobi.habittrackingandroid.presentation.ItemBasePosistionListener
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class BottomSheetPauseTaskFragment: BottomSheetDialogFragment() {
    private var pauseInfos = mutableListOf<Tag>()

    var actionPause: ((Int) -> Unit)? = null
    var currentPos = 0

    private var pauseValue = listOf<Int>(1,3,7,-1)
    var oldPostionTag = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = BottomsheetPauseTaskBinding.inflate(inflater, container, false)

        pauseInfos = mutableListOf<Tag>(
            Tag(getString(R.string.for_1_day)),
            Tag(getString(R.string.for_3_days)),
            Tag(getString(R.string.for_7_days)),
            Tag(getString(R.string.until_i_change_it))
        )
        val adapter = BaseBindingAdapter(
            R.layout.item_tag,
            layoutInflater,
            object : DiffUtil.ItemCallback<Tag>() {
                override fun areItemsTheSame(oldItem: Tag, newItem: Tag): Boolean {
                    return oldItem.name == newItem.name
                }

                override fun areContentsTheSame(oldItem: Tag, newItem: Tag): Boolean {
                    return oldItem == newItem
                }
            })
        adapter.submitList(pauseInfos)
        adapter.setListener(object : ItemBasePosistionListener {
            override fun onItemClicked(p: Int) {
                if (oldPostionTag != -1 && oldPostionTag != p) {
                    pauseInfos[oldPostionTag].isSelected = false
                    oldPostionTag = p

                } else {
                    oldPostionTag = p
                }

                pauseInfos[p].isSelected = true
                adapter.notifyDataSetChanged()
                currentPos = p
            }

        })

        binding.rcvPause.adapter = adapter
        binding.layoutBtnSave.btnSave.text = "NEXT"
        binding.layoutBtnSave.btnSave.setOnClickListener {
            actionPause?.invoke(pauseValue[currentPos])
            this.dismiss()

        }
        return binding.root
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState).apply {
            setOnShowListener {
                val bottomSheet = findViewById<View>(com.google.android.material.R.id.design_bottom_sheet) as FrameLayout
                bottomSheet.setBackgroundResource(android.R.color.transparent)
            }
        }
    }

}