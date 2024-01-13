package com.cscmobi.habittrackingandroid.presentation.ui.activity

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.cscmobi.habittrackingandroid.R
import com.cscmobi.habittrackingandroid.base.BaseActivity
import com.cscmobi.habittrackingandroid.databinding.ActivityMainBinding
import com.cscmobi.habittrackingandroid.presentation.ui.view.HomeFragment
import com.google.android.material.bottomnavigation.BottomNavigationView


class MainActivity : BaseActivity<ActivityMainBinding>() {

    private val fragment1 = HomeFragment()
    private val fragment2 = HomeFragment()
    private val fragment3 = HomeFragment()
    private val fragment4 = HomeFragment()

    override fun getLayoutRes(): Int {
        return R.layout.activity_main
    }

    override fun initView() {

        initFragments()
        showFragment(fragment1)

           val listener =  BottomNavigationView.OnNavigationItemSelectedListener { item ->
                when (item.itemId) {
                    R.id.page_1 -> {
                        showFragment(fragment1)
                        return@OnNavigationItemSelectedListener true
                    }

                    R.id.page_2 -> {
                        showFragment(fragment2)
                        return@OnNavigationItemSelectedListener true
                    }

                    R.id.page_3 -> {
                        showFragment(fragment3)
                        return@OnNavigationItemSelectedListener true
                    }

                    R.id.page_4 -> {
                        showFragment(fragment4)
                        return@OnNavigationItemSelectedListener true
                    }
                }
                false
            }

        binding.bottomNav.setOnNavigationItemSelectedListener(listener);

    }

    fun showFragment(fmShow: Fragment?) {
//        transaction.setCustomAnimations(
//            R.anim.bottom_up,
//            R.anim.bottom_down
//        )

        val transaction: FragmentTransaction = supportFragmentManager
            .beginTransaction()

        transaction.hide(fragment1)
        transaction.hide(fragment2)
        transaction.hide(fragment3)
        transaction.hide(fragment4)

        fmShow?.let {
            transaction.show(it)
        }
        transaction.commit()
    }

    private fun initFragments() {
        addFragment(R.id.frame_container,fragment1,"fragment1")
        addFragment(R.id.frame_container,fragment2,"fragment2")
        addFragment(R.id.frame_container,fragment3,"fragment3")
        addFragment(R.id.frame_container,fragment4,"fragment4")

    }


}