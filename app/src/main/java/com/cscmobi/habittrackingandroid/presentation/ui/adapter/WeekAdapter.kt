package com.cscmobi.habittrackingandroid.presentation.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.cscmobi.habittrackingandroid.data.model.WeekCalenderItem
import com.cscmobi.habittrackingandroid.databinding.ItemWeekcalenderBinding
import com.cscmobi.habittrackingandroid.databinding.ItemWeekcalenderSelectedBinding
import com.cscmobi.habittrackingandroid.presentation.OnItemClickPositionListener

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
            binding.txtDate.text = item.date.toString()
            binding.root.setOnClickListener {
                onItemClickAdapter.onItemClick(adapterPosition)
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