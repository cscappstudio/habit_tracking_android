package com.cscmobi.habittrackingandroid.thanhlv.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cscmobi.habittrackingandroid.R
import com.cscmobi.habittrackingandroid.databinding.ItemYearCalendarBinding
import com.cscmobi.habittrackingandroid.thanhlv.model.DayCalendarModel

class YearCalendarAdapter(private var mContext: Context) :
    RecyclerView.Adapter<YearCalendarAdapter.ViewHolder>() {
    class ViewHolder(var binding: ItemYearCalendarBinding) : RecyclerView.ViewHolder(binding.root)

    private var mList = mutableListOf<DayCalendarModel>()

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(list: MutableList<DayCalendarModel>) {
        this.mList = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemYearCalendarBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (mList.isEmpty()) return
        val item = mList[position]
        if (!item.isPauseAllTask && item.progress >= 100) {
            holder.binding.itemDay.background = mContext.getDrawable(R.drawable.bg_item_yearly_calendar_2)
        } else if (!item.isPauseAllTask && item.progress >= 0) {
            holder.binding.itemDay.background = mContext.getDrawable(R.drawable.bg_item_yearly_calendar_1)
        }
    }

    override fun getItemCount(): Int {
        return mList.size
    }
}