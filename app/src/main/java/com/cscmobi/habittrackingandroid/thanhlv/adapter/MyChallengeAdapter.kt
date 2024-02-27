package com.cscmobi.habittrackingandroid.thanhlv.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cscmobi.habittrackingandroid.R
import com.cscmobi.habittrackingandroid.databinding.ItemMonthCalendarBinding
import com.cscmobi.habittrackingandroid.databinding.ItemMoodRecordBinding
import com.cscmobi.habittrackingandroid.databinding.ItemMyChallengeBinding
import com.cscmobi.habittrackingandroid.thanhlv.model.Challenge
import com.cscmobi.habittrackingandroid.thanhlv.model.DayCalendarModel

class MyChallengeAdapter(private var mContext: Context) :
    RecyclerView.Adapter<MyChallengeAdapter.ViewHolder>() {
    class ViewHolder(var binding: ItemMyChallengeBinding) : RecyclerView.ViewHolder(binding.root)

    private var mList = mutableListOf<Challenge>()
    private var mCallBack: MyChallengeCallback? = null

    interface MyChallengeCallback {
        fun onClickItem(challenge: Challenge)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(data: MutableList<Challenge>?) {
        if (data != null) this.mList = data
        notifyDataSetChanged()
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

    @SuppressLint("UseCompatLoadingForDrawables", "SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (mList.isEmpty()) return
        val itemData = mList[position]
        holder.binding.root.setOnClickListener {
            mCallBack?.onClickItem(itemData)
        }
        holder.binding.tvTitle.text = itemData.name
        holder.binding.tvDays.text = itemData.duration.toString() + " days"
        if (itemData.image.isEmpty())
            holder.binding.imgCover.setImageResource(R.drawable.img_target)
        else
            holder.binding.imgCover.setImageBitmap(BitmapFactory.decodeStream(mContext.assets.open(itemData.image)))
    }

    override fun getItemCount(): Int {
        return this.mList.size
    }
}