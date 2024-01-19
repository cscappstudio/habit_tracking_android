package com.cscmobi.habittrackingandroid.thanhlv.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.cscmobi.habittrackingandroid.databinding.ItemAllChallengeBinding
import com.cscmobi.habittrackingandroid.databinding.ItemMonthCalendarBinding
import com.cscmobi.habittrackingandroid.databinding.ItemMoodRecordBinding
import com.cscmobi.habittrackingandroid.databinding.ItemMyChallengeBinding
import com.cscmobi.habittrackingandroid.databinding.ItemTextTag2Binding
import com.cscmobi.habittrackingandroid.databinding.ItemTextTagBinding
import com.cscmobi.habittrackingandroid.thanhlv.model.DayCalendarModel
import com.cscmobi.habittrackingandroid.thanhlv.model.FeelingTagModel

class FeelingTag2Adapter(private var mContext: Context) :
    RecyclerView.Adapter<FeelingTag2Adapter.ViewHolder>() {
    class ViewHolder(var binding: ItemTextTag2Binding) : RecyclerView.ViewHolder(binding.root)
    private var mList = mutableListOf<FeelingTagModel>()

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(list: MutableList<FeelingTagModel>) {
        this.mList = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemTextTag2Binding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    }

    override fun getItemCount(): Int {
        if (mList.isEmpty()) return 5
        return mList.size
    }
}