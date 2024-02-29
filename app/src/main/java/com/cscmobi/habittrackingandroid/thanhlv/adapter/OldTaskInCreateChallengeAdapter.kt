package com.cscmobi.habittrackingandroid.thanhlv.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.BitmapFactory
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cscmobi.habittrackingandroid.databinding.ItemOldTaskPopupBinding
import com.cscmobi.habittrackingandroid.thanhlv.model.CreateTaskChallenge

class OldTaskInCreateChallengeAdapter(private var mContext: Context) :
    RecyclerView.Adapter<OldTaskInCreateChallengeAdapter.ViewHolder>() {
    class ViewHolder(var binding: ItemOldTaskPopupBinding) :
        RecyclerView.ViewHolder(binding.root)

    private var mList = mutableListOf<CreateTaskChallenge>()
    private var mCallBack: OldTaskInCreateChallengeAdapter? = null

    interface OldTaskInCreateChallengeAdapter {
        fun onClickAdd(item: CreateTaskChallenge)
    }

    fun setCallBack(callback: OldTaskInCreateChallengeAdapter) {
        this.mCallBack = callback
    }


    @SuppressLint("NotifyDataSetChanged")
    fun setData(data: MutableList<CreateTaskChallenge>) {
        this.mList = data
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemOldTaskPopupBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    @SuppressLint("UseCompatLoadingForDrawables", "SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (mList.isEmpty()) return

        holder.binding.btnAddTask.setOnClickListener {
            mCallBack?.onClickAdd(mList[position])
        }

        val item = mList[position]
        holder.binding.tvTitle.text = item.name
        holder.binding.icTask.setImageBitmap(BitmapFactory.decodeStream(mContext.assets.open(item.icon)))
        holder.binding.icTask.imageTintList = ColorStateList.valueOf(Color.WHITE)
        holder.binding.icTask.backgroundTintList =
            ColorStateList.valueOf(Color.parseColor(item.color))

    }

    override fun getItemCount(): Int {
        return mList.size
    }
}