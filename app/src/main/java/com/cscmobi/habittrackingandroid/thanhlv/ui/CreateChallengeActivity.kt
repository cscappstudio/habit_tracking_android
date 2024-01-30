package com.cscmobi.habittrackingandroid.thanhlv.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.cscmobi.habittrackingandroid.R
import com.cscmobi.habittrackingandroid.data.model.ChallengeJoinedHistory
import com.cscmobi.habittrackingandroid.data.model.Tasks
import com.cscmobi.habittrackingandroid.databinding.ActivityCreateChallengeBinding
import com.cscmobi.habittrackingandroid.databinding.ActivityOnboardBinding
import com.cscmobi.habittrackingandroid.databinding.ActivityQuestionFoBinding
import com.cscmobi.habittrackingandroid.presentation.ui.activity.MainActivity
import com.cscmobi.habittrackingandroid.thanhlv.adapter.AddTaskChallengeAdapter
import com.cscmobi.habittrackingandroid.thanhlv.adapter.DetailChallengeAdapter
import com.cscmobi.habittrackingandroid.thanhlv.adapter.OnBoardAdapter
import com.cscmobi.habittrackingandroid.thanhlv.database.AppDatabase
import com.cscmobi.habittrackingandroid.thanhlv.model.Challenge
import com.cscmobi.habittrackingandroid.thanhlv.model.CreateTaskChallenge
import com.cscmobi.habittrackingandroid.thanhlv.model.OnBoardModel
import com.cscmobi.habittrackingandroid.thanhlv.model.Task
import com.thanhlv.fw.helper.MyUtils.Companion.rippleEffect
import com.thanhlv.fw.helper.RunUtils
import com.thanhlv.fw.spf.SPF
import kotlinx.coroutines.runBlocking
import java.util.*
import kotlin.collections.ArrayList

class CreateChallengeActivity : BaseActivity2() {

    private lateinit var binding: ActivityCreateChallengeBinding

    override fun setupScreen() {
        binding = ActivityCreateChallengeBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun initView() {
        recyclerView()
    }

    override fun controllerView() {
        binding.btnBackHeader.setOnClickListener {
            onBackPressed()
        }
        binding.edtCycle.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
            }

            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
            }

            override fun afterTextChanged(editable: Editable) {
                val cycleNum = binding.edtCycle.text.toString().toIntOrNull() ?: 0
                if (cycleNum <= 0) return
                generateData()
            }
        })

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
            finish()
//            intent.action = "create_new_task"
//            startActivity(intent)
        }
    }

    private fun addNewTask(item: CreateTaskChallenge) {
        val intent = Intent(this, CreateNewTaskChallengeActivity::class.java)
        intent.action = "create_new_task"
        resultActivityCallback.launch(intent)
    }
}