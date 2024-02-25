package com.cscmobi.habittrackingandroid.thanhlv.ui

import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.cscmobi.habittrackingandroid.R
import java.util.Objects

abstract class PopupBase : DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Objects.requireNonNull(Objects.requireNonNull(dialog)?.window)
            ?.requestFeature(Window.FEATURE_NO_TITLE)
        Objects.requireNonNull(dialog!!.window)?.setBackgroundDrawableResource(R.color.transparent)
        return requireActivity().layoutInflater.inflate(R.layout.activity_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onStart() {
        super.onStart()
        try {
            if (dialog != null && dialog!!.window != null) {
                dialog!!.window!!.setFlags(
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                )
                dialog!!.window?.setLayout(
                    WindowManager.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.MATCH_PARENT
                )
                dialog!!.window!!.setGravity(Gravity.CENTER)
                dialog!!.setCanceledOnTouchOutside(false)
                dialog!!.window!!.decorView.systemUiVisibility =
                    (View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION)
                dialog!!.window!!.clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE)
            }
        } catch (e: Exception) {
            handleException(e)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

    override fun onStop() {
        super.onStop()
    }


    abstract fun clickBackSystem()

    override fun onResume() {
        super.onResume()
        Objects.requireNonNull(dialog)
            ?.setOnKeyListener { _: DialogInterface?, keyCode: Int, _: KeyEvent? ->
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    //Hide your keyboard here!!!
//                    MyUtils.hideSoftInput(requireActivity())
                    updateUI()
                    clickBackSystem()
//                    MyUtils.hideNavigationBar(requireActivity())
                    return@setOnKeyListener true // pretend we've processed it
                } else return@setOnKeyListener false // pass on to be processed as normal
            }
    }

    override fun show(manager: FragmentManager, tag: String?) {
        try {
            val ft = manager.beginTransaction()
            ft.add(this, tag)
            ft.commitAllowingStateLoss()
        } catch (e: IllegalStateException) {
            Log.d("ABSDIALOGFRAG", "Exception", e)
        }
    }


    abstract fun updateUI()
//    abstract fun getLayout(): Int
    override fun getTheme() = R.style.CustomBottomSheetDialogTheme

    companion object {

        fun handleException(e: Exception) {
            try {
                e.printStackTrace()
                Log.e("Error", "handleException", e)
            } catch (ex: Exception) {
                ex.message?.let { Log.e("Error Exception", it) }
            }
        }
    }
}