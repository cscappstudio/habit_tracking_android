package com.cscmobi.habittrackingandroid.thanhlv.ui

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import com.cscmobi.habittrackingandroid.R
import com.cscmobi.habittrackingandroid.databinding.ActivityCreateChallengeBinding
import com.cscmobi.habittrackingandroid.presentation.ui.view.BottomSheetCollectionFragment
import com.cscmobi.habittrackingandroid.thanhlv.adapter.AddTaskChallengeAdapter
import com.cscmobi.habittrackingandroid.thanhlv.model.CreateTaskChallenge
import com.google.gson.Gson
import com.thanhlv.fw.helper.MyUtils

class CreateChallengeActivity : BaseActivity2() {

    private lateinit var binding: ActivityCreateChallengeBinding

    override fun setupScreen() {
        binding = ActivityCreateChallengeBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun initView() {
        mImgChallenge = R.drawable.bg_add_icon
        binding.bgAddImg.setImageResource(mImgChallenge)
        listDayView.add(binding.btnMon)
        listDayView.add(binding.btnTue)
        listDayView.add(binding.btnWed)
        listDayView.add(binding.btnThu)
        listDayView.add(binding.btnFri)
        listDayView.add(binding.btnSat)
        listDayView.add(binding.btnSun)
        recyclerView()
    }

    private var listDayText = arrayListOf(
        "Monday",
        "Tuesday",
        "Wednesday",
        "Thursday",
        "Friday",
        "Saturday",
        "Sunday"
    )
    private var listDayState = arrayListOf(false, false, false, false, false, false, false, false)
    private var listDayView = arrayListOf<TextView>()

    private var mImgChallenge = 0
    override fun controllerView() {
        binding.btnBackHeader.setOnClickListener {
            onBackPressed()
        }
        binding.bgAddImg.setOnClickListener {
            val bottomSheetFragment = BottomSheetCollectionFragment()
            binding.btnMon

            bottomSheetFragment.listener =
                object : BottomSheetCollectionFragment.IBottomCollection {
                    override fun next(resDrawable: Int) {
                        //
                        binding.bgAddImg.setImageResource(resDrawable)
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
                generateData()
            }
        })

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
            binding.icSpinRepeat.animate().rotationBy(180f).setDuration(150).start()
            MyUtils.expandView(binding.selectRepeatDay, 200, 64)
        } else {
            binding.icSpinRepeat.animate().rotationBy(-180f).setDuration(150).start()
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

        var textRepeat = "Repeat on:"
        var notAllDay = 0
        for (i in 0..6) {
            if (listDayState[i]) {
                textRepeat = textRepeat + " " + listDayText[i] + ","
                notAllDay++
            }
        }
        if (notAllDay == 0 || notAllDay == 7) {
            binding.tvRepeatDay.text = "Repeat every day"
        } else {
            textRepeat = textRepeat.substring(0, textRepeat.length - 1)
            binding.tvRepeatDay.text = textRepeat
        }
    }

    private var mData = mutableListOf<CreateTaskChallenge>()
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
        mData = list.toMutableList()
        adapter?.setData(mData)
    }

    override fun loadData() {

    }

    private var adapter: AddTaskChallengeAdapter? = null
    private fun recyclerView() {
        if (adapter == null) {
            adapter = AddTaskChallengeAdapter(this)
            adapter?.setCallBack(object : AddTaskChallengeAdapter.AddTaskChallengeCallback {
                override fun onClickItem(item: CreateTaskChallenge) {
                    if (item.type != 3) {
                        addNewTask(item)
                    } else {
                        editTask(item)
                    }
                }

                override fun onClickDelete(item: CreateTaskChallenge) {
                    deleteTask(item)
                }

            })
            binding.rcAddTask.adapter = adapter
            binding.rcAddTask.layoutManager = LinearLayoutManager(this)
        }
        generateData()

    }

    private fun deleteTask(item: CreateTaskChallenge) {

    }

    private fun editTask(item: CreateTaskChallenge) {

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
            updateRecycler(mTaskNew)
        }
    }

    private fun updateRecycler(mTaskNew: CreateTaskChallenge?) {

        var ii = 0
        var day_ = 1
        for (i in 0 until mData.size) {
            if (mData[i].day == mTaskNew?.day && mData[i].type == mTaskNew.type ) {
                mData[i] = mTaskNew
                if (mData[i].type == 0) mData[i].type = 2
                if (mData[i].type == 1) mData[i].type = 3
                ii = i
                day_ = mTaskNew.day
                break
            }
        }
        mData.add(ii+1, CreateTaskChallenge(day_, 1))
        adapter?.setData(mData)

    }

    private fun addNewTask(item: CreateTaskChallenge) {
        val intent = Intent(this, CreateNewTaskChallengeActivity::class.java)
//        intent.action = "create_new_task"
        intent.putExtra("data", Gson().toJson(item))
        resultActivityCallback.launch(intent)
    }
}