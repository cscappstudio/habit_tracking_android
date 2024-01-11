package com.cscmobi.habittrackingandroid.presentation.ui.activity

import androidx.fragment.app.Fragment
import com.cscmobi.habittrackingandroid.R
import com.cscmobi.habittrackingandroid.base.BaseActivity
import com.cscmobi.habittrackingandroid.databinding.ActivityNewhabitBinding
import com.cscmobi.habittrackingandroid.presentation.ui.view.CollectionFragment

class NewHabitActivity: BaseActivity<ActivityNewhabitBinding>() {
    override fun getLayoutRes(): Int {
        return  R.layout.activity_newhabit
    }
    var collectionFragment = CollectionFragment()

    override fun initView() {
        addFragmentNotHide(R.id.fr_container,collectionFragment,"collectionFragment")
    }

    fun replaceFragment(fr: Fragment,tagFragment: String) {
        replaceFragment(R.id.fr_container,fr, tagFragment)
    }

    override fun setEvent() {

    }
    private fun initFragments() {

    }
}