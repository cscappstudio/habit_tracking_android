package com.cscmobi.habittrackingandroid.thanhlv.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cscmobi.habittrackingandroid.R
import com.cscmobi.habittrackingandroid.data.model.TaskTimelineModel
import com.cscmobi.habittrackingandroid.databinding.ItemTaskTimelineFullBinding
import com.cscmobi.habittrackingandroid.utils.CalendarUtil
import com.thanhlv.fw.helper.DisplayUtils

class DetailChallengeAdapter(private var mContext: Context) :
    RecyclerView.Adapter<DetailChallengeAdapter.ViewHolder>() {
    class ViewHolder(var binding: ItemTaskTimelineFullBinding) :
        RecyclerView.ViewHolder(binding.root)

    private var mList = mutableListOf<TaskTimelineModel>()
    private var mCallBack: MoodRecordCallback? = null

    interface MoodRecordCallback {
        fun onClickItem(pos: Int)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(data: MutableList<TaskTimelineModel>) {
        this.mList = data
        notifyDataSetChanged()
    }

    fun setCallBack(callback: MoodRecordCallback) {
        this.mCallBack = callback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemTaskTimelineFullBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    @SuppressLint("UseCompatLoadingForDrawables", "SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (mList.isEmpty()) return
        val item = mList[position]
        holder.binding.root.setOnClickListener {
            mCallBack?.onClickItem(position)
        }

        holder.binding.tvTitle.text = item.task.name
//        holder.binding.tvDay.visibility = if (item.task.taskNo == 0) View.VISIBLE else View.GONE

        if (item.task.taskNo == 0) {
            holder.binding.tvDay.visibility = View.VISIBLE
            if (item.task.startDate == null)
                holder.binding.tvDay.text = "Day " + item.task.dayNo.toString()
            else holder.binding.tvDay.text = CalendarUtil.getTitleDayMonth(item.task.startDate!!)
            holder.binding.root.layoutParams.height = DisplayUtils.dpToPx(119f)
        } else {
            holder.binding.tvDay.visibility = View.GONE
            holder.binding.root.layoutParams.height = DisplayUtils.dpToPx(96f)
        }
        if (item.type == 0) {
            if (item.status == 0)
                holder.binding.vectorTimeline.setImageResource(R.drawable.ic_timeline_start_no)
            if (item.status == 1)
                holder.binding.vectorTimeline.setImageResource(R.drawable.ic_timeline_start_fill)
            if (item.status == 2)
                holder.binding.vectorTimeline.setImageResource(R.drawable.ic_timeline_start_no_fill)
            if (item.status == 3)
                holder.binding.vectorTimeline.setImageResource(R.drawable.ic_timeline_start_fill_old)
        } else if (item.type == 1) {
            if (item.status == 0)
                holder.binding.vectorTimeline.setImageResource(R.drawable.ic_timeline_no_fill_old)
            if (item.status == 1)
                holder.binding.vectorTimeline.setImageResource(R.drawable.ic_timeline_fill)
            if (item.status == 2)
                holder.binding.vectorTimeline.setImageResource(R.drawable.ic_timeline_no_fill)
            if (item.status == 3)
                holder.binding.vectorTimeline.setImageResource(R.drawable.ic_timeline_fill_old)
        } else if (item.type == 2) {
            if (item.status == 0)
                holder.binding.vectorTimeline.setImageResource(R.drawable.ic_timeline_end)
            if (item.status == 1)
                holder.binding.vectorTimeline.setImageResource(R.drawable.ic_timeline_end_fill)
            if (item.status == 2)
                holder.binding.vectorTimeline.setImageResource(R.drawable.ic_timeline_end_no_fill)
        }

    }

    override fun getItemCount(): Int {
        return mList.size
    }
}