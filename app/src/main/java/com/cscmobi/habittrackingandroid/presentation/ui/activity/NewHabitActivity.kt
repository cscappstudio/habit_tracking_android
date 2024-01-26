package com.cscmobi.habittrackingandroid.presentation.ui.activity

import android.util.Log
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.cscmobi.habittrackingandroid.R
import com.cscmobi.habittrackingandroid.base.BaseActivity
import com.cscmobi.habittrackingandroid.thanhlv.model.Task
import com.cscmobi.habittrackingandroid.databinding.ActivityNewhabitBinding
import com.cscmobi.habittrackingandroid.presentation.ui.intent.CollectionIntent
import com.cscmobi.habittrackingandroid.presentation.ui.view.CollectionFragment
import com.cscmobi.habittrackingandroid.presentation.ui.view.CreateCollectionFragment
import com.cscmobi.habittrackingandroid.presentation.ui.view.NewHabitFragment
import com.cscmobi.habittrackingandroid.presentation.ui.viewmodel.CollectionViewModel
import com.cscmobi.habittrackingandroid.utils.Constant
import com.cscmobi.habittrackingandroid.utils.ObjectWrapperForBinder
import kotlinx.coroutines.launch
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
        addFragment(R.id.fr_container,collectionFragment,"collectionFragment")

    }

    override fun onResume() {
        super.onResume()
        intent.extras?.let {
            val dataTask  = it.getBinder(Constant.EditTask) as? ObjectWrapperForBinder
            dataTask?.let { task ->
                replaceFragmentNotToBackStack(R.id.fr_container,newHabitFragment,NewHabitFragment.TAG)
                lifecycleScope.launch {
                  collectionViewModel.userIntent.send(CollectionIntent.EditTask(task.data as Task))

                }
            }

        }
    }

    fun replaceFragment(fr: Fragment,tagFragment: String) {
        replaceFragment(R.id.fr_container,fr, tagFragment)
    }

    fun addFragmentNotHide(fr: Fragment,tagFragment: String) {
        addFragmentNotHide(R.id.fr_container,fr, tagFragment)
    }

    override fun setEvent() {

    }

    override fun addTask(task: Task) {
        createCollectionFragment.addTask(task)
        Toast.makeText(this, "add Task", Toast.LENGTH_SHORT).show()
    }


}