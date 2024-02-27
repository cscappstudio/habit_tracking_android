package com.cscmobi.habittrackingandroid.thanhlv.ui

import android.annotation.SuppressLint
import android.graphics.BitmapFactory
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.cscmobi.habittrackingandroid.R
import com.cscmobi.habittrackingandroid.data.model.ChallengeJoinedHistory
import com.cscmobi.habittrackingandroid.databinding.ActivityDetailChallengeBinding
import com.cscmobi.habittrackingandroid.thanhlv.adapter.DetailChallengeAdapter
import com.cscmobi.habittrackingandroid.thanhlv.database.AppDatabase
import com.cscmobi.habittrackingandroid.thanhlv.model.Challenge
import com.cscmobi.habittrackingandroid.thanhlv.model.TaskTimelineModel
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
                binding.progressBar.visibility = View.GONE
            } else {
                binding.btnStartChallenge.visibility = View.VISIBLE
                binding.btnOptionTop.visibility = View.GONE
                binding.progressBar.visibility = View.VISIBLE
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
            mChallenge.joinedHistory = listOf(joined)
            AppDatabase.getInstance(applicationContext).dao().updateChallenge(mChallenge)
            delay(500)
            val newMyChallenges =
                ArrayList(AppDatabase.getInstance(applicationContext).dao().getMyChallenge())
            newMyChallenges.sortByDescending {
                val last = it.joinedHistory!!.size
                it.joinedHistory!![last - 1].date
            }

            ChallengeFragment.myChallenges.postValue(newMyChallenges)
        }
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
                val new = TaskTimelineModel(it)

                new.type = 1
                new.status = 0
                temp.add(new)
            }
        }
        if (temp.isEmpty()) return mutableListOf()
        temp[0].type = 0
        temp[0].status = 0
        temp[1].type = 1
        temp[1].status = 2

        temp[temp.size - 1].type = 2
        temp[temp.size - 1].status = 0
        return temp
    }

}