package com.cscmobi.habittrackingandroid.thanhlv.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.adjust.sdk.Util
import com.cscmobi.habittrackingandroid.R
import com.cscmobi.habittrackingandroid.databinding.ItemAllChallengeBinding
import com.cscmobi.habittrackingandroid.databinding.ItemMonthCalendarBinding
import com.cscmobi.habittrackingandroid.databinding.ItemMoodRecordBinding
import com.cscmobi.habittrackingandroid.databinding.ItemMyChallengeBinding
import com.cscmobi.habittrackingandroid.thanhlv.model.Challenge
import com.cscmobi.habittrackingandroid.thanhlv.model.DayCalendarModel
import com.cscmobi.habittrackingandroid.utils.Utils

class AllChallengeAdapter(private var mContext: Context) :
    RecyclerView.Adapter<AllChallengeAdapter.ViewHolder>() {
    class ViewHolder(var binding: ItemAllChallengeBinding) : RecyclerView.ViewHolder(binding.root)

    private var mList = mutableListOf<Challenge>()
    private var mCallBack: AllChallengeCallback? = null

    interface AllChallengeCallback {
        fun onClickItem(challenge: Challenge)
    }

    fun setData(data: MutableList<Challenge>?) {
        if (data != null) this.mList = data
    }

    fun setCallBack(callback: AllChallengeCallback) {
        this.mCallBack = callback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemAllChallengeBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        if (mList.isEmpty()) return
        val itemData = mList[position]
        holder.binding.root.setOnClickListener {
            mCallBack?.onClickItem(itemData)
        }
        holder.binding.tvTitle.text = itemData.name
        if (itemData.image.isEmpty())
            holder.binding.imgCover.setImageResource(R.drawable.img_target)
        else
            holder.binding.imgCover.setImageDrawable(Utils.loadImageFromAssets(mContext, itemData.image))
//            holder.binding.imgCover.setImageBitmap(BitmapFactory.decodeStream(mContext.assets.open(itemData.image)))
    }

    override fun getItemCount(): Int {
        return this.mList.size
    }
}