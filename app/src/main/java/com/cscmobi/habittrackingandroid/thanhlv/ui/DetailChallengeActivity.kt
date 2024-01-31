package com.cscmobi.habittrackingandroid.thanhlv.ui

import android.annotation.SuppressLint
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.cscmobi.habittrackingandroid.R
import com.cscmobi.habittrackingandroid.data.model.ChallengeJoinedHistory
import com.cscmobi.habittrackingandroid.databinding.ActivityDetailChallengeBinding
import com.cscmobi.habittrackingandroid.thanhlv.adapter.DetailChallengeAdapter
import com.cscmobi.habittrackingandroid.thanhlv.database.AppDatabase
import com.cscmobi.habittrackingandroid.thanhlv.model.Challenge
import com.google.gson.Gson
import com.thanhlv.fw.helper.MyUtils.Companion.configKeyboardBelowEditText
import com.thanhlv.fw.helper.MyUtils.Companion.rippleEffect
import kotlinx.coroutines.runBlocking
import java.util.*


class DetailChallengeActivity : BaseActivity2() {
    private lateinit var binding: ActivityDetailChallengeBinding
    private var mChallenge: Challenge? = null

    override fun setupScreen() {
        binding = ActivityDetailChallengeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        configKeyboardBelowEditText(this)
    }

    override fun loadData() {
//        mChallenge = intent.getSerializableExtra("data") as Challenge?
        mChallenge =
            Gson().fromJson<Challenge>(intent.getStringExtra("data"), Challenge::class.java)
        println("thanhlv override fun loadData() {  ===== " + mChallenge?.id)
    }

    @SuppressLint("SetTextI18n")
    override fun initView() {
        binding.btnStartChallenge.rippleEffect()
        recyclerView()
        if (mChallenge != null) {
            binding.tvTitleChallenge.text = mChallenge?.name
            binding.tvDays.text = mChallenge?.duration.toString() + " days"
            if (!mChallenge?.image.isNullOrEmpty())
                binding.imgChallenge.setImageResource(mChallenge?.image!!.toInt())
            else binding.imgChallenge.setImageResource(R.drawable.img_target)

            if (!mChallenge?.joinedHistory.isNullOrEmpty()) {
                binding.btnStartChallenge.visibility = View.GONE
                binding.btnOptionTop.visibility = View.VISIBLE
            }
            else {
                binding.btnStartChallenge.visibility = View.VISIBLE
                binding.btnOptionTop.visibility = View.GONE
            }

        }
    }

    override fun controllerView() {
        binding.btnBackHeader.setOnClickListener {
            onBackPressed()
        }

        binding.btnStartChallenge.setOnClickListener {
            joinChallenge(mChallenge)
        }
    }

    private fun joinChallenge(mChallenge: Challenge?) {
        if (mChallenge == null) return

        runBlocking {
            val joined = ChallengeJoinedHistory(Date().time)
            mChallenge.joinedHistory = listOf(joined)
            AppDatabase.getInstance(applicationContext).dao().updateChallenge(mChallenge)
        }
    }

    private fun recyclerView() {
        binding.rcTasks.adapter = DetailChallengeAdapter(this)
        binding.rcTasks.layoutManager = LinearLayoutManager(this)

    }

}