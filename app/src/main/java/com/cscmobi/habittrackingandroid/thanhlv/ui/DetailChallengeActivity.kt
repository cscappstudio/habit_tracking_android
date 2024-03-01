package com.cscmobi.habittrackingandroid.thanhlv.ui

import android.annotation.SuppressLint
import android.graphics.BitmapFactory
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.PrimaryKey
import com.cscmobi.habittrackingandroid.R
import com.cscmobi.habittrackingandroid.data.model.ChallengeJoinedHistory
import com.cscmobi.habittrackingandroid.data.model.TaskTimelineModel
import com.cscmobi.habittrackingandroid.databinding.ActivityDetailChallengeBinding
import com.cscmobi.habittrackingandroid.thanhlv.adapter.DetailChallengeAdapter
import com.cscmobi.habittrackingandroid.thanhlv.database.AppDatabase
import com.cscmobi.habittrackingandroid.thanhlv.model.Challenge
import com.cscmobi.habittrackingandroid.utils.CalendarUtil
import com.cscmobi.habittrackingandroid.utils.CalendarUtil.Companion.getDaysBetween
import com.google.gson.Gson
import com.thanhlv.fw.helper.MyUtils.Companion.configKeyboardBelowEditText
import com.thanhlv.fw.helper.MyUtils.Companion.rippleEffect
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import java.util.*
import kotlin.collections.ArrayList


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
        if (mChallenge != null) {

            recyclerView()
            binding.tvTitleChallenge.text = mChallenge?.name
            binding.tvDays.text = mChallenge?.duration.toString() + " days"
            if (!mChallenge?.image.isNullOrEmpty())
                binding.imgChallenge
                    .setImageBitmap(BitmapFactory.decodeStream(assets.open(mChallenge?.image!!)))
            else binding.imgChallenge.setImageResource(R.drawable.img_target)

            if (!mChallenge?.joinedHistory.isNullOrEmpty()) {
                binding.btnStartChallenge.visibility = View.GONE
                binding.btnOptionTop.visibility = View.VISIBLE
                binding.progressBar.visibility = View.VISIBLE
            } else {
                binding.btnStartChallenge.visibility = View.VISIBLE
                binding.btnOptionTop.visibility = View.GONE
                binding.progressBar.visibility = View.GONE
            }

        }
    }

    override fun controllerView() {
        binding.btnBackHeader.setOnClickListener {
            onBackPressed()
        }

        binding.btnStartChallenge.setOnClickListener {
            joinChallenge()
            finish()
        }

        binding.btnOptionTop.setOnClickListener {
            val menuDropDown = MenuDropDown(this@DetailChallengeActivity)
            menuDropDown.showAsDropDown(it)
        }
    }

    private fun joinChallenge() {
        if (mChallenge == null) return
        runBlocking {
            val joined = ChallengeJoinedHistory(System.currentTimeMillis())
            if (mChallenge!!.joinedHistory.isEmpty()) mChallenge!!.joinedHistory = listOf(joined)
            else {
                val tem = mChallenge!!.joinedHistory
                tem.toMutableList().add(joined)
                mChallenge!!.joinedHistory = tem.toList()
            }

            resolverDataJoinChallenge()
            AppDatabase.getInstance(applicationContext).dao().updateChallenge(mChallenge!!)

            //update task challenge to task all
            delay(600)
            val newMyChallenges =
                ArrayList(AppDatabase.getInstance(applicationContext).dao().getMyChallenge())
            newMyChallenges.sortByDescending {
                it.joinedHistory.last().date
            }

            ChallengeFragment.allChallenges.postValue(
                AppDatabase.getInstance(applicationContext).dao().getAllChallenge()
            )
            ChallengeFragment.myChallenges.postValue(newMyChallenges)
        }
    }

    private suspend fun resolverDataJoinChallenge() {
        if (mChallenge == null) return
        if (mChallenge!!.repeat.isEmpty()) mChallenge!!.repeat = listOf(2, 3, 4, 5, 6, 7, 1)
        val listDate = getListDateFill(mChallenge!!.repeat, mChallenge!!.duration)
        var startDate = 0

        var out = false
        for (i in 1..mChallenge!!.duration / mChallenge!!.cycle) {
            for (j in 0 until mChallenge!!.days.size) {
                for (k in 0 until mChallenge!!.days[j].tasks!!.size) {
                    if (getDaysBetween(listDate[0], listDate[startDate] ) > (mChallenge!!.duration - 1)) {
                        out = true
                        break
                    }
                    val task = mChallenge!!.days[j].tasks!![k].parserToTask()
                    task.challenge = mChallenge!!.name
                    task.startDate = listDate[startDate]
                    task.imgChallenge = mChallenge!!.image
                    task.endDate.isOpen = true
                    task.endDate.endDate = task.startDate!!
                    val taskId = AppDatabase.getInstance(applicationContext).dao().insertTask(task)
                    mChallenge!!.days[j].tasks!![k].startDate = task.startDate
                    mChallenge!!.days[j].tasks!![k].startDate = taskId
                    println("thanhlv -------------- yyyyyyyyy ==== " + taskId)
                }
                CalendarUtil
                startDate++
                if (getDaysBetween(listDate[0], listDate[startDate] ) > (mChallenge!!.duration - 1)) {
                    out = true
                    break
                }
            }
            if (out) break
        }
    }

    private fun getListDateFill(repeat: List<Int>, duration: Int): List<Long> {
        var startDate = System.currentTimeMillis()
        var nextDay = CalendarUtil.startWeekMs(startDate)
        var rangeDate = arrayListOf<Long>()
        rangeDate.add(nextDay)
        for (i in 0..duration + 7) {
            nextDay = CalendarUtil.nextDay(nextDay)
            rangeDate.add(nextDay)
        }
        val tem = arrayListOf<Long>()
        rangeDate.forEach {
            if (repeat.contains(CalendarUtil.dayOfWeek(it))) tem.add(it)
        }

        rangeDate = arrayListOf<Long>()
        for (i in 0 until tem.size)
            if (startDate <= tem[i]) {
                startDate = tem[i]
                break
            }
        tem.forEach {
            if (it >= startDate) {
                rangeDate.add(it)
            }
        }
        return rangeDate
    }

    private var adapter: DetailChallengeAdapter? = null
    private fun recyclerView() {
        adapter = DetailChallengeAdapter(this)
        adapter?.setData(getTasks())
        binding.rcTasks.adapter = adapter
        binding.rcTasks.layoutManager = LinearLayoutManager(this)

    }

    private fun getTasks(): MutableList<TaskTimelineModel> {
        if (mChallenge == null) return mutableListOf()

        val temp = mutableListOf<TaskTimelineModel>()

        mChallenge?.days?.forEach { _it ->
            _it.tasks?.forEach {
                it.dayNo = _it.dayNo
                val new = TaskTimelineModel(it)
                new.type = 1
                new.status = 0
                temp.add(new)
            }
        }
        if (temp.isEmpty()) return mutableListOf()
        temp[0].type = 0
        temp[0].status = 2
        temp[1].type = 1
        temp[1].status = 0

        temp[temp.size - 1].type = 2
        temp[temp.size - 1].status = 0
        return temp
    }

}