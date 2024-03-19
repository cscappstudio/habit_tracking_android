package com.cscmobi.habittrackingandroid.thanhlv.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.BitmapFactory
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cscmobi.habittrackingandroid.databinding.ItemImageChallengeBinding
import com.cscmobi.habittrackingandroid.thanhlv.model.ImageChallengeModel

class ImageChallengeAdapter(private var mContext: Context, var mList: List<ImageChallengeModel>) :
    RecyclerView.Adapter<ImageChallengeAdapter.ViewHolder>() {
    class ViewHolder(var binding: ItemImageChallengeBinding) : RecyclerView.ViewHolder(binding.root)

    private var mCallback: ImageChallengeCallback? = null
    interface ImageChallengeCallback{
        fun onClickItem(image: String)
    }

    fun setCallback(callback: ImageChallengeCallback) {
        this.mCallback = callback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemImageChallengeBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (mList.isEmpty()) return
        val item = mList[position]
        holder.binding.image.setImageBitmap(BitmapFactory.decodeStream(mContext.assets.open(item.image)))
        if (item.isSelected)
            holder.binding.rootView.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#DCF1EB"))
            else
                holder.binding.rootView.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#F5F5F5"))
        holder.binding.root.setOnClickListener {
            mList.forEach { it.isSelected = false }
            mList[position].isSelected = true
            mCallback?.onClickItem(item.image)
        }
    }

    override fun getItemCount(): Int {
        return mList.size
    }
}