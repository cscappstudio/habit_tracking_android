package com.cscmobi.habittrackingandroid.thanhlv.ui

import android.annotation.SuppressLint
import android.graphics.BitmapFactory
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.cscmobi.habittrackingandroid.R
import com.cscmobi.habittrackingandroid.data.model.ChallengeJoinedHistory
import com.cscmobi.habittrackingandroid.data.model.TaskTimelineModel
import com.cscmobi.habittrackingandroid.databinding.ActivityDetailChallengeBinding
import com.cscmobi.habittrackingandroid.thanhlv.adapter.DetailChallengeAdapter
import com.cscmobi.habittrackingandroid.thanhlv.database.AppDatabase
import com.cscmobi.habittrackingandroid.thanhlv.model.Challenge
import com.cscmobi.habittrackingandroid.utils.CalendarUtil
import com.google.gson.Gson
import com.thanhlv.fw.helper.MyUtils.Companion.configKeyboardBelowEditText
import com.thanhlv.fw.helper.MyUtils.Companion.rippleEffect
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.toList
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
            joinChallenge(mChallenge)
            finish()
        }

        binding.btnOptionTop.setOnClickListener {
            val menuDropDown = MenuDropDown(this@DetailChallengeActivity)
            menuDropDown.showAsDropDown(it)
        }
    }

    private fun joinChallenge(mChallenge: Challenge?) {
        if (mChallenge == null) return
        runBlocking {
            val joined = ChallengeJoinedHistory(System.currentTimeMillis())
            if (mChallenge.joinedHistory.isNullOrEmpty()) mChallenge.joinedHistory = listOf(joined)
            else {
                val tem = mChallenge.joinedHistory
                tem!!.toMutableList().add(joined)
                mChallenge.joinedHistory = tem.toList()
            }

            resolverDataJoinChallenge(mChallenge)
            AppDatabase.getInstance(applicationContext).dao().updateChallenge(mChallenge)

            //update task challenge to task all

            delay(600)
            val newMyChallenges =
                ArrayList(AppDatabase.getInstance(applicationContext).dao().getMyChallenge())
            newMyChallenges.sortByDescending {
                val last = it.joinedHistory!!.size
                it.joinedHistory!![last - 1].date
            }

            ChallengeFragment.allChallenges.postValue(AppDatabase.getInstance(applicationContext).dao().getAllChallenge())
            ChallengeFragment.myChallenges.postValue(newMyChallenges)

        }
    }

    private suspend fun resolverDataJoinChallenge(challenge: Challenge) {
        if (challenge.repeat.isNullOrEmpty()) challenge.repeat = listOf(2, 3, 4, 5, 6, 7, 1)
        val listDate = getListDateFill(challenge.repeat!!, challenge.duration, challenge.cycle)
        var startDate = 0

        while (startDate < challenge.duration/challenge.cycle) {
            challenge.days?.forEach {
                it.tasks?.forEach { _task ->
                    val task = _task.parserToTask()
                    task.challenge = challenge.name
                    task.startDate = listDate[startDate]
                    task.imgChallenge = challenge.image
                    task.endDate.isOpen = true
                    task.endDate.endDate = task.startDate!!
                    AppDatabase.getInstance(applicationContext).dao().insertTask(task)
                }
                startDate++
            }
        }

    }

    private fun getListDateFill(repeat: List<Int>, duration: Int, cycle: Int): List<Long> {
        var today = System.currentTimeMillis()
        var thisWeek = arrayListOf<Long>()
        var startWeek = CalendarUtil.startWeekMs(today)
        thisWeek.add(startWeek)
        for (i in 3..8) {
            startWeek = CalendarUtil.nextDay(startWeek)
            thisWeek.add(startWeek)
        }
        val temW = arrayListOf<Long>()
        thisWeek.forEach {
            if (repeat.contains(CalendarUtil.dayOfWeek(it)))
                temW.add(it)
        }
        thisWeek = arrayListOf<Long>()
        thisWeek = temW

        for (i in 0 until thisWeek.size)
            if (today <= thisWeek[i]) {
                today = thisWeek[i]
                break
            }

        val listDate = arrayListOf<Long>()
        val n = duration / cycle
        for (i in 0..n) {
            val tem = kotlin.collections.ArrayList(thisWeek)
            for (j in 0 until tem.size) {
                tem[j] = tem[j] + i * 7 * 24 * 60 * 60 * 1000L
            }
            listDate.addAll(tem)
        }
        val tem = arrayListOf<Long>()
        var nn = 0
        for (i in 0 until listDate.size){
            if (listDate[i] >= today) {
                tem.add(listDate[i])
                nn++
            }
            if (nn == duration) break
        }

        return tem
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