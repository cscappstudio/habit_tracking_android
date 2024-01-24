package com.cscmobi.habittrackingandroid.thanhlv.ui

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.view.*
import android.widget.RelativeLayout
import androidx.fragment.app.DialogFragment
import androidx.viewbinding.ViewBinding
import com.thanhlv.fw.helper.MyUtils

abstract class BaseDialogFragment<VB : ViewBinding>(private val bindingInflater: (layoutInflater: LayoutInflater) -> VB) :
    DialogFragment() {
    private var _binding: VB? = null
    protected val binding get() = _binding!!


    override fun onResume() {
        super.onResume()
        dialog?.window!!.setFlags(
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
        )
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            dialog?.window!!.setDecorFitsSystemWindows(false)
            dialog?.window!!.insetsController!!.hide(WindowInsets.Type.navigationBars())
//            dialog?.window!!.insetsController!!.show(WindowInsets.Type.statusBars())
            dialog?.window!!.insetsController!!.systemBarsBehavior =
                WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        } else {
            dialog?.window!!.decorView.systemUiVisibility = (
                    View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                            or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            or View.SYSTEM_UI_FLAG_FULLSCREEN)
        }
        dialog?.window!!.clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE)


    }


//    abstract fun clickBackSystem()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        // create dialog in an arbitrary way
        val dialog = super.onCreateDialog(savedInstanceState)
        setMarginBottom(dialog, 0)
        return dialog
    }

    open fun setMarginBottom(
        dialog: Dialog,
        marginBottom: Int
    ): Dialog? {
        val window: Window = dialog.window
            ?: // dialog window is not available, cannot apply margins
            return dialog
        val context: Context = dialog.context

        // set dialog to fullscreen
        val root = RelativeLayout(context)
        root.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(root)
        // set background to get rid of additional margins
        window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))


        dialog.window!!.setFlags(
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
        )

        // apply left and top margin directly
        dialog.window!!.setGravity(Gravity.BOTTOM or Gravity.START)
        val layoutParams = dialog.window!!.attributes
//        layoutParams.y = AppUtils.parserDpi2Px(context, marginBottom.toFloat()) // top margin

//        layoutParams.verticalMargin = AppUtils.parserDpi2Px(context, marginBottom.toFloat()).toFloat()
        dialog.window!!.attributes = layoutParams

        window.setLayout(
            -1, -1
//            AppUtils.getHeightPx(context) - AppUtils.parserDpi2Px(
//                context,
//                marginBottom.toFloat()
//            )
        )
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.setDecorFitsSystemWindows(false)
            window.insetsController!!.hide(WindowInsets.Type.navigationBars())
            window.insetsController!!.hide(WindowInsets.Type.statusBars())
            window.insetsController!!.systemBarsBehavior =
                WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        } else {
            dialog.window!!.decorView.systemUiVisibility = (
                    View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                            or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION)
        }

        dialog.window!!.clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE)
        return dialog
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = bindingInflater.invoke(inflater)

        dialog!!.setCanceledOnTouchOutside(false)
        return binding.root
    }

//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//    }

    override fun onDestroyView() {
        super.onDestroyView()
        MyUtils.hideStatusBar(requireActivity())
        _binding = null
    }

}