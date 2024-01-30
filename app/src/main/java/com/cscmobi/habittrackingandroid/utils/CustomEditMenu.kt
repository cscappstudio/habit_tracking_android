package com.cscmobi.habittrackingandroid.utils

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupWindow
import com.cscmobi.habittrackingandroid.R
import com.cscmobi.habittrackingandroid.databinding.MenuEditTaskBinding

class CustomEditMenu (context: Context, editAction: () -> Unit, deleteAction: () -> Unit) :
    PopupWindow(context) {

    init {

        val layoutInflater = LayoutInflater.from(context)

        val binding =  MenuEditTaskBinding.inflate(layoutInflater)

        contentView = binding.root
        setBackgroundDrawable(null)
        width =  Utils.dpToPx(context.resources.getDimension(com.intuit.sdp.R.dimen._65sdp),context)
        height =  Utils.dpToPx(context.resources.getDimension(com.intuit.sdp.R.dimen._45sdp),context)
        isFocusable = true
        isOutsideTouchable = true



        // Set click listeners for menu items
        binding.tvEdit.setOnClickListener {
            // Handle click for Menu Item 1
            editAction.invoke()
            dismiss() // Dismiss the popup menu
        }

        binding.tvDelete.setOnClickListener {
            // Handle click for Menu Item 2
            deleteAction.invoke()
            dismiss() // Dismiss the popup menu
        }
    }
}

