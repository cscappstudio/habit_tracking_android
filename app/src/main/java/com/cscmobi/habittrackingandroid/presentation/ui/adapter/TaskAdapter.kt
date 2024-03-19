package com.cscmobi.habittrackingandroid.presentation.ui.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.cscmobi.habittrackingandroid.R
import com.cscmobi.habittrackingandroid.databinding.ItemTaskBinding
import com.cscmobi.habittrackingandroid.presentation.ItemTaskWithEdit
import com.cscmobi.habittrackingandroid.presentation.ui.custom.SwipeRevealLayout
import com.cscmobi.habittrackingandroid.presentation.ui.custom.ViewBinderHelper
import com.cscmobi.habittrackingandroid.thanhlv.model.Task
import com.cscmobi.habittrackingandroid.utils.Constant.IDLE
import com.cscmobi.habittrackingandroid.utils.Helper
import com.cscmobi.habittrackingandroid.utils.Helper.createBubbleShowCaseBuilder
import com.cscmobi.habittrackingandroid.utils.Utils.toDate
import com.cscmobi.habittrackingandroid.utils.setDrawableString
import com.cscmobi.habittrackingandroid.utils.setSpanTextView
import com.elconfidencial.bubbleshowcase.BubbleShowCaseSequence
import com.thanhlv.ads.lib.AdMobUtils
import com.thanhlv.fw.helper.MyClick
import java.util.Calendar
import java.util.Date


class TaskAdapter(
    private val activity: Activity,
    private val onItemClickAdapter: ItemTaskWithEdit<Task>
) :
    ListAdapter<Task, TaskAdapter.ViewHolder>(DIFF_CALLBACK()) {
    private val binderHelper = ViewBinderHelper()

    var date: Long = Helper.currentDate.toDate()


    override fun getItemCount(): Int {
        return this.currentList.size + 1
    }

    inner class ViewHolder(val binding: ItemTaskBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Task, onItemClickAdapter: ItemTaskWithEdit<Task>) {
            if (date <= Helper.currentDate.toDate()) {
                binding.rdCheck.isEnabled = true
            } else {
                binding.rdCheck.isEnabled = false
                binding.rdCheck.isChecked = false
            }
            if (item.id == IDLE && layoutPosition == 1) {
                binding.swipeLayout.visibility = View.GONE
                binding.adView.visibility = View.VISIBLE
                AdMobUtils.createNativeAd(
                    binding.root.context,
                    binding.root.context.getString(R.string.native_id),
                    binding.adView,
                    object : AdMobUtils.Companion.LoadAdCallback {
                        override fun onLoaded(ad: Any?) {

                        }

                        override fun onLoadFailed() {
//                        currentList.removeAt(1)
//                        notifyItemRemoved(1)
                        }

                    })

            } else {
                binding.swipeLayout.visibility = View.VISIBLE
                binding.adView.visibility = View.GONE
            }
//            val iconResourceId = binding.root.context.resources.getIdentifier(
//                item.ava,
//                "drawable",
//                binding.root.context.packageName
//            )

            var isPause = false

            binding.frMenu.visibility = View.INVISIBLE
            binding.swipeLayout.close(true)



            item.ava?.let { binding.shapeableImageView.setDrawableString(it) }
            binding.txtNameTask.text = item.name

            binding.shapeableImageView.backgroundTintList =
                ColorStateList.valueOf(Color.TRANSPARENT)

            if (item.pauseDate != null || item.pause != 0) {


                if (item.pause != -1) {
                    var c = Calendar.getInstance()
                    c.time = Date(item.pauseDate!!)
                    c.add(Calendar.DAY_OF_MONTH, item.pause - 1)

                    if (date in item.pauseDate!!.toDate()..c.time.time) {
                        binding.ivPlay.visibility = View.VISIBLE
                        binding.rdCheck.visibility = View.GONE
                        isPause = true
                    } else {
                        binding.ivPlay.visibility = View.GONE
                        binding.rdCheck.visibility = View.VISIBLE
                        isPause = false
                    }
                } else {
                    binding.ivPlay.visibility = View.VISIBLE
                    binding.rdCheck.visibility = View.GONE
                    isPause = true
                }


            } else {
                binding.ivPlay.visibility = View.GONE
                binding.rdCheck.visibility = View.VISIBLE
                isPause = false
            }

            item.goal?.let {
                if (it.isOn) {
                    binding.txtUnit.visibility = View.VISIBLE
                    binding.txtGoal.visibility = View.VISIBLE
                    binding.txtUnit.text = it.unit
                    binding.txtGoal.text = "${it.currentProgress}/${it.target}"


                } else {
                    binding.txtUnit.visibility = View.GONE
                    binding.txtGoal.visibility = View.GONE
                }


                if (it.currentProgress >= it.target) {

                    binding.txtGoal.setTextColor(Color.WHITE)
                    binding.ctTask.backgroundTintList =
                        ColorStateList.valueOf(Color.parseColor(item.color))
                    binding.txtNameTask.setTextColor(Color.WHITE)

                    binding.shapeableImageView.backgroundTintList =
                        ColorStateList.valueOf(Color.TRANSPARENT)
                    binding.shapeableImageView.imageTintList = ColorStateList.valueOf(Color.WHITE)
                    binding.txtNameTask.showStrikeThrough(true)
                    binding.txtUnit.setTextColor(Color.WHITE)
                    binding.rdCheck.isChecked = true

                } else {
                    binding.txtNameTask.showStrikeThrough(false)
                    binding.txtGoal.text = "${it.currentProgress}/${it.target}"

                    binding.txtGoal.setTextColor(
                        ContextCompat.getColor(
                            binding.root.context,
                            R.color.dark_gray
                        )
                    )
                    binding.txtUnit.setTextColor(
                        ContextCompat.getColor(
                            binding.root.context,
                            R.color.grey
                        )
                    )

                    binding.txtGoal.setSpanTextView(R.color.coral_red)
                    binding.ctTask.backgroundTintList = ColorStateList.valueOf(Color.WHITE)

                    binding.txtGoal.setAlpha(0.5f)
                    binding.txtUnit.setAlpha(0.5f)
                    binding.txtNameTask.setAlpha(0.5f)

                    binding.txtNameTask.setTextColor(
                        ContextCompat.getColor(
                            binding.root.context,
                            R.color.shadow_gray
                        )
                    )

                    binding.shapeableImageView.setAlpha(0.5f)

                    binding.shapeableImageView.backgroundTintList =
                        ColorStateList.valueOf(Color.parseColor(item.color))
                    binding.shapeableImageView.imageTintList = ColorStateList.valueOf(Color.WHITE)
                    binding.rdCheck.isChecked = false

                }

                binding.rdCheck.setOnClickListener(
                    object : MyClick(500) {
                        override fun onMyClick(v: View, count: Long) {
                            if (date <= Helper.currentDate.toDate())
                                onItemClickAdapter.onItemChange(
                                    layoutPosition,
                                    item,
                                    binding.rdCheck.isChecked
                                )
                        }
                    }
                )
            }



            binding.swipeLayout.setSwipeListener(object : SwipeRevealLayout.SwipeListener {
                override fun onClosed(view: SwipeRevealLayout?) {
                    if (isPause) {

                        binding.ivPlay.visibility = View.VISIBLE

                    } else binding.rdCheck.visibility = View.VISIBLE


                    binding.frMenu.visibility = View.INVISIBLE


                }

                @SuppressLint("UseCompatLoadingForDrawables")
                override fun onOpened(view: SwipeRevealLayout?) {
                    binding.frMenu.visibility = View.VISIBLE

                    BubbleShowCaseSequence()
                        .addShowCase(
                            activity.createBubbleShowCaseBuilder(
                                binding.ivSkip,
                                binding.root.context.getString(
                                    R.string.pausing_a_task_doesn_t_break_your_streak_you_can_resume_when_ready
                                ), "showcase_skip"
                            )
                                .closeActionImage(activity.getDrawable(R.drawable.arrow_right_circle))

                        ) //First BubbleShowCase to show
                        .addShowCase(
                            activity.createBubbleShowCaseBuilder(
                                binding.ivEdit,
                                binding.root.context.getString(
                                    R.string.tap_to_edit
                                ), "showcase_edit"
                            )
                                .closeActionImage(activity.getDrawable(R.drawable.arrow_right_circle))
                        ) //Second BubbleShowCase to show
                        .addShowCase(
                            activity.createBubbleShowCaseBuilder(
                                binding.ivvDelete,
                                binding.root.context.getString(
                                    R.string.tap_to_delete
                                ), "showcase_delete"
                            ).closeActionImage(activity.getDrawable(R.drawable.ic_round_close))
                        ) //Third BubbleShowCase to show
                        .show() //Display the ShowCaseSequence
                }

                override fun onSlide(view: SwipeRevealLayout?, slideOffset: Float) {
                    if (isPause) binding.ivPlay.visibility = View.INVISIBLE
                    else
                        binding.rdCheck.visibility = View.INVISIBLE
                }

            })

            binding.ivSkip.setOnClickListener {
                onItemClickAdapter.skip(item, layoutPosition)
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

            binding.ivPlay.setOnClickListener {
                onItemClickAdapter.onResume(layoutPosition, item)
                binding.ivPlay.visibility = View.GONE
                binding.rdCheck.visibility = View.VISIBLE

                notifyItemChanged(layoutPosition)
            }


        }


    }
    fun TextView.showStrikeThrough(show: Boolean) {
        paintFlags =
            if (show) paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            else paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
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
        if (position < currentList.size) {
            binderHelper.bind(holder.binding.swipeLayout, getItem(position).name)
            holder.bind(getItem(position), onItemClickAdapter)
        } else {
            holder.binding.root.visibility = View.GONE
        }
    }
}