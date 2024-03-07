package com.cscmobi.habittrackingandroid.thanhlv.ui

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.cscmobi.habittrackingandroid.R
import com.cscmobi.habittrackingandroid.databinding.ActivityQfo2Binding
import com.cscmobi.habittrackingandroid.presentation.ui.activity.MainActivity
import java.util.*

class QuestionFO2Activity : BaseActivity2() {

    private lateinit var binding: ActivityQfo2Binding

    override fun setupScreen() {
        binding = ActivityQfo2Binding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun initView() {
    }

    override fun controllerView() {
        binding.btnNext.setOnClickListener {
            if (hasNotificationPermission()) {
                Handler(Looper.getMainLooper()).postDelayed({
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                }, 300)
            } else {
                requestNotificationPermission()
            }

        }
    }

    override fun loadData() {

    }


    @SuppressLint("MissingSuperCall")
    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        finish()
    }

}