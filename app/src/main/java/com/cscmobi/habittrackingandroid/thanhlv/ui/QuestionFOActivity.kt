package com.cscmobi.habittrackingandroid.thanhlv.ui

import android.annotation.SuppressLint
import android.content.Intent
import com.cscmobi.habittrackingandroid.databinding.ActivityQuestionFoBinding
import com.cscmobi.habittrackingandroid.thanhlv.data.ChallengeData.Companion.mChallengeDefaultList
import com.cscmobi.habittrackingandroid.thanhlv.database.AppDatabase
import com.cscmobi.habittrackingandroid.thanhlv.model.Challenge
import com.thanhlv.fw.helper.MyUtils.Companion.rippleEffect
import com.thanhlv.fw.helper.RunUtils
import kotlinx.coroutines.runBlocking
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
            startActivity(Intent(this, QuestionFO2Activity::class.java))
            finish()
        }
        binding.btnAnswer2.setOnClickListener {
            startActivity(Intent(this, QuestionFO2Activity::class.java))
            finish()
        }
        binding.btnAnswer3.setOnClickListener {
            startActivity(Intent(this, QuestionFO2Activity::class.java))
            finish()
        }
        binding.btnAnswer4.setOnClickListener {
            startActivity(Intent(this, QuestionFO2Activity::class.java))
            finish()
        }
        binding.btnAnswer5.setOnClickListener {
            startActivity(Intent(this, QuestionFO2Activity::class.java))
            finish()
        }

    }

    override fun loadData() {
        RunUtils.runInBackground {
            runBlocking {

                if (AppDatabase.getInstance(applicationContext).dao().getAllChallenge()
                        .isEmpty()
                ) {
                    generationDataDefault()
                }
//                    Handler().postDelayed({
                runBlocking {
                    ChallengeFragment.allChallenges.postValue(
                        AppDatabase.getInstance(applicationContext).dao().getAllChallenge()
                    )
                    ChallengeFragment.myChallenges.postValue(
                        AppDatabase.getInstance(applicationContext).dao().getMyChallenge()
                    )
                }
//                    }, 500)

            }
        }

    }

    suspend fun generationDataDefault() {
        mChallengeDefaultList.forEach {
            val challenge = Challenge()
            challenge.name = it.name
            challenge.description = it.description
            challenge.image = it.image
            challenge.duration = it.duration
            challenge.cycle = it.cycle
            challenge.repeat = it.repeat
            challenge.days = it.days
            AppDatabase.getInstance(applicationContext).dao().insertChallenge(challenge)
        }

    }

    @SuppressLint("MissingSuperCall")
    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
    }

}