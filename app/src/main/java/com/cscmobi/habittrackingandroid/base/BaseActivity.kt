package com.cscmobi.habittrackingandroid.base

import android.os.Bundle
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.ninenox.kotlinlocalemanager.AppCompatActivityBase
import com.thanhlv.fw.helper.MyUtils
import javax.inject.Inject

abstract class BaseActivity<VB: ViewDataBinding>: AppCompatActivity() {
    @Inject
    protected lateinit var binding: VB

    @LayoutRes
    abstract fun getLayoutRes(): Int

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, getLayoutRes()) as VB
        initView()
    }

    override fun onResume() {
        super.onResume()
        MyUtils.hideStatusBar(this)
    }

    abstract fun initView()

    protected fun addFragment(@IdRes containerViewId: Int, @NonNull fragment: Fragment, @NonNull fragmentTag: String) {
        supportFragmentManager.beginTransaction()
            .add(containerViewId,fragment,fragmentTag)
//                .addToBackStack(fragmentTag)
            .hide(fragment)
            .commit()
    }

    protected fun replaceFragment(@IdRes containerViewId: Int, @NonNull fragment: Fragment, @NonNull fragmentTag: String) {
        supportFragmentManager.beginTransaction()
            .replace(containerViewId,fragment,fragmentTag)
//            .addToBackStack(fragmentTag)
            .commit()
    }

}