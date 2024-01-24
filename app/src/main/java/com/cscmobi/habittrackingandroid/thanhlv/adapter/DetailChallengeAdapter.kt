package com.cscmobi.habittrackingandroid.thanhlv.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cscmobi.habittrackingandroid.databinding.ItemTaskTimelineFullBinding
import com.cscmobi.habittrackingandroid.thanhlv.model.DayCalendarModel

class DetailChallengeAdapter(private var mContext: Context) :
    RecyclerView.Adapter<DetailChallengeAdapter.ViewHolder>() {
    class ViewHolder(var binding: ItemTaskTimelineFullBinding) : RecyclerView.ViewHolder(binding.root)

    private var mList = mutableListOf<DayCalendarModel>()
    private var mCallBack : MoodRecordCallback? = null

    interface MoodRecordCallback{
        fun onClickItem(pos: Int)
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

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.root.setOnClickListener {
            mCallBack?.onClickItem(position)
        }
    }

    override fun getItemCount(): Int {
        return 15
    }
}