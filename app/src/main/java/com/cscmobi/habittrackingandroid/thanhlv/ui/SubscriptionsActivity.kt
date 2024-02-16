package com.cscmobi.habittrackingandroid.thanhlv.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Handler
import android.os.Looper
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.cscmobi.habittrackingandroid.R
import com.cscmobi.habittrackingandroid.databinding.ActivityOnboardBinding
import com.cscmobi.habittrackingandroid.databinding.ActivitySubscritptionsBinding
import com.cscmobi.habittrackingandroid.thanhlv.adapter.SlideSubsAdapter
import com.cscmobi.habittrackingandroid.thanhlv.model.OnBoardModel
import com.google.android.material.tabs.TabLayoutMediator
import com.thanhlv.fw.helper.MyUtils.Companion.rippleEffect
import java.util.*

class SubscriptionsActivity : BaseActivity2() {

    private lateinit var binding: ActivitySubscritptionsBinding

    override fun setupScreen() {
        binding = ActivitySubscritptionsBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun initView() {

        binding.btnNext.rippleEffect()
        binding.vpOnboard.clipToPadding = false
        binding.vpOnboard.clipChildren = false
        binding.vpOnboard.isUserInputEnabled = false
        binding.vpOnboard.getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER

        val mAdapter = SlideSubsAdapter(this, getListPhoto())
        binding.vpOnboard.adapter = mAdapter
        TabLayoutMediator(binding.indicator, binding.vpOnboard) { _, _ -> }.attach()
    }

    private fun getListPhoto(): ArrayList<OnBoardModel> {
        val list = ArrayList<OnBoardModel>()
        list.add(
            OnBoardModel(R.drawable.img_onboard_1, "Unlimited habits", "free user can only have 3 habits/day. Upgrade to Premium for no limit, just habit")
        )
        list.add(
            OnBoardModel(R.drawable.img_onboard_2, "Advanced statistics", "Unlock more graph for weekly, monthly and yearly performance")
        )
        list.add(
            OnBoardModel(R.drawable.img_onboard_3, "Custom challenges", "Create your own challenge and tailor habits to fit your life")
        )
        list.add(
            OnBoardModel(R.drawable.img_onboard_4, "No ads", "Zero distractions, 100% focus! Enjoy an ad-free experience with Premium.")
        )
        return list
    }

    private val timer = Timer() // This will create a new Thread
    var ii = 0
    override fun controllerView() {

        val handler = Handler(Looper.getMainLooper())
        val runnable = Runnable {
            if (ii > 3) {
                ii = -1
            }
            binding.vpOnboard.setCurrentItem(ii++, true)
        }


        timer.schedule(object : TimerTask() {
            // task to be scheduled
            override fun run() {
                handler.post(runnable)
            }
        }, 100, 1200)

        binding.btnNext.setOnClickListener {
            startActivity(Intent(this, QuestionFOActivity::class.java))
            finish()
        }
        binding.vpOnboard.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            @SuppressLint("UseCompatLoadingForDrawables")
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                ii = position
            }
        })
    }

    override fun loadData() {
    }

//    @SuppressLint("MissingSuperCall")
//    @Deprecated("Deprecated in Java")
//    override fun onBackPressed() {
//    }

}