package com.cscmobi.habittrackingandroid.thanhlv.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.cscmobi.habittrackingandroid.databinding.ItemAllChallengeBinding
import com.cscmobi.habittrackingandroid.databinding.ItemMonthCalendarBinding
import com.cscmobi.habittrackingandroid.databinding.ItemMoodRecordBinding
import com.cscmobi.habittrackingandroid.databinding.ItemMyChallengeBinding
import com.cscmobi.habittrackingandroid.databinding.ItemTextTagBinding
import com.cscmobi.habittrackingandroid.thanhlv.model.DayCalendarModel
import com.cscmobi.habittrackingandroid.thanhlv.model.FeelingTagModel

class FeelingTagAdapter(private var mContext: Context) :
    RecyclerView.Adapter<FeelingTagAdapter.ViewHolder>() {
    class ViewHolder(var binding: ItemTextTagBinding) : RecyclerView.ViewHolder(binding.root)

    private var mList = mutableListOf<FeelingTagModel>()
    private var mCallBack: FeelingTagCallback? = null

    private var colorSelected = "#E59045"

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(list: MutableList<FeelingTagModel>) {
        this.mList = list
        if (list.isEmpty()) return
        when (list[0].type) {
            2 -> {
                colorSelected = "#E59045"
            }

            3 -> {
                colorSelected = "#8ED0AE"
            }

            4 -> {
                colorSelected = "#92BFD2"
            }

            5 -> {
                colorSelected = "#92BFD2"
            }

            else -> {
                colorSelected = "#EC9B96"
            }
        }
        notifyDataSetChanged()
    }

    interface FeelingTagCallback {
        fun onClickItem(pos: Int)
    }

    fun setCallBack(callback: FeelingTagCallback) {
        this.mCallBack = callback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(
            ItemTextTagBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }


    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (mList.isEmpty()) return
        val item = mList[position]
        holder.binding.root.backgroundTintList =
            if (item.selected) ColorStateList.valueOf(Color.parseColor(colorSelected))
            else null
//        holder.binding.tvTitle.setTextColor(
//            if (item.selected) Color.parseColor("#ffffff")
//            else Color.parseColor("#EDCA15")
//        )

        holder.binding.tvTitle.text = item.describe
        holder.binding.root.setOnClickListener {
            item.selected = !item.selected

            holder.binding.root.backgroundTintList =
                if (item.selected) ColorStateList.valueOf(Color.parseColor(colorSelected))
                else null
//            holder.binding.tvTitle.setTextColor(
//                if (item.selected) Color.parseColor("#ffffff")
//                else Color.parseColor("#EDCA15")
//            )

            mCallBack?.onClickItem(position)
        }
    }

    private fun toggleItemView(view: View, selected: Boolean) {
        view.visibility = if (selected) View.VISIBLE else View.GONE
    }

    override fun getItemCount(): Int {
        if (mList.isEmpty()) return 0
        return mList.size
    }
}