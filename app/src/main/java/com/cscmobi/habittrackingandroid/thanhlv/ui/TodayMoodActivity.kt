package com.cscmobi.habittrackingandroid.thanhlv.ui

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
import com.cscmobi.habittrackingandroid.thanhlv.database.AppDatabase
import com.cscmobi.habittrackingandroid.thanhlv.model.FeelingTagModel
import com.cscmobi.habittrackingandroid.thanhlv.model.Mood
import com.thanhlv.fw.helper.MyUtils
import kotlinx.coroutines.runBlocking
import java.util.*
import kotlin.collections.ArrayList


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
                createMoodSuccess()
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

    private fun createMoodSuccess() {
        val listDescribe = ArrayList<String>()
        mListDescribe.forEach { if (it.selected) listDescribe.add(it.feeling) }
        val listBecause = ArrayList<String>()
        mListBecause.forEach { if (it.selected) listBecause.add(it.feeling) }
        val newMood = Mood(
            Date(),
            currentMood,
            listDescribe.toList(),
            listBecause.toList(),
            binding.edtNote.text.toString()
        )
        runBlocking {
            AppDatabase.getInstance(applicationContext).dao().insertMood(newMood)
            println("thanhlv  AppDatabase.getInstance(applicationContext).dao().insertMood(newMood)")
        }

    }

    override fun onBackPressed() {
        if (currentStep == 0) super.onBackPressed()
        else {
            if (currentStep == 1 || currentStep == 2) {
                binding.tvQuestion.text = "How are you feeling today?"
                binding.showMoodStep2.visibility = View.GONE
                binding.showMoodStep1.visibility = View.GONE
                binding.btnNext.isEnabled = false
                binding.btnNext.backgroundTintList =
                    ColorStateList.valueOf(Color.parseColor("#B5B5B5"))
                currentStep = 0
            }
            if (currentStep == 3) {
                mListDescribe.clear()
                mListDescribe = mutableListOf<FeelingTagModel>()
                mListDescribe = getDataDescribe(currentMood)
                adapter?.updateData(mListDescribe)
                binding.bgNote.visibility = View.GONE
                currentStep = 2
            }
        }
    }

    private fun gotoMoodStep3() {
        currentStep = 3
        mListBecause.clear()
        mListBecause = mutableListOf<FeelingTagModel>()
        mListBecause = getDataBecause()
        adapter?.updateData(mListBecause)
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
    private var currentMood = 0
    private fun gotoMoodStep1(mood: Int) {
        currentStep = 1
        currentMood = mood
        Handler(Looper.getMainLooper()).postDelayed({
            binding.showMoodStep1.visibility = View.VISIBLE
            binding.imgMoodLarge.startAnimation(AnimationUtils.loadAnimation(this, R.anim.pulse2))
            binding.btnNext.isEnabled = true
            binding.btnNext.backgroundTintList =
                ColorStateList.valueOf(Color.parseColor("#54BA8F"))
        }, 500)
        when (mood) {
            1 -> {
                binding.tvMood.text = "Great"
                binding.tvQuestion.text = "Are you feeling great today?"
                binding.imgMoodLarge.setImageResource(R.drawable.ic_mood_great)
            }
            2 -> {
                binding.tvMood.text = "Good"
                binding.tvQuestion.text = "Are you feeling good today?"
                binding.imgMoodLarge.setImageResource(R.drawable.ic_mood_good)
            }
            3 -> {
                binding.tvMood.text = "Neutral"
                binding.tvQuestion.text = "Are you feeling neutral today?"
                binding.imgMoodLarge.setImageResource(R.drawable.ic_mood_neutral)
            }
            4 -> {
                binding.tvMood.text = "Not great"
                binding.tvQuestion.text = "Are you feeling not great today?"
                binding.imgMoodLarge.setImageResource(R.drawable.ic_mood_not_great)
            }
            5 -> {
                binding.tvMood.text = "Bad"
                binding.tvQuestion.text = "Are you feeling bad today?"
                binding.imgMoodLarge.setImageResource(R.drawable.ic_mood_bad)
            }
        }
        recyclerView(mood)
    }

    private var adapter: FeelingTagAdapter? = null
    private fun recyclerView(mood: Int) {
        mListDescribe.clear()
        mListDescribe = mutableListOf<FeelingTagModel>()
        mListDescribe = getDataDescribe(mood)
        adapter = FeelingTagAdapter(this)
        adapter?.updateData(mListDescribe)
        adapter?.setCallBack(object : FeelingTagAdapter.FeelingTagCallback {
            override fun onClickItem(pos: Int) {

                var sum = 0
                mListDescribe.forEach {
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


    private var mListBecause = mutableListOf<FeelingTagModel>()
    private var mListDescribe = mutableListOf<FeelingTagModel>()
    private fun getDataDescribe(mood: Int): MutableList<FeelingTagModel> {

        val list = mutableListOf<FeelingTagModel>()
        when (mood) {
            1 -> {
                list.add(FeelingTagModel("Confident", false))
                list.add(FeelingTagModel("Energetic", false))
                list.add(FeelingTagModel("Excited", false))
                list.add(FeelingTagModel("Grateful", false))
                list.add(FeelingTagModel("Hopeful", false))
                list.add(FeelingTagModel("Joyful", false))
                list.add(FeelingTagModel("Loved", false))
                list.add(FeelingTagModel("Optimistic", false))
                list.add(FeelingTagModel("Proud", false))
                list.add(FeelingTagModel("Satisfied", false))
            }

            2 -> {
                list.add(FeelingTagModel("Comfortable", false))
                list.add(FeelingTagModel("Confident", false))
                list.add(FeelingTagModel("Energetic", false))
                list.add(FeelingTagModel("Excited", false))
                list.add(FeelingTagModel("Hopeful", false))
                list.add(FeelingTagModel("Loved", false))
                list.add(FeelingTagModel("Optimistic", false))
                list.add(FeelingTagModel("Proud", false))
                list.add(FeelingTagModel("Relaxed", false))
                list.add(FeelingTagModel("Satisfied", false))
            }

            3 -> {
                list.add(FeelingTagModel("Bored", false))
                list.add(FeelingTagModel("Busy", false))
                list.add(FeelingTagModel("Calm", false))
                list.add(FeelingTagModel("Confused", false))
                list.add(FeelingTagModel("Fine", false))
                list.add(FeelingTagModel("Nervous", false))
                list.add(FeelingTagModel("Nonchalant", false))
                list.add(FeelingTagModel("Positive", false))
                list.add(FeelingTagModel("Relaxed", false))
            }

            4 -> {
                list.add(FeelingTagModel("Anxious", false))
                list.add(FeelingTagModel("Disappointed", false))
                list.add(FeelingTagModel("Dissatisfied", false))
                list.add(FeelingTagModel("Distracted", false))
                list.add(FeelingTagModel("Gloomy", false))
                list.add(FeelingTagModel("Nervous", false))
                list.add(FeelingTagModel("Sad", false))
                list.add(FeelingTagModel("Stressed", false))
                list.add(FeelingTagModel("Tired", false))
                list.add(FeelingTagModel("Worried", false))
            }

            5 -> {
                list.add(FeelingTagModel("Angry", false))
                list.add(FeelingTagModel("Depressed", false))
                list.add(FeelingTagModel("Frustrated", false))
                list.add(FeelingTagModel("Hopeless", false))
                list.add(FeelingTagModel("Hurt", false))
                list.add(FeelingTagModel("Lonely", false))
                list.add(FeelingTagModel("Miserable", false))
                list.add(FeelingTagModel("Overwhelmed", false))
                list.add(FeelingTagModel("Sad", false))
                list.add(FeelingTagModel("Worried", false))
            }
        }

        return list
    }

    private fun getDataBecause(): MutableList<FeelingTagModel> {
        val list = mutableListOf<FeelingTagModel>()
        list.add(FeelingTagModel("work", false))
        list.add(FeelingTagModel("school", false))
        list.add(FeelingTagModel("family", false))
        list.add(FeelingTagModel("friend", false))
        list.add(FeelingTagModel("pet", false))
        list.add(FeelingTagModel("Fitness", false))
        list.add(FeelingTagModel("health", false))
        list.add(FeelingTagModel("Finances", false))
        list.add(FeelingTagModel("Hobbies", false))
        list.add(FeelingTagModel("Sleep", false))
        list.add(FeelingTagModel("Achievements", false))
        list.add(FeelingTagModel("Weather", false))
        list.add(FeelingTagModel("Food", false))
        list.add(FeelingTagModel("News", false))
        list.add(FeelingTagModel("others", false))
        return list
    }


}