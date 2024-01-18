package com.cscmobi.habittrackingandroid.thanhlv.ui

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.GridLayoutManager
import com.cscmobi.habittrackingandroid.R
import com.cscmobi.habittrackingandroid.databinding.ActivityMoodTodayBinding
import com.cscmobi.habittrackingandroid.thanhlv.adapter.FeelingTagAdapter
import com.cscmobi.habittrackingandroid.thanhlv.model.FeelingTagModel
import com.thanhlv.fw.helper.MyUtils


class TodayMoodActivity : BaseActivity2() {
    private lateinit var binding: ActivityMoodTodayBinding

    override fun setupScreen() {
        binding = ActivityMoodTodayBinding.inflate(layoutInflater)
        setContentView(binding.root)
        MyUtils.configKeyboardBelowEditText(this)
    }

    override fun loadData() {
    }

    override fun initView() {
    }

    override fun controllerView() {
        binding.btnBackHeader.setOnClickListener {
            onBackPressed()
        }
        binding.btnMoodGreat.setOnClickListener {
            gotoMoodStep1(1)
        }
        binding.btnMoodGood.setOnClickListener {
            gotoMoodStep1(2)
        }
        binding.btnMoodNeutral.setOnClickListener {
            gotoMoodStep1(3)
        }
        binding.btnMoodNotGreat.setOnClickListener {
            gotoMoodStep1(4)
        }
        binding.btnMoodBad.setOnClickListener {
            gotoMoodStep1(5)
        }

        binding.btnNext.setOnClickListener {
            if (currentStep == 3) {
                finish()
            }
            if (currentStep == 2) {
                gotoMoodStep3()
            }
            if (currentStep == 1) {
                gotoMoodStep2()
            }
        }

    }

    override fun onBackPressed() {
        if (currentStep == 0) super.onBackPressed()
        else {
            if (currentStep == 1 || currentStep == 2) {
                binding.showMoodStep2.visibility = View.GONE
                binding.showMoodStep1.visibility = View.GONE
                binding.btnNext.isEnabled = false
                binding.btnNext.backgroundTintList =
                    ColorStateList.valueOf(Color.parseColor("#B5B5B5"))
                currentStep = 0
            }
            if (currentStep == 3) {
                mList.clear()
                mList = mutableListOf<FeelingTagModel>()
                mList = getData()
                adapter?.updateData(mList)
                binding.bgNote.visibility = View.GONE
                currentStep = 2
            }
        }
    }

    private fun gotoMoodStep3() {
        currentStep = 3
        mList.clear()
        mList = mutableListOf<FeelingTagModel>()
        mList = getData2()
        adapter?.updateData(mList)
        binding.bgNote.visibility = View.VISIBLE
        binding.tvQuestion.text = "What’s making you feel great?"
        binding.btnNext.isEnabled = false
        binding.btnNext.backgroundTintList =
            ColorStateList.valueOf(Color.parseColor("#B5B5B5"))
    }

    private fun gotoMoodStep2() {
        currentStep = 2
        binding.btnNext.isEnabled = false
        binding.tvQuestion.text = "Which describe your feelings?"
        binding.btnNext.backgroundTintList =
            ColorStateList.valueOf(Color.parseColor("#B5B5B5"))
        binding.showMoodStep1.visibility = View.GONE
        binding.showMoodStep2.visibility = View.VISIBLE
    }

    private var currentStep = 0
    private fun gotoMoodStep1(mood: Int) {
        currentStep = 1

        Handler(Looper.getMainLooper()).postDelayed({
            binding.showMoodStep1.visibility = View.VISIBLE
            binding.imgMood.startAnimation(AnimationUtils.loadAnimation(this, R.anim.pulse2))
            binding.btnNext.isEnabled = true
            binding.btnNext.backgroundTintList =
                ColorStateList.valueOf(Color.parseColor("#54BA8F"))
        }, 500)
        when (mood) {
            1 -> {
                binding.tvMood.text = "Great"
                binding.tvQuestion.text = "Are you feeling great today?"
            }
            2 -> {
                binding.tvMood.text = "Good"
                binding.tvQuestion.text = "Are you feeling good today?"
            }
            3 -> {
                binding.tvMood.text = "Neutral"
                binding.tvQuestion.text = "Are you feeling neutral today?"
            }
            4 -> {
                binding.tvMood.text = "Not great"
                binding.tvQuestion.text = "Are you feeling not great today?"
            }
            5 -> {
                binding.tvMood.text = "Bad"
                binding.tvQuestion.text = "Are you feeling bad today?"
            }
        }
        recyclerView()
    }

    private var adapter: FeelingTagAdapter? = null
    private fun recyclerView() {
        mList.clear()
        mList = mutableListOf<FeelingTagModel>()
        mList = getData()
        adapter = FeelingTagAdapter(this)
        adapter?.updateData(mList)
        adapter?.setCallBack(object : FeelingTagAdapter.FeelingTagCallback {
            override fun onClickItem(pos: Int) {

                var sum = 0
                mList.forEach {
                    if (it.selected) sum++
                }
                if (sum == 0) {
                    binding.btnNext.backgroundTintList =
                        ColorStateList.valueOf(Color.parseColor("#B5B5B5"))
                    binding.btnNext.isEnabled = false
                } else {
                    binding.btnNext.backgroundTintList =
                        ColorStateList.valueOf(Color.parseColor("#54BA8F"))
                    binding.btnNext.isEnabled = true
                }

            }

        })
        binding.rcFeeling.layoutManager = GridLayoutManager(this, 3)
        binding.rcFeeling.adapter = adapter

    }


    private var mList = mutableListOf<FeelingTagModel>()
    private fun getData(): MutableList<FeelingTagModel> {

        val list = mutableListOf<FeelingTagModel>()
        list.add(FeelingTagModel("abc", false))
        list.add(FeelingTagModel("abc", false))
        list.add(FeelingTagModel("abc", false))
        list.add(FeelingTagModel("abc", false))
        list.add(FeelingTagModel("abc", false))
        list.add(FeelingTagModel("abc", false))
        list.add(FeelingTagModel("abc", false))
        list.add(FeelingTagModel("abc", false))
        list.add(FeelingTagModel("abc", false))
        list.add(FeelingTagModel("abc", false))
        list.add(FeelingTagModel("abc", false))
        list.add(FeelingTagModel("abc", false))
        list.add(FeelingTagModel("abc", false))
        list.add(FeelingTagModel("abc", false))
        return list
    }
    private fun getData2(): MutableList<FeelingTagModel> {
        val list = mutableListOf<FeelingTagModel>()
        list.add(FeelingTagModel("abc", false))
        list.add(FeelingTagModel("abc", false))
        list.add(FeelingTagModel("abc", false))
        list.add(FeelingTagModel("abc", false))
        list.add(FeelingTagModel("abc", false))
        list.add(FeelingTagModel("abc", false))
        list.add(FeelingTagModel("abc", false))
        list.add(FeelingTagModel("abc", false))
        list.add(FeelingTagModel("abc", false))
        list.add(FeelingTagModel("abc", false))
        list.add(FeelingTagModel("abc", false))
        list.add(FeelingTagModel("abc", false))
        list.add(FeelingTagModel("abc", false))
        list.add(FeelingTagModel("abc", false))
        list.add(FeelingTagModel("abc", false))
        list.add(FeelingTagModel("abc", false))
        return list
    }


}