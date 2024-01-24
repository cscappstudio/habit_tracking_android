package com.cscmobi.habittrackingandroid.presentation.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.cscmobi.habittrackingandroid.R
import com.cscmobi.habittrackingandroid.data.model.WeekCalenderItem
import com.cscmobi.habittrackingandroid.databinding.ItemWeekcalenderBinding
import com.cscmobi.habittrackingandroid.databinding.ItemWeekcalenderSelectedBinding
import com.cscmobi.habittrackingandroid.presentation.OnItemClickPositionListener
import com.cscmobi.habittrackingandroid.utils.Helper
import kotlin.random.Random

class WeekAdapter( val onItemClickAdapter: OnItemClickPositionListener) : ListAdapter<WeekCalenderItem, RecyclerView.ViewHolder>(WeekAdapter.DIFF_CALLBACK()){

     class ViewHolderItemSelect constructor(val binding: ItemWeekcalenderSelectedBinding): RecyclerView.ViewHolder(binding.root) {

        fun bind(item: WeekCalenderItem) {
            binding.txtDate.text = item.date.toString()
            binding.txtDay.text = item.day
        }

        companion object {
            fun onBind(parent: ViewGroup): ViewHolderItemSelect {
                val view = ItemWeekcalenderSelectedBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                return ViewHolderItemSelect(view)
            }
        }
    }

    class ViewHolderItemUnSelect constructor(val binding: ItemWeekcalenderBinding): RecyclerView.ViewHolder(binding.root) {

        fun bind(item: WeekCalenderItem,  onItemClickAdapter: OnItemClickPositionListener) {
            var test = Random.nextBoolean()

            if ((item.localDate ?: Helper.currentDate).isAfter(Helper.currentDate)) {
                binding.sbWeek.setProgressDisplayAndInvalidate(binding.sbWeek.min)
                binding.sbWeek.resetBgColorWhenProgressIs0(ContextCompat.getColor(binding.root.context,
                    R.color.silver),ContextCompat.getColor(binding.root.context,
                    R.color.hexD4D4D4),true)

            } else {
                binding.sbWeek.resetBgColorWhenProgressIs0(ContextCompat.getColor(binding.root.context,
                    R.color.silver),ContextCompat.getColor(binding.root.context,
                    R.color.hexD4D4D4),false)

                if (test) {
                    binding.sbWeek.setProgressDisplayAndInvalidate(binding.sbWeek.max)
                } else {
                    binding.sbWeek.setProgressDisplayAndInvalidate(50)

                }
            }






            binding.root.setOnClickListener {
                    onItemClickAdapter.onItemClick(layoutPosition)
            }
        }

        companion object {
            fun onBind(parent: ViewGroup): ViewHolderItemUnSelect {
                val view = ItemWeekcalenderBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                return ViewHolderItemUnSelect(view)
            }
        }
    }
    companion object {
        @JvmStatic
        fun DIFF_CALLBACK(): DiffUtil.ItemCallback<WeekCalenderItem> = object : DiffUtil.ItemCallback<WeekCalenderItem>() {


            override fun areItemsTheSame(
                oldItem: WeekCalenderItem,
                newItem: WeekCalenderItem
            ): Boolean {
                return oldItem.date == newItem.date
            }

            override fun areContentsTheSame(
                oldItem: WeekCalenderItem,
                newItem: WeekCalenderItem
            ): Boolean {
                return (oldItem.date == newItem.date) && oldItem.day == newItem.day
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return  when(viewType) {
            ViewType.DAYUNSELECT.ordinal -> ViewHolderItemUnSelect.onBind(parent)
            else -> ViewHolderItemSelect.onBind(parent)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder.itemViewType) {
            ViewType.DAYSELECT.ordinal -> (holder as ViewHolderItemSelect).bind(getItem(position))
            else -> (holder as ViewHolderItemUnSelect).bind(getItem(position),onItemClickAdapter)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position).isSelected) {
            false -> ViewType.DAYUNSELECT.ordinal
            else ->  ViewType.DAYSELECT.ordinal
        }
    }

    enum class ViewType(type: Int) {
        DAYSELECT(1),
        DAYUNSELECT(0)
    }
}