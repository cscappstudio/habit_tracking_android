package com.cscmobi.habittrackingandroid.thanhlv.ui

import android.content.Context
import android.view.LayoutInflater
import android.widget.PopupWindow
import com.cscmobi.habittrackingandroid.databinding.ActivityDetailChallengeBinding
import com.cscmobi.habittrackingandroid.databinding.MenuDropDownLayoutBinding
import com.thanhlv.fw.helper.DisplayUtils

class MenuDropDown(context: Context) : PopupWindow(context) {

    init {
        val binding = MenuDropDownLayoutBinding.inflate(LayoutInflater.from(context))
        contentView = binding.root
        setBackgroundDrawable(null)
        width = DisplayUtils.dpToPx(160f)
        height = DisplayUtils.dpToPx(100f)
        isFocusable = true
        isOutsideTouchable = true
    }

}