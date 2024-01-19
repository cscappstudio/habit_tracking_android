package com.cscmobi.habittrackingandroid.presentation.ui.adapter

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.cscmobi.habittrackingandroid.R
import com.cscmobi.habittrackingandroid.data.dto.entities.Task
import com.cscmobi.habittrackingandroid.databinding.ItemTaskBinding
import com.cscmobi.habittrackingandroid.presentation.ItemTaskWithEdit
import com.cscmobi.habittrackingandroid.presentation.ui.custom.SwipeRevealLayout
import com.cscmobi.habittrackingandroid.presentation.ui.custom.ViewBinderHelper
import kotlin.random.Random


class TaskAdapter(private val onItemClickAdapter: ItemTaskWithEdit<Task>): ListAdapter<Task,TaskAdapter.ViewHolder>(DIFF_CALLBACK()) {
    private val binderHelper = ViewBinderHelper()

    class ViewHolder(val binding: ItemTaskBinding) :  RecyclerView.ViewHolder(binding.root){
         fun bind(item: Task, onItemClickAdapter: ItemTaskWithEdit<Task>) {

             var isTaskDone = Random.nextBoolean()

             if (isTaskDone) {
                 binding.ctTask.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(binding.root.context,R.color.blue))

             } else {

             }

             binding.swipeLayout.setSwipeListener(object : SwipeRevealLayout.SwipeListener{
                 override fun onClosed(view: SwipeRevealLayout?) {
                     binding.rdCheck.visibility = View.VISIBLE

                 }

                 override fun onOpened(view: SwipeRevealLayout?) {
                     binding.rdCheck.visibility = View.INVISIBLE
                 }

                 override fun onSlide(view: SwipeRevealLayout?, slideOffset: Float) {
                 }

             }) 
             
             binding.ivSkip.setOnClickListener {
                 onItemClickAdapter.skip(item,adapterPosition)
                 
             }

             binding.ivEdit.setOnClickListener {
                 onItemClickAdapter.edit(item,adapterPosition)
             }

             binding.ivvDelete.setOnClickListener {
                 onItemClickAdapter.delete(item,adapterPosition)
             }
             
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