package com.cscmobi.habittrackingandroid.utils

import android.content.Context
import android.content.Intent
import android.text.SpannableString
import android.view.Gravity
import android.view.LayoutInflater
import android.view.WindowManager
import androidx.appcompat.app.AlertDialog
import com.cscmobi.habittrackingandroid.databinding.DialogCongratulationBinding
import com.cscmobi.habittrackingandroid.databinding.DialogDeleteTaskBinding

object DialogUtils {
    fun showDeleteTaskDialog(context: Context, deleteFuture: () -> Unit, deleteAll: () -> Unit) {
        val binding = DialogDeleteTaskBinding.inflate(LayoutInflater.from(context));
        val alertDialog = AlertDialog.Builder(context)
            .setView(binding.root)
            .show()
        alertDialog.window?.setBackgroundDrawable(null)

        binding.ivClose.setOnClickListener {
            alertDialog.dismiss()
        }

        binding.btnDeleteFuture.setOnClickListener {
            deleteFuture.invoke()
            alertDialog.dismiss()
        }

        binding.btnDeleteAll.setOnClickListener {
            deleteAll.invoke()
            alertDialog.dismiss()
        }
    }

    fun showCongratulationDialog(context: Context,title: String,content1: SpannableString, content2: String) {
        val binding = DialogCongratulationBinding.inflate(LayoutInflater.from(context))
        val alertDialog = AlertDialog.Builder(context)
            .setView(binding.root)
            .show()

        val layoutParams = WindowManager.LayoutParams()
        layoutParams.copyFrom(alertDialog.window?.attributes)
        layoutParams?.gravity = Gravity.CENTER
        alertDialog.window?.attributes = layoutParams

        alertDialog.window?.setBackgroundDrawable(null)

        binding.txtTitle.text = title
        binding.txtContent1.text = content1
        binding.txtContent2.text = content2

        binding.btnClaim.setOnClickListener {
            alertDialog.dismiss()
        }
    }

}