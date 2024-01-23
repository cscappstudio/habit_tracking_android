package com.cscmobi.habittrackingandroid.presentation.ui


import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.cscmobi.habittrackingandroid.R
import com.google.android.material.imageview.ShapeableImageView

@BindingAdapter("app:setbackgroundColorShapeIV")
fun setBackgroundColorShapeIV(iv: ShapeableImageView, res: Int) {
    iv.setBackgroundColor(ContextCompat.getColor(iv.context,res))
}


@BindingAdapter("app:setResDrawalbe")
fun setResDrawalbe(iv: AppCompatImageView, res: Int) {
    iv.setImageResource(res)
}

@BindingAdapter("app:setDrawableString")
fun setDrawableString(iv: AppCompatImageView, path: String) {
    val iconResourceId = iv.context.resources.getIdentifier(path, "drawable", iv.context.packageName)

    iv.setImageResource(iconResourceId)
}


