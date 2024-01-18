package com.cscmobi.habittrackingandroid.utils

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.graphics.Rect
import android.os.Build
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.MarginLayoutParams
import android.view.Window
import android.view.WindowManager
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText


fun Activity.hideStatusBar() {
    setWindowFlag(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, true)
    window.decorView.systemUiVisibility =
        View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
    if (Build.VERSION.SDK_INT >= 22) {
        setWindowFlag(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false)
        window.statusBarColor = Color.TRANSPARENT
    }
}

fun Activity.setWindowFlag(bits: Int, on: Boolean) {
    val win = window
    val winParams = win.attributes
    if (on) {
        winParams.flags = winParams.flags or bits
    } else {
        winParams.flags = winParams.flags and bits.inv()
    }
    win.attributes = winParams
}

fun EditText.onDone(callback: () -> Unit) {
    setOnEditorActionListener { _, actionId, _ ->
        if (actionId == EditorInfo.IME_ACTION_DONE) {
            callback.invoke()
            return@setOnEditorActionListener true
        }
        false
    }
}

fun Activity.hideKeyboard() {
    val imm = this.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    //Find the currently focused view, so we can grab the correct window token from it.
    var view = this.currentFocus
    //If no view currently has focus, create a new one, just so we can grab a window token from it
    if (view == null) {
        view = View(this)
    }
    imm.hideSoftInputFromWindow(view.windowToken, 0)
}


fun ViewGroup.setVisibleforView(context: Context, activity: Activity) {
    viewTreeObserver.addOnGlobalLayoutListener {
        val screenHeight = rootView.height

        val rec = Rect()
        val mRootWindow: Window = activity.getWindow()

        var view = mRootWindow.getDecorView();
        if (screenHeight >= 1499)
            view.getWindowVisibleDisplayFrame(rec)
        else getWindowVisibleDisplayFrame(rec)


        var margin = context.resources.getDimension(com.intuit.sdp.R.dimen._55sdp).toInt()
        //finding screen height

        //finding keyboard height
        val keypadHeight = screenHeight - Math.abs(rec.bottom)

        if (keypadHeight > screenHeight * 0.15) {
            this.setMarginExtensionFunction(
                margin,
                0,
                margin,
                keypadHeight - getActionBarHeight(context) + resources.getDimension(com.intuit.sdp.R.dimen._20sdp)
                    .toInt()
            )

        } else {
            this.setMarginExtensionFunction(
                margin, keypadHeight, margin, resources.getDimension(com.intuit.sdp.R.dimen._20sdp).toInt()
            )

        }
    }
}

fun View.setMarginExtensionFunction(left: Int, top: Int, right: Int, bottom: Int) {
    var params = layoutParams as ViewGroup.MarginLayoutParams
    params.setMargins(left, top, right, bottom)
    layoutParams = params
}

fun getActionBarHeight(context: Context): Int {
    val tv = TypedValue()
    if (context.theme?.resolveAttribute(android.R.attr.actionBarSize, tv, true)!!) {
        return TypedValue.complexToDimensionPixelSize(tv.data, context.resources.displayMetrics)
    }
    return 0
}


fun Activity.setVisibilityKeyboard(check: Boolean) {
    var imm = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    //Show soft-keyboard:
    if (check == true) {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

    }
    //hide keyboard :
    else
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

}

fun hideKeyboardFrom(context: Context, view: View) {
    val imm = context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(view.windowToken, 0)
}

fun View.setMargins( left: Int, top: Int, right: Int, bottom: Int) {
    if (this.layoutParams is MarginLayoutParams) {
        val p = this.layoutParams as MarginLayoutParams
        p.setMargins(left, top, right, bottom)
        this.requestLayout()
    }
}