package com.cscmobi.habittrackingandroid.presentation.ui.activity

import android.content.Intent
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.cscmobi.habittrackingandroid.R
import com.cscmobi.habittrackingandroid.base.BaseActivity
import com.cscmobi.habittrackingandroid.databinding.ActivityMainBinding
import com.cscmobi.habittrackingandroid.presentation.ui.view.HomeFragment
import com.cscmobi.habittrackingandroid.thanhlv.ui.ChallengeFragment
import com.cscmobi.habittrackingandroid.thanhlv.ui.ProfileFragment
import com.cscmobi.habittrackingandroid.thanhlv.ui.ProgressFragment
import com.cscmobi.habittrackingandroid.utils.Utils
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.shape.CornerFamily
import com.google.android.material.shape.MaterialShapeDrawable


class MainActivity : BaseActivity<ActivityMainBinding>() {

    private val fragment1 = HomeFragment()
    private val fragment2 = ProgressFragment()
    private val fragment3 = ChallengeFragment()
    private val fragment4 = ProfileFragment()

    override fun getLayoutRes(): Int {
        return R.layout.activity_main
    }

    override fun initView() {

        binding.bottomNavigationView.background = null
//        binding.bottomNavigationView.menu.getItem(2).isEnabled = false

        initFragments()
        showFragmentInActivity(fragment1)

        val listener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
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

        val shapeDrawable: MaterialShapeDrawable = binding.bottomAppBar.background as MaterialShapeDrawable
        shapeDrawable.shapeAppearanceModel = shapeDrawable.shapeAppearanceModel
            .toBuilder()
            .setTopLeftCorner(CornerFamily.ROUNDED, Utils.dpToPx(20f,this).toFloat())
            .setTopRightCorner(CornerFamily.ROUNDED, Utils.dpToPx(20f,this).toFloat())
//            .setBottomRightCorner(CornerFamily.ROUNDED, DisplayUtils.dpToPx(30f).toFloat())
//            .setBottomLeftCorner(CornerFamily.ROUNDED, DisplayUtils.dpToPx(30f).toFloat())
            .build()



     binding.bottomNavigationView.setOnNavigationItemSelectedListener(listener);

    }

    override fun setEvent() {
        binding.fab.setOnClickListener {
            startActivity(Intent(this, NewHabitActivity::class.java))
        }
    }

    fun showFragmentInActivity(fmShow: Fragment?) {

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
        addFragment(R.id.frame_container, fragment1, "fragment1")
        addFragment(R.id.frame_container, fragment2, "fragment2")
        addFragment(R.id.frame_container, fragment3, "fragment3")
        addFragment(R.id.frame_container, fragment4, "fragment4")

    }


}