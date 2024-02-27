package com.cscmobi.habittrackingandroid.presentation.ui.adapter

import android.content.Context
import android.content.res.ColorStateList
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.cscmobi.habittrackingandroid.R
import com.cscmobi.habittrackingandroid.data.model.HabitCollection
import com.cscmobi.habittrackingandroid.databinding.ItemCollectionBinding
import com.cscmobi.habittrackingandroid.databinding.ItemCreateCollectionBinding
import com.cscmobi.habittrackingandroid.presentation.OnItemClickPositionListener
import com.cscmobi.habittrackingandroid.utils.setDrawableString
import java.util.Locale

class CollectionAdapter(
    private val context: Context,
    private val items: MutableList<HabitCollection>,
    private val onItemClickAdapter: OnItemClickPositionListener
) :
    ListAdapter<HabitCollection, RecyclerView.ViewHolder>(CollectionAdapter.DIFF_CALLBACK()),
    Filterable {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            CollectionType.CREATE.ordinal -> ViewHolderCreateCollection.onBind(parent)
            else -> ViewHolderCollection.onBind(parent)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder.itemViewType) {
            CollectionType.CREATE.ordinal -> (holder as ViewHolderCreateCollection).bind(
                onItemClickAdapter
            )

            else -> (holder as ViewHolderCollection).bind(getItem(position), onItemClickAdapter)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (position) {
            0 -> CollectionType.CREATE.ordinal
            else -> CollectionType.ITEM.ordinal
        }
    }

    class ViewHolderCreateCollection constructor(val binding: ItemCreateCollectionBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(onItemClickAdapter: OnItemClickPositionListener) {
            binding.root.setOnClickListener {
                onItemClickAdapter.onItemClick(adapterPosition)

            }
        }

        companion object {
            fun onBind(parent: ViewGroup): ViewHolderCreateCollection {
                val view = ItemCreateCollectionBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                return ViewHolderCreateCollection(view)
            }
        }
    }

    class ViewHolderCollection constructor(val binding: ItemCollectionBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: HabitCollection, onItemClickAdapter: OnItemClickPositionListener) {

            binding.ivCollection.setDrawableString(item.image!!)

//            if (item.name.isIn) {
//                val resourceValue = binding.root.context.getString(resourceId)
//                // Now, resourceValue contains the string resource corresponding to "aaaa"
//                binding.txtCollection.text =  resourceValue
//            } else {
//                // Handle the case where the resource with the specified name is not found
//               // binding.txtCollection.text =  item.name
//                binding.txtCollection.text =  item.name
//            }
//            try {
//                val resourceValue = binding.root.context.getString(item.name.toInt())
//                binding.txtCollection.text = resourceValue
//            }catch (e: Exception) {
//                binding.txtCollection.text =  item.name
//
//            }

            binding.txtCollection.text = item.name

            binding.llBg.backgroundTintList = ColorStateList.valueOf(
                ContextCompat.getColor(
                    binding.root.context,
                    item.resColorBg ?: R.color.blue
                )
            )
            binding.root.setOnClickListener {
                onItemClickAdapter.onItemClick(adapterPosition)
            }
        }

        companion object {
            fun onBind(parent: ViewGroup): ViewHolderCollection {
                val view = ItemCollectionBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                return ViewHolderCollection(view)
            }
        }
    }

    enum class CollectionType(type: Int) {
        CREATE(0),
        ITEM(1)
    }

    companion object {
        @JvmStatic
        fun DIFF_CALLBACK(): DiffUtil.ItemCallback<HabitCollection> =
            object : DiffUtil.ItemCallback<HabitCollection>() {

                override fun areItemsTheSame(
                    oldItem: HabitCollection,
                    newItem: HabitCollection
                ): Boolean {
                    return oldItem.name == newItem.name
                }

                override fun areContentsTheSame(
                    oldItem: HabitCollection,
                    newItem: HabitCollection
                ): Boolean {
                    return (oldItem.name == newItem.name) && oldItem.image == newItem.image
                }
            }
    }

    override fun getFilter(): Filter {
        return object : Filter() {

            override fun performFiltering(constraint: CharSequence?): FilterResults {

                return FilterResults().apply {

                    values = if (constraint.isNullOrEmpty())
                        items
                    else
                        onFilter(items, constraint.toString())
                }
            }

            @Suppress("UNCHECKED_CAST")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {

                submitList(results?.values as? List<HabitCollection>)

            }
        }
    }

    fun onFilter(list: MutableList<HabitCollection>, constraint: String): List<HabitCollection> {
        val filterPattern = constraint.lowercase(Locale.getDefault())

        val filteredList = list.filter {

            it.name.lowercase().contains(filterPattern)

        }.toMutableList()

        filteredList.add(0, HabitCollection())

        return filteredList

    }
}