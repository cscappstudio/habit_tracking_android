package com.cscmobi.habittrackingandroid.thanhlv.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cscmobi.habittrackingandroid.R
import com.cscmobi.habittrackingandroid.databinding.ItemMonthCalendarBinding
import com.cscmobi.habittrackingandroid.databinding.ItemMoodRecordBinding
import com.cscmobi.habittrackingandroid.thanhlv.model.DayCalendarModel
import com.cscmobi.habittrackingandroid.thanhlv.model.Mood
import java.util.Calendar

class MoodRecordAdapter(private var mContext: Context) :
    RecyclerView.Adapter<MoodRecordAdapter.ViewHolder>() {
    class ViewHolder(var binding: ItemMoodRecordBinding) : RecyclerView.ViewHolder(binding.root)

    private var mList = mutableListOf<Mood>()
    private var mCallBack: MoodRecordCallback? = null

    interface MoodRecordCallback {
        fun onClickItem(mood: Mood)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(data: ArrayList<Mood>) {
        this.mList = data
        notifyDataSetChanged()
    }

    fun setCallBack(callback: MoodRecordCallback) {
        this.mCallBack = callback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemMoodRecordBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (mList.isEmpty()) return
        val itemMood = mList[position]
        when (itemMood.state) {
            1 -> {
                holder.binding.icMood.setImageResource(R.drawable.ic_mood_great)
                holder.binding.tvTitle.text = "Great"
            }
            2 -> {
                holder.binding.icMood.setImageResource(R.drawable.ic_mood_good)
                holder.binding.tvTitle.text = "Good"
            }
            3 -> {
                holder.binding.icMood.setImageResource(R.drawable.ic_mood_neutral)
                holder.binding.tvTitle.text = "Neutral"
            }
            4 -> {
                holder.binding.icMood.setImageResource(R.drawable.ic_mood_not_great)
                holder.binding.tvTitle.text = "Not great"
            }
            5 -> {
                holder.binding.icMood.setImageResource(R.drawable.ic_mood_bad)
                holder.binding.tvTitle.text = "Bab"
            }
        }
        holder.binding.tvDes.text = if (itemMood.describe.isEmpty()) "" else itemMood.describe[0]
        holder.binding.tvBecause.text = if (itemMood.becauseOf.isEmpty()) "" else itemMood.becauseOf[0]

        val day = Calendar.getInstance()
        day.timeInMillis = itemMood.date

        holder.binding.tvDay.text = day[Calendar.DAY_OF_MONTH].toString()

        if (itemMood.describe.size + itemMood.becauseOf.size > 2)
            holder.binding.tvEtc.visibility = View.VISIBLE
        else holder.binding.tvEtc.visibility = View.GONE

        holder.binding.root.setOnClickListener {
            mCallBack?.onClickItem(mList[position])
        }

    }

    override fun getItemCount(): Int {
        return mList.size
    }
}