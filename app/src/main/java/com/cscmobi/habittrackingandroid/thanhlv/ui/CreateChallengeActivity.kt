package com.cscmobi.habittrackingandroid.thanhlv.ui

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.BitmapFactory
import android.graphics.Color
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import com.cscmobi.habittrackingandroid.R
import com.cscmobi.habittrackingandroid.data.model.ChallengeDays
import com.cscmobi.habittrackingandroid.data.model.TaskInChallenge
import com.cscmobi.habittrackingandroid.databinding.ActivityCreateChallengeBinding
import com.cscmobi.habittrackingandroid.presentation.ui.view.BottomSheetCollectionFragment
import com.cscmobi.habittrackingandroid.thanhlv.adapter.AddTaskChallengeAdapter
import com.cscmobi.habittrackingandroid.thanhlv.database.AppDatabase
import com.cscmobi.habittrackingandroid.thanhlv.model.Challenge
import com.cscmobi.habittrackingandroid.thanhlv.model.CreateTaskChallenge
import com.google.gson.Gson
import com.thanhlv.fw.helper.MyUtils
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking

class CreateChallengeActivity : BaseActivity2() {

    private lateinit var binding: ActivityCreateChallengeBinding

    override fun setupScreen() {
        binding = ActivityCreateChallengeBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun initView() {
        mImgChallenge = ""
        binding.bgAddImg.setImageResource(R.drawable.bg_add_icon)
        listDayView.add(binding.btnMon)
        listDayView.add(binding.btnTue)
        listDayView.add(binding.btnWed)
        listDayView.add(binding.btnThu)
        listDayView.add(binding.btnFri)
        listDayView.add(binding.btnSat)
        listDayView.add(binding.btnSun)
        recyclerView()
    }

    private var listDayText = arrayListOf<String>()
    private var listDayState = arrayListOf(false, false, false, false, false, false, false, false)
    private var mRepeatData = arrayListOf(2, 3, 4, 5, 6, 7, 1)
    private var listDayView = arrayListOf<TextView>()

    private var mImgChallenge = "album_collection1.png"
    override fun controllerView() {
        binding.btnStartChallenge.setOnClickListener {
            performCreateChallenge()
        }
        binding.btnBackHeader.setOnClickListener {
            onBackPressed()
        }
        binding.bgAddImg.setOnClickListener {
            val bottomSheetFragment = BottomSheetImageChallenge()
            binding.btnMon

            bottomSheetFragment.listener =
                object : BottomSheetImageChallenge.IBottomCollection {
                    override fun next(resDrawable: String) {
                        //
                        binding.bgAddImg.setImageBitmap(
                            BitmapFactory.decodeStream(
                                assets.open(
                                    resDrawable
                                )
                            )
                        )
                        binding.icPlusAva.visibility = View.GONE
                        mImgChallenge = resDrawable
                    }
                }

            bottomSheetFragment.show(supportFragmentManager, "")
        }
        binding.edtCycle.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
            }

            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
            }

            override fun afterTextChanged(editable: Editable) {
                validateData()
                generateData()
            }
        })
        handleSetupRepeat()
    }

    private fun performCreateChallenge() {
        if (binding.edtName.text.toString().isEmpty()) {
            Toast.makeText(this, "Name empty!", Toast.LENGTH_SHORT).show()
            binding.edtName.requestFocus()
            return
        }

        val listDayTask = arrayListOf<ChallengeDays>()
        for (i in 1..binding.edtCycle.text.toString().toInt()) {
            val challengeDay = ChallengeDays(i)
            val taskInDay = arrayListOf<TaskInChallenge>()
            var taskNo = 0
            mDataTaskCreateChallenge.forEach {
                if (it.day == i && it.type > 1) {
                    val task = it.parserToTaskInChallenge()
                    task.dayNo = i
                    task.taskNo = taskNo

                    taskNo += 1
                    taskInDay.add(task)
                }
            }
            challengeDay.tasks = taskInDay.toList()
            listDayTask.add(challengeDay)
        }

        runBlocking {
            val challenge = Challenge()
            challenge.name = binding.edtName.text.toString()
            challenge.description = binding.textNote.text.toString()
            challenge.image = mImgChallenge
            challenge.duration = binding.edtDuration.text.toString().toInt()
            challenge.cycle = binding.edtCycle.text.toString().toInt()
            challenge.repeat = mRepeatData
            challenge.days = listDayTask.toList()
            AppDatabase.getInstance(applicationContext).dao().insertChallenge(challenge)
            delay(500)
            val all = AppDatabase.getInstance(applicationContext).dao().getAllChallenge()

            ChallengeFragment.allChallenges.postValue(all)
            finish()
        }
    }

    private fun validateData(): Boolean {
        val durationNum = binding.edtDuration.text.toString().toIntOrNull() ?: 0
        val cycleNum = binding.edtCycle.text.toString().toIntOrNull() ?: 0
        if (cycleNum == 0) {
            binding.rcAddTask.visibility = View.GONE
            binding.edtCycle.requestFocus()
            Toast.makeText(this, getString(R.string.cycle_must_be_more_than_0), Toast.LENGTH_SHORT)
                .show()
            return false
        }
        if (durationNum == 0 || durationNum < cycleNum) {
            binding.edtDuration.requestFocus()
            Toast.makeText(
                this,
                getString(R.string.duration_must_be_more_than_cycle),
                Toast.LENGTH_SHORT
            ).show()
            return false
        }
        return true
    }

    private fun handleSetupRepeat() {
        listDayText.add(getString(R.string.monday))
        listDayText.add(getString(R.string.tuesday))
        listDayText.add(getString(R.string.wednesday))
        listDayText.add(getString(R.string.thursday))
        listDayText.add(getString(R.string.friday))
        listDayText.add(getString(R.string.saturday))
        listDayText.add(getString(R.string.sunday))

        binding.btnRepeat.setOnClickListener {
            spinnerRepeatDay()
        }
        binding.btnMon.setOnClickListener {
            clickDayView(0, binding.btnMon)
        }
        binding.btnTue.setOnClickListener {
            clickDayView(1, binding.btnTue)
        }
        binding.btnWed.setOnClickListener {
            clickDayView(2, binding.btnWed)
        }
        binding.btnThu.setOnClickListener {
            clickDayView(3, binding.btnThu)
        }
        binding.btnFri.setOnClickListener {
            clickDayView(4, binding.btnFri)
        }
        binding.btnSat.setOnClickListener {
            clickDayView(5, binding.btnSat)
        }
        binding.btnSun.setOnClickListener {
            clickDayView(6, binding.btnSun)
        }
    }


    private fun spinnerRepeatDay() {
        if (binding.selectRepeatDay.visibility == View.GONE) {
            binding.icSpinRepeat.animate().rotationBy(180f).setDuration(200).start()
            MyUtils.expandView(binding.selectRepeatDay, 200, 64)
        } else {
            binding.icSpinRepeat.animate().rotationBy(-180f).setDuration(200).start()
            MyUtils.collapseView(binding.selectRepeatDay, 200, 0)
        }
    }

    private fun clickDayView(day: Int, view: TextView) {
        listDayState[day] = !listDayState[day]
        if (listDayState[day]) {
            view.backgroundTintList = ColorStateList.valueOf(
                Color.parseColor("#CC54BA8F")
            )
            view.setTextColor(Color.parseColor("#ffffff"))
        } else {
            view.backgroundTintList = ColorStateList.valueOf(
                Color.parseColor("#F5F5F5")
            )
            view.setTextColor(Color.parseColor("#b5b5b5"))
        }

        var textRepeat = getString(R.string.repeat_on)
        var notAllDay = 0
        for (i in 0..6) {
            if (listDayState[i]) {
                textRepeat = textRepeat + " " + listDayText[i] + ","
                notAllDay++
            }
        }
        if (notAllDay == 0 || notAllDay == 7) {
            binding.tvRepeatDay.text = getString(R.string.repeat_every_day)
            mRepeatData = arrayListOf(2, 3, 4, 5, 6, 7, 1)
        } else {
            textRepeat = textRepeat.substring(0, textRepeat.length - 1)
            binding.tvRepeatDay.text = textRepeat
            mRepeatData = arrayListOf()
            for (i in 0..5) {
                if (listDayState[i]) {
                    mRepeatData.add(i + 2)
                }
            }
            if (listDayState[6]) {
                mRepeatData.add(1)
            }
        }

    }

    companion object {
        var mDataTaskCreateChallenge = mutableListOf<CreateTaskChallenge>()
    }

    private fun generateData() {
        val cycleNum = binding.edtCycle.text.toString().toIntOrNull() ?: 0
        if (cycleNum <= 0) {
            binding.rcAddTask.visibility = View.GONE
            return
        }
        binding.rcAddTask.visibility = View.VISIBLE
        val list = arrayListOf<CreateTaskChallenge>()
        for (i in 1..cycleNum) {
            list.add(CreateTaskChallenge(i, 0))
        }
        mDataTaskCreateChallenge = list.toMutableList()
        adapter?.setData(mDataTaskCreateChallenge)
    }

    override fun loadData() {

    }

    private var adapter: AddTaskChallengeAdapter? = null
    private fun recyclerView() {
        if (adapter == null) {
            adapter = AddTaskChallengeAdapter(this)
            adapter?.setCallBack(object : AddTaskChallengeAdapter.AddTaskChallengeCallback {
                override fun onClickItem(item: CreateTaskChallenge, pos: Int) {
                    mCurrentTaskClick = pos
                    if (item.type == 1 || item.type == 0) {
                        addNewTask(item)
                    } else {
                        editTask(item)
                    }
                }

                override fun onClickDelete(item: CreateTaskChallenge, pos: Int) {
                    deleteTask(item, pos)
                }

            })
            binding.rcAddTask.adapter = adapter
            binding.rcAddTask.layoutManager = LinearLayoutManager(this)
        }
        generateData()

    }

    private fun deleteTask(item: CreateTaskChallenge, pos: Int) {
        if (item.type == 2) mDataTaskCreateChallenge[pos + 1].type -= 1
        adapter?.notifyItemRemoved(pos)
        mDataTaskCreateChallenge.remove(item)
        adapter?.setData(mDataTaskCreateChallenge)
    }

    private fun editTask(item: CreateTaskChallenge) {
        val intent = Intent(this, CreateNewTaskChallengeActivity::class.java)
        intent.putExtra("data", Gson().toJson(item))
        intent.action = "action_edit"
        resultActivityCallback.launch(intent)
    }

    private val resultActivityCallback: ActivityResultLauncher<Intent> = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == 868) {
            val mTaskNew =
                Gson().fromJson<CreateTaskChallenge>(
                    result.data?.getStringExtra("data_new_task_result"),
                    CreateTaskChallenge::class.java
                )
            updateRecycler(mTaskNew, false)
        }
        if (result.resultCode == 999) {
            val mTaskNew =
                Gson().fromJson<CreateTaskChallenge>(
                    result.data?.getStringExtra("data_new_task_result"),
                    CreateTaskChallenge::class.java
                )
            updateRecycler(mTaskNew, true)
        }
    }

    private var mCurrentTaskClick = 0
    private fun updateRecycler(mTaskNew: CreateTaskChallenge, isEdit: Boolean) {
        if (mDataTaskCreateChallenge[mCurrentTaskClick].type == 0)
            mDataTaskCreateChallenge[mCurrentTaskClick].type = 2
        if (mDataTaskCreateChallenge[mCurrentTaskClick].type == 1)
            mDataTaskCreateChallenge[mCurrentTaskClick].type = 3

        mDataTaskCreateChallenge[mCurrentTaskClick].name = mTaskNew.name
        mDataTaskCreateChallenge[mCurrentTaskClick].color = mTaskNew.color
        mDataTaskCreateChallenge[mCurrentTaskClick].icon = mTaskNew.icon

        if (!isEdit)
            mDataTaskCreateChallenge.add(
                mCurrentTaskClick + 1,
                CreateTaskChallenge(mDataTaskCreateChallenge[mCurrentTaskClick].day, 1)
            )
        adapter?.setData(mDataTaskCreateChallenge)
        checkAvailableChallenge()
    }

    private fun addNewTask(item: CreateTaskChallenge) {
        if (mDataTaskCreateChallenge.isNotEmpty() && mDataTaskCreateChallenge.size > binding.edtCycle.text.toString()
                .toInt()
        ) {
            showPopupChoseTask(item)
            return
        }
        val intent = Intent(this, CreateNewTaskChallengeActivity::class.java)
        intent.putExtra("data", Gson().toJson(item))
        resultActivityCallback.launch(intent)
    }

    private fun showPopupChoseTask(item: CreateTaskChallenge) {
        val popupChoseTask = PopupChoseTaskChallenge()
        popupChoseTask.callback = object : PopupChoseTaskChallenge.Callback {
            override fun onclickBack() {
            }

            override fun onclickCreateNew() {
                val intent =
                    Intent(this@CreateChallengeActivity, CreateNewTaskChallengeActivity::class.java)
                intent.putExtra("data", Gson().toJson(item))
                resultActivityCallback.launch(intent)
            }

            override fun onclickItemTask(task: CreateTaskChallenge) {
                updateRecycler(task, false)
            }
        }
        popupChoseTask.show(supportFragmentManager, "")
    }

    private fun checkAvailableChallenge() {
        if (mDataTaskCreateChallenge.isEmpty() || mDataTaskCreateChallenge.size < binding.edtCycle.text.toString()
                .toInt() * 2
        ) {
            binding.btnStartChallenge.isEnabled = false
            binding.btnStartChallenge.backgroundTintList =
                ColorStateList.valueOf(Color.parseColor("#B5B5B5"))
            return
        }
        var dem = 0
        for (i in 1..binding.edtCycle.text.toString().toInt()) {
            for (j in 0 until mDataTaskCreateChallenge.size)
                if (mDataTaskCreateChallenge[j].day == i && mDataTaskCreateChallenge[j].type > 1) {
                    dem += 1
                    break
                }
        }
        if (dem == binding.edtCycle.text.toString().toInt()) {
            binding.btnStartChallenge.isEnabled = true
            binding.btnStartChallenge.backgroundTintList =
                ColorStateList.valueOf(Color.parseColor("#52B89C"))
        }
    }
}