package com.cscmobi.habittrackingandroid.presentation.ui.activity

import androidx.fragment.app.Fragment
import com.cscmobi.habittrackingandroid.R
import com.cscmobi.habittrackingandroid.base.BaseActivity
import com.cscmobi.habittrackingandroid.databinding.ActivityNewhabitBinding
import com.cscmobi.habittrackingandroid.presentation.ui.view.CollectionFragment
import com.cscmobi.habittrackingandroid.presentation.ui.view.CreateCollectionFragment
import com.cscmobi.habittrackingandroid.presentation.ui.view.NewHabitFragment
import com.cscmobi.habittrackingandroid.presentation.ui.viewmodel.CollectionViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class NewHabitActivity: BaseActivity<ActivityNewhabitBinding>() {
    private val collectionViewModel: CollectionViewModel by viewModel()

    override fun getLayoutRes(): Int {
        return  R.layout.activity_newhabit
    }
    val collectionFragment = CollectionFragment()
    val newHabitFragment = NewHabitFragment()
    val createCollectionFragment = CreateCollectionFragment()
    override fun initView() {
        collectionViewModel.setUp()
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