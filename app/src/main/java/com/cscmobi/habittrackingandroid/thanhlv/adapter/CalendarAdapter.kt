package com.cscmobi.habittrackingandroid.thanhlv.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cscmobi.habittrackingandroid.databinding.ItemDayCalendarBinding
import com.cscmobi.habittrackingandroid.thanhlv.model.DayCalendarModel

class CalendarAdapter(private var mContext: Context, private var callback: Callback) :
    RecyclerView.Adapter<CalendarAdapter.ViewHolder>() {
    class ViewHolder(var binding: ItemDayCalendarBinding) : RecyclerView.ViewHolder(binding.root)
    interface Callback {
        fun onClickDayCalendar(ovulationCalendarModel: DayCalendarModel)
    }

    private var mList = mutableListOf<DayCalendarModel>()

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(list: MutableList<DayCalendarModel>) {
        this.mList = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemDayCalendarBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val ovulationCalendarModel = mList[position]
        holder.binding.tvDay.text = ovulationCalendarModel.getDay()
        holder.binding.root.setOnClickListener {
            callback.onClickDayCalendar(ovulationCalendarModel)
        }
    }

    override fun getItemCount(): Int {
        return mList.size
    }
}