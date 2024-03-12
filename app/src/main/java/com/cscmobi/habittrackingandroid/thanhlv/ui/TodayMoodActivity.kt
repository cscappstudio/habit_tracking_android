package com.cscmobi.habittrackingandroid.thanhlv.ui

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.res.ColorStateList
import android.graphics.Color
import android.net.ConnectivityManager
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.GridLayoutManager
import com.cscmobi.habittrackingandroid.R
import com.cscmobi.habittrackingandroid.databinding.ActivityMoodTodayBinding
import com.cscmobi.habittrackingandroid.thanhlv.adapter.FeelingTagAdapter
import com.cscmobi.habittrackingandroid.thanhlv.data.MoodData
import com.cscmobi.habittrackingandroid.thanhlv.data.MoodData.Companion.mDescribeList
import com.cscmobi.habittrackingandroid.thanhlv.database.AppDatabase
import com.cscmobi.habittrackingandroid.thanhlv.model.FeelingTagModel
import com.cscmobi.habittrackingandroid.thanhlv.model.Mood
import com.cscmobi.habittrackingandroid.thanhlv.ui.MoodActivity.Companion.mAllMoods
import com.thanhlv.ads.lib.AdMobUtils
import com.thanhlv.fw.constant.AppConfigs
import com.thanhlv.fw.helper.MyUtils
import com.thanhlv.fw.helper.NetworkHelper
import com.thanhlv.fw.remoteconfigs.RemoteConfigs
import com.thanhlv.fw.spf.SPF
import kotlinx.coroutines.runBlocking
import java.util.*
import kotlin.collections.ArrayList


class TodayMoodActivity : BaseActivity2() {
    private lateinit var binding: ActivityMoodTodayBinding
    private var popupLogTodayMood: PopupLogTodayMood? = null

    override fun setupScreen() {
        binding = ActivityMoodTodayBinding.inflate(layoutInflater)
        setContentView(binding.root)
        MyUtils.configKeyboardBelowEditText(this)
    }

    override fun loadData() {
        val moodData = MoodData(this)
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
            if (currentStep == 1) {
                binding.showMoodStep0.visibility = View.GONE
                popupLogTodayMood = PopupLogTodayMood().newInstance(currentMood)
                popupLogTodayMood?.setCallback2(object : PopupLogTodayMood.Callback {
                    override fun onClickBack(step: Int) {

                    }

                    override fun onClickNext(
                        listDescribe: MutableList<FeelingTagModel>,
                        listBecause: MutableList<FeelingTagModel>, note: String
                    ) {
                        val listDescribe_ = ArrayList<String>()
                        listDescribe.forEach { if (it.selected) listDescribe_.add(it.describe) }
                        val listBecause_ = ArrayList<String>()
                        listBecause.forEach { if (it.selected) listBecause_.add(it.describe) }

                        createMoodSuccess(listDescribe_, listBecause_, note)
                        finish()
                    }

                    override fun onClickClose() {
                    }

                })
                popupLogTodayMood?.show(supportFragmentManager, "")
            }
        }

    }

    private fun createMoodSuccess(
        listDescribe: ArrayList<String>,
        listBecause: ArrayList<String>,
        noteText: String
    ) {
        runBlocking {
            val existTodayMood = existToday()
            if (existTodayMood == null) {
                val newMood = Mood(
                    Date().time,
                    currentMood,
                    listDescribe.toList(),
                    listBecause.toList(),
                    noteText
                )
                AppDatabase.getInstance(applicationContext).dao().insertMood(newMood)
            } else {
                existTodayMood.state = currentMood
                existTodayMood.describe = listDescribe.toList()
                existTodayMood.becauseOf = listBecause.toList()
                existTodayMood.note = noteText
                AppDatabase.getInstance(applicationContext).dao().updateMood(existTodayMood)
            }
        }

    }

    private fun existToday(): Mood? {
        mAllMoods.forEach {
            val today = Calendar.getInstance()
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = it.date
            if (calendar[Calendar.DAY_OF_MONTH] == today[Calendar.DAY_OF_MONTH]
                && calendar[Calendar.MONTH] == today[Calendar.MONTH]
                && calendar[Calendar.YEAR] == today[Calendar.YEAR]
            ) return it
        }
        return null
    }

    override fun onBackPressed() {
        if (currentStep == 0) super.onBackPressed()
        else {
            if (currentStep == 1) {
                binding.tvQuestion.text = getString(R.string.how_are_you_feeling_today)
                binding.showMoodStep1.visibility = View.GONE
                binding.showMoodStep0.visibility = View.VISIBLE
                binding.btnNext.isEnabled = false
                binding.btnNext.backgroundTintList =
                    ColorStateList.valueOf(Color.parseColor("#B5B5B5"))
                currentStep = 0
            }
        }
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
        }, 200)
        when (mood) {
            1 -> {
                binding.tvMood.text = getString(R.string.great)
                binding.tvQuestion.text = getString(R.string.are_you_feeling_great_today)
                binding.imgMoodLarge.setImageResource(R.drawable.ic_mood_great)
            }

            2 -> {
                binding.tvMood.text = getString(R.string.good)
                binding.tvQuestion.text = getString(R.string.are_you_feeling_good_today)
                binding.imgMoodLarge.setImageResource(R.drawable.ic_mood_good)
            }

            3 -> {
                binding.tvMood.text = getString(R.string.neutral)
                binding.tvQuestion.text = getString(R.string.are_you_feeling_neutral_today)
                binding.imgMoodLarge.setImageResource(R.drawable.ic_mood_neutral)
            }

            4 -> {
                binding.tvMood.text = getString(R.string.not_great)
                binding.tvQuestion.text = getString(R.string.are_you_feeling_not_great_today)
                binding.imgMoodLarge.setImageResource(R.drawable.ic_mood_not_great)
            }

            5 -> {
                binding.tvMood.text = getString(R.string.bad)
                binding.tvQuestion.text = getString(R.string.are_you_feeling_bad_today)
                binding.imgMoodLarge.setImageResource(R.drawable.ic_mood_bad)
            }
        }
    }

    private val mIntentFilter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
    private val mReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            loadBanner()
        }
    }

    private fun loadBanner() {
        if (NetworkHelper.isNetworkAvailable(this@TodayMoodActivity)
            && !SPF.isProApp(this@TodayMoodActivity)
            && RemoteConfigs.instance.getConfigValue(AppConfigs.KEY_AD_BANNER_LOG_MOOD).asBoolean()
        ) {
            binding.bannerView.visibility = View.VISIBLE
            AdMobUtils.createBanner(
                this@TodayMoodActivity,
                getString(R.string.admob_banner_id),
                AdMobUtils.BANNER_NORMAL,
                binding.bannerView,
                null
            )
        } else {
            binding.bannerView.visibility = View.GONE
        }
    }

    override fun onStart() {
        super.onStart()
        registerReceiver(mReceiver, mIntentFilter)
    }

    override fun onStop() {
        super.onStop()
        unregisterReceiver(mReceiver)
    }

}