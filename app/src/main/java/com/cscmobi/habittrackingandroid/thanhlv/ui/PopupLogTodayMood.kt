package com.cscmobi.habittrackingandroid.thanhlv.ui

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import com.cscmobi.habittrackingandroid.R
import com.cscmobi.habittrackingandroid.databinding.PopupLogMoodTodayBinding
import com.cscmobi.habittrackingandroid.thanhlv.adapter.FeelingTagAdapter
import com.cscmobi.habittrackingandroid.thanhlv.data.MoodData
import com.cscmobi.habittrackingandroid.thanhlv.model.FeelingTagModel
import com.thanhlv.fw.helper.MyUtils

class PopupLogTodayMood :
    BaseDialogFragment<PopupLogMoodTodayBinding>(PopupLogMoodTodayBinding::inflate) {

    private var mListBecause = mutableListOf<FeelingTagModel>()
    private var mListDescribe = mutableListOf<FeelingTagModel>()
    private var adapter: FeelingTagAdapter? = null
    var callback: Callback? = null

    private var currentStep = 2
    private var currentMood = 1

    fun newInstance(
        mood: Int
    ) = PopupLogTodayMood().apply {
        arguments = Bundle().apply {
            putInt("mood_index", mood)
        }
    }

    fun setCallback2(callback: Callback) {
        this.callback = callback
    }

    override fun clickBackSystem() {
        callback?.onClickBack(currentStep)
        if (currentStep == 2) dismissAllowingStateLoss()
        if (currentStep == 3) {
            mListDescribe.clear()
            mListDescribe = mutableListOf()
            mListDescribe = getDataDescribe(currentMood)
            adapter?.updateData(mListDescribe)
            binding.bgNote.visibility = View.GONE
            gotoMoodStep2()
        }
    }

    interface Callback {
        fun onClickBack(step: Int)
        fun onClickNext(
            listDescribe: MutableList<FeelingTagModel>,
            listBecause: MutableList<FeelingTagModel>,
            note: String
        )

        fun onClickClose()
    }

    private fun gotoMoodStep2() {
        currentStep = 2
        binding.btnNext.isEnabled = false
        binding.tvQuestion.text = getString(R.string.which_describe_your_feelings)
        binding.btnNext.backgroundTintList =
            ColorStateList.valueOf(Color.parseColor("#B5B5B5"))
        binding.showMoodStep2.visibility = View.VISIBLE
    }

    private fun gotoMoodStep3() {
        currentStep = 3
        mListBecause.clear()
        mListBecause = mutableListOf()
        mListBecause = getDataBecause()
        adapter?.updateData(mListBecause)
        binding.bgNote.visibility = View.VISIBLE
        binding.tvQuestion.text = getString(R.string.what_making_you_feel_great)
        binding.btnNext.isEnabled = false
        binding.btnNext.backgroundTintList =
            ColorStateList.valueOf(Color.parseColor("#B5B5B5"))
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        MyUtils.configKeyboardBelowEditText(requireActivity())
        currentMood = requireArguments().getInt("mood_index")
        when (currentMood) {
            2 -> {
                binding.rootView.backgroundTintList =
                    ColorStateList.valueOf(Color.parseColor("#EEC9AA"))
                binding.btnBackHeader.backgroundTintList =
                    ColorStateList.valueOf(Color.parseColor("#F3D9C3"))
                binding.btnCloseHeader.backgroundTintList =
                    ColorStateList.valueOf(Color.parseColor("#F3D9C3"))
                binding.bgNote.backgroundTintList =
                    ColorStateList.valueOf(Color.parseColor("#FDE5D1"))
            }
            3 -> {
                binding.rootView.backgroundTintList =
                    ColorStateList.valueOf(Color.parseColor("#BEE4B8"))
                binding.btnBackHeader.backgroundTintList =
                    ColorStateList.valueOf(Color.parseColor("#D1ECCD"))
                binding.btnCloseHeader.backgroundTintList =
                    ColorStateList.valueOf(Color.parseColor("#D1ECCD"))
                binding.bgNote.backgroundTintList =
                    ColorStateList.valueOf(Color.parseColor("#D5F3D0"))
            }
            4 -> {
                binding.rootView.backgroundTintList =
                    ColorStateList.valueOf(Color.parseColor("#B6D6DD"))
                binding.btnBackHeader.backgroundTintList =
                    ColorStateList.valueOf(Color.parseColor("#CCE2E7"))
                binding.btnCloseHeader.backgroundTintList =
                    ColorStateList.valueOf(Color.parseColor("#CCE2E7"))
                binding.bgNote.backgroundTintList =
                    ColorStateList.valueOf(Color.parseColor("#D9EFF4"))
            }
            5 -> {
                binding.rootView.backgroundTintList =
                    ColorStateList.valueOf(Color.parseColor("#F1E3FF"))
                binding.btnBackHeader.backgroundTintList =
                    ColorStateList.valueOf(Color.parseColor("#D3ADFA"))
                binding.btnCloseHeader.backgroundTintList =
                    ColorStateList.valueOf(Color.parseColor("#D3ADFA"))
                binding.bgNote.backgroundTintList =
                    ColorStateList.valueOf(Color.parseColor("#D8BDF3"))
            }
            else -> {
                binding.rootView.backgroundTintList =
                    ColorStateList.valueOf(Color.parseColor("#EBB2BD"))
                binding.btnBackHeader.backgroundTintList =
                    ColorStateList.valueOf(Color.parseColor("#EFC1CA"))
                binding.btnCloseHeader.backgroundTintList =
                    ColorStateList.valueOf(Color.parseColor("#EFC1CA"))
                binding.bgNote.backgroundTintList =
                    ColorStateList.valueOf(Color.parseColor("#FAD2DA"))
            }
        }
        recyclerView(currentMood)
        gotoMoodStep2()
        controllerView()
    }

    private fun recyclerView(mood: Int) {
        mListDescribe.clear()
        mListDescribe = mutableListOf()
        mListDescribe = getDataDescribe(mood)
        adapter = FeelingTagAdapter(requireContext())
        adapter?.updateData(mListDescribe)
        adapter?.setCallBack(object : FeelingTagAdapter.FeelingTagCallback {
            override fun onClickItem(pos: Int) {
                var sum = 0
                mListDescribe.forEach {
                    if (it.selected) sum++
                }
                if (sum == 0) {
                    binding.btnNext.backgroundTintList =
                        ColorStateList.valueOf(Color.parseColor("#B5B5B5"))
                    binding.btnNext.isEnabled = false
                } else {
                    binding.btnNext.backgroundTintList =
                        ColorStateList.valueOf(Color.parseColor("#01100C"))
                    binding.btnNext.isEnabled = true
                }
            }
        })
        binding.rcFeeling.layoutManager = GridLayoutManager(requireContext(), 3)
        binding.rcFeeling.adapter = adapter

    }


    private fun controllerView() {
        binding.btnNext.setOnClickListener {
            if (currentStep == 3) {
                callback?.onClickNext(mListDescribe, mListBecause, binding.edtNote.text.toString())
                dismissAllowingStateLoss()
            }
            if (currentStep == 2) {
                gotoMoodStep3()
            }
        }

        binding.btnCloseHeader.setOnClickListener {
            dismissAllowingStateLoss()
        }
        binding.btnBackHeader.setOnClickListener {
            clickBackSystem()
        }
    }


    private fun getDataBecause(): MutableList<FeelingTagModel> {
        val list = mutableListOf<FeelingTagModel>()
        MoodData.mDescribeList.forEach {
            if (it.mood == "because_of") {
                it.type = currentMood
                it.selected = false
                list.add(it)
            }
        }
        return list
    }

    private fun getDataDescribe(mood: Int): MutableList<FeelingTagModel> {

        val list = mutableListOf<FeelingTagModel>()
        when (mood) {
            1 -> {
                MoodData.mDescribeList.forEach {
                    if (it.mood == "great") {
                        it.selected = false
                        it.type = currentMood
                        list.add(it)
                    }
                }
            }

            2 -> {
                MoodData.mDescribeList.forEach {
                    if (it.mood == "good") {
                        it.selected = false
                        it.type = currentMood
                        list.add(it)
                    }
                }
            }

            3 -> {
                MoodData.mDescribeList.forEach {
                    if (it.mood == "neutral") {
                        it.selected = false
                        it.type = currentMood
                        list.add(it)
                    }
                }
            }

            4 -> {
                MoodData.mDescribeList.forEach {
                    if (it.mood == "not_great") {
                        it.selected = false
                        it.type = currentMood
                        list.add(it)
                    }
                }
            }

            5 -> {
                MoodData.mDescribeList.forEach {
                    if (it.mood == "bad") {
                        it.selected = false
                        it.type = currentMood
                        list.add(it)
                    }
                }
            }
        }
        return list
    }


}