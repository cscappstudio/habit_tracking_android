package com.cscmobi.habittrackingandroid.thanhlv.ui

import android.annotation.SuppressLint
import android.graphics.BitmapFactory
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.cscmobi.habittrackingandroid.R
import com.cscmobi.habittrackingandroid.data.model.ChallengeJoinedHistory
import com.cscmobi.habittrackingandroid.data.model.TaskInDay
import com.cscmobi.habittrackingandroid.data.model.TaskTimelineModel
import com.cscmobi.habittrackingandroid.databinding.ActivityDetailChallengeBinding
import com.cscmobi.habittrackingandroid.presentation.ui.view.HomeFragment
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
import kotlin.math.roundToInt


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

            if (mChallenge?.joinedHistory != null) {
                binding.btnStartChallenge.visibility = View.GONE
                binding.btnOptionTop.visibility = View.VISIBLE
                binding.progressBar.visibility = View.VISIBLE
                if (doneTask == 0) binding.progressBar.progress = 5
                else
                    binding.progressBar.progress = doneTask * 100 / totalTask
                if (isMissChallenge || isDoneChallenge) {
                    binding.btnStartChallenge.visibility = View.VISIBLE
                    binding.btnStartChallenge.text = "RESTART CHALLENGE"
                }
            } else {
                binding.btnStartChallenge.visibility = View.VISIBLE
                binding.btnOptionTop.visibility = View.GONE
                binding.progressBar.visibility = View.GONE
            }

        }
    }

    private var hasReset = true

    override fun controllerView() {
        binding.btnBackHeader.setOnClickListener {
            onBackPressed()
        }

        binding.btnStartChallenge.setOnClickListener {
            joinChallenge()
            finish()
        }

        binding.btnOptionTop.setOnClickListener {
            val menuDropDown = MenuDropDown(this@DetailChallengeActivity, hasReset,
                {
                    performResetChallenge()
                    hasReset = false
                },
                {
                    performCancelChallenge()
                    finish()
                }
            )
            menuDropDown.showAsDropDown(it)
        }
    }

    private fun performCancelChallenge() {
        if (mChallenge == null) return
        runBlocking {
            mChallenge?.days?.forEach { it ->
                if (!it.tasks.isNullOrEmpty()) {

                    val history = AppDatabase.getInstance(this@DetailChallengeActivity).dao()
                        .getHistoryByDate2(it.tasks!![0].startDate!!)
                    println("thanhlv yyyyyyyyyyyyyyyy " + it.tasks!![0].startDate + " ;;;;; " + history)
                    for (i in it.tasks?.indices!!) {
                        if (it.tasks!![i].id != null && it.tasks!![i].startDate != null) {
                            val task = AppDatabase.getInstance(this@DetailChallengeActivity).dao()
                                .getTaskById(it.tasks!![i].id!!)
                            if (history != null) {
                                for (j in history.taskInDay.indices)
                                    if (history.taskInDay[j].taskId == it.tasks!![i].id) {
                                        val newList = history.taskInDay.toMutableList()
                                        newList.remove(history.taskInDay[j])
                                        history.taskInDay = newList.toList()
                                        break
                                    }
                                var taskDoneSize = 0
                                history.taskInDay.forEach {
                                    if (it.progress == 100) taskDoneSize++
                                }
                                history.progressDay = if (history.taskInDay.isEmpty()) 0 else
                                    (taskDoneSize.toFloat() * 100f / history.taskInDay.size.toFloat()).roundToInt()

//                                if (history.taskInDay.isEmpty()) {
//                                    val history2 =
//                                        AppDatabase.getInstance(this@DetailChallengeActivity).dao()
//                                            .getHistoryByDate2(it.tasks!![0].startDate!!)
//                                    if (history2 != null)
//                                        AppDatabase.getInstance(this@DetailChallengeActivity)
//                                            .dao()
//                                            .deleteHistory(history2)
//                                } else
                                AppDatabase.getInstance(this@DetailChallengeActivity).dao()
                                    .updateHistory2(
                                        history.id,
                                        history.taskInDay,
                                        history.progressDay
                                    )

                                HomeFragment.updateChallenge = true
                            }

                            if (task != null) AppDatabase.getInstance(this@DetailChallengeActivity)
                                .dao()
                                .deleteTask(task)
                            it.tasks!![i].id = null
                            it.tasks!![i].startDate = null
                            it.tasks!![i].state = 0
                        }
                    }
                }
            }

            mChallenge?.joinedHistory = null
            AppDatabase.getInstance(applicationContext).dao().updateChallenge(mChallenge!!)
            val newMyChallenges =
                ArrayList(AppDatabase.getInstance(applicationContext).dao().getMyChallenge())
            newMyChallenges.sortByDescending {
                it.joinedHistory?.date
            }
            ChallengeFragment.myChallenges.postValue(newMyChallenges)
        }
    }

    private fun performResetChallenge() {

        if (mChallenge == null) return
        performCancelChallenge()
        val joined = ChallengeJoinedHistory(System.currentTimeMillis())
        mChallenge!!.joinedHistory = joined
        runBlocking {
            resolverDataJoinChallenge()
            recyclerView()
            binding.progressBar.progress = 5
        }

    }

    private fun joinChallenge() {
        if (mChallenge == null) return
        runBlocking {
            val joined = ChallengeJoinedHistory(System.currentTimeMillis())
            mChallenge!!.joinedHistory = joined

            resolverDataJoinChallenge()
            AppDatabase.getInstance(applicationContext).dao().updateChallenge(mChallenge!!)

            //update task challenge to task all
            delay(300)
            val newMyChallenges =
                ArrayList(AppDatabase.getInstance(applicationContext).dao().getMyChallenge())
            newMyChallenges.sortByDescending {
                it.joinedHistory?.date
            }

//            ChallengeFragment.allChallenges.postValue(
//                AppDatabase.getInstance(applicationContext).dao().getAllChallenge()
//            )
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
                    if (getDaysBetween(
                            listDate[0],
                            listDate[startDate]
                        ) > (mChallenge!!.duration - 1)
                    ) {
                        out = true
                        break
                    }
//                    if (mChallenge!!.days[j].tasks!![k].id != null)
//                        AppDatabase.getInstance(applicationContext).dao()
//                            .deleteTaskById(mChallenge!!.days[j].tasks!![k].id!!)
                    val task = mChallenge!!.days[j].tasks!![k].parserToTask()
                    task.challenge = mChallenge!!.name
                    task.startDate = listDate[startDate]
                    task.imgChallenge = mChallenge!!.image
                    task.endDate.isOpen = true
                    task.endDate.endDate = task.startDate!!
                    val taskId = AppDatabase.getInstance(applicationContext).dao().insertTask(task)
                    mChallenge!!.days[j].tasks!![k].startDate = task.startDate
                    mChallenge!!.days[j].tasks!![k].id = taskId
                }
                startDate++
                if (getDaysBetween(
                        listDate[0],
                        listDate[startDate]
                    ) > (mChallenge!!.duration - 1)
                ) {
                    out = true
                    break
                }
            }
            if (out) break
        }
    }

    private fun getListDateFill(repeat: List<Int>, duration: Int): List<Long> {
        val startDate = CalendarUtil.startDayMs(System.currentTimeMillis())

        var nextDay = CalendarUtil.startWeekMs(startDate)
        var rangeDate = arrayListOf<Long>()
        rangeDate.add(nextDay)
        for (i in 0..duration + 7) {
            nextDay = CalendarUtil.nextDayMs(nextDay)
            rangeDate.add(nextDay)
        }
        val tem = arrayListOf<Long>()
        rangeDate.forEach {
            if (repeat.contains(CalendarUtil.dayOfWeek(it))) tem.add(it)
        }

        rangeDate = arrayListOf<Long>()
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

    private var isMissChallenge = false
    private var isDoneChallenge = true
    private var totalTask = 0
    private var doneTask = 0
    private fun getTasks(): MutableList<TaskTimelineModel> {
        if (mChallenge == null) return mutableListOf()

        val temp = mutableListOf<TaskTimelineModel>()
        runBlocking {
            isMissChallenge = false
            isDoneChallenge = true
            totalTask = 0
            doneTask = 0
            mChallenge?.days?.forEach { _it ->
                _it.tasks?.forEach {
                    it.dayNo = _it.dayNo
                    if (mChallenge?.joinedHistory != null && it.startDate != null) {
                        val idTask = it.id
                        val historyByDate =
                            AppDatabase.getInstance(applicationContext).dao()
                                .getHistoryByDate2(CalendarUtil.startDayMs(it.startDate!!))
                        if (historyByDate != null && historyByDate.taskInDay.isNotEmpty()) {

                            var progressTask = -1
                            historyByDate.taskInDay.forEach { task_ ->
                                if (task_.taskId == idTask) {
                                    progressTask = task_.progress
                                    return@forEach
                                }
                            }
                            if (progressTask == 100) {
                                doneTask += 1
                                it.state = 1
                                if (getDaysBetween(it.startDate!!, System.currentTimeMillis()) > 0)
                                    it.state = 3
                            } else {
                                if (!isMissChallenge)
                                    isMissChallenge =
                                        CalendarUtil.startDayMs(it.startDate!!) < CalendarUtil.startDayMs(
                                            System.currentTimeMillis()
                                        )
                                it.state = 2
                                isDoneChallenge = false
                            }
                        }
                    }

                    val new = TaskTimelineModel(it)
                    new.type = 1
                    new.status = it.state

                    temp.add(new)
                    totalTask += 1
                }
            }
            if (temp.isEmpty()) return@runBlocking

            if (temp.size > 1) {
                if (mChallenge?.joinedHistory == null) {
                    temp[0].type = 0
                    temp[0].status = 2
                    temp[1].type = 1
                    temp[1].status = 0
                    temp[temp.size - 1].type = 2
                    temp[temp.size - 1].status = 0
                } else {
                    temp[0].type = 0
                    temp[temp.size - 1].type = 2
                }
            } else temp[0].type = 3
        }
        return temp
    }

}
