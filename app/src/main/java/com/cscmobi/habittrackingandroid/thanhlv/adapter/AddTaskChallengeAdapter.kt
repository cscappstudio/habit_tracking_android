package com.cscmobi.habittrackingandroid.thanhlv.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.BitmapFactory
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cscmobi.habittrackingandroid.R
import com.cscmobi.habittrackingandroid.databinding.ItemAddTaskChallengeBinding
import com.cscmobi.habittrackingandroid.databinding.ItemTaskTimelineFullBinding
import com.cscmobi.habittrackingandroid.thanhlv.model.CreateTaskChallenge
import com.cscmobi.habittrackingandroid.thanhlv.model.DayCalendarModel
import com.thanhlv.fw.helper.DisplayUtils

class AddTaskChallengeAdapter(private var mContext: Context) :
    RecyclerView.Adapter<AddTaskChallengeAdapter.ViewHolder>() {
    class ViewHolder(var binding: ItemAddTaskChallengeBinding) :
        RecyclerView.ViewHolder(binding.root)

    private var mList = mutableListOf<CreateTaskChallenge>()
    private var mCallBack: AddTaskChallengeCallback? = null

    interface AddTaskChallengeCallback {
        fun onClickItem(item: CreateTaskChallenge, pos: Int)
        fun onClickDelete(item: CreateTaskChallenge, pos: Int)
    }

    fun setCallBack(callback: AddTaskChallengeCallback) {
        this.mCallBack = callback
    }


    @SuppressLint("NotifyDataSetChanged")
    fun setData(data: MutableList<CreateTaskChallenge>) {
        this.mList = ArrayList(data)
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemAddTaskChallengeBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    @SuppressLint("UseCompatLoadingForDrawables", "SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (mList.isEmpty()) return
        holder.binding.showBtnAdd.setOnClickListener {
            mCallBack?.onClickItem(mList[position], position)
        }
        holder.binding.tvTitle.setOnClickListener {
            mCallBack?.onClickItem(mList[position], position)
        }

        holder.binding.btnDelete.setOnClickListener {
            mCallBack?.onClickDelete(mList[position], position)
        }

        val item = mList[position]
        when (item.type) {// 0 = textday+add; 1 = add; 2 = text + task; 3 = task
            1 -> {
                holder.binding.root.layoutParams.height = DisplayUtils.dpToPx(96f)
                holder.binding.tvDay.visibility = View.GONE
                holder.binding.showBtnAdd.visibility = View.VISIBLE
                holder.binding.showTask.visibility = View.GONE
            }

            2 -> {
                holder.binding.root.layoutParams.height = DisplayUtils.dpToPx(112f)
                holder.binding.tvDay.visibility = View.VISIBLE
                holder.binding.tvDay.text = mContext.getString(R.string.day_) +" "+ item.day.toString()
                holder.binding.showBtnAdd.visibility = View.GONE
                holder.binding.showTask.visibility = View.VISIBLE
                holder.binding.tvTitle.text = item.name
                holder.binding.icTask.setImageBitmap(
                    BitmapFactory.decodeStream(
                        mContext.assets.open(
                            item.icon!!
                        )
                    )
                )
                holder.binding.icTask.imageTintList = ColorStateList.valueOf(Color.WHITE)
                holder.binding.icTask.backgroundTintList =
                    ColorStateList.valueOf(Color.parseColor(item.color))
            }

            3 -> {
                holder.binding.root.layoutParams.height = DisplayUtils.dpToPx(86f)
                holder.binding.tvDay.visibility = View.GONE
                holder.binding.showBtnAdd.visibility = View.GONE
                holder.binding.showTask.visibility = View.VISIBLE
                holder.binding.tvTitle.text = item.name
                holder.binding.icTask.setImageBitmap(
                    BitmapFactory.decodeStream(
                        mContext.assets.open(
                            item.icon
                        )
                    )
                )
                holder.binding.icTask.imageTintList = ColorStateList.valueOf(Color.WHITE)
                holder.binding.icTask.backgroundTintList =
                    ColorStateList.valueOf(Color.parseColor(item.color))
            }

            else -> {
                holder.binding.tvDay.text = mContext.getString(R.string.day_) + item.day.toString()
                holder.binding.root.layoutParams.height = DisplayUtils.dpToPx(112f)
                holder.binding.tvDay.visibility = View.VISIBLE
                holder.binding.showBtnAdd.visibility = View.VISIBLE
                holder.binding.showTask.visibility = View.GONE
            }
        }
    }

    override fun getItemCount(): Int {
        return mList.size
    }
}