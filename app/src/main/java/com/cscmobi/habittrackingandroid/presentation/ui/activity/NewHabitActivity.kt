package com.cscmobi.habittrackingandroid.presentation.ui.activity

import android.widget.Toast
import androidx.fragment.app.Fragment
import com.cscmobi.habittrackingandroid.R
import com.cscmobi.habittrackingandroid.base.BaseActivity
import com.cscmobi.habittrackingandroid.data.dto.entities.Task
import com.cscmobi.habittrackingandroid.databinding.ActivityNewhabitBinding
import com.cscmobi.habittrackingandroid.presentation.ui.view.BottomsheetNewHabitFragment
import com.cscmobi.habittrackingandroid.presentation.ui.view.CollectionFragment
import com.cscmobi.habittrackingandroid.presentation.ui.view.CreateCollectionFragment
import com.cscmobi.habittrackingandroid.presentation.ui.view.NewHabitFragment
import com.cscmobi.habittrackingandroid.presentation.ui.viewmodel.CollectionViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class NewHabitActivity: BaseActivity<ActivityNewhabitBinding>(),
    NewHabitFragment.INewHabitListener {
    private val collectionViewModel: CollectionViewModel by viewModel()

    override fun getLayoutRes(): Int {
        return  R.layout.activity_newhabit
    }
    val collectionFragment = CollectionFragment()
    val newHabitFragment = NewHabitFragment()
    val createCollectionFragment = CreateCollectionFragment()
//    val bottomsheetNewHabitFragment = BottomsheetNewHabitFragment()

    override fun initView() {
        collectionViewModel.setUp()
        addFragmentNotHide(R.id.fr_container,collectionFragment,"collectionFragment")
     //   addFragment(R.id.fr_container,bottomsheetNewHabitFragment,BottomsheetNewHabitFragment.tag)

    }

    fun replaceFragment(fr: Fragment,tagFragment: String) {
        replaceFragment(R.id.fr_container,fr, tagFragment)
    }

    fun addFragmentNotHide(fr: Fragment,tagFragment: String) {
        addFragmentNotHide(R.id.fr_container,fr, tagFragment)
    }

//    fun showBottomSheetFragment(caseBottomSheet: Int) {
//        bottomsheetNewHabitFragment.case = caseBottomSheet
//        supportFragmentManager.beginTransaction().show(bottomsheetNewHabitFragment).commit()
//    }

    override fun setEvent() {

    }
    private fun initFragments() {

    }

    override fun addTask(task: Task) {
        createCollectionFragment.addTask(task)
        Toast.makeText(this, "add Task", Toast.LENGTH_SHORT).show()
    }
}