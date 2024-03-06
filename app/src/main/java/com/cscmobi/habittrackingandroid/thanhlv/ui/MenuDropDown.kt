package com.cscmobi.habittrackingandroid.thanhlv.ui

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.widget.PopupWindow
import com.cscmobi.habittrackingandroid.databinding.ActivityDetailChallengeBinding
import com.cscmobi.habittrackingandroid.databinding.MenuDropDownLayoutBinding
import com.thanhlv.fw.helper.DisplayUtils

class MenuDropDown(
    context: Context,
    reset: Boolean,
    resetAction: () -> Unit,
    cancelAction: () -> Unit
) : PopupWindow(context) {

    init {
        val binding = MenuDropDownLayoutBinding.inflate(LayoutInflater.from(context))
        contentView = binding.root
        setBackgroundDrawable(null)
        width = DisplayUtils.dpToPx(160f)
        height = DisplayUtils.dpToPx(100f)
        isFocusable = true
        isOutsideTouchable = true


        // Set click listeners for menu items
        if (reset)
            binding.btnReset.setOnClickListener {
                // Handle click for Menu Item 1
                resetAction.invoke()
                dismiss() // Dismiss the popup menu
            } else {
            binding.btnReset.isEnabled = false
            binding.btnReset.setTextColor(Color.parseColor("#b5b5b5"))
        }

        binding.btnCancel.setOnClickListener {
            // Handle click for Menu Item 2
            cancelAction.invoke()
            dismiss() // Dismiss the popup menu
        }
    }

}