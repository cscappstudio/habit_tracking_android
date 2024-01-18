package com.cscmobi.habittrackingandroid.thanhlv.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cscmobi.habittrackingandroid.databinding.ItemMonthCalendarBinding
import com.cscmobi.habittrackingandroid.databinding.ItemMoodRecordBinding
import com.cscmobi.habittrackingandroid.databinding.ItemMyChallengeBinding
import com.cscmobi.habittrackingandroid.thanhlv.model.DayCalendarModel

class MyChallengeAdapter(private var mContext: Context) :
    RecyclerView.Adapter<MyChallengeAdapter.ViewHolder>() {
    class ViewHolder(var binding: ItemMyChallengeBinding) : RecyclerView.ViewHolder(binding.root)

    private var mList = mutableListOf<DayCalendarModel>()
    private var mCallBack : MyChallengeCallback? = null

    interface MyChallengeCallback{
        fun onClickItem(pos: Int)
    }

    fun setCallBack(callback: MyChallengeCallback) {
        this.mCallBack = callback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemMyChallengeBinding.inflate(
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
        return 7
    }
}