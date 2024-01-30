package com.cscmobi.habittrackingandroid.base

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.cscmobi.habittrackingandroid.BR
import java.util.Locale


class BaseBindingAdapter2<T>(
    @field:LayoutRes private val resId: Int,
    private val inflater: LayoutInflater,
    diffUtil: DiffUtil.ItemCallback<T>,
    private val originalData: MutableList<T>
) :
    ListAdapter<T, BaseBindingAdapter2.BaseBindingHolder2>(diffUtil), Filterable {
    private var listener: BaseBindingAdapter.BaseBindingListener? = null
    private var param: ViewGroup.LayoutParams? = null
    private var filter: Filter? = null

    fun setData(data: ArrayList<T>) {
        this.submitList(data)
        notifyDataSetChanged()
    }

    fun setLayoutParam(param: ViewGroup.LayoutParams?) {
        this.param = param
    }

    fun setListener(listener: BaseBindingAdapter.BaseBindingListener?) {
        this.listener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseBindingHolder2 {
        val binding = DataBindingUtil.inflate<ViewDataBinding>(
            inflater, resId, parent, false
        )
        if (this.param != null) binding.root.layoutParams = this.param

        return BaseBindingHolder2(binding)
    }

    override fun onBindViewHolder(holder: BaseBindingAdapter2.BaseBindingHolder2, position: Int) {
        val t =  getItem(position)

        holder.binding.setVariable(BR.item, t)
        holder.binding.setVariable(BR.position, position)
        if (listener != null) {
            holder.binding.setVariable(BR.listener, listener)
        }
        holder.binding.executePendingBindings()
    }



    class BaseBindingHolder2(val binding: ViewDataBinding) : RecyclerView.ViewHolder(
        binding.root
    )


    init {
        filter = object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val filteredList = if (constraint.isNullOrBlank()) {
                    originalData
                } else {
                    val filterPattern = constraint.toString().lowercase(Locale.getDefault())
                    originalData.filter {
                        // Implement your filtering logic here
                        // For example, check if the item contains the constraint in its toString()
                        it.toString().lowercase(Locale.getDefault()).contains(filterPattern)
                    } as ArrayList<T>
                }

                val results = FilterResults()
                results.values = filteredList
                return results
            }

            @Suppress("UNCHECKED_CAST")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                submitList(results?.values as? List<T>)
                notifyDataSetChanged()
            }
        }
    }

    override fun getFilter(): Filter? = filter


}