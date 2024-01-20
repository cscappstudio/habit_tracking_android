package com.cscmobi.habittrackingandroid.presentation.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.DiffUtil
import com.cscmobi.habittrackingandroid.R
import com.cscmobi.habittrackingandroid.base.BaseActivity
import com.cscmobi.habittrackingandroid.base.BaseBindingAdapter
import com.cscmobi.habittrackingandroid.data.dto.entities.Task
import com.cscmobi.habittrackingandroid.data.model.CheckList
import com.cscmobi.habittrackingandroid.databinding.ActivityDetailTaskBinding
import com.cscmobi.habittrackingandroid.presentation.ItemBasePosistionListener
import com.cscmobi.habittrackingandroid.presentation.ui.view.CustomCalenderFragment
import com.cscmobi.habittrackingandroid.presentation.ui.view.NewHabitFragment

class DetailTaskActivity : BaseActivity<ActivityDetailTaskBinding>() {
    val childFragment: CustomCalenderFragment = CustomCalenderFragment()
    private lateinit var checklistAdapter: BaseBindingAdapter<CheckList>

    private var task = Task()
    private var checkList = mutableListOf<CheckList>()

    private var testDataCheckList =  mutableListOf<CheckList>(
        CheckList(true,"11111"),
        CheckList(false,"22222"),
        CheckList(false,"33333"),
    )
    override fun getLayoutRes(): Int {
        return R.layout.activity_detail_task
    }

    override fun initView() {
        val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        transaction.replace(binding.fragContainer.id, childFragment).commit()
        initCheckList()
    }

    private fun initCheckList() {
        binding.layoutChecklist.edtAdd.visibility = View.GONE
        binding.layoutChecklist.ivAdd.visibility = View.GONE

        //checkList = task.checklist as MutableList<CheckList>
        checkList = testDataCheckList

        checklistAdapter = BaseBindingAdapter(
            R.layout.item_checklist,
            layoutInflater,
            object : DiffUtil.ItemCallback<CheckList>() {
                override fun areItemsTheSame(oldItem: CheckList, newItem: CheckList): Boolean {
                    return oldItem.title == newItem.title
                }

                override fun areContentsTheSame(oldItem: CheckList, newItem: CheckList): Boolean {
                    return oldItem == newItem
                }
            })

        checklistAdapter.setListener(object : ItemBasePosistionListener {
            override fun onItemClicked(p: Int) {
                checkList[p].status = !  checkList[p].status
            }
        }
        )

        checklistAdapter.submitList(checkList)

        binding.layoutChecklist.rcvSubtask.adapter = checklistAdapter
    }

    override fun setEvent() {

        binding.ivBack.setOnClickListener {
            finish()
        }

        binding.ivEdit.setOnClickListener {
            Intent(this,NewHabitActivity::class.java).run {
                this.putExtra(NewHabitFragment.TAG, NewHabitFragment.NewHabitFragmentState.EDITTASK)
                startActivity(this)
            }
        }

        binding.ivSkip.setOnClickListener {
            Toast.makeText(this, "skip", Toast.LENGTH_SHORT).show()
        }

        binding.ivDelete.setOnClickListener {
            Toast.makeText(this, "delete", Toast.LENGTH_SHORT).show()
            finish()

        }
    }

}