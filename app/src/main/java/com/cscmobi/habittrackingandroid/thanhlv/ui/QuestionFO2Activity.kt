package com.cscmobi.habittrackingandroid.thanhlv.ui

import android.annotation.SuppressLint
import android.content.Intent
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
            startActivity(Intent(this, MainActivity::class.java))
            finish()
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