package com.cscmobi.habittrackingandroid.thanhlv.ui

import android.app.Dialog
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.cscmobi.habittrackingandroid.R
import com.cscmobi.habittrackingandroid.databinding.PopupChoseAvaProfileLayoutBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.thanhlv.fw.spf.SPF

class PopupChoseAvaProfile :
    BottomSheetDialogFragment() {
    var callback: Callback? = null


    private lateinit var binding: PopupChoseAvaProfileLayoutBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = PopupChoseAvaProfileLayoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (dialog as? BottomSheetDialog)?.behavior?.apply {
            isFitToContents = true
//            expandedOffset = 200
            state = BottomSheetBehavior.STATE_EXPANDED
        }

        if (!SPF.getAvaProfile(requireContext()).isNullOrEmpty()) {
            binding.imgAva.setImageResource(SPF.getAvaProfile(requireContext())!!.toInt())
        } else binding.imgAva.setImageResource(R.drawable.ava_profile_1)
        controllerView()

    }

    private var avaProfileRes = R.drawable.ava_profile_1

    private fun controllerView() {
        binding.imgAva1.setOnClickListener {
            avaProfileRes = R.drawable.ava_profile_1
            binding.imgAva.setImageResource(R.drawable.ava_profile_1)
            binding.btnChange.backgroundTintList =
                ColorStateList.valueOf(Color.parseColor("#54BA8F"))
            binding.btnChange.isEnabled = true
        }
        binding.imgAva2.setOnClickListener {
            avaProfileRes = R.drawable.ava_profile_2
            binding.imgAva.setImageResource(R.drawable.ava_profile_2)
            binding.btnChange.backgroundTintList =
                ColorStateList.valueOf(Color.parseColor("#54BA8F"))
            binding.btnChange.isEnabled = true
        }
        binding.imgAva3.setOnClickListener {
            avaProfileRes = R.drawable.ava_profile_3
            binding.imgAva.setImageResource(R.drawable.ava_profile_3)
            binding.btnChange.backgroundTintList =
                ColorStateList.valueOf(Color.parseColor("#54BA8F"))
            binding.btnChange.isEnabled = true
        }
        binding.imgAva4.setOnClickListener {
            avaProfileRes = R.drawable.ava_profile_4
            binding.imgAva.setImageResource(R.drawable.ava_profile_4)
            binding.btnChange.backgroundTintList =
                ColorStateList.valueOf(Color.parseColor("#54BA8F"))
            binding.btnChange.isEnabled = true
        }

        binding.btnChange.setOnClickListener {
            callback?.clickChange(avaProfileRes)
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

    interface Callback {
        fun clickChange(ava: Int)
    }

}