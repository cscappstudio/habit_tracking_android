package com.cscmobi.habittrackingandroid.thanhlv.ui

import android.app.Dialog
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
import com.cscmobi.habittrackingandroid.thanhlv.adapter.FeelingTag2Adapter
import com.cscmobi.habittrackingandroid.thanhlv.model.Mood
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.gson.Gson
import com.thanhlv.fw.spf.SPF

class PopupDetailMood :
    BottomSheetDialogFragment() {

    private var mMood: Mood? = null

    companion object {
        fun newInstance(mood: Mood) = PopupDetailMood().apply {
            arguments = Bundle().apply {
                putString("MOOD_OBJ", Gson().toJson(mood))
            }
        }
    }

    private lateinit var binding: ActivityDetailMoodBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ActivityDetailMoodBinding.inflate(inflater, container, false)
        mMood =
            Gson().fromJson<Mood>(requireArguments().getString("MOOD_OBJ"), Mood::class.java)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (dialog as? BottomSheetDialog)?.behavior?.apply {
            isFitToContents = false
            expandedOffset = 100
            state = BottomSheetBehavior.STATE_EXPANDED
        }

        recyclerView()
        if (mMood?.note.isNullOrEmpty()) {
            binding.showNote.visibility = View.GONE
        } else {
            binding.showNote.visibility = View.VISIBLE
            binding.textNote.text = mMood?.note
        }

        when (mMood?.state) {
            2 -> {
                binding.tvMood.text = getString(R.string.to_day_was_good)
                binding.icMood.setImageResource(R.drawable.ic_mood_good)
            }
            3 -> {
                binding.tvMood.text = getString(R.string.to_day_was_neutral)
                binding.icMood.setImageResource(R.drawable.ic_mood_neutral)
            }
            4 -> {
                binding.tvMood.text = getString(R.string.to_day_was_not_great)
                binding.icMood.setImageResource(R.drawable.ic_mood_not_great)
            }
            5 -> {
                binding.tvMood.text = getString(R.string.to_day_was_bad)
                binding.icMood.setImageResource(R.drawable.ic_mood_bad)
            }
            else -> {
                binding.tvMood.text = getString(R.string.to_day_was_great)
                binding.icMood.setImageResource(R.drawable.ic_mood_great)
            }
        }
        controllerView()

    }

    private fun recyclerView() {
        if (!mMood?.describe.isNullOrEmpty()) {
            binding.rcFeeling.adapter = FeelingTag2Adapter(requireContext(), ArrayList(mMood?.describe!!))
            binding.rcFeeling.layoutManager = GridLayoutManager(requireContext(), 3)
        }
        if (!mMood?.becauseOf.isNullOrEmpty()) {
            binding.rcBecause.adapter = FeelingTag2Adapter(requireContext(), ArrayList(mMood?.becauseOf!!))
            binding.rcBecause.layoutManager = GridLayoutManager(requireContext(), 3)
        }
    }

    private fun controllerView() {
        binding.btnBackHeader.setOnClickListener {
            dismissAllowingStateLoss()
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState).apply {
            setOnShowListener {
                val bottomSheet =
                    findViewById<View>(com.google.android.material.R.id.design_bottom_sheet) as FrameLayout
                bottomSheet.setBackgroundResource(android.R.color.transparent)
            }
        }


    }

}