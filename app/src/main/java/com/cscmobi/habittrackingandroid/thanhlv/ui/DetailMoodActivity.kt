package com.cscmobi.habittrackingandroid.thanhlv.ui

import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.cscmobi.habittrackingandroid.R
import com.cscmobi.habittrackingandroid.databinding.ActivityDetailMoodBinding
import com.cscmobi.habittrackingandroid.thanhlv.adapter.FeelingTag2Adapter
import com.cscmobi.habittrackingandroid.thanhlv.adapter.FeelingTagAdapter
import com.cscmobi.habittrackingandroid.thanhlv.adapter.MoodRecordAdapter
import com.cscmobi.habittrackingandroid.thanhlv.model.Challenge
import com.cscmobi.habittrackingandroid.thanhlv.model.Mood
import com.google.gson.Gson
import com.thanhlv.fw.helper.MyUtils.Companion.configKeyboardBelowEditText


class DetailMoodActivity : BaseActivity2() {
    private lateinit var binding: ActivityDetailMoodBinding
    private var mMood: Mood? = null

    override fun setupScreen() {
        binding = ActivityDetailMoodBinding.inflate(layoutInflater)
        setContentView(binding.root)
        configKeyboardBelowEditText(this)
    }

    override fun loadData() {
        mMood =
            Gson().fromJson<Mood>(intent.getStringExtra("data_mood"), Mood::class.java)
        println("thanhlv override fun loadData() {  ===== " + mMood?.date)
    }

    override fun initView() {
        recyclerView()
        if (mMood?.note.isNullOrEmpty()) {
            binding.showNote.visibility = View.GONE
        } else {
            binding.showNote.visibility = View.VISIBLE
            binding.textNote.text = mMood?.note
        }

        when (mMood?.state) {
            2 -> {
                binding.tvMood.text = "To day was GOOD"
                binding.icMood.setImageResource(R.drawable.ic_mood_good)
            }
            3 -> {
                binding.tvMood.text = "To day was NEUTRAL"
                binding.icMood.setImageResource(R.drawable.ic_mood_neutral)
            }
            4 -> {
                binding.tvMood.text = "To day was NOT GREAT"
                binding.icMood.setImageResource(R.drawable.ic_mood_not_great)
            }
            5 -> {
                binding.tvMood.text = "To day was BAD"
                binding.icMood.setImageResource(R.drawable.ic_mood_bad)
            }
            else -> {
                binding.tvMood.text = "To day was GREAT"
                binding.icMood.setImageResource(R.drawable.ic_mood_great)
            }
        }

    }

    override fun controllerView() {
        binding.btnBackHeader.setOnClickListener {
            onBackPressed()
        }
    }

    private fun recyclerView() {
        if (!mMood?.describe.isNullOrEmpty()) {
            binding.rcFeeling.adapter = FeelingTag2Adapter(this, ArrayList(mMood?.describe!!))
            binding.rcFeeling.layoutManager = GridLayoutManager(this, 3)
        }
        if (!mMood?.becauseOf.isNullOrEmpty()) {
            binding.rcBecause.adapter = FeelingTag2Adapter(this, ArrayList(mMood?.becauseOf!!))
            binding.rcBecause.layoutManager = GridLayoutManager(this, 3)
        }
    }

}