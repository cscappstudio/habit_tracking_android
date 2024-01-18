package com.cscmobi.habittrackingandroid.base

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.cscmobi.habittrackingandroid.BR
class BaseBindingAdapter<T>(
    @field:LayoutRes private val resId: Int,
    private val inflater: LayoutInflater,
     diffUtil: DiffUtil.ItemCallback<T>
) :
    ListAdapter<T, BaseBindingAdapter.BaseBindingHolder>(diffUtil) {
    private var listener: BaseBindingListener? = null
    private var param: ViewGroup.LayoutParams? = null

    fun setData(data: ArrayList<T>) {
        this.submitList(data)
        notifyDataSetChanged()
    }

    fun setLayoutParam(param: ViewGroup.LayoutParams?) {
        this.param = param
    }

    fun setListener(listener: BaseBindingListener?) {
        this.listener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseBindingHolder {
        val binding = DataBindingUtil.inflate<ViewDataBinding>(
            inflater, resId, parent, false
        )
        if (this.param != null) binding.root.layoutParams = this.param

        return BaseBindingHolder(binding)
    }



    override fun onBindViewHolder(holder: BaseBindingHolder, position: Int) {

        val t =  getItem(position)

        holder.binding.setVariable(BR.item, t)
        holder.binding.setVariable(BR.position, position)
        if (listener != null) {
            holder.binding.setVariable(BR.listener, listener)
        }
        holder.binding.executePendingBindings()
    }

    class BaseBindingHolder(val binding: ViewDataBinding) : RecyclerView.ViewHolder(
        binding.root
    )

    interface BaseBindingListener

}