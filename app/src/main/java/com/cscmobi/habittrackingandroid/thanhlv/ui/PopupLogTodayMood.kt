package com.cscmobi.habittrackingandroid.thanhlv.ui

import android.app.Dialog
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.recyclerview.widget.GridLayoutManager
import com.cscmobi.habittrackingandroid.R
import com.cscmobi.habittrackingandroid.databinding.ActivityDetailMoodBinding
import com.cscmobi.habittrackingandroid.databinding.PopupChoseAvaProfileLayoutBinding
import com.cscmobi.habittrackingandroid.databinding.PopupLogMoodTodayBinding
import com.cscmobi.habittrackingandroid.thanhlv.adapter.FeelingTag2Adapter
import com.cscmobi.habittrackingandroid.thanhlv.adapter.FeelingTagAdapter
import com.cscmobi.habittrackingandroid.thanhlv.data.MoodData
import com.cscmobi.habittrackingandroid.thanhlv.model.FeelingTagModel
import com.cscmobi.habittrackingandroid.thanhlv.model.Mood
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.gson.Gson
import com.thanhlv.fw.spf.SPF

class PopupLogTodayMood :
    BaseDialogFragment<PopupLogMoodTodayBinding>(PopupLogMoodTodayBinding::inflate) {

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
    }


    var callback: Callback? = null

    interface Callback {
        fun onClickBack(step: Int)
        fun onClickNext(
            listDescribe: MutableList<FeelingTagModel>,
            listBecause: MutableList<FeelingTagModel>
        )

        fun onClickClose()
    }

//    override fun onAttach(context: Context) {
//        super.onAttach(context)
//        try {
//            callback = context as Callback
//        } catch (_: Exception) {
//        }
//    }

    var currentStep = 2
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
        mListBecause = mutableListOf<FeelingTagModel>()
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

        val mood = requireArguments().getInt("mood_index")
        recyclerView(mood)
        gotoMoodStep2()
        controllerView()
    }

    private var mListBecause = mutableListOf<FeelingTagModel>()
    private var mListDescribe = mutableListOf<FeelingTagModel>()

    private var adapter: FeelingTagAdapter? = null
    private fun recyclerView(mood: Int) {
        mListDescribe.clear()
        mListDescribe = mutableListOf<FeelingTagModel>()
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
                        ColorStateList.valueOf(Color.parseColor("#54BA8F"))
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
                callback?.onClickNext(mListDescribe, mListBecause)
                dismissAllowingStateLoss()
            }

            if (currentStep == 2) {
                gotoMoodStep3()
            }
        }

//        binding.btnNext.setOnClickListener {
//            if (currentStep == 1) gotoMoodStep2()
//            if (currentStep == 2) {
//                gotoMoodStep3()
//            }
//            if (currentStep == 3) {
//                callback?.onClickNext(mListDescribe, mListBecause)
//                dismissAllowingStateLoss()
//            }
//        }
    }


    private fun getDataBecause(): MutableList<FeelingTagModel> {
        val list = mutableListOf<FeelingTagModel>()
        MoodData.mDescribeList.forEach {
            if (it.mood == "because_of")
                list.add(it)
        }
        return list
    }

    private fun getDataDescribe(mood: Int): MutableList<FeelingTagModel> {

        val list = mutableListOf<FeelingTagModel>()
        when (mood) {
            1 -> {
                MoodData.mDescribeList.forEach {
                    if (it.mood == "great")
                        list.add(it)
                }
            }

            2 -> {
                MoodData.mDescribeList.forEach {
                    if (it.mood == "good")
                        list.add(it)
                }
            }

            3 -> {
                MoodData.mDescribeList.forEach {
                    if (it.mood == "neutral")
                        list.add(it)
                }
            }

            4 -> {
                MoodData.mDescribeList.forEach {
                    if (it.mood == "not_great")
                        list.add(it)
                }
            }

            5 -> {
                MoodData.mDescribeList.forEach {
                    if (it.mood == "bad")
                        list.add(it)
                }
            }
        }

        return list
    }


}