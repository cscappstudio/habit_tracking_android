package com.cscmobi.habittrackingandroid.thanhlv.ui

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.widget.PopupWindow
import com.cscmobi.habittrackingandroid.R
import com.cscmobi.habittrackingandroid.databinding.ActivityDetailChallengeBinding
import com.cscmobi.habittrackingandroid.databinding.MenuDropDownLayoutBinding
import com.thanhlv.fw.helper.DisplayUtils

class MenuDropDown(
    context: Context,
    type: Int, // 1 = reset/stop, 2 = edit/delete
    reset: Boolean,
    resetAction: () -> Unit,
    cancelAction: () -> Unit
) : PopupWindow(context) {

    init {
        val binding = MenuDropDownLayoutBinding.inflate(LayoutInflater.from(context))
        contentView = binding.root
        setBackgroundDrawable(null)
        isFocusable = true
        isOutsideTouchable = true

        if (type == 2) {
            binding.btnReset.text = context.getString(R.string.edit)
            binding.btnCancel.text = context.getString(R.string.delete)
            binding.btnCancel.setTextColor(Color.parseColor("#FB7950"))
        } else {
            binding.btnReset.text = context.getString(R.string.reset)
            binding.btnCancel.text = context.getString(R.string.stop)
            if (reset) binding.btnReset.setOnClickListener {
                resetAction.invoke()
                dismiss()
            } else {
                binding.btnReset.isEnabled = false
                binding.btnReset.setTextColor(Color.parseColor("#b5b5b5"))
            }
        }
        binding.btnCancel.setOnClickListener {
            cancelAction.invoke()
            dismiss() // Dismiss the popup menu
        }
    }

}