package com.cscmobi.habittrackingandroid.thanhlv.ui

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.BitmapFactory
import android.graphics.Color
import android.net.Uri
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import com.cscmobi.habittrackingandroid.R
import com.cscmobi.habittrackingandroid.databinding.ActivityCreateNewTaskChallengeBinding
import com.cscmobi.habittrackingandroid.thanhlv.model.Challenge
import com.cscmobi.habittrackingandroid.thanhlv.model.CreateTaskChallenge
import com.cscmobi.habittrackingandroid.utils.Utils
import com.google.gson.Gson
import com.thanhlv.fw.helper.MyUtils

class CreateNewTaskChallengeActivity : BaseActivity2() {

    private lateinit var binding: ActivityCreateNewTaskChallengeBinding
    private var mTaskNew: CreateTaskChallenge? = null
    private var mTaskOriginal: CreateTaskChallenge? = null

    override fun setupScreen() {
        binding = ActivityCreateNewTaskChallengeBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun loadData() {
        mTaskNew =
            Gson().fromJson<CreateTaskChallenge>(
                intent.getStringExtra("data"),
                CreateTaskChallenge::class.java
            )

        if (intent != null && intent.action != null && intent.action == "action_edit") {
            mTaskOriginal = mTaskNew
        }
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
        if (mTaskNew?.color.isNullOrEmpty()) mTaskNew?.color = "#B6D6DD"
        else {
            val colorAlpha = "#33" + mTaskNew?.color?.substring(1, 7)
            binding.icAva.backgroundTintList =
                ColorStateList.valueOf(Color.parseColor(colorAlpha))
        }
        if (!mTaskNew?.name.isNullOrEmpty()) {
            binding.edtName.setText(mTaskNew?.name!!)
        }
        if (!mTaskNew?.icon.isNullOrEmpty()) {
            binding.icAva.setImageDrawable(Utils.loadImageFromAssets(this, mTaskNew?.icon!!))
            binding.icAva.imageTintList = ColorStateList.valueOf(Color.parseColor(mTaskNew?.color))
        }
        if (mTaskOriginal != null) {
            binding.btnCreate.text = getString(R.string.save)
            validateData()
        }
    }

    private val popupEmoji = PopupChoseEmojiTask()
    override fun controllerView() {
        binding.btnBackHeader.setOnClickListener {
            onBackPressed()
        }
        binding.rootView.setOnClickListener {
            MyUtils.hideSoftInput(it)
        }
        binding.edtName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
            }

            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
            }

            override fun afterTextChanged(editable: Editable) {
                validateData()
            }
        })

        popupEmoji.callback = object : PopupChoseEmojiTask.Callback {
            override fun clickChange(ava: String) {
                mIconTask = ava
                binding.icAva.setImageBitmap(BitmapFactory.decodeStream(assets.open(ava)))
            }
        }
        binding.icAva.setOnClickListener {
            popupEmoji.show(supportFragmentManager, "")
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

    var mIconTask = "avatask/emoji-sing-right-note.png"

    private fun createNewTask() {
        validateData()
        mTaskNew?.name = binding.edtName.text.toString()
        mTaskNew?.icon = mIconTask

        val intent2 = Intent(this, CreateChallengeActivity::class.java)
        intent2.putExtra("data_new_task_result", Gson().toJson(mTaskNew))

        if (mTaskOriginal != null) {
            setResult(999, intent2)
        } else setResult(868, intent2)
        finish()
    }

    private fun validateData() {
        if (binding.edtName.text.toString().isEmpty()) {
            binding.edtName.requestFocus()
            Toast.makeText(this, getString(R.string.please_type_name_of_task), Toast.LENGTH_SHORT)
                .show()
            binding.btnCreate.isEnabled = false
            binding.btnCreate.backgroundTintList =
                ColorStateList.valueOf(Color.parseColor("#b5b5b5"))
            return
        }

        binding.btnCreate.isEnabled = true
        binding.btnCreate.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#54BA8F"))
    }

    private fun resolveColorButton(color: Int) {
        for (i in 0..4) {
            btnColorView[i].backgroundTintList = ColorStateList.valueOf(
                Color.parseColor(btnColor[i])
            )
        }
        btnColorView[color - 1].backgroundTintList = null
        mTaskNew?.color = btnColor[color - 1]

        val colorAlpha = "#33" + mTaskNew?.color?.substring(1, 7)
        binding.icAva.backgroundTintList =
            ColorStateList.valueOf(Color.parseColor(colorAlpha))
        binding.icAva.imageTintList = ColorStateList.valueOf(Color.parseColor(mTaskNew?.color))
    }

    private val btnColor = arrayListOf<String>()
    private val btnColorView = arrayListOf<View>()

}