package com.cscmobi.habittrackingandroid.presentation.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.cscmobi.habittrackingandroid.R
import com.cscmobi.habittrackingandroid.databinding.ItemDetailTaskCalenderBinding

class DetailTaskCalenderAdapter :
    ListAdapter<DetailData, DetailTaskCalenderAdapter.CalendarViewHolder?>(object :
        DiffUtil.ItemCallback<DetailData>() {
        override fun areItemsTheSame(oldItem: DetailData, newItem: DetailData): Boolean {
            return oldItem.day == newItem.day
        }

        override fun areContentsTheSame(oldItem: DetailData, newItem: DetailData): Boolean {
            return oldItem == newItem
        }
    }) {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalendarViewHolder {
        val view = ItemDetailTaskCalenderBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return CalendarViewHolder(view)
    }

    override fun onBindViewHolder(holder: CalendarViewHolder, position: Int) {
        holder.bind(getItem(position))
    }


    inner class CalendarViewHolder constructor(val binding: ItemDetailTaskCalenderBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: DetailData) {

            binding.txtDay.text = item.day
            binding.txtDay.setTextColor(
                ContextCompat.getColor(
                    binding.root.context,
                    R.color.bottle_green
                )
            )
            if (item.progress == -2) {
                binding.sbWeek.visibility = View.GONE
               if (!item.textDateState) {
                   binding.txtDay.setTextColor(ContextCompat.getColor(binding.root.context, R.color.gray))
               } else {
                   binding.txtDay.setTextColor(
                       ContextCompat.getColor(
                           binding.root.context,
                           R.color.bottle_green
                       )
                   )
               }

            } else if (item.progress == -1) {
                binding.sbWeek.visibility = View.GONE

                binding.txtDay.setTextColor(
                    ContextCompat.getColor(
                        binding.root.context,
                        R.color.bottle_green
                    )
                )
            }

            else {
                binding.sbWeek.visibility = View.VISIBLE

                if (item.progress == 100) {
                    binding.sbWeek.setProgressDisplayAndInvalidate(binding.sbWeek.max)
                    binding.txtDay.visibility = View.GONE

                } else {
                    binding.sbWeek.visibility = View.VISIBLE
                    binding.sbWeek.setProgressDisplayAndInvalidate(item.progress)
                    binding.txtDay.setTextColor(
                        ContextCompat.getColor(
                            binding.root.context,
                            R.color.grey
                        )
                    )
                }

            }

        }

    }
}

data class DetailData(
    var day: String,
    var progress: Int = -2,
    var textDateState: Boolean = true
)