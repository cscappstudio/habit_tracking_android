package com.cscmobi.habittrackingandroid.thanhlv.ui

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import com.cscmobi.habittrackingandroid.R
import com.cscmobi.habittrackingandroid.databinding.PopupChoseAvaProfileLayoutBinding
import com.thanhlv.fw.spf.SPF
import java.util.*

class PopupChoseAvaProfile2(var callback: Callback?) :
    BaseDialogFragment<PopupChoseAvaProfileLayoutBinding>(PopupChoseAvaProfileLayoutBinding::inflate) {


    interface Callback {
        fun clickChange(ava: Int)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Objects.requireNonNull(dialog)
            ?.setOnKeyListener { _: DialogInterface?, keyCode: Int, _: KeyEvent? ->
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    dismissAllowingStateLoss()
                    return@setOnKeyListener true // pretend we've processed it
                } else return@setOnKeyListener false // pass on to be processed as normal
            }
//        dialog?.setCanceledOnTouchOutside(true)
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

//        binding.rootView.setOnClickListener {
//            callback?.clickChange(avaProfileRes)
//            dismissAllowingStateLoss()
//        }
    }


    @SuppressLint("PrivateResource")
    override fun getTheme(): Int {
        return com.google.android.material.R.style.Base_ThemeOverlay_MaterialComponents_Dialog
    }

}