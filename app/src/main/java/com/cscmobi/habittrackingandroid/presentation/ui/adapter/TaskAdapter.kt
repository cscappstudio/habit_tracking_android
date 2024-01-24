package com.cscmobi.habittrackingandroid.presentation.ui.adapter

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.cscmobi.habittrackingandroid.R
import com.cscmobi.habittrackingandroid.thanhlv.model.Task
import com.cscmobi.habittrackingandroid.databinding.ItemTaskBinding
import com.cscmobi.habittrackingandroid.presentation.ItemTaskWithEdit
import com.cscmobi.habittrackingandroid.presentation.ui.custom.SwipeRevealLayout
import com.cscmobi.habittrackingandroid.presentation.ui.custom.ViewBinderHelper
import com.cscmobi.habittrackingandroid.utils.setSpanTextView
import kotlin.random.Random


class TaskAdapter(private val onItemClickAdapter: ItemTaskWithEdit<Task>) :
    ListAdapter<Task, TaskAdapter.ViewHolder>(DIFF_CALLBACK()) {
    private val binderHelper = ViewBinderHelper()

   inner  class ViewHolder(val binding: ItemTaskBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Task, onItemClickAdapter: ItemTaskWithEdit<Task>) {

            val iconResourceId = binding.root.context.resources.getIdentifier(
                item.ava,
                "drawable",
                binding.root.context.packageName
            )

            binding.shapeableImageView.setImageResource(iconResourceId)
            binding.txtNameTask.text = item.name

            binding.shapeableImageView.backgroundTintList = ColorStateList.valueOf(Color.TRANSPARENT)

            item.goal?.let {
                if (it.isOn == true) {
                    binding.txtUnit.visibility = View.VISIBLE
                    binding.txtGoal.visibility = View.VISIBLE
                    binding.txtUnit.text = it.unit
                    binding.txtGoal.text = "${it.currentProgress}/${it.target}"



                } else {
                    binding.txtUnit.visibility = View.GONE
                    binding.txtGoal.visibility = View.GONE
                }


                if (it.currentProgress == it.target) {

                    binding.txtGoal.setTextColor(Color.WHITE)
                    binding.ctTask.backgroundTintList =
                        ColorStateList.valueOf(Color.parseColor(item.color))
                    binding.txtNameTask.setTextColor(Color.WHITE)

                    binding.shapeableImageView.backgroundTintList = ColorStateList.valueOf(Color.TRANSPARENT)
                    binding.shapeableImageView.imageTintList = ColorStateList.valueOf(Color.WHITE)
                    binding.line.visibility = View.VISIBLE
                    binding.txtUnit.setTextColor(Color.WHITE)
                    binding.rdCheck.isChecked = true

                } else {
                    binding.txtGoal.text = "${it.currentProgress}/${it.target}"

                    binding.txtGoal.setTextColor(ContextCompat.getColor(binding.root.context,R.color.dark_gray))
                    binding.txtUnit.setTextColor(ContextCompat.getColor(binding.root.context,R.color.grey))

                    binding.txtGoal.setSpanTextView(R.color.coral_red)
                    binding.ctTask.backgroundTintList = ColorStateList.valueOf(Color.WHITE)
                    binding.txtNameTask.setTextColor(
                        ContextCompat.getColor(
                            binding.root.context,
                            R.color.shadow_gray
                        )
                    )

                    binding.shapeableImageView.backgroundTintList = ColorStateList.valueOf(Color.parseColor(item.color))
                    binding.shapeableImageView.imageTintList = ColorStateList.valueOf(Color.WHITE)
                    binding.line.visibility = View.INVISIBLE
                    binding.rdCheck.isChecked = false

                }
                
                binding.rdCheck.setOnCheckedChangeListener { buttonView, isChecked ->
                    onItemClickAdapter.onItemChange(layoutPosition,item,isChecked)
                }
            }




            binding.swipeLayout.setSwipeListener(object : SwipeRevealLayout.SwipeListener {
                override fun onClosed(view: SwipeRevealLayout?) {
                    binding.rdCheck.visibility = View.VISIBLE
                    binding.frMenu.visibility = View.INVISIBLE


                }

                override fun onOpened(view: SwipeRevealLayout?) {
                    binding.frMenu.visibility = View.VISIBLE
                }

                override fun onSlide(view: SwipeRevealLayout?, slideOffset: Float) {
                    binding.rdCheck.visibility = View.INVISIBLE

                }

            })

            binding.ivSkip.setOnClickListener {
                onItemClickAdapter.skip(item, layoutPosition)

                Toast.makeText(binding.root.context, "aaaa", Toast.LENGTH_SHORT).show()

            }

            binding.ivEdit.setOnClickListener {
                onItemClickAdapter.edit(item, layoutPosition)
            }

            binding.ivvDelete.setOnClickListener {
                onItemClickAdapter.delete(item, layoutPosition)
            }

            binding.ctTask.setOnClickListener {
                onItemClickAdapter.onItemClicked(item, layoutPosition)
            }




        }



    }

    fun onBind(parent: ViewGroup): ViewHolder {
        val view = ItemTaskBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(view)
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
                    return oldItem == newItem
                }
            }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return onBind(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        binderHelper.bind(holder.binding.swipeLayout, getItem(position).name)
        holder.bind(getItem(position), onItemClickAdapter)
    }
}