package com.thanhlv.fw.helper

import android.os.SystemClock
import android.view.View

abstract class MyClick : View.OnClickListener {

    private var delayTime: Long = 500
    private var mLastClickTime: Long = 0

    constructor()
    constructor(delayTimeMs: Long) {
        this.delayTime = delayTimeMs
    }

    abstract fun onMyClick(v: View, count: Long)
    override fun onClick(v: View) {
        val currentClickTime = SystemClock.uptimeMillis()
        val elapsedTime = currentClickTime - mLastClickTime
        if (elapsedTime <= delayTime) return
        mLastClickTime = currentClickTime
        onMyClick(v, clickCounter++)
    }

    companion object {
        var clickCounter = 0L
    }
}
