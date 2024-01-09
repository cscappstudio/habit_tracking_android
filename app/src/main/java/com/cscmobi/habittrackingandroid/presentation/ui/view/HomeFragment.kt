package com.cscmobi.habittrackingandroid.presentation.ui.view

import android.graphics.Color
import android.view.View
import com.cscmobi.habittrackingandroid.base.BaseFragment
import com.cscmobi.habittrackingandroid.databinding.FragmentHomeBinding
import java.util.Random


class HomeFragment : BaseFragment<FragmentHomeBinding>( FragmentHomeBinding::inflate) {
    override fun initView(view: View) {
        val rnd = Random()
        val color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256))
        binding.frame.setBackgroundColor(color)
    }

}