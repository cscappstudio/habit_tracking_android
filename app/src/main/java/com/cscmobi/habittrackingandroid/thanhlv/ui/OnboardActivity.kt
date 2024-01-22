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
import com.cscmobi.habittrackingandroid.presentation.ui.activity.MainActivity
import com.cscmobi.habittrackingandroid.thanhlv.adapter.OnBoardAdapter
import com.cscmobi.habittrackingandroid.thanhlv.model.OnBoardModel
import com.thanhlv.fw.helper.MyUtils.Companion.rippleEffect
import java.util.*

class OnboardActivity : BaseActivity2() {

    private lateinit var binding: ActivityOnboardBinding

    override fun setupScreen() {
        binding = ActivityOnboardBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun initView() {

        binding.btnNext.rippleEffect()
        binding.vpOnboard.clipToPadding = false
        binding.vpOnboard.clipChildren = false
        binding.vpOnboard.isUserInputEnabled = false
        binding.vpOnboard.getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER

        val photoAdapter = OnBoardAdapter(this, getListPhoto())
        binding.vpOnboard.adapter = photoAdapter
    }

    private fun getListPhoto(): ArrayList<OnBoardModel> {
        val list = ArrayList<OnBoardModel>()
        list.add(
            OnBoardModel(R.drawable.img_onboard_1, "Say hi to your new", "Habit Planner")
        )
        list.add(
            OnBoardModel(R.drawable.img_onboard_2, "Say hi to your new", "Todo list")
        )
        list.add(
            OnBoardModel(R.drawable.img_onboard_3, "Say hi to your new", "Calendar")
        )
        list.add(
            OnBoardModel(R.drawable.img_onboard_4, "Say hi to your new", "Journal")
        )
        list.add(
            OnBoardModel(R.drawable.img_onboard_1, "Say hi to your new", "Habit Planner")
        )
        list.add(
            OnBoardModel(R.drawable.img_onboard_2, "Say hi to your new", "Todo list")
        )
        list.add(
            OnBoardModel(R.drawable.img_onboard_3, "Say hi to your new", "Calendar")
        )
        list.add(
            OnBoardModel(R.drawable.img_onboard_4, "Say hi to your new", "Journal")
        )
        list.add(
            OnBoardModel(R.drawable.img_onboard_1, "Say hi to your new", "Habit Planner")
        )
        list.add(
            OnBoardModel(R.drawable.img_onboard_2, "Say hi to your new", "Todo list")
        )
        list.add(
            OnBoardModel(R.drawable.img_onboard_3, "Say hi to your new", "Calendar")
        )
        list.add(
            OnBoardModel(R.drawable.img_onboard_4, "Say hi to your new", "Journal")
        )
        return list
    }

    private val timer = Timer() // This will create a new Thread
    var ii = 0
    override fun controllerView() {

        val handler = Handler(Looper.getMainLooper())
        val runnable = Runnable {
            if (ii == 11) {
                ii = 0
            }
            if (ii > 2) {
                binding.btnNext.isEnabled = true
                binding.btnNext.backgroundTintList = ColorStateList.valueOf(
                    Color.parseColor("#01100C")
                )
            }
            binding.vpOnboard.setCurrentItem(ii++, true)
        }


        timer.schedule(object : TimerTask() {
            // task to be scheduled
            override fun run() {
                handler.post(runnable)
            }
        }, 100, 1600)

        binding.btnNext.setOnClickListener {
            startActivity(Intent(this, QuestionFOActivity::class.java))
            finish()
        }
        binding.vpOnboard.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            @SuppressLint("UseCompatLoadingForDrawables")
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                ii = position
                if (position % 4 == 0) binding.root.background =
                    getDrawable(R.color.bg_color_onboard_1)
                if (position % 4 == 1) binding.root.background =
                    getDrawable(R.color.bg_color_onboard_2)
                if (position % 4 == 2) binding.root.background =
                    getDrawable(R.color.bg_color_onboard_3)
                if (position % 4 == 3) binding.root.background =
                    getDrawable(R.color.bg_color_onboard_4)
            }
        })
    }

    override fun loadData() {
    }

    @SuppressLint("MissingSuperCall")
    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
    }

}