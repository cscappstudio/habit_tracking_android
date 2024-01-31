package com.cscmobi.habittrackingandroid.thanhlv.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.cscmobi.habittrackingandroid.databinding.ItemAllChallengeBinding
import com.cscmobi.habittrackingandroid.databinding.ItemEmojiTaskBinding
import com.cscmobi.habittrackingandroid.databinding.ItemMonthCalendarBinding
import com.cscmobi.habittrackingandroid.databinding.ItemMoodRecordBinding
import com.cscmobi.habittrackingandroid.databinding.ItemMyChallengeBinding
import com.cscmobi.habittrackingandroid.databinding.ItemTextTag2Binding
import com.cscmobi.habittrackingandroid.databinding.ItemTextTagBinding
import com.cscmobi.habittrackingandroid.thanhlv.model.DayCalendarModel
import com.cscmobi.habittrackingandroid.thanhlv.model.FeelingTagModel

class EmojiAdapter(private var mContext: Context, var mList: List<String>) :
    RecyclerView.Adapter<EmojiAdapter.ViewHolder>() {
    class ViewHolder(var binding: ItemEmojiTaskBinding) : RecyclerView.ViewHolder(binding.root)

    private var mCallback: EmojiCallback? = null
    interface EmojiCallback{
        fun onClickItem(emoji: String)
    }

    fun setCallback(callback: EmojiCallback) {
        this.mCallback = callback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemEmojiTaskBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (mList.isEmpty()) return
        holder.binding.imgAva.setImageURI(Uri.parse(mList[position]))
        holder.binding.root.setOnClickListener {
            mCallback?.onClickItem(mList[position])
        }
    }

    override fun getItemCount(): Int {
        return mList.size
    }
}