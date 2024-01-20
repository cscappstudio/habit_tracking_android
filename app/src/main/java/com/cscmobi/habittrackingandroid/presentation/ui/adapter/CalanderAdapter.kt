package com.cscmobi.habittrackingandroid.presentation.ui.adapter

import android.content.res.ColorStateList
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.cscmobi.habittrackingandroid.R
import com.cscmobi.habittrackingandroid.databinding.CalendarCellBinding
import com.cscmobi.habittrackingandroid.databinding.ItemCreateCollectionBinding
import com.cscmobi.habittrackingandroid.databinding.ItemTextDayofmonthBinding
import com.cscmobi.habittrackingandroid.presentation.ItemWithPostionListener
import com.cscmobi.habittrackingandroid.utils.Helper
import com.google.android.material.animation.AnimatableView.Listener
import java.util.Calendar


class CalendarAdapter :
    ListAdapter<CalenderData, CalendarAdapter.CalendarViewHolder?>(object :
        DiffUtil.ItemCallback<CalenderData>() {
        override fun areItemsTheSame(oldItem: CalenderData, newItem: CalenderData): Boolean {
            return oldItem.day == newItem.day
        }

        override fun areContentsTheSame(oldItem: CalenderData, newItem: CalenderData): Boolean {
            return oldItem == newItem
        }
    }) {

    var colorSelect: Int = -1

    private var onItemListener: ItemWithPostionListener<CalenderData>? = null
    fun setListener(listener: ItemWithPostionListener<CalenderData>) {
        onItemListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalendarViewHolder {
        val view = ItemTextDayofmonthBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return CalendarViewHolder(view)
    }

    override fun onBindViewHolder(holder: CalendarViewHolder, position: Int) {
        holder.bind(getItem(position))
    }


    inner class CalendarViewHolder constructor(val binding: ItemTextDayofmonthBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: CalenderData) {

//            val layoutParams: ViewGroup.LayoutParams = binding.root.layoutParams
//            layoutParams.height = ((binding.root.height * 0.166666666).toInt())

            binding.txtDay.text = item.day
            if (item.day.isNotEmpty()) {
            if (item.day.toInt() <  Helper.currentDate.dayOfMonth) {

                binding.txtDay.setTextColor(ContextCompat.getColor(binding.root.context, R.color.gray))
                binding.frContainer.background = null
            }
            else {
                if (item.isSelected ) {
                    binding.txtDay.setTextColor(
                        ContextCompat.getColor(
                            binding.root.context,
                            R.color.white
                        )
                    )
                    binding.frContainer.setBackgroundResource(R.drawable.bg_circle)
                    binding.frContainer.backgroundTintList = ColorStateList.valueOf(
                        if (colorSelect != -1) colorSelect else
                        ContextCompat.getColor(
                            binding.root.context,
                            R.color.orange
                        )
                    )

                } else {
                    binding.txtDay.setTextColor(
                        ContextCompat.getColor(
                            binding.root.context,
                            R.color.bottle_green
                        )
                    )
                    binding.frContainer.background = null
                }

                binding.root.setOnClickListener {

                    if (adapterPosition in 1..31)
                    onItemListener?.onItemClicked(item, adapterPosition)
                }
            }
            } else {
                binding.frContainer.background = null

            }
        }

    }
}

data class CalenderData(
    var day: String = "",
    var isSelected: Boolean = false
)
