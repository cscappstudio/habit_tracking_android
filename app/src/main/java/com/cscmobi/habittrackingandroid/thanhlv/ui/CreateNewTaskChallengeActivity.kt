package com.cscmobi.habittrackingandroid.thanhlv.ui

import android.content.res.ColorStateList
import android.graphics.Color
import android.net.Uri
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import com.cscmobi.habittrackingandroid.databinding.ActivityCreateNewTaskChallengeBinding
import com.cscmobi.habittrackingandroid.thanhlv.model.CreateTaskChallenge
import com.google.gson.Gson

class CreateNewTaskChallengeActivity : BaseActivity2() {

    private lateinit var binding: ActivityCreateNewTaskChallengeBinding

    override fun setupScreen() {
        binding = ActivityCreateNewTaskChallengeBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun loadData() {
    }

    override fun initView() {
        btnColorView.add(binding.color1)
        btnColorView.add(binding.color2)
        btnColorView.add(binding.color3)
        btnColorView.add(binding.color4)
        btnColorView.add(binding.color5)
        btnColor.add("#B6D6DD")
        btnColor.add("#EBB2BD")
        btnColor.add("#EEC9AA")
        btnColor.add("#BEE4B8")
        btnColor.add("#CFB2EB")
    }

    private val popupEmoji = PopupChoseEmojiTask()
    override fun controllerView() {
        binding.btnBackHeader.setOnClickListener {
            onBackPressed()
        }
        binding.edtName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
            }

            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
            }

            override fun afterTextChanged(editable: Editable) {
            }
        })
        popupEmoji.callback = object : PopupChoseEmojiTask.Callback{
            override fun clickChange(ava: String) {

                binding.icAva.setImageURI(Uri.parse(ava))
            }
        }

        binding.btnChangeIcon.setOnClickListener {
            popupEmoji.show(supportFragmentManager, "")
        }


        binding.btnCreate.setOnClickListener {

        }

        binding.btnCreate.setOnClickListener {
            createNewTask()
        }

        binding.color1.setOnClickListener {
            resolveColorButton(1)
        }
        binding.color2.setOnClickListener {
            resolveColorButton(2)
        }
        binding.color3.setOnClickListener {
            resolveColorButton(3)
        }
        binding.color4.setOnClickListener {
            resolveColorButton(4)
        }
        binding.color5.setOnClickListener {
            resolveColorButton(5)
        }

    }

    private fun createNewTask() {
        val newTask = CreateTaskChallenge(1, 0)
        intent.putExtra("data_new_task", Gson().toJson(newTask))
        setResult(868, null)
    }

    private fun resolveColorButton(color: Int) {
        for (i in 0..4) {
            btnColorView[i].backgroundTintList = ColorStateList.valueOf(
                Color.parseColor(btnColor[i])
            )
        }
        btnColorView[color - 1].backgroundTintList = null
    }

    private val btnColor = arrayListOf<String>()
    private val btnColorView = arrayListOf<View>()

}