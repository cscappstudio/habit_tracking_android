package com.cscmobi.habittrackingandroid.presentation.ui


import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.cscmobi.habittrackingandroid.R
import com.cscmobi.habittrackingandroid.utils.Utils
import com.cscmobi.habittrackingandroid.utils.setDrawableString
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
    if (Utils.isAssetImage(iv.context, path)) {
        val drawable = Utils.loadImageFromAssets(iv.context,path)
        iv.setImageDrawable(drawable)
    } else {
        val iconResourceId =
            iv.context.resources.getIdentifier(path, "drawable", iv.context.packageName)
        iv.setImageResource(iconResourceId)
    }
}

@BindingAdapter("app:setDrawableAsset")
fun setDrawableAsset(iv: AppCompatImageView, path: String) {
    val drawable = Utils.loadImageFromAssets(iv.context,path)
    iv.setImageDrawable(drawable)
}

@BindingAdapter("custom:setText")
fun setText(tv: TextView,s: String) {
    try {
        val resourceValue = tv.context.getString(s.toInt())
        tv.setText(resourceValue)
    }catch (e: Exception) {
        tv.setText(s)

    }
}


