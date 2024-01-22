package com.cscmobi.habittrackingandroid.thanhlv.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Handler
import android.os.Looper
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.cscmobi.habittrackingandroid.R
import com.cscmobi.habittrackingandroid.databinding.ActivityOnboardBinding
import com.cscmobi.habittrackingandroid.databinding.ActivityQuestionFoBinding
import com.cscmobi.habittrackingandroid.presentation.ui.activity.MainActivity
import com.cscmobi.habittrackingandroid.thanhlv.adapter.OnBoardAdapter
import com.cscmobi.habittrackingandroid.thanhlv.model.OnBoardModel
import com.thanhlv.fw.helper.MyUtils.Companion.rippleEffect
import java.util.*

class QuestionFOActivity : BaseActivity2() {

    private lateinit var binding: ActivityQuestionFoBinding

    override fun setupScreen() {
        binding = ActivityQuestionFoBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun initView() {
        binding.btnAnswer1.rippleEffect()
        binding.btnAnswer2.rippleEffect()
        binding.btnAnswer3.rippleEffect()
        binding.btnAnswer4.rippleEffect()
        binding.btnAnswer5.rippleEffect()
    }

    override fun controllerView() {
        binding.btnAnswer1.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
        binding.btnAnswer2.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
        binding.btnAnswer3.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
        binding.btnAnswer4.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
        binding.btnAnswer5.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

    }

    override fun loadData() {
    }

    @SuppressLint("MissingSuperCall")
    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
    }

}