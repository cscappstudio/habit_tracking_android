package com.cscmobi.habittrackingandroid.presentation.ui.adapter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.cscmobi.habittrackingandroid.data.model.Task
import com.cscmobi.habittrackingandroid.databinding.ItemTaskBinding
import com.cscmobi.habittrackingandroid.presentation.ItemWithPostionListener
import com.cscmobi.habittrackingandroid.presentation.ui.custom.ViewBinderHelper


class TaskAdapter(private val onItemClickAdapter: ItemWithPostionListener<Task>): ListAdapter<Task,TaskAdapter.ViewHolder>(DIFF_CALLBACK()) {
    private val binderHelper = ViewBinderHelper()

    class ViewHolder(val binding: ItemTaskBinding) :  RecyclerView.ViewHolder(binding.root){
         fun bind(item: Task, onItemClickAdapter: ItemWithPostionListener<Task>) {

             binding.root.setOnClickListener {
                 onItemClickAdapter.onItemClicked(item,adapterPosition)
             }
         }

         companion object {
             fun onBind(parent: ViewGroup): ViewHolder {
                 val view = ItemTaskBinding.inflate(
                     LayoutInflater.from(parent.context),
                     parent,
                     false
                 )
                 return ViewHolder(view)
             }
         }
    }

    fun saveStates(outState: Bundle?) {
        binderHelper.saveStates(outState)
    }

    fun restoreStates(inState: Bundle?) {
        binderHelper.restoreStates(inState)
    }

    companion object {
        @JvmStatic
        fun DIFF_CALLBACK(): DiffUtil.ItemCallback<Task> =
            object : DiffUtil.ItemCallback<Task>() {

                override fun areItemsTheSame(
                    oldItem: Task,
                    newItem: Task
                ): Boolean {
                    return oldItem.name == newItem.name
                }

                override fun areContentsTheSame(
                    oldItem: Task,
                    newItem: Task
                ): Boolean {
                    return  oldItem == newItem
                }
            }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return  ViewHolder.onBind(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        binderHelper.bind(holder.binding.swipeLayout,getItem(position).name)
        holder.bind(getItem(position), onItemClickAdapter)
    }
}