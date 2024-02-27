package com.cscmobi.habittrackingandroid.thanhlv.ui

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import com.cscmobi.habittrackingandroid.R
import com.cscmobi.habittrackingandroid.databinding.PopupChoseEmojiTaskBinding
import java.util.*

class PopupChoseEmojiTask2(var callback: Callback?) :
    BaseDialogFragment<PopupChoseEmojiTaskBinding>(PopupChoseEmojiTaskBinding::inflate) {


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

        controllerView()
    }

    private var avaProfileRes = R.drawable.ava_profile_1

    private fun controllerView() {

//        binding.rootView.setOnClickListener {
//            callback?.clickChange(avaProfileRes)
//            dismissAllowingStateLoss()
//        }

        binding.btnBackHeader.setOnClickListener {
            dismissAllowingStateLoss()
        }
    }


    @SuppressLint("PrivateResource")
    override fun getTheme(): Int {
        return com.google.android.material.R.style.Base_ThemeOverlay_MaterialComponents_Dialog
    }

}