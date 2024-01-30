package com.cscmobi.habittrackingandroid.utils

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
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

}