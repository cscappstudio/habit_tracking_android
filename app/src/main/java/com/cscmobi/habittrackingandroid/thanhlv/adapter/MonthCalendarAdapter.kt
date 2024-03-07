package com.cscmobi.habittrackingandroid.thanhlv.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import com.cscmobi.habittrackingandroid.R
import com.cscmobi.habittrackingandroid.databinding.ItemMonthCalendarBinding
import com.cscmobi.habittrackingandroid.thanhlv.model.DayCalendarModel

class MonthCalendarAdapter(private var mContext: Context, private var callback: Callback) :
    RecyclerView.Adapter<MonthCalendarAdapter.ViewHolder>() {
    class ViewHolder(var binding: ItemMonthCalendarBinding) : RecyclerView.ViewHolder(binding.root)
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
            ItemMonthCalendarBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (mList.isEmpty()) return
        val itemDay = mList[position]
        holder.binding.tvDay.text = itemDay.getDay()
        if (itemDay.type == 1 || itemDay.mood > 0) {
            holder.binding.tvDay.visibility = View.GONE
            holder.binding.imgDone.visibility = View.GONE
            holder.binding.circleProgress.visibility = View.GONE
            holder.binding.imgMood.visibility = View.VISIBLE
            when (itemDay.mood) {
                1 -> {
                    holder.binding.imgMood.setImageResource(R.drawable.ic_mood_great)
                }
                2 -> {
                    holder.binding.imgMood.setImageResource(R.drawable.ic_mood_good)
                }
                3 -> {
                    holder.binding.imgMood.setImageResource(R.drawable.ic_mood_neutral)
                }
                4 -> {
                    holder.binding.imgMood.setImageResource(R.drawable.ic_mood_not_great)
                }
                5 -> {
                    holder.binding.imgMood.setImageResource(R.drawable.ic_mood_bad)
                }
                else -> {
                    holder.binding.imgMood.visibility = View.GONE
                    holder.binding.tvDay.visibility = View.VISIBLE
                }
            }
            if (itemDay.mood > 0)
            holder.binding.imgMood.startAnimation(
                AnimationUtils.loadAnimation(
                    mContext,
                    R.anim.pulse2
                )
            )
        } else {
            if (itemDay.date.isEmpty()) {
                holder.binding.tvDay.visibility = View.GONE
                holder.binding.circleProgress.visibility = View.GONE
                holder.binding.imgMood.visibility = View.GONE
                holder.binding.imgDone.visibility = View.GONE
            } else {
                holder.binding.tvDay.visibility = View.VISIBLE
                holder.binding.imgMood.visibility = View.GONE
                holder.binding.imgDone.visibility = View.GONE
                if (itemDay.isPauseAllTask)
                    holder.binding.circleProgress.setProgress(0)
                else {
                    if (itemDay.progress >= 100) {
                        holder.binding.circleProgress.setProgress(itemDay.progress)
                        holder.binding.tvDay.visibility = View.GONE
                        holder.binding.imgDone.visibility = View.VISIBLE
                    } else {
                        holder.binding.tvDay.visibility = View.VISIBLE
                        holder.binding.imgDone.visibility = View.GONE
                        if (itemDay.progress >= 0) {
                            holder.binding.circleProgress.setProgress(itemDay.progress)
                        } else {
                            holder.binding.circleProgress.visibility = View.GONE
                        }
                    }
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return mList.size
    }
}