package com.cscmobi.habittrackingandroid.base

import android.os.Bundle
import android.view.View
import android.view.View.SYSTEM_UI_FLAG_FULLSCREEN
import android.view.View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.cscmobi.habittrackingandroid.utils.hideStatusBar
import javax.inject.Inject


abstract class BaseActivity<VB: ViewDataBinding>: AppCompatActivity() {
    @Inject
    protected lateinit var binding: VB

    @LayoutRes
    abstract fun getLayoutRes(): Int

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        hideStatusBar()
        window.decorView.apply {
            // Hide both the navigation bar and the status bar.
            // SYSTEM_UI_FLAG_FULLSCREEN is only available on Android 4.1 and higher, but as
            // a general rule, you should design your app to hide the status bar whenever you
            // hide the navigation bar.
            systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_FULLSCREEN
        }
        binding = DataBindingUtil.setContentView(this, getLayoutRes()) as VB
        initView()
        setEvent()
    }

    abstract fun initView()
    abstract fun setEvent()

    protected fun addFragment(@IdRes containerViewId: Int, @NonNull fragment: Fragment, @NonNull fragmentTag: String) {
        supportFragmentManager.beginTransaction()
            .add(containerViewId,fragment,fragmentTag)
//                .addToBackStack(fragmentTag)
            .hide(fragment)
            .commit()
    }
    protected fun addFragmentNotHide(@IdRes containerViewId: Int, @NonNull fragment: Fragment, @NonNull fragmentTag: String) {
        supportFragmentManager.beginTransaction()
            .add(containerViewId,fragment,fragmentTag)
//              .addToBackStack(fragmentTag)
            .commit()
    }
    protected fun replaceFragment(@IdRes containerViewId: Int, @NonNull fragment: Fragment, @NonNull fragmentTag: String) {
        supportFragmentManager.beginTransaction()
            .replace(containerViewId,fragment,fragmentTag)
           .addToBackStack(fragmentTag)
            .commit()
    }

}