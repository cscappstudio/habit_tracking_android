package com.cscmobi.habittrackingandroid.thanhlv.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.cscmobi.habittrackingandroid.databinding.ItemOnboardLayoutBinding
import com.cscmobi.habittrackingandroid.thanhlv.model.OnBoardModel


class OnBoardAdapter(private val context: Context, list: ArrayList<OnBoardModel>) :
    RecyclerView.Adapter<OnBoardAdapter.ViewHolder>() {
    private val list: ArrayList<OnBoardModel>

    init {
        this.list = list
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemOnboardLayoutBinding = ItemOnboardLayoutBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, @SuppressLint("RecyclerView") position: Int) {
        val slideItem: OnBoardModel = list[position]
        Glide.with(context)
            .load(slideItem.resId)
            .into(holder.binding.imgPhoto)
//        holder.binding.tvTitle.text = slideItem.text1
        holder.binding.tvDes.text = slideItem.text2
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class ViewHolder(binding: ItemOnboardLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val binding: ItemOnboardLayoutBinding

        init {
            this.binding = binding
        }
    }
}