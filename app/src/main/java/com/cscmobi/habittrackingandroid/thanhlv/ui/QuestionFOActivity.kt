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
import com.cscmobi.habittrackingandroid.data.model.ChallengeJoinedHistory
import com.cscmobi.habittrackingandroid.data.model.Tasks
import com.cscmobi.habittrackingandroid.databinding.ActivityOnboardBinding
import com.cscmobi.habittrackingandroid.databinding.ActivityQuestionFoBinding
import com.cscmobi.habittrackingandroid.presentation.ui.activity.MainActivity
import com.cscmobi.habittrackingandroid.thanhlv.adapter.OnBoardAdapter
import com.cscmobi.habittrackingandroid.thanhlv.database.AppDatabase
import com.cscmobi.habittrackingandroid.thanhlv.model.Challenge
import com.cscmobi.habittrackingandroid.thanhlv.model.OnBoardModel
import com.cscmobi.habittrackingandroid.thanhlv.model.Task
import com.thanhlv.fw.helper.MyUtils.Companion.rippleEffect
import com.thanhlv.fw.helper.RunUtils
import com.thanhlv.fw.spf.SPF
import kotlinx.coroutines.runBlocking
import java.util.*
import kotlin.collections.ArrayList

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
            RunUtils.runInBackground {
                runBlocking {

                    if (AppDatabase.getInstance(applicationContext).dao().getAllChallenge()
                            .isEmpty()
                    ) {
                        generationDataDefault()
                    }
//                    Handler().postDelayed({
                        runBlocking {
                            ChallengeFragment.allChallenges =
                                AppDatabase.getInstance(applicationContext).dao().getAllChallenge()
                            ChallengeFragment.myChallenges =
                                AppDatabase.getInstance(applicationContext).dao().getMyChallenge()
                        }
//                    }, 500)

                }
            }

    }

    suspend fun generationDataDefault() {
        //challenge default
        var challenge = Challenge()
        challenge.name = "Digital Detox Challenge"
        challenge.duration = 7

        var task = Task()
        var taskList = ArrayList<Tasks>()
        AppDatabase.getInstance(applicationContext).dao().insert(task)
        var tasks = Tasks("Assess your screen time", task.id)
        taskList.add(tasks)

        task = Task()
        AppDatabase.getInstance(applicationContext).dao().insert(task)
        tasks = Tasks("Set Screen Time Limits", task.id)
        taskList.add(tasks)

        task = Task()
        AppDatabase.getInstance(applicationContext).dao().insert(task)
        tasks = Tasks("Engage in non-screen activities", task.id)
        taskList.add(tasks)

        task = Task()
        AppDatabase.getInstance(applicationContext).dao().insert(task)
        tasks = Tasks("Delete Unnecessary Apps", task.id)
        taskList.add(tasks)

        task = Task()
        AppDatabase.getInstance(applicationContext).dao().insert(task)
        tasks = Tasks("Call a friend", task.id)
        taskList.add(tasks)

        task = Task()
        AppDatabase.getInstance(applicationContext).dao().insert(task)
        tasks = Tasks("Schedule social media time", task.id)
        taskList.add(tasks)

        task = Task()
        AppDatabase.getInstance(applicationContext).dao().insert(task)
        tasks = Tasks("Reflect and Plan Ahead", task.id)
        taskList.add(tasks)

        challenge.tasks = taskList.toList()
        challenge.image = R.drawable.img_challenge_digital_detox.toString()

        AppDatabase.getInstance(applicationContext).dao().insertChallenge(challenge)


        //challenge default
        challenge = Challenge()
        challenge.name = "Relieve anxiety"
        challenge.duration = 7

        task = Task()
        taskList = ArrayList<Tasks>()
        AppDatabase.getInstance(applicationContext).dao().insert(task)
        tasks = Tasks("Acknowledge your feeling", task.id)
        taskList.add(tasks)

        task = Task()
        AppDatabase.getInstance(applicationContext).dao().insert(task)
        tasks = Tasks("Practice Mindful Breathing", task.id)
        taskList.add(tasks)

        task = Task()
        AppDatabase.getInstance(applicationContext).dao().insert(task)
        tasks = Tasks("Write down your feeling", task.id)
        taskList.add(tasks)

        task = Task()
        AppDatabase.getInstance(applicationContext).dao().insert(task)
        tasks = Tasks("Exercise", task.id)
        taskList.add(tasks)

        task = Task()
        AppDatabase.getInstance(applicationContext).dao().insert(task)
        tasks = Tasks("Create a structured routine", task.id)
        taskList.add(tasks)

        task = Task()
        AppDatabase.getInstance(applicationContext).dao().insert(task)
        tasks = Tasks("Maitain self-care (relax in the bath/shower -> icon bồn tắm)", task.id)
        taskList.add(tasks)

        task = Task()
        AppDatabase.getInstance(applicationContext).dao().insert(task)
        tasks = Tasks("Digital Detox", task.id)
        taskList.add(tasks)

        challenge.tasks = taskList.toList()
        challenge.image = R.drawable.img_challenge_relieve_my_anxiety.toString()

        AppDatabase.getInstance(applicationContext).dao().insertChallenge(challenge)

        //challenge default
        challenge = Challenge()
        challenge.name = "Work boost"
        challenge.duration = 5

        task = Task()
        taskList = ArrayList<Tasks>()
        AppDatabase.getInstance(applicationContext).dao().insert(task)
        tasks = Tasks("Build a to-do list", task.id)
        taskList.add(tasks)

        task = Task()
        AppDatabase.getInstance(applicationContext).dao().insert(task)
        tasks = Tasks("Write down your distractions", task.id)
        taskList.add(tasks)

        task = Task()
        AppDatabase.getInstance(applicationContext).dao().insert(task)
        tasks = Tasks("Focus on one task for 25 mins", task.id)
        taskList.add(tasks)

        task = Task()
        AppDatabase.getInstance(applicationContext).dao().insert(task)
        tasks = Tasks("Batch your tasks", task.id)
        taskList.add(tasks)

        task = Task()
        AppDatabase.getInstance(applicationContext).dao().insert(task)
        tasks = Tasks("Refine your workspace", task.id)
        taskList.add(tasks)

        challenge.tasks = taskList.toList()
        challenge.image = R.drawable.img_challenge_work_boost.toString()

        AppDatabase.getInstance(applicationContext).dao().insertChallenge(challenge)

        //challenge default
        challenge = Challenge()
        challenge.name = "Morning Glow"
        challenge.duration = 7

        task = Task()
        taskList = ArrayList<Tasks>()
        AppDatabase.getInstance(applicationContext).dao().insert(task)
        tasks = Tasks("Drink water when you wake up", task.id)
        taskList.add(tasks)

        task = Task()
        AppDatabase.getInstance(applicationContext).dao().insert(task)
        tasks = Tasks("Eat a Healthy Breakfast", task.id)
        taskList.add(tasks)

        task = Task()
        AppDatabase.getInstance(applicationContext).dao().insert(task)
        tasks = Tasks("Stretch and exercise", task.id)
        taskList.add(tasks)

        task = Task()
        AppDatabase.getInstance(applicationContext).dao().insert(task)
        tasks = Tasks("Make your bed", task.id)
        taskList.add(tasks)

        challenge.tasks = taskList.toList()
        challenge.image = R.drawable.img_challenge_morning_glow.toString()

        AppDatabase.getInstance(applicationContext).dao().insertChallenge(challenge)

    }

    @SuppressLint("MissingSuperCall")
    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
    }

}