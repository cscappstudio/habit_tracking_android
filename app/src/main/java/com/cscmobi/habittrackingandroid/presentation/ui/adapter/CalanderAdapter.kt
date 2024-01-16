package com.cscmobi.habittrackingandroid.presentation.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.cscmobi.habittrackingandroid.databinding.CalendarCellBinding
import com.cscmobi.habittrackingandroid.presentation.ItemWithPostionListener


class CalendarAdapter(private val onItemListener: ItemWithPostionListener<String>) :
    ListAdapter<String, CalendarAdapter.CalendarViewHolder?>(object :
        DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }
    }) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalendarViewHolder {
        return CalendarViewHolder.onBind(parent)
    }

    override fun onBindViewHolder(holder: CalendarViewHolder, position: Int) {
        holder.bind(getItem(position), onItemListener)
    }

    class CalendarViewHolder constructor(val binding: CalendarCellBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: String, onItemClickAdapter: ItemWithPostionListener<String>) {

            val layoutParams: ViewGroup.LayoutParams = binding.root.layoutParams
            layoutParams.height = ((binding.root.height * 0.166666666).toInt())

            binding.cellDayText.text = item

            binding.root.setOnClickListener {
                onItemClickAdapter.onItemClicked(item, adapterPosition)

            }
        }

        companion object {
            fun onBind(parent: ViewGroup): CalendarViewHolder {
                val view = CalendarCellBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                return CalendarViewHolder(view)
            }
        }

    }
}
